import java.io.IOException;

/**
 * Main class for Cilent.
 * @author ZHU Ziyao - 3035772145
 * @version 0.1
 */
public class ClientMain {
    /**
     * Main method to start a client.
     *
     * @param args unused.
     */
    public static void main(String[] args) {
        try {
            new GameFrame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
