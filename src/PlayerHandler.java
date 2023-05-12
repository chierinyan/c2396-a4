import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

/**
 * The class for handling a client.
 *
 * @author ZHU Ziyao - 3035772145
 * @version 0.1
 */
public class PlayerHandler extends Thread {
    private final int playerHandled;
    private static final int[] boardStatus = new int[9];
    private static int connected = 0;
    private final DataInputStream dis;
    private final DataOutputStream[] doss;

    /**
     * Allocates a new PlayerHandler object for handling one player.
     *
     * @param playerHandled The number of player handled.
     * @param dis The DataInputStream of the player handled.
     * @param doss The DataOutputStream for both players.
     */
    public PlayerHandler(int playerHandled, DataInputStream dis, DataOutputStream[] doss) {
        this.playerHandled = playerHandled;
        this.dis = dis;
        this.doss = doss;
        connected++;

        for (int i = 0; i < 9; i++) {
            boardStatus[i] = -1;
        }
    }

    /**
     * @return count of connections.
     */
    public static int getConnected() {
        return connected;
    }

    /**
     * Thread for interact with a client.
     */
    @Override
    public void run() {
        while (true) {
            try {
                int pose = dis.readInt();
                boardStatus[pose] = playerHandled;
                broadcast(playerHandled * 10 + pose);
            } catch (IOException e) {
                try {
                    doss[1 ^ playerHandled].writeInt(ServerMain.DISCONNECTED);
                    connected = 0;
                    return;
                } catch (IOException ex) {
                    return;
                }
            }

            int progress = judge();
            if (progress != ServerMain.ONGOING) {
                broadcast(progress);
                connected = 0;
                return;
            }
        }
    }

    private void broadcast(int msg) {
        for (int i=0; i<2; i++) {
            try {
                doss[i].writeInt(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int judge() {
        int[][] lines = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
        for (int player = 0; player < 2; player++) {
            checkLines:
            for (int[] line : lines) {
                for (int grid : line) {
                    if (boardStatus[grid] != player) {
                        continue checkLines;
                    }
                }
                return -(player+1);
            }
        }
        for (int grid : boardStatus) {
            if (grid == -1) {
                return ServerMain.ONGOING;
            }
        }
        return ServerMain.DRAW;
    }
}
