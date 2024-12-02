package kr.jbnu.se.std;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;

/**
 * Create a JPanel on which we draw and listen for keyboard and mouse events.
 * 
 * @author www.gametutorial.net
 */

public abstract class Canvas extends JPanel implements KeyListener, MouseListener {
    
    // Keyboard states - Here are stored states for keyboard keys - is it down or not.
    private static final boolean[] keyboardState = new boolean[525];
    
    // Mouse states - Here are stored states for mouse keys - is it down or not.
    private static final boolean[] mouseState = new boolean[3];



    public Canvas()
    {
        // We use double buffer to draw on the screen.
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setBackground(Color.black);
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        // If you will draw your own mouse cursor or if you just want that mouse cursor disapear,
        // insert "true" into if condition and mouse cursor will be removed.
        if(true)
        {
            BufferedImage blankCursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(blankCursorImg, new Point(0, 0), null);
            this.setCursor(blankCursor);
        }

        // Adds the keyboard listener to JPanel to receive key events from this component.
        this.addKeyListener(this);
        // Adds the mouse listener to JPanel to receive mouse events from this component.
        this.addMouseListener(this);
    }
    
    
    // This method is overridden in kr.jbnu.se.std.Framework.java and is used for drawing to the screen.
    public abstract void Draw(Graphics2D g2d);
    
    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;        
        super.paintComponent(g2d);        
        Draw(g2d);
    }
       
    
    // Keyboard
    /**
     * Is keyboard key "key" down?
     * 
     * @param key Number of key for which you want to check the state.
     * @return true if the key is down, false if the key is not down.
     */
    public static boolean keyboardKeyState(int key)
    {
        return keyboardState[key];
    }

    // Methods of the keyboard listener.
    @Override
    public void keyPressed(KeyEvent e) {
        ArrayList<Weapon> weapons = Game.getWeapons();
        requestFocusInWindow();
        if (this.hasFocus()) {//테스트코드임 끝나면 지워
            System.out.println("asdfasdf");
        }
        keyboardState[e.getKeyCode()] = true;
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_1:
                Game.changeWeapon(weapons.get(0));
                break;
            case KeyEvent.VK_2:
                if(weapons.size() >= 2) {
                    Game.changeWeapon(weapons.get(1));
                }
                break;
            case KeyEvent.VK_3:
                if(weapons.size() >= 3) {
                    Game.changeWeapon(weapons.get(2));
                }
                break;
            case KeyEvent.VK_4:
                if(weapons.size() >= 4) {
                    Game.changeWeapon(weapons.get(3));
                }
                break;
            case KeyEvent.VK_5:
                if(weapons.size() >= 5) {
                    Game.changeWeapon(weapons.get(4));
                }
                break;
            case KeyEvent.VK_R:
                if (Game.getRubberduckSkill() > 0) {
                    Game.setRubberduckSkill(Game.getRubberduckSkill()-1);
                    //여기에 999스킬 발동을 넣어야 함
                }
                break;
            default:
                break;
        }
    }




    @Override
    public void keyReleased(KeyEvent e) {
        keyboardState[e.getKeyCode()] = false;
    }
    
    @Override
    public void keyTyped(KeyEvent e) { }

    public void keyReleasedFramework(KeyEvent e) {
            int keyCode = e.getKeyCode();

    }
    // Mouse
    /**
     * Is mouse button "button" down?
     * Parameter "button" can be "MouseEvent.BUTTON1" - Indicates mouse button #1
     * or "MouseEvent.BUTTON2" - Indicates mouse button #2 ...
     * 
     * @param button Number of mouse button for which you want to check the state.
     * @return true if the button is down, false if the button is not down.
     */
    public static boolean mouseButtonState(int button)
    {
        return mouseState[button - 1];
    }
    
    // Sets mouse key status.
    private void mouseKeyStatus(MouseEvent e, boolean status)
    {
        int getButton = e.getButton();

        switch (getButton){
            case MouseEvent.BUTTON1:
                mouseState[0] = status;
                break;
            case MouseEvent.BUTTON2:
                mouseState[1] = status;
                break;
            case MouseEvent.BUTTON3:
                mouseState[2] = status;
                break;
            default:
                break;
        }
    }
    public boolean checkWeapon(String weaponname, ArrayList<Weapon> weapon){
        for(Weapon changingweapon: weapon){
            if(changingweapon.getName().equals(weaponname)) return true;
        }
        return false;
    }

    // Methods of the mouse listener.
    @Override
    public void mousePressed(MouseEvent e)
    {
        mouseKeyStatus(e, true);
    }
    
    @Override
    public void mouseReleased(MouseEvent e)
    {
        mouseKeyStatus(e, false);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) { }
    
    @Override
    public void mouseEntered(MouseEvent e) { }
    
    @Override
    public void mouseExited(MouseEvent e) { }
    
}
