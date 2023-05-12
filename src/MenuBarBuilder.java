import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author ZHU Ziyao - 3035772145
 * @version 0.1
 */

public class MenuBarBuilder {
    private final JMenuBar menuBar = new JMenuBar();

    /**
     * Constructor for build the menu bar.
     */
    public MenuBarBuilder() {
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new Exit());
        JMenu control = new JMenu("Control");
        control.add(exit);
        menuBar.add(control);

        JMenuItem instruction = new JMenuItem("Instruction");
        instruction.addActionListener(new Instructor());
        JMenu help = new JMenu("Help");
        help.add(instruction);
        menuBar.add(help);
    }

    /**
     * Getter for menuBar.
     *
     * @return The constructed menu bar.
     */
    public JMenuBar getMenuBar() {
        return menuBar;
    }

    private static class Exit implements ActionListener {
        /**
         * Exit the game when Exit was pressed.
         *
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    private class Instructor implements ActionListener {
        private static final String message = """
                Some information about the game:
                Criteria for a valid move:
                - The move is not occupied by any mark.
                - The move is made in the player's turn.
                - The move is made within the 3 x 3 board.
                The game would continue and switch among the opposite player until it reaches either one of the following conditions:
                - Player 1 wins.
                - Player 2 wins.
                - Draw.""";

        /**
         * Show help message when instruction was pressed.
         *
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(menuBar, message, "Message", JOptionPane.PLAIN_MESSAGE);
        }
    }
}
