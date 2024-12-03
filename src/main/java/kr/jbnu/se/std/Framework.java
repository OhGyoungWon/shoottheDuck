package kr.jbnu.se.std;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * kr.jbnu.se.std.Framework that controls the game (kr.jbnu.se.std.Game.java) that created it, update it and draw it on the screen.
 * 
 * @author www.gametutorial.net
 */

public class Framework extends Canvas {
    
    /**
     * Width of the frame.
     */
    public static int frameWidth;
    /**
     * Height of the frame.
     */
    public static int frameHeight;

    /**
     * Time of one second in nanoseconds.
     * 1 second = 1 000 000 000 nanoseconds
     */
    public static final long SEC_IN_NANOSECOND = 1000000000L;
    
    /**
     * Time of one millisecond in nanoseconds.
     * 1 millisecond = 1 000 000 nanoseconds
     */
    public static final long MILISEC_IN_NANOSEC = 1000000L;
    
    /**
     * FPS - Frames per second
     * How many times per second the game should update?
     */
    private final int GAME_FPS = 60;
    /**
     * Pause between updates. It is in nanoseconds.
     */
    private final long GAME_UPDATE_PERIOD = SEC_IN_NANOSECOND / GAME_FPS;
    private static boolean userPressedEscape = false;

    public boolean isUserPressedEscape(){ return userPressedEscape; }
    public static void setUserPressedEscape(boolean isEscape) { Framework.userPressedEscape = isEscape; }

    /**
     * Possible states of the game
     */
    public enum GameState{STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAMEOVER, DESTROYED}
    /**
     * Current state of the game
     */
    public static GameState gameState;
    
    /**
     * Elapsed game time in nanoseconds.
     */
    private static long gameTime;
    // It is used for calculating elapsed time.
    private static long lastTime;
    
    // The actual game
    private transient Game game;

    
    /**
     * Image for menu.
     */
    private transient BufferedImage shootTheDuckMenuImg;
    private transient BufferedImage leaderboardImg;
    
    public Framework ()
    {
        super();

        gameState = GameState.VISUALIZING;
        
        //We start game in new thread.
        Thread gameThread;
        gameThread = new Thread(this::GameLoop);
        gameThread.start();
    }
    
    
   /**
     * Set variables and objects.
     * This method is intended to set the variables and objects for this class, variables and objects for the actual game can be set in kr.jbnu.se.std.Game.java.
     */
    private void Initialize()
    {
        //...
    }
    
    /**
     * Load files - images, sounds, ...
     * This method is intended to load files for this class, files for the actual game can be loaded in kr.jbnu.se.std.Game.java.
     */
    private void LoadContent()
    {
        try
        {
            URL shootTheDuckMenuImgUrl = this.getClass().getResource("/images/menu.jpg");
            assert shootTheDuckMenuImgUrl != null;
            shootTheDuckMenuImg = ImageIO.read(shootTheDuckMenuImgUrl);
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            URL leaderboardImgUrl = this.getClass().getResource("/images/leaderboard.png");
            assert leaderboardImgUrl != null;
            leaderboardImg = ImageIO.read(leaderboardImgUrl);
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * In specific intervals of time (GAME_UPDATE_PERIOD) the game/logic is updated and then the game is drawn on the screen.
     */
    private boolean isRunning = true; // Flag to control the loop

    private void GameLoop() {
        // Variables for visualizing state
        long visualizingTime = 0, lastVisualizingTime = System.nanoTime();

        // Variables for frame timing
        long beginTime, timeTaken, timeLeft;

        while (isRunning) { // Loop runs as long as isRunning is true
            beginTime = System.nanoTime();

            switch (gameState) {
                case PLAYING:
                    gameTime += System.nanoTime() - lastTime;
                    game.UpdateGame(gameTime, mousePosition());
                    lastTime = System.nanoTime();
                    break;

                case GAMEOVER:
                    checkForExitCondition(); // Check if we should stop
                    break;

                case MAIN_MENU:
                    // Menu logic (e.g., waiting for user input)
                    break;

                case OPTIONS:
                    // Options menu logic
                    break;

                case GAME_CONTENT_LOADING:
                    // Loading content logic
                    break;

                case STARTING:
                    Initialize();
                    LoadContent();
                    gameState = GameState.MAIN_MENU;
                    break;

                case VISUALIZING:
                    if (this.getWidth() > 1 && visualizingTime > SEC_IN_NANOSECOND) {
                        frameWidth = this.getWidth();
                        frameHeight = this.getHeight();
                        gameState = GameState.STARTING;
                    } else {
                        visualizingTime += System.nanoTime() - lastVisualizingTime;
                        lastVisualizingTime = System.nanoTime();
                    }
                    break;

                case DESTROYED:
                    stopGame(); // Exit the loop when the game is destroyed
                    break;
            }

            repaint(); // Repaint the screen

            // Calculate sleep time
            timeTaken = System.nanoTime() - beginTime;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / MILISEC_IN_NANOSEC;
            if (timeLeft < 10) timeLeft = 10;

            try {
                Thread.sleep(timeLeft);
            } catch (InterruptedException ex) {
                // Handle thread interruption
            }
        }
    }

    // Method to stop the game loop
    private void stopGame() {
        isRunning = false;
    }

    // Check for exit conditions in the GAMEOVER state
    private void checkForExitCondition() {
        if (gameState == GameState.GAMEOVER && userPressedEscape) {
            stopGame();
        }
    }



    /**
     * Draw the game to the screen. It is called through repaint() method in GameLoop() method.
     */
    @Override
    public void Draw(Graphics2D g2d)
    {
        switch (gameState)
        {
            case PLAYING:
                game.Draw(g2d, mousePosition());
                if(Game.getLevel()%5 == 1){
                    applyTintFilter(g2d, new Color(0, 0, 0, 0)); //낮
                } else if (Game.getLevel()%5 == 2){
                    applyTintFilter(g2d, new Color(255, 100, 50, 50)); //노을
                } else if (Game.getLevel()%5 == 3){
                    applyTintFilter(g2d, new Color(0, 0, 50, 75)); //밤
                } else if (Game.getLevel()%5 == 4){
                    applyTintFilter(g2d, new Color(255, 200, 150, 50)); //여명
                } else if (Game.getLevel()%5 == 0){
                    applyTintFilter(g2d, new Color(150, 200, 255, 40)); //아침
                }
                break;
            case GAMEOVER:
                game.DrawGameOver(g2d, mousePosition());
            break;
            case MAIN_MENU:
                g2d.drawImage(shootTheDuckMenuImg, 0, 0, frameWidth, frameHeight, null);
                g2d.drawImage(leaderboardImg, 0, 70, frameWidth, frameHeight, null);
                Leaderboard.drawLeaderboard(g2d, Framework.frameWidth, Framework.frameHeight);
            break;
            case OPTIONS:
                //...
            break;
            case GAME_CONTENT_LOADING:
                g2d.setColor(Color.white);
                g2d.drawString("GAME is LOADING", frameWidth / 2 - 50, frameHeight / 2);
            break;
            default:
                break;
        }
    }

    private void applyTintFilter(Graphics2D g2d, Color color) {
        g2d.setColor(color);
        g2d.fillRect(0, 0, frameWidth, frameHeight);
    }
    
    /**
     * Starts new game.
     */
    private void newGame()
    {
        // We set gameTime to zero and lastTime to current time for later calculations.
        gameTime = 0;
        lastTime = System.nanoTime();
        
        game = new Game();
    }
    
    /**
     *  Restart game - reset game time and call RestartGame() method of game object so that reset some variables.
     */
    private void restartGame()
    {
        // We set gameTime to zero and lastTime to current time for later calculations.
        gameTime = 0;
        lastTime = System.nanoTime();
        
        game.RestartGame();
        
        // We change game status so that the game can start.
        gameState = GameState.PLAYING;
    }
    
    /**
     * Returns the position of the mouse pointer in game frame/window.
     * If mouse position is null than this method return 0,0 coordinate.
     * 
     * @return Point of mouse coordinates.
     */
    private Point mousePosition()
    {
        try
        {
            Point mp = this.getMousePosition();
            
            if(mp != null)
                return this.getMousePosition();
            else
                return new Point(0, 0);
        }
        catch (Exception e)
        {
            return new Point(0, 0);
        }
    }
    
    /**
     * This method is called when keyboard key is released.
     * 
     * @param e KeyEvent
     */
    @Override
    public void keyReleasedFramework(KeyEvent e)
    {
        switch (gameState)
        {
            case GAMEOVER:
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    System.exit(0);
                else if(e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER)
                    restartGame();
            break;
            case PLAYING:
                //
            break;
            case MAIN_MENU:
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    System.exit(0);
                break;
            default:
                break;
        }
    }
    
    /**
     * This method is called when mouse button is clicked.
     * 
     * @param e MouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
        switch (gameState)
        {
            case MAIN_MENU:
                if(e.getButton() == MouseEvent.BUTTON1)
                    newGame();
                break;
            default:
                break;
        }
    }

}
