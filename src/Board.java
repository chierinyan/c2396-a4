import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Builds the main game board.
 * Arranges the grids. Send message to server when any grid got clicked.
 *
 * @author ZHU Ziyao - 3035772145
 * @version 0.1
 */
public class Board extends Thread {
    private final Grid[] grids = new Grid[9];
    private final JPanel board = new JPanel();

    private final DataOutputStream dos;

    /**
     * Allocates a new Board object as a game board.
     *
     * @param dos The DataOutputStream to server.
     */
    public Board(DataOutputStream dos) {
        this.dos = dos;

        board.setBackground(Color.BLACK);
        board.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        for (int i = 0; i < 9; i++) {
            grids[i] = new Grid();
            grids[i].addActionListener(new MoveListener(i));

            gbc.gridx = i % 3;
            gbc.gridy = i / 3;
            board.add(grids[i], gbc);
        }
    }

    /**
     * @return The generated game board.
     */
    public JPanel getBoard() {
        return board;
    }

    /**
     * Place a mark.
     *
     * @param player Who placed the mark.
     * @param pose Where the mark was placed.
     */
    public void updateStatus(int player, int pose) {
        grids[pose].setStatus(player);
    }

    /**
     * Disable all the grids on the board.
     */
    public void lockBoard() {
        for (int i=0; i<9; i++) {
            grids[i].setEnabled(false);
        }
    }

    /**
     * Enable the grids that have not yet been marked.
     */
    public void unlockBoard() {
        for (int i=0; i<9; i++) {
            grids[i].unlock();
        }
    }

    private class MoveListener implements ActionListener {
        private final int pose;

        public MoveListener(int pose) {
            this.pose = pose;
        }

        /**
         * Invoked when button pressed.
         *
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                dos.writeInt(pose);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            lockBoard();
        }
    }
}
