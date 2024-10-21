package kr.jbnu.se.std;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
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
    private ArrayList<Weaponduck.Smgduck> smgduck;
    private ArrayList<Weaponduck.Rifduck> rifduck;
    private ArrayList<Weaponduck.Odinduck> odinduck;
    private ArrayList<Itemduck.chickenfly> chickenflies;
    private ArrayList<Itemduck.cokefly> cokeflies;
    private ArrayList<Itemduck.pizzafly> pizzaflies;

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
    private BufferedImage superduckImg;
    
    /**
     * Shotgun sight image.
     */
    private BufferedImage sightImg;
    private BufferedImage revImg;
    private BufferedImage smgImg;
    private BufferedImage rifImg;
    private BufferedImage odinImg;

    /**
     * Middle width of the sight image.
     */
    private int sightImgMiddleWidth;
    /**
     * Middle height of the sight image.
     */
    private int sightImgMiddleHeight;

    public Weapon currentweapon;

    public static Levels lvdata;
    public static int gamelevel;
    public static int money;


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
        
        ducks = new ArrayList<>();
        superducks = new ArrayList<>();
        chickenflies = new ArrayList<>();
        cokeflies = new ArrayList<>();
        pizzaflies = new ArrayList<>();
        smgduck = new ArrayList<>();
        rifduck = new ArrayList<>();
        odinduck = new ArrayList<>();

        runawayDucks = 0;
        killedDucks = 15;
        score = 0;
        money = 0;
        shoots = 0;
        gamelevel = 1;
        lvdata = getlvdata();
        currentweapon = new Weapon.Revolver(revImg);

        lastTimeShoot = 0;
        timeBetweenShots = currentweapon.fireDelay;
    }

    /**
     * Load game files - images, sounds, ...
     */
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

            URL superduckImgUrl = this.getClass().getResource("/images/superduck.png");
            superduckImg = ImageIO.read(superduckImgUrl);
            
            URL sightImgUrl = this.getClass().getResource("/images/sight.png");
            sightImg = ImageIO.read(sightImgUrl);
            sightImgMiddleWidth = sightImg.getWidth() / 2;
            sightImgMiddleHeight = sightImg.getHeight() / 2;

            URL revImgUrl = this.getClass().getResource("/images/revolver.png");
            revImg = ImageIO.read(revImgUrl);

            URL smgImgUrl = this.getClass().getResource("/images/smg.png");
            smgImg = ImageIO.read(smgImgUrl);

            URL rifImgUrl = this.getClass().getResource("/images/rifle.png");
            rifImg = ImageIO.read(rifImgUrl);

            URL odinImgUrl = this.getClass().getResource("/images/odin.png");
            odinImg = ImageIO.read(odinImgUrl);
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

        runawayDucks = 0;
        killedDucks = 0;
        score = 0;
        money = 0;
        shoots = 0;
        gamelevel = 1;
        currentweapon = new Weapon.Revolver(revImg);

        lastTimeShoot = 0;
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
        if(System.nanoTime() - Duck.lastDuckTime >= lvdata.sumdly && superducks.isEmpty())
        {
            // Here we create new duck and add it to the array list.
            ducks.add(new Duck(Duck.duckLines[Duck.nextDuckLines][0] + random.nextInt(200),
                    Duck.duckLines[Duck.nextDuckLines][1], lvdata.speed, lvdata.ducksc, lvdata.duckhp, duckImg));

            // Here we increase nextDuckLines so that next duck will be created in next line.
            Duck.nextDuckLines++;
            if(Duck.nextDuckLines >= Duck.duckLines.length)
                Duck.nextDuckLines = 0;

            Duck.lastDuckTime = System.nanoTime();
        }
        if(killedDucks % 20 == 0 && killedDucks != 0 && superducks.isEmpty()){
            superducks.add(new Superduck(Duck.duckLines[Duck.nextDuckLines][0] + random.nextInt(200),
                    (int) (Framework.frameHeight*0.6), lvdata.speed/3, lvdata.bosssc, superduckImg));
        }
        if(killedDucks == 30 && smgduck.isEmpty()){
            smgduck.add(new Weaponduck.Smgduck(Framework.frameWidth + random.nextInt(200),
                    (int) (Framework.frameHeight*0.6), lvdata.speed, lvdata.ducksc*2, lvdata.duckhp*2, smgImg ));
        }

        // Update all of the ducks.
        for(int i = 0; i < ducks.size(); i++) {
            // Move the duck.
            ducks.get(i).Update();

            // Checks if the duck leaves the screen and remove it if it does.
            if(ducks.get(i).x < 0 - duckImg.getWidth())
            {
                ducks.remove(i);
                runawayDucks++;
            }
        }
        for(int i = 0; i < superducks.size(); i++){
            superducks.get(i).Update();

            // Checks if the duck leaves the screen and remove it if it does.
            if(superducks.get(i).x < 0 - superduckImg.getWidth())
            {
                superducks.remove(i);
                runawayDucks = 999;
            }
        }

        // Does player shoots?
        if(Canvas.mouseButtonState(MouseEvent.BUTTON1))
        {
            // Checks if it can shoot again.
            if(System.nanoTime() - lastTimeShoot >= timeBetweenShots)
            {
                shoots++;

                // We go over all the ducks and we look if any of them was shoot.
                for(int i = 0; i < ducks.size(); i++)
                {
                    // We check, if the mouse was over ducks head or body, when player has shot.
                    if(new Rectangle(ducks.get(i).x + 18, ducks.get(i).y, 27, 30).contains(mousePosition) ||
                       new Rectangle(ducks.get(i).x + 30, ducks.get(i).y + 30, 88, 25).contains(mousePosition))
                    {
                        ducks.get(i).hp-=currentweapon.getDamage();

                        if(ducks.get(i).hp <= 0){
                            killedDucks++;
                            score += ducks.get(i).score;
                            money += ducks.get(i).score;

                            // Remove the duck from the array list.
                            ducks.remove(i);

                            // We found the duck that player shoot so we can leave the for loop.
                            break;
                        }
                    }
                }
                for(int i = 0; i < superducks.size(); i++){
                    if (new Rectangle(superducks.get(i).x, superducks.get(i).y,
                            superduckImg.getWidth(), superduckImg.getHeight()).contains(mousePosition)){
                        if(!superducks.isEmpty() && new Rectangle(superducks.get(i).x, superducks.get(i).y,
                                superduckImg.getWidth(), superduckImg.getHeight()).contains(mousePosition))
                        {
                            superducks.get(i).hp-=currentweapon.getDamage();

                            if(superducks.get(i).hp <= 0){
                                killedDucks++;
                                score += superducks.get(i).score;
                                money += superducks.get(i).score;

                                // Remove the duck from the array list.
                                superducks.remove(i);
                                gamelevel++;

                                // We found the duck that player shoot so we can leave the for loop.
                                break;
                            }
                        }
                    }
                }



                lastTimeShoot = System.nanoTime();
            }
        }

        // When 200 ducks runaway, the game ends.
        if(runawayDucks >= 100)
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
        for(int i = 0; i < ducks.size(); i++) {
            ducks.get(i).Draw(g2d);
        }
        for(int i = 0; i < superducks.size(); i++){
            superducks.get(i).Draw(g2d);
        }
        for(int i = 0; i < smgduck.size(); i++){
            smgduck.get(i).Draw(g2d);
        }


        g2d.drawImage(grassImg, 0, Framework.frameHeight - grassImg.getHeight(), Framework.frameWidth, grassImg.getHeight(), null);

        g2d.drawImage(sightImg, mousePosition.x - sightImgMiddleWidth, mousePosition.y - sightImgMiddleHeight, null);

        g2d.setFont(font);
        g2d.setColor(Color.darkGray);

        g2d.drawString("RUNAWAY: " + runawayDucks, 10, 21);
        g2d.drawString("KILLS: " + killedDucks, 160, 21);
        g2d.drawString("SHOOTS: " + shoots, 299, 21);
        g2d.drawString("SCORE: " + score, 440, 21);
        g2d.drawString("Money: " + money, 560, 21);
        g2d.drawString("Weapon: " + currentweapon.getName(), 680, 21);
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
        g2d.drawString("Your score is " + score + ".", Framework.frameWidth / 2 - 39, (int)(Framework.frameHeight * 0.65) + 1);
        g2d.drawString("Press space or enter to restart.", Framework.frameWidth / 2 - 149, (int)(Framework.frameHeight * 0.70) + 1);
        g2d.setColor(Color.red);
        g2d.drawString("kr.jbnu.se.std.Game Over", Framework.frameWidth / 2 - 40, (int)(Framework.frameHeight * 0.65));
        g2d.drawString("Press space or enter to restart.", Framework.frameWidth / 2 - 150, (int)(Framework.frameHeight * 0.70));
    }

    public int getScore(){
        return score;
    }
    public static Levels getlvdata(){
        if(gamelevel == 1){
            return new Levels.lev1();
        }
        else if(gamelevel == 2){
            return new Levels.lev2();
        }
        else if(gamelevel == 3){
            return new Levels.lev3();
        }
        else{
            return new Levels.lev4();
        }
    }
}
