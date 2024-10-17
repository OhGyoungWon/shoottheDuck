package kr.jbnu.se.std;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Actual game.
 * 
 * @author www.gametutorial.net
 */

public class Game {
    
    /**
     * We use this to generate a random number.
     */
    private Random random;
    
    /**
     * Font that we will use to write statistic to the screen.
     */
    private Font font;
    
    /**
     * Array list of the ducks.
     */
    private ArrayList<Duck> ducks;
    private ArrayList<Superduck> superducks;
    
    /**
     * How many ducks leave the screen alive?
     */
    private int runawayDucks;
    
   /**
     * How many ducks the player killed?
     */
    private int killedDucks;
    
    /**
     * For each killed duck, the player gets points.
     */
    private int score;
    private int money;
    private int GameLevel;
    
   /**
     * How many times a player is shot?
     */
    private int shoots;
    
    /**
     * Last time of the shoot.
     */
    private long lastTimeShoot;    
    /**
     * The time which must elapse between shots.
     */
    private long timeBetweenShots;

    /**
     * kr.jbnu.se.std.Game background image.
     */
    private BufferedImage backgroundImg;
    
    /**
     * Bottom grass.
     */
    private BufferedImage grassImg;
    
    /**
     * kr.jbnu.se.std.Duck image.
     */
    private BufferedImage duckImg;
    
    /**
     * Shotgun sight image.
     */
    private BufferedImage sightImg;

    private BufferedImage revImg;
    private BufferedImage smgImg;
    private BufferedImage rifImg;
    private BufferedImage odinImg;
    private BufferedImage superduckImg;
    /**
     * Middle width of the sight image.
     */
    private int sightImgMiddleWidth;
    /**
     * Middle height of the sight image.
     */
    private int sightImgMiddleHeight;

    private static Weapon currentweapon;
    public static ArrayList<Weapon> weapons = new ArrayList<>();
    

    public Game()
    {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;
        
        Thread threadForInitGame = new Thread() {
            @Override
            public void run(){
                // Sets variables and objects for the game.
                Initialize();
                // Load game files (images, sounds, ...)
                LoadContent();
                
                Framework.gameState = Framework.GameState.PLAYING;
            }
        };
        threadForInitGame.start();
    }
    
    
   /**
     * Set variables and objects for the game.
     */
    private void Initialize()
    {
        random = new Random();
        font = new Font("monospaced", Font.BOLD, 18);
        
        ducks = new ArrayList<Duck>();
        superducks = new ArrayList<Superduck>();
        weapons.add(new Weapon.Revolver(revImg));
        weapons.add(new Weapon.SMG(smgImg));
        weapons.add(new Weapon.Rifle(rifImg));
        weapons.add(new Weapon.Odin(odinImg));
        currentweapon = weapons.get(0);
        runawayDucks = 0;
        killedDucks = 15;
        score = 0;
        money = 0;
        shoots = 0;
        GameLevel = 1;
        
        lastTimeShoot = 0;
        timeBetweenShots = currentweapon.fireDelay;
    }
    
    /**
     * Load game files - images, sounds, ...
     */
    public static void handleKeyEvent(KeyEvent e) {//무기 교체 개같이 실패
        // 여기에서 키 이벤트에 따른 무기 교체 로직을 처리
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_1:
                changeweapon(0);
                break;
            case KeyEvent.VK_2:
                changeweapon(1);
                break;
            case KeyEvent.VK_3:
                changeweapon(2);
                break;
            case KeyEvent.VK_4:
                changeweapon(3);
                break;
        }
    }
    private void LoadContent()
    {
        try
        {
            URL backgroundImgUrl = this.getClass().getResource("/images/background.jpg");
            backgroundImg = ImageIO.read(backgroundImgUrl);
            
            URL grassImgUrl = this.getClass().getResource("/images/grass.png");
            grassImg = ImageIO.read(grassImgUrl);
            
            URL duckImgUrl = this.getClass().getResource("/images/duck.png");
            duckImg = ImageIO.read(duckImgUrl);
            
            URL sightImgUrl = this.getClass().getResource("/images/sight.png");
            sightImg = ImageIO.read(sightImgUrl);
            sightImgMiddleWidth = sightImg.getWidth() / 2;
            sightImgMiddleHeight = sightImg.getHeight() / 2;

            URL odinImgUrl = this.getClass().getResource("/images/odin.png");
            odinImg = ImageIO.read(odinImgUrl);

            URL revImgUrl = this.getClass().getResource("/images/revolver.png");
            revImg = ImageIO.read(revImgUrl);

            URL smgImgUrl = this.getClass().getResource("/images/smg.png");
            smgImg = ImageIO.read(smgImgUrl);

            URL rifImgUrl = this.getClass().getResource("/images/rifle.png");
            rifImg = ImageIO.read(rifImgUrl);

            URL superduckImgUrl = this.getClass().getResource("/images/superduck.png");
            superduckImg = ImageIO.read(superduckImgUrl);
        }
        catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * Restart game - reset some variables.
     */
    public void RestartGame()
    {
        // Removes all of the ducks from this list.
        ducks.clear();
        
        // We set last duckt time to zero.
        Duck.lastDuckTime = 0;

        ducks = new ArrayList<Duck>();
        superducks = new ArrayList<Superduck>();
        weapons.add(new Weapon.Revolver(revImg));
        currentweapon = weapons.get(0);
        runawayDucks = 0;
        killedDucks = 0;
        score = 0;
        money = 0;
        shoots = 0;
        GameLevel = 1;

        lastTimeShoot = 0;
        timeBetweenShots = currentweapon.fireDelay;
    }
    
    
    /**
     * Update game logic.
     * 
     * @param gameTime gameTime of the game.
     * @param mousePosition current mouse position.
     */
    public void UpdateGame(long gameTime, Point mousePosition)
    {
        // Creates a new duck, if it's the time, and add it to the array list.
        if(System.nanoTime() - Duck.lastDuckTime >= Duck.timeBetweenDucks && superducks.isEmpty())
        {
            // Here we create new duck and add it to the array list.
            ducks.add(new Duck(Duck.duckLines[Duck.nextDuckLines][0] + random.nextInt(200), Duck.duckLines[Duck.nextDuckLines][1], Duck.duckLines[Duck.nextDuckLines][2], Duck.duckLines[Duck.nextDuckLines][3], duckImg));
            
            // Here we increase nextDuckLines so that next duck will be created in next line.
            Duck.nextDuckLines++;
            if(Duck.nextDuckLines >= Duck.duckLines.length)
                Duck.nextDuckLines = 0;
            
            Duck.lastDuckTime = System.nanoTime();
        }
        if(killedDucks % 20 == 0 && killedDucks != 0 && superducks.isEmpty()) {
            superducks.add(new Superduck(Superduck.duckLines[Duck.nextDuckLines][0] + random.nextInt(200),
                    Superduck.duckLines[Duck.nextDuckLines][1],
                    Superduck.duckLines[Duck.nextDuckLines][2],
                    Superduck.duckLines[Duck.nextDuckLines][3],
                    superduckImg));
            Superduck.hp = 10*GameLevel;

            // Increase nextDuckLines for the next duck.
            Superduck.nextDuckLines++;
            if (Superduck.nextDuckLines >= Superduck.duckLines.length)
                Superduck.nextDuckLines = 0;

            Superduck.lastDuckTime = System.nanoTime();
        }
        
        // Update all of the ducks.
        for(int i = 0; i < ducks.size(); i++)
        {
            // Move the duck.
            ducks.get(i).Update();
            
            // Checks if the duck leaves the screen and remove it if it does.
            if(ducks.get(i).x < 0 - duckImg.getWidth())
            {
                ducks.remove(i);
                runawayDucks++;
            }
        }
        for (int i = 0; i < superducks.size(); i++) {
            superducks.get(i).Update();

            // Check if the superduck leaves the screen and remove it if it does.
            if (superducks.get(i).x < -superduckImg.getWidth()) {
                superducks.remove(i);
                runawayDucks = 999;
            }
        }



        // Does player shoots?
        if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {
            // Checks if it can shoot again.
            if (System.nanoTime() - lastTimeShoot >= timeBetweenShots) {
                shoots++;
                lastTimeShoot = System.nanoTime();
                // We go over all the ducks and we look if any of them was shot.
                for (int i = 0; i < ducks.size(); i++) {
                    // We check if the mouse was over ducks head or body when player has shot.
                    if (new Rectangle(ducks.get(i).x + 18, ducks.get(i).y, 27, 30).contains(mousePosition) ||
                            new Rectangle(ducks.get(i).x + 30, ducks.get(i).y + 30, 88, 25).contains(mousePosition)) {
                        Duck.hp-= currentweapon.getDamage();
                        if(Duck.hp <= 0) {
                            killedDucks++;
                            score += ducks.get(i).score;
                            money += ducks.get(i).score;
                            // Remove the duck from the array list.
                            Duck.hp = GameLevel;
                            ducks.remove(i);

                            // We found the duck that player shot so we can leave the for loop.
                            break;
                        }
                    }
                }

                // Check for shooting superducks
                for (int i = 0; i < superducks.size(); i++) {
                    if (new Rectangle(superducks.get(i).x, superducks.get(i).y, superduckImg.getWidth(), superduckImg.getHeight())
                            .contains(mousePosition)) {
                        Superduck.hp-= currentweapon.getDamage();
                        if (Superduck.hp <= 0) {
                            killedDucks++;
                            score += superducks.get(i).score;
                            money += superducks.get(i).score;
                            // Remove the duck from the array list.
                            superducks.clear();
                            GameLevel++;
                            Superduck.hp = 10*GameLevel;
                            weapons.add(new Weapon.SMG(smgImg));
                            break;
                        }
                    }
                }

                lastTimeShoot = System.nanoTime();
            }
        }

        // When 200 ducks runaway, the game ends.
        if(runawayDucks >= 200)
            Framework.gameState = Framework.GameState.GAMEOVER;
    }
    
    /**
     * Draw the game to the screen.
     * 
     * @param g2d Graphics2D
     * @param mousePosition current mouse position.
     */

    public void Draw(Graphics2D g2d, Point mousePosition)
    {
        g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        
        // Here we draw all the ducks.
        for(int i = 0; i < ducks.size(); i++)
        {
            ducks.get(i).Draw(g2d);
        }
        for(int i = 0; i < superducks.size(); i++)
        {
            superducks.get(i).Draw(g2d);
        }
        
        g2d.drawImage(grassImg, 0, Framework.frameHeight - grassImg.getHeight(), Framework.frameWidth, grassImg.getHeight(), null);
        
        g2d.drawImage(sightImg, mousePosition.x - sightImgMiddleWidth, mousePosition.y - sightImgMiddleHeight, null);
        
        g2d.setFont(font);
        g2d.setColor(Color.darkGray);
        
        g2d.drawString("RUNAWAY: " + runawayDucks, 10, 21);
        g2d.drawString("KILLS: " + killedDucks, 160, 21);
        g2d.drawString("SHOOTS: " + shoots, 299, 21);
        g2d.drawString("SCORE: " + score, 440, 21);
        g2d.drawString("MONEY: " + money, 560, 21);
        g2d.drawString("LEVEL: " + GameLevel, 700, 21);
        g2d.drawString("WEAPON : " + currentweapon.getName(), 820, 21);
    }
    
    
    /**
     * Draw the game over screen.
     * 
     * @param g2d Graphics2D
     * @param mousePosition Current mouse position.
     */
    public void DrawGameOver(Graphics2D g2d, Point mousePosition)
    {
        Draw(g2d, mousePosition);
        
        // The first text is used for shade.
        g2d.setColor(Color.black);
        g2d.drawString("kr.jbnu.se.std.Game Over", Framework.frameWidth / 2 - 39, (int)(Framework.frameHeight * 0.65) + 1);
        g2d.drawString("Press space or enter to restart.", Framework.frameWidth / 2 - 149, (int)(Framework.frameHeight * 0.70) + 1);
        g2d.setColor(Color.red);
        g2d.drawString("kr.jbnu.se.std.Game Over", Framework.frameWidth / 2 - 40, (int)(Framework.frameHeight * 0.65));
        g2d.drawString("Press space or enter to restart.", Framework.frameWidth / 2 - 150, (int)(Framework.frameHeight * 0.70));
    }

    public static void changeweapon(int n) {
        if(weapons.size() > n){
            currentweapon = weapons.get(n);
        }
    }

}
