package kr.jbnu.se.std;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Creates frame and set its properties.
 * 
 * @author www.gametutorial.net
 */

public class Window extends JFrame{

    private Window()
    {
        // Sets the title for this frame.
        this.setTitle("Shoot the duck");

        // Sets size of the frame.
        if(false) // Full screen mode
        {
            // Disables decorations for this frame.
            this.setUndecorated(true);
            // Puts the frame to full screen.
            this.setExtendedState(this.MAXIMIZED_BOTH);
        }
        else // kr.jbnu.se.std.Window mode
        {
            this.setTitle("Shoot the duck");
            // Size of the frame.
            this.setSize(800, 600);
            // Puts frame to center of the screen.
            this.setLocationRelativeTo(null);
            // So that frame cannot be resizable by the user.
            this.setResizable(false);
        }

        // Exit the application when user close frame.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(new Framework());
        this.setVisible(true);
    }

    public static void main(String[] args)
    {
        // Use the event dispatch thread to build the UI for thread-safety.
        SwingUtilities.invokeLater(() -> {
            try {
                FirebaseInitializer.initialize(); // Firebase 초기화
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LoginUI().setVisible(true);   // 로그인 UI 표시
        });
    }
}
