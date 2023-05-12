import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.Socket;

/**
 * Builds the display window for a client and starts the game.
 *
 * @author ZHU Ziyao - 3035772145
 * @version 0.1
 */
public class GameFrame extends Thread {
    private final JFrame gameFrame = new JFrame();

    private final JTextField nameInput = new JTextField(10);
    private final JButton submit = new JButton("Submit");
    private final JLabel prompt = new JLabel("Enter your player name...");

    private final Socket socket = new Socket("localhost", ServerMain.PORT);
    private final DataInputStream dis = new DataInputStream(socket.getInputStream());
    private final DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
    private final Board board = new Board(dos);

    private boolean turn;

    /**
     * Allocates a new GameFrame object.
     *
     * @throws IOException on connection failed.
     */
    public GameFrame() throws IOException {
        if (dis.readInt() == ServerMain.REFUSED) {
            JOptionPane.showMessageDialog(gameFrame, "Server refused the connection. please retry later.",
                    "Connection Failed", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setTitle("Tic Tac Toe");

        JMenuBar menuBar = new MenuBarBuilder().getMenuBar();
        gameFrame.setJMenuBar(menuBar);

        gameFrame.add(prompt, BorderLayout.NORTH);

        gameFrame.add(board.getBoard(), BorderLayout.CENTER);

        JPanel inputArea = new JPanel();
        SubmitListener submitListener = new SubmitListener();
        nameInput.addActionListener(submitListener);
        submit.addActionListener(submitListener);
        inputArea.add(nameInput);
        inputArea.add(submit);
        gameFrame.add(inputArea, BorderLayout.SOUTH);

        gameFrame.pack();
        gameFrame.setVisible(true);
    }

    private class SubmitListener implements ActionListener {
        /**
         * Invoked when submit button is pressed.
         *
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameInput.getText();
            if (name.length() == 0) { return; }
            gameFrame.setTitle(String.format("Tic Tac Toe - Player: %s", name));
            prompt.setText(String.format("WELCOME %s", name));

            nameInput.setEnabled(false);
            submit.setEnabled(false);

            start();
        }
    }

    private void play() {
        if (turn) {
            board.unlockBoard();
            prompt.setText("Your opponent has moved, now is your turn.");
        } else {
            prompt.setText("Valid move, wait for your opponent.");
        }
        turn = !turn;
    }

    /**
     * Start the game.
     *
     * Thread required or the player can not exit the game.
     */
    @Override
    public void run() {
        try {
            turn = dis.readBoolean();
            int player = turn ? 1 : 2;
            if (turn) {
                board.unlockBoard();
            }
            turn = !turn;

            int msg;
            mainGame:
            while (true) {
                msg = dis.readInt();
                switch (msg) {
                    case (-1):
                    case (-2):
                        if (msg == -player) {
                            JOptionPane.showMessageDialog(gameFrame, "Congratulations! You Win!",
                                    "Message", JOptionPane.PLAIN_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(gameFrame, "You lose.",
                                    "Message", JOptionPane.PLAIN_MESSAGE);
                        }
                        break;
                    case (ServerMain.DRAW):
                        JOptionPane.showMessageDialog(gameFrame, "Draw.",
                                "Message", JOptionPane.PLAIN_MESSAGE);
                        break;
                    case (ServerMain.DISCONNECTED):
                        JOptionPane.showMessageDialog(gameFrame, "Game Ends. One of the players left.",
                                "Message", JOptionPane.PLAIN_MESSAGE);
                        break;
                    default:
                        board.updateStatus(msg / 10, msg % 10);
                        play();
                        continue mainGame;
                }
                board.lockBoard();
                break;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
