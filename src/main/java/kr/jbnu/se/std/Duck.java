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
     * Last time when the duck was created.
     */
    public static long lastDuckTime;
    
    /**
     * kr.jbnu.se.std.Duck lines.
     * Where is starting location for the duck?
     * Speed of the duck?
     * How many points is a duck worth?
     */
    protected static final int[][] duckLines = {
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
     * @param duckImg Image of the duck.
     */
    public Duck(int x, int y, BufferedImage duckImg)
    {
        this.x = x;
        this.y = y;

        this.hp = Game.getLvData().duckhp;

        this.speed = Game.getLvData().speed;
        
        this.score = Game.getLvData().ducksc;
        
        this.duckImg = duckImg;        
    }
    
    
    /**
     * Move the duck.
     */
    public void Update()
    {
        x += (int) speed;
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
}
