package kr.jbnu.se.std;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.io.IOException;

/**
 * Creates frame and set its properties.
 *
 * @author www.gametutorial.net
 */

public class Window extends JFrame {

    private Window() {
        // Sets the title for this frame.
        this.setTitle("Shoot the duck");

        // Sets size of the frame.
        if (true) { // Full screen mode
            // Disables decorations for this frame.
            this.setUndecorated(true);
            // Puts the frame to full screen.
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else { // Windowed mode
            // Size of the frame.
            this.setSize(1280, 720);
            // Puts frame to center of the screen.
            this.setLocationRelativeTo(null);
            // So that frame cannot be resizable by the user.
            this.setResizable(false);
        }

        // Exit the application when user close frame.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add the Framework panel (game loop)
        this.setContentPane(new Framework());

        this.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        // Use the event dispatch thread to build the UI for thread-safety.
        SwingUtilities.invokeLater(new Runnable() {
            @Override

            public void run() {
                new Window();
            }
        });
    }
}
