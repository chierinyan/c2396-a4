import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Main class for server.
 *
 * @author ZHU Ziyao - 3035772145
 * @version 0.1
 */
public class ServerMain {
    /**
     * The server port.
     */
    static final int PORT = 23333;
    /**
     * Status codes
     */
    static final int CONNECTED = -3;
    static final int REFUSED = -4;
    static final int ONGOING = -5;
    static final int DRAW = -6;
    static final int DISCONNECTED = -7;

    /**
     * Main method to start a server.
     *
     * @param args unused.
     */
    public static void main(String[] args) {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(ServerMain.PORT);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Server started. Listening 0.0.0.0:23333");

        mainGame:
        while (true) {
            DataInputStream[] diss = new DataInputStream[2];
            DataOutputStream[] doss = new DataOutputStream[2];

            try {
                for (int i = 0; i < 2; i++) {
                    Socket socket = ss.accept();

                    System.out.printf("Player %d connected\n", i);
                    diss[i] = new DataInputStream(socket.getInputStream());
                    doss[i] = new DataOutputStream(socket.getOutputStream());

                    if (PlayerHandler.getConnected() == 2) {
                        doss[i].writeInt(ServerMain.REFUSED);
                        continue mainGame;
                    }
                    else {
                        doss[i].writeInt(ServerMain.CONNECTED);
                    }
                }

                System.out.println("Starting game.");
                doss[0].writeBoolean(true);
                doss[1].writeBoolean(false);

                for (int i = 0; i < 2; i++) {
                    PlayerHandler playerHandler = new PlayerHandler(i, diss[i], doss);
                    playerHandler.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
