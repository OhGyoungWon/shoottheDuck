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

    /**
     * How many ducks leave the screen alive?
     */
    public static int runawayDucks;
    
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
     * 레벨 정수형으로 선언
      */
    private int level;
    /**
     * 레벨 검증 변수, 레벨 업데이트 조건 만족 시 코드 중복실행 방지용
     */

    // 권총 클래스 추가
    private Pistol pistol;

    private Shop shop;

    private static ArrayList<Weapon> weapons;

    /**
     * Last time of the shoot.
     */
    private long lastTimeShoot;    
    /**
     * The time which must elapse between shots.
     */
    public static long timeBetweenShots;

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

    // 사운드 플레이어 추가
    private SoundPlayer soundPlayer;

    public static Weapon currentweapon;

    public static Levels lvdata;
    public static int gamelevel;
    public static int money;

    public static int adiatt;
    public static int atspdpls;
    public static int redspd;

    public static int rubberduckskills;
    public static boolean nuclearswitch;

    private ArrayList<DamageText> damageTexts;

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

                soundPlayer.play("backgroundMusic");

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
        soundPlayer = new SoundPlayer(); // 사운드 플레이어 생성

        random = new Random();
        font = new Font("monospaced", Font.BOLD, 18);
        shop = new Shop();

        ducks = new ArrayList<>();
        superducks = new ArrayList<>();
        smgduck = new ArrayList<>();
        rifduck = new ArrayList<>();
        odinduck = new ArrayList<>();

        runawayDucks = 0;
        killedDucks = 15;
        score = 0;
        money = 0;
        shoots = 0;
        level = 1;
        nuclearswitch = false;

        adiatt = 0;
        redspd = 0;
        atspdpls = 0;
        gamelevel = 1;
        lvdata = getlvdata();
        currentweapon = new Weapon.Revolver(revImg);
        weapons = new ArrayList<>();
        weapons.add(new Weapon.Revolver(revImg));
        lastTimeShoot = 0;
        if(currentweapon.fireDelay > redspd){
            timeBetweenShots = currentweapon.fireDelay - redspd;
        }
        else{
            timeBetweenShots = 50_000_000;
        }
        damageTexts = new ArrayList<>();
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

            URL sightImgUrl = this.getClass().getResource("/images/pistol_sight.png");
            sightImg = ImageIO.read(sightImgUrl);
            sightImgMiddleWidth = sightImg.getWidth() / 2;
            sightImgMiddleHeight = sightImg.getHeight() / 2;

            URL pistolImgUrl = this.getClass().getResource("/images/pistol.png");
            BufferedImage pistolImg = ImageIO.read(pistolImgUrl);
            pistol = new Pistol(pistolImg, 11, 10_000_000L);  // 11프레임, 프레임당 100ms

            URL gunshotSoundUrl = this.getClass().getResource("/sounds/single-gunshot.wav");
            soundPlayer.loadSound("gunshot", gunshotSoundUrl);

            URL backgroundMusicUrl = this.getClass().getResource("/sounds/Fluffing a Duck.wav");
            soundPlayer.loadSound("backgroundMusic", backgroundMusicUrl);

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
        superducks.clear();
        smgduck.clear();
        rifduck.clear();
        odinduck.clear();
        damageTexts.clear();
        weapons.clear();
        currentweapon = new Weapon.Revolver(revImg);
        weapons.add(new Weapon.Revolver(revImg));
        
        // We set last duckt time to zero.
        Duck.lastDuckTime = 0;

        runawayDucks = 0;
        killedDucks = 0;
        score = 0;
        money = 0;
        shoots = 0;
        gamelevel = 1;
        currentweapon = new Weapon.Revolver(revImg);
        adiatt = 0;
        redspd = 0;

        level = 1;
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

        if (shop.isShopOpen()) {
            if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {
                shop.handleClick(mousePosition);  // 상점 클릭 처리
            }
            return;
        }
        if(nuclearswitch){
            reduceHealthOfAllObjects();
            nuclearswitch = false;
        }
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

        //Weapon ducks 소환
        if(killedDucks == 30 && smgduck.isEmpty()){
            smgduck.add(new Weaponduck.Smgduck(Duck.duckLines[Duck.nextDuckLines][0] + random.nextInt(200),
                    (int) (Framework.frameHeight*0.2), lvdata.speed, lvdata.ducksc*2, lvdata.duckhp*2, smgImg ));
        }
        if(killedDucks == 50 && rifduck.isEmpty()){
            rifduck.add(new Weaponduck.Rifduck(Framework.frameWidth + random.nextInt(200),
                    (int) (Framework.frameHeight*0.2), lvdata.speed, lvdata.ducksc*2, lvdata.duckhp*2, rifImg ));
        }
        if(killedDucks == 70 && odinduck.isEmpty()){
            odinduck.add(new Weaponduck.Odinduck(Framework.frameWidth + random.nextInt(200),
                    (int) (Framework.frameHeight*0.2), lvdata.speed, lvdata.ducksc*2, lvdata.duckhp*2, odinImg ));
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
        for(int i = 0; i < smgduck.size(); i++){
            smgduck.get(i).Update();

            // Checks if the duck leaves the screen and remove it if it does.
            if(smgduck.get(i).x < 0 - smgImg.getWidth())
            {
                smgduck.remove(i);
                runawayDucks++;
            }
        }
        for(int i = 0; i < rifduck.size(); i++){
            rifduck.get(i).Update();

            // Checks if the duck leaves the screen and remove it if it does.
            if(rifduck.get(i).x < 0 - rifImg.getWidth())
            {
                rifduck.remove(i);
                runawayDucks++;
            }
        }
        for(int i = 0; i < odinduck.size(); i++){
            odinduck.get(i).Update();

            // Checks if the duck leaves the screen and remove it if it does.
            if(odinduck.get(i).x < 0 - odinImg.getWidth())
            {
                odinduck.remove(i);
                runawayDucks++;
            }
        }

        // Does player shoots?
        if(Canvas.mouseButtonState(MouseEvent.BUTTON1))
        {
            // Checks if it can shoot again.
            if(System.nanoTime() - lastTimeShoot >= timeBetweenShots)
            {

                shoots++;
// 총 사운드 재생
                soundPlayer.play("gunshot");

                // 총 모션 재생
                if (!pistol.isShooting()) {
                    pistol.startShooting();
                }

                // We go over all the ducks and we look if any of them was shoot.
                for(int i = 0; i < ducks.size(); i++)
                {
                    // We check, if the mouse was over ducks head or body, when player has shot.
                    if(new Rectangle(ducks.get(i).x + 11, ducks.get(i).y     , 44, 50).contains(mousePosition) ||
                       new Rectangle(ducks.get(i).x + 15, ducks.get(i).y + 39, 64, 44).contains(mousePosition))
                    {
                        ducks.get(i).hp-=currentweapon.getDamage() + adiatt;
                        damageTexts.add(new DamageText(mousePosition.x, mousePosition.y, currentweapon.getDamage() + adiatt));
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
                            superducks.get(i).hp-=currentweapon.getDamage() + adiatt;
                            damageTexts.add(new DamageText(mousePosition.x, mousePosition.y, currentweapon.getDamage() + adiatt));
                            if(superducks.get(i).hp <= 0){
                                killedDucks++;
                                score += superducks.get(i).score;
                                money += superducks.get(i).score;

                                // Remove the duck from the array list.
                                superducks.remove(i);
                                shop.openShop();
                                gamelevel++;

                                // We found the duck that player shoot so we can leave the for loop.
                                break;
                            }
                        }
                    }
                }
                for(int i = 0; i < smgduck.size(); i++){
                    if (new Rectangle(smgduck.get(i).x, smgduck.get(i).y,
                            smgImg.getWidth(), smgImg.getHeight()).contains(mousePosition)){
                        if(!smgduck.isEmpty() && new Rectangle(smgduck.get(i).x, smgduck.get(i).y,
                                smgImg.getWidth(), smgImg.getHeight()).contains(mousePosition))
                        {
                            smgduck.get(i).hp-=currentweapon.getDamage() + adiatt;
                            damageTexts.add(new DamageText(mousePosition.x, mousePosition.y, currentweapon.getDamage() + adiatt));
                            if(smgduck.get(i).hp <= 0){
                                killedDucks++;
                                score += smgduck.get(i).score;
                                money += smgduck.get(i).score;

                                // Remove the duck from the array list.
                                smgduck.remove(i);
                                currentweapon = new Weapon.SMG(smgImg);
                                timeBetweenShots = currentweapon.fireDelay;
                                weapons.add(new Weapon.SMG(smgImg));

                                // We found the duck that player shoot so we can leave the for loop.
                                break;
                            }
                        }
                    }
                }
                for(int i = 0; i < rifduck.size(); i++){
                    if (new Rectangle(rifduck.get(i).x, rifduck.get(i).y,
                            rifImg.getWidth(), rifImg.getHeight()).contains(mousePosition)){
                        if(!rifduck.isEmpty() && new Rectangle(rifduck.get(i).x, rifduck.get(i).y,
                                rifImg.getWidth(), rifImg.getHeight()).contains(mousePosition))
                        {
                            rifduck.get(i).hp-=currentweapon.getDamage() + adiatt;
                            damageTexts.add(new DamageText(mousePosition.x, mousePosition.y, currentweapon.getDamage() + adiatt));
                            if(rifduck.get(i).hp <= 0){
                                killedDucks++;
                                score += rifduck.get(i).score;
                                money += rifduck.get(i).score;

                                // Remove the duck from the array list.
                                rifduck.remove(i);
                                currentweapon = new Weapon.Rifle(rifImg);
                                timeBetweenShots = currentweapon.fireDelay;
                                weapons.add(new Weapon.Rifle(smgImg));

                                // We found the duck that player shoot so we can leave the for loop.
                                break;
                            }
                        }
                    }
                }
                for(int i = 0; i < odinduck.size(); i++){
                    if (new Rectangle(odinduck.get(i).x, odinduck.get(i).y,
                            odinImg.getWidth(), odinImg.getHeight()).contains(mousePosition)){
                        if(!odinduck.isEmpty() && new Rectangle(odinduck.get(i).x, odinduck.get(i).y,
                                odinImg.getWidth(), odinImg.getHeight()).contains(mousePosition))
                        {
                            odinduck.get(i).hp-=currentweapon.getDamage() + adiatt;
                            damageTexts.add(new DamageText(mousePosition.x, mousePosition.y, currentweapon.getDamage() + adiatt));
                            if(odinduck.get(i).hp <= 0){
                                killedDucks++;
                                score += odinduck.get(i).score;
                                money += odinduck.get(i).score;

                                // Remove the duck from the array list.
                                odinduck.remove(i);
                                currentweapon = new Weapon.Odin(smgImg);
                                timeBetweenShots = currentweapon.fireDelay;
                                weapons.add(new Weapon.Odin(smgImg));

                                // We found the duck that player shoot so we can leave the for loop.
                                break;
                            }
                        }
                    }
                }
                for(int i = 0; i < odinduck.size(); i++){
                    if (new Rectangle(odinduck.get(i).x, odinduck.get(i).y,
                            odinImg.getWidth(), odinImg.getHeight()).contains(mousePosition)){
                        if(!odinduck.isEmpty() && new Rectangle(odinduck.get(i).x, odinduck.get(i).y,
                                odinImg.getWidth(), odinImg.getHeight()).contains(mousePosition))
                        {
                            odinduck.get(i).hp-=currentweapon.getDamage() + adiatt;
                            damageTexts.add(new DamageText(mousePosition.x, mousePosition.y, currentweapon.getDamage() + adiatt));
                            if(odinduck.get(i).hp <= 0){
                                killedDucks++;
                                score += odinduck.get(i).score;
                                money += odinduck.get(i).score;

                                // Remove the duck from the array list.
                                odinduck.remove(i);
                                currentweapon = new Weapon.Odin(smgImg);
                                timeBetweenShots = currentweapon.fireDelay;
                                weapons.add(new Weapon.Odin(smgImg));

                                // We found the duck that player shoot so we can leave the for loop.
                                break;
                            }
                        }
                    }
                }

                lastTimeShoot = System.nanoTime();
            }
        }
        for (int i = 0; i < damageTexts.size(); i++) {
            if (!damageTexts.get(i).update()) {
                damageTexts.remove(i);
                i--;
            }
        }
        pistol.update();

        // When 200 ducks runaway, the game ends.
        if(runawayDucks >= 100) {
            soundPlayer.stop("backgroundMusic");
            Framework.gameState = Framework.GameState.GAMEOVER;
        }

        if (shop.isShopOpen()) {
            if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {
                shop.handleClick(mousePosition);  // 상점 클릭 처리
            }
            return;
        }
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
        for(int i = 0; i < rifduck.size(); i++){
            rifduck.get(i).Draw(g2d);
        }
        for(int i = 0; i < odinduck.size(); i++){
            odinduck.get(i).Draw(g2d);
        }

        if (shop.isShopOpen()) {
            shop.drawShop(g2d);
        }

        for (DamageText damageText : damageTexts) {
            damageText.draw(g2d);
        }

        g2d.drawImage(grassImg, 0, Framework.frameHeight - grassImg.getHeight(), Framework.frameWidth, grassImg.getHeight(), null);
        
        g2d.drawImage(sightImg, mousePosition.x - sightImgMiddleWidth, mousePosition.y - sightImgMiddleHeight, null);

        g2d.drawImage(pistol.getCurrentFrame(), 0, Framework.frameHeight - (Framework.frameHeight / 4), (Framework.frameHeight / 15) * 8, (Framework.frameHeight / 15) * 5, null);

        g2d.setFont(font);
        g2d.setColor(Color.darkGray);
        
        g2d.drawString("RUNAWAY: " + runawayDucks, 10, 21);
        g2d.drawString("KILLS: " + killedDucks, 160, 21);
        g2d.drawString("SHOOTS: " + shoots, 299, 21);
        g2d.drawString("SCORE: " + score, 440, 21);
        g2d.drawString("Money: " + money, 560, 21);
        g2d.drawString("LEVEL: " + level, 680, 21);
        g2d.drawString("Weapon: " + currentweapon.getName(), 840, 21);
        shop.drawPurchaseMessage(g2d);
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
    public static ArrayList<Weapon> getWeapons(){
        return weapons;
    }
    public void reduceHealthOfAllObjects() {
        // ducks 리스트의 모든 객체 체력을 999 감소
        for (int i = 0; i < ducks.size(); i++) {
            Duck duck = ducks.get(i);
            duck.hp -= 999;

            // DamageText 추가
            damageTexts.add(new DamageText(duck.x, duck.y, 999));

            if (duck.hp <= 0) {
                ducks.remove(i);
                i--; // 리스트에서 객체를 제거한 후 인덱스 조정
            }
        }

        // superducks 리스트의 모든 객체 체력을 999 감소
        for (int i = 0; i < superducks.size(); i++) {
            Superduck superduck = superducks.get(i);
            superduck.hp -= 999;

            // DamageText 추가
            damageTexts.add(new DamageText(superduck.x, superduck.y, 999));

            if (superduck.hp <= 0) {
                superducks.remove(i);
                i--;
            }
        }

        // smgduck 리스트의 모든 객체 체력을 999 감소
        for (int i = 0; i < smgduck.size(); i++) {
            Weaponduck.Smgduck smg = smgduck.get(i);
            smg.hp -= 999;

            // DamageText 추가
            damageTexts.add(new DamageText(smg.x, smg.y, 999));

            if (smg.hp <= 0) {
                smgduck.remove(i);
                i--;
            }
        }

        // rifduck 리스트의 모든 객체 체력을 999 감소
        for (int i = 0; i < rifduck.size(); i++) {
            Weaponduck.Rifduck rif = rifduck.get(i);
            rif.hp -= 999;

            // DamageText 추가
            damageTexts.add(new DamageText(rif.x, rif.y, 999));

            if (rif.hp <= 0) {
                rifduck.remove(i);
                i--;
            }
        }

        // odinduck 리스트의 모든 객체 체력을 999 감소
        for (int i = 0; i < odinduck.size(); i++) {
            Weaponduck.Odinduck odin = odinduck.get(i);
            odin.hp -= 999;

            // DamageText 추가
            damageTexts.add(new DamageText(odin.x, odin.y, 999));

            if (odin.hp <= 0) {
                odinduck.remove(i);
                i--;
            }
        }

        // 여기서 다른 객체 리스트에 대해서도 동일한 처리를 해줄 수 있습니다.
    }
}
