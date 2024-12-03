package kr.jbnu.se.std;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * The duck class.
 * 
 * @author www.gametutorial.net
 */

public class Duck implements Damageable {
    
    /**
     * How much time must pass in order to create a new duck?
     */
    /**
     * Last time when the duck was created.
     */
    public static long lastDuckTime = 0;
    
    /**
     * kr.jbnu.se.std.Duck lines.
     * Where is starting location for the duck?
     * Speed of the duck?
     * How many points is a duck worth?
     */
    public static int[][] duckLines = {
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.60)},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.65)},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.70)},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.78)}
    };
    /**
     * Indicate which is next duck line.
     */
    public static int nextDuckLines = 0;
    
    
    /**
     * X coordinate of the duck.
     */
    private int x;
    /**
     * Y coordinate of the duck.
     */
    private int y;
    
    /**
     * How fast the duck should move? And to which direction?
     */
    private float speed;
    
    /**
     * How many points this duck is worth?
     */
    private int score;
    private int hp;

    /**
     * kr.jbnu.se.std.Duck image.
     */
    private BufferedImage duckImg;
    
    
    /**
     * Creates new duck.
     * 
     * @param x Starting x coordinate.
     * @param y Starting y coordinate.
     * @param speed The speed of this duck.
     * @param score How many points this duck is worth?
     * @param duckImg Image of the duck.
     * @param hp Duck's hp
     */
    public Duck(int x, int y, float speed, int score, int hp, BufferedImage duckImg)
    {
        this.x = x;
        this.y = y;

        this.hp = Game.getlvdata().duckhp;

        this.speed = Game.getlvdata().speed;
        
        this.score = Game.getlvdata().ducksc;
        
        this.duckImg = duckImg;        
    }
    
    
    /**
     * Move the duck.
     */
    public void Update()
    {
        x += speed;
    }
    
    /**
     * Draw the duck to the screen.
     * @param g2d Graphics2D
     */
    public void Draw(Graphics2D g2d)
    {
        g2d.drawImage(duckImg, x, y, null);
    }

    @Override
    public int getHp() {
        return hp;
    }

    @Override
    public void reduceHp(int amount) {
        this.hp -= amount;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    public int getScore() {
        return score;
    }

    @Override
    public void setSpeed(int newspeed){
        this.speed = newspeed;
    }
}
