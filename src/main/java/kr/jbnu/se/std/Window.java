package kr.jbnu.se.std;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.io.IOException;

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
            // Size of the frame.
            this.setSize(800, 600);
            // Puts frame to center of the screen.
            this.setLocationRelativeTo(null);
            // So that frame cannot be resizable by the user.
            this.setResizable(false);
        }

        // Exit the application when user close frame.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setContentPane(new LoginUI(this));

        this.setVisible(true);
    }

    public void switchToFramework() {
        this.setContentPane(new Framework());
        this.revalidate();  // 화면을 다시 그리도록 요청
        this.repaint();  // 변경 사항 반영
    }

    public void switchToLogin() {
        this.setContentPane(new LoginUI(this));
        this.revalidate();  // 화면을 다시 그리도록 요청
        this.repaint();  // 변경 사항 반영
    }

    public void switchToSignUp() {
        this.setContentPane(new SignUpUI(this));
        this.revalidate();  // 화면을 다시 그리도록 요청
        this.repaint();  // 변경 사항 반영
    }

    public static void main(String[] args) throws IOException {
        // Use the event dispatch thread to build the UI for thread-safety.
        FirebaseConfig.initialize();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Window();
            }
        });
    }
}
