import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;

/**
 * A grid on the board.
 * Records and displays if any mark was placed. Can be clicked as a JButton to make a move.
 *
 * @author ZHU Ziyao - 3035772145
 * @version 0.1
 */
class Grid extends JButton {
    private int status = -1;
    private static final Dimension preferredSize = new Dimension(100, 100);
    private static final Font font = new Font("Arial", Font.BOLD, 48);

    /**
     * Allocates a new Grid object for a single grid on the game board.
     */
    public Grid() {
        setPreferredSize(preferredSize);
        setFont(font);
        setEnabled(false);
    }

    /**
     * Place a mark on the grid.
     *
     * @param player The player who made the move.
     */
    public void setStatus(int player) {
        this.status = player;
        if (status == 0) {
            setUI(new MetalButtonUI() {
                protected Color getDisabledTextColor() {
                    return Color.BLUE;
                }
            });
            setText("X");
        } else {
            setUI(new MetalButtonUI() {
                protected Color getDisabledTextColor() {
                    return Color.ORANGE;
                }
            });
            setText("O");
        }
    }

    /**
     * Enable the grid if no mark has been placed.
     */
    public void unlock() {
        setEnabled(status == -1);
    }
}
