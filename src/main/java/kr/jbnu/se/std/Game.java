package kr.jbnu.se.std;


import java.awt.*;
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
    private ArrayList<SuperDuck> superDucks;
    private ArrayList<Weaponduck.Smgduck> smgDuck;
    private ArrayList<Weaponduck.Rifduck> rifDuck;
    private ArrayList<Weaponduck.Sniperduck> sniperDuck;

    private static int runawayDucks;
    
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
    private DrawGun pistol;
    private DrawGun revolver;
    private DrawGun submachine;
    private DrawGun rifle;
    private DrawGun sniper;

    private Shop shop;

    private static ArrayList<Weapon> weapons;

    /**
     * Last time of the shoot.
     */
    private long lastTimeShoot;    
    private static long timeBetweenShots;

    /**
     * kr.jbnu.se.std.Game background image.
     */
    private BufferedImage backgroundImg;
    
    /**
     * Bottom grass.
     */
    private BufferedImage grassImg;
    private BufferedImage backGrassImg;

    /**
     * kr.jbnu.se.std.Duck image.
     */
    private BufferedImage duckImg;
    private BufferedImage superDuckImg;

    /**
     * Shotgun sight image.
     */
    private BufferedImage sightImg;
    private BufferedImage revImg;
    private BufferedImage smgImg;
    private BufferedImage rifImg;
    private BufferedImage snipImg;

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

    private static Weapon currentweapon;

    private static Levels lvData;
    private static int gameLevel;
    private static int money;

    private static int adiatt;
    private static int atspdpls;
    private static int redspd;

    private static int rubberduckskills;
    private static boolean nuclearswitch;

    private static boolean isReloading = false;  // 장전 중인지 여부
    private static int currentAmmo;  // 현재 남은 탄약 수
    private static int maxAmmo;  // 탄창 크기
    private long reloadStartTime;  // 장전 시작 시간
    private static long reloadDuration;  // 장전 시간 1.5초

    private ArrayList<DamageText> damageTexts;

    public Game()
    {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;

        Thread threadForInitGame = new Thread(() -> {
            // Sets variables and objects for the game.
            Initialize();
            // Load game files (images, sounds, ...)
            LoadContent();

            soundPlayer.play("backgroundMusic");

            Framework.gameState = Framework.GameState.PLAYING;
        });
        threadForInitGame.start();
    }

    public static void setNuclearswitch(boolean b) {
        Game.nuclearswitch = b;
    }

    public static int getMaxAmmo() {
        return maxAmmo;
    }

    public static void setMaxAmmo(int maxAmmo) {
        Game.maxAmmo = maxAmmo;
    }

    /**
     * The time which must elapse between shots.
     */
    public static long getTimeBetweenShots() {
        return timeBetweenShots;
    }

    public static void setTimeBetweenShots(long timeBetweenShots) {
        Game.timeBetweenShots = timeBetweenShots;
    }

    /**
     * 리더보드 출력
     */
    public static Weapon getCurrentweapon() {
        return currentweapon;
    }

    public static void setCurrentweapon(Weapon currentweapon) {
        Game.currentweapon = currentweapon;
    }

    public static int getCurrentAmmo() {
        return currentAmmo;
    }

    public static void setCurrentAmmo(int currentAmmo) {
        Game.currentAmmo = currentAmmo;
    }

    public static boolean isIsReloading() {
        return isReloading;
    }

    public static void setIsReloading(boolean isReloading) {
        Game.isReloading = isReloading;
    }

    /**
     * How many ducks leave the screen alive?
     */



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
        superDucks = new ArrayList<>();
        smgDuck = new ArrayList<>();
        rifDuck = new ArrayList<>();
        sniperDuck = new ArrayList<>();

        setRunawayDucks(0);
        killedDucks = 0;
        score = 0;
        money = 0;
        shoots = 0;
        level = 1;
        nuclearswitch = false;
        reloadDuration = 1_500_000_000L;
        setIsReloading(false);

        adiatt = 0;
        redspd = 0;
        atspdpls = 0;
        gameLevel = 1;
        lvData = getlvdata();
        setCurrentweapon(new Weapon.Revolver(revImg));
        weapons = new ArrayList<>();
        weapons.add(new Weapon.Revolver(revImg));
        lastTimeShoot = 0;
        if(getCurrentweapon().fireDelay > redspd){
            setTimeBetweenShots(getCurrentweapon().fireDelay - redspd);
        }
        else{
            setTimeBetweenShots(50_000_000);
        }
        damageTexts = new ArrayList<>();

        setMaxAmmo(getCurrentweapon().maxammo);
        setCurrentAmmo(getMaxAmmo());

    }

    /**
     * Load game files - images, sounds, ...
     */
    private void LoadContent()
    {
        try
        {
            URL backgroundImgUrl = this.getClass().getResource("/images/background.png");
            if (backgroundImgUrl != null) {
                backgroundImg = ImageIO.read(backgroundImgUrl);
            }

            URL grassImgUrl = this.getClass().getResource("/images/grass.png");
            if (grassImgUrl != null) {
                grassImg = ImageIO.read(grassImgUrl);
            }

            URL backgrassImgUrl = this.getClass().getResource("/images/backgrass.png");
            if (backgrassImgUrl != null) {
                backGrassImg = ImageIO.read(backgrassImgUrl);
            }

            URL duckImgUrl = this.getClass().getResource("/images/duck.png");
            if (duckImgUrl != null) {
                duckImg = ImageIO.read(duckImgUrl);
            }

            URL superduckImgUrl = this.getClass().getResource("/images/superduck.png");
            assert superduckImgUrl != null;
            superDuckImg = ImageIO.read(superduckImgUrl);

            URL sightImgUrl = this.getClass().getResource("/images/sight.png");
            assert sightImgUrl != null;
            sightImg = ImageIO.read(sightImgUrl);
            sightImgMiddleWidth = sightImg.getWidth() / 2;
            sightImgMiddleHeight = sightImg.getHeight() / 2;

            URL pistolImgUrl = this.getClass().getResource("/images/pistol.png");
            assert pistolImgUrl != null;
            BufferedImage pistolImg = ImageIO.read(pistolImgUrl);
            pistol = new DrawGun(pistolImg, 11, 10_000_000L);  // 11프레임, 프레임당 100ms

            URL revolverImgUrl = this.getClass().getResource("/images/revolver.png");
            assert revolverImgUrl != null;
            BufferedImage revolverImg = ImageIO.read(revolverImgUrl);
            revolver = new DrawGun(revolverImg, 10, 30_000_000L);  // 10프레임, 프레임당 300ms

            URL submachineImgUrl = this.getClass().getResource("/images/submachine.png");
            assert submachineImgUrl != null;
            BufferedImage submachineImg = ImageIO.read(submachineImgUrl);
            submachine = new DrawGun(submachineImg, 12, 8_000_000L);

            URL rifleImgUrl = this.getClass().getResource("/images/rifle.png");
            assert rifleImgUrl != null;
            BufferedImage rifleImg = ImageIO.read(rifleImgUrl);
            rifle = new DrawGun(rifleImg, 12, 10_000_000L);

            URL sniperImgUrl = this.getClass().getResource("/images/sniper.png");
            assert sniperImgUrl != null;
            BufferedImage sniperImg = ImageIO.read(sniperImgUrl);
            sniper = new DrawGun(sniperImg, 56, 26_000_000L);

            URL gunshotSoundUrl = this.getClass().getResource("/sounds/single-gunshot.wav");
            soundPlayer.loadSound("gunshot", gunshotSoundUrl);

            URL smgSoundUrl = this.getClass().getResource("/sounds/Submachine.wav");
            soundPlayer.loadSound("smg", smgSoundUrl);

            URL rifleSoundUrl = this.getClass().getResource("/sounds/Rifle.wav");
            soundPlayer.loadSound("rifle", rifleSoundUrl);

            URL sniperSoundUrl = this.getClass().getResource("/sounds/Sniper.wav");
            soundPlayer.loadSound("sniper", sniperSoundUrl);

            URL backgroundMusicUrl = this.getClass().getResource("/sounds/Fluffing a Duck.wav");
            soundPlayer.loadSound("backgroundMusic", backgroundMusicUrl);

            URL smgImgUrl = this.getClass().getResource("/images/gunbox.png");
            assert smgImgUrl != null;
            smgImg = ImageIO.read(smgImgUrl);

            URL rifImgUrl = this.getClass().getResource("/images/gunbox.png");
            assert rifImgUrl != null;
            rifImg = ImageIO.read(rifImgUrl);

            URL snipImgUrl = this.getClass().getResource("/images/gunbox.png");
            assert snipImgUrl != null;
            snipImg = ImageIO.read(snipImgUrl);

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
        superDucks.clear();
        smgDuck.clear();
        rifDuck.clear();
        sniperDuck.clear();
        damageTexts.clear();
        weapons.clear();
        setCurrentweapon(new Weapon.Revolver(revImg));
        weapons.add(new Weapon.Revolver(revImg));
        
        // We set last duckt time to zero.
        Duck.lastDuckTime = 0;

        setRunawayDucks(0);
        killedDucks = 0;
        score = 0;
        money = 0;
        shoots = 0;
        gameLevel = 1;
        setCurrentweapon(new Weapon.Revolver(revImg));
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
        String currentEmail = LoginUI.getUserEmail();

        System.out.println("Current weapon: " + getCurrentweapon().getName());
        System.out.println("Damage: " + getCurrentweapon().getDamage());
        System.out.println("Fire delay: " + getTimeBetweenShots());


        if (shop.isShopOpen()) {
            if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {
                shop.handleClick(mousePosition);  // 상점 클릭 처리
            }
            return;
        }
        if (isIsReloading()) {
            if (System.nanoTime() - reloadStartTime >= reloadDuration) {  // 1.5초 장전 시간
                setIsReloading(false);  // 장전 완료
                setCurrentAmmo(getMaxAmmo());  // 탄창 재충전
            }
        }
        if(nuclearswitch){
            reduceHealthOfAllObjects();
            nuclearswitch = false;
        }
        // Creates a new duck, if it's the time, and add it to the array list.
        if(System.nanoTime() - Duck.lastDuckTime >= lvData.sumdly && superDucks.isEmpty())
        {
            // Here we create new duck and add it to the array list.
            ducks.add(new Duck(Duck.duckLines[Duck.nextDuckLines][0] + random.nextInt(200),
                    Duck.duckLines[Duck.nextDuckLines][1], lvData.speed, lvData.ducksc, lvData.duckhp, duckImg));

            // Here we increase nextDuckLines so that next duck will be created in next line.
            Duck.nextDuckLines++;
            if(Duck.nextDuckLines >= Duck.duckLines.length)
                Duck.nextDuckLines = 0;

            Duck.lastDuckTime = System.nanoTime();
        }
        if(killedDucks % 20 == 0 && killedDucks != 0 && superDucks.isEmpty()){
            superDucks.add(new SuperDuck(Duck.duckLines[Duck.nextDuckLines][0] + random.nextInt(200),
                                            (int) (Framework.frameHeight*0.6),
                    superDuckImg));
        }

        //Weapon ducks 소환
        if(killedDucks == 30 && smgDuck.isEmpty()){
            smgDuck.add(new Weaponduck.Smgduck(Duck.duckLines[Duck.nextDuckLines][0] + random.nextInt(200),
                    (int) (Framework.frameHeight*0.25), lvData.speed, lvData.ducksc*2, lvData.duckhp*2, smgImg ));
        }
        if(killedDucks == 50 && rifDuck.isEmpty()){
            rifDuck.add(new Weaponduck.Rifduck(Framework.frameWidth + random.nextInt(200),
                    (int) (Framework.frameHeight*0.25), lvData.speed, lvData.ducksc*2, lvData.duckhp*2, rifImg ));
        }
        if(killedDucks == 70 && sniperDuck.isEmpty()){
            sniperDuck.add(new Weaponduck.Sniperduck(Framework.frameWidth + random.nextInt(200),
                    (int) (Framework.frameHeight*0.25), lvData.speed, lvData.ducksc*2, lvData.duckhp*2, snipImg));
        }

        // Update all of the ducks.
        for(int i = 0; i < ducks.size(); i++) {
            // Move the duck.
            ducks.get(i).Update();

            // Checks if the duck leaves the screen and remove it if it does.
            if(ducks.get(i).x < -duckImg.getWidth())
            {
                ducks.remove(i);
                setRunawayDucks(getRunawayDucks() + 1);
            }
        }
        for(int i = 0; i < superDucks.size(); i++){
            superDucks.get(i).Update();

            // Checks if the duck leaves the screen and remove it if it does.
            if(superDucks.get(i).getX() < -superDuckImg.getWidth())
            {
                superDucks.remove(i);
                setRunawayDucks(999);
            }
        }
        for(int i = 0; i < smgDuck.size(); i++){
            smgDuck.get(i).Update();

            // Checks if the duck leaves the screen and remove it if it does.
            if(smgDuck.get(i).getX() < -smgImg.getWidth())
            {
                smgDuck.remove(i);
                setRunawayDucks(getRunawayDucks() + 1);
            }
        }
        for(int i = 0; i < rifDuck.size(); i++){
            rifDuck.get(i).Update();

            // Checks if the duck leaves the screen and remove it if it does.
            if(rifDuck.get(i).getX() < -rifImg.getWidth())
            {
                rifDuck.remove(i);
                setRunawayDucks(getRunawayDucks() + 1);
            }
        }
        for(int i = 0; i < sniperDuck.size(); i++){
            sniperDuck.get(i).Update();

            // Checks if the duck leaves the screen and remove it if it does.
            if(sniperDuck.get(i).getX() < -snipImg.getWidth())
            {
                sniperDuck.remove(i);
                setRunawayDucks(getRunawayDucks() + 1);
            }
        }

        // Does player shoots?
        if(Canvas.mouseButtonState(MouseEvent.BUTTON1) && !isIsReloading())
        {
            // Checks if it can shoot again.
            if(System.nanoTime() - lastTimeShoot >= getTimeBetweenShots())
            {

                if(getCurrentAmmo() > 0) {
                    shoots++;
                    setCurrentAmmo(getCurrentAmmo() - 1);
                    // 총 사운드 재생

                    // 총 모션 재생
                    if (!pistol.isShooting() && getCurrentweapon().getName().equals("Pistol")) {
                        pistol.startShooting();
                    } else if (!revolver.isShooting() && getCurrentweapon().getName().equals("Revolver")) {
                        soundPlayer.play("gunshot");
                        revolver.startShooting();
                    } else if (!submachine.isShooting() && getCurrentweapon().getName().equals("SMG")) {
                        soundPlayer.play("smg");
                        submachine.startShooting();
                    } else if (!rifle.isShooting() && getCurrentweapon().getName().equals("Rifle")) {
                        soundPlayer.play("rifle");
                        rifle.startShooting();
                    } else if (!sniper.isShooting() && getCurrentweapon().getName().equals("Sniper")) {
                        soundPlayer.play("sniper");
                        sniper.startShooting();
                    }

                    // We go over all the ducks and we look if any of them was shoot.
                    for (int i = 0; i < ducks.size(); i++) {
                        // We check, if the mouse was over ducks head or body, when player has shot.
                        if (new Rectangle(ducks.get(i).x + 11, ducks.get(i).y, 44, 50).contains(mousePosition) ||
                                new Rectangle(ducks.get(i).x + 15, ducks.get(i).y + 39, 64, 44).contains(mousePosition)) {
                            ducks.get(i).hp -= getCurrentweapon().getDamage() + adiatt;
                            damageTexts.add(new DamageText(mousePosition.x, mousePosition.y, getCurrentweapon().getDamage() + adiatt));
                            if (ducks.get(i).hp <= 0) {
                                killedDucks++;
                                score += ducks.get(i).score;
                                money += ducks.get(i).score;
                                InGameData.saveScore(currentEmail, score);
                                // Remove the duck from the array list.
                                ducks.remove(i);

                                // We found the duck that player shoot so we can leave the for loop.
                                break;
                            }
                        }
                    }
                    for (int i = 0; i < superDucks.size(); i++) {
                        int currentHp = superDucks.get(i).getHp();
                        int currentX = superDucks.get(i).getX();
                        int currentY = superDucks.get(i).getY();


                        if (new Rectangle(currentX, currentY,
                                superDuckImg.getWidth(), superDuckImg.getHeight()).contains(mousePosition)) {
                            if (!superDucks.isEmpty() && new Rectangle(currentX, currentY, superDuckImg.getWidth(), superDuckImg.getHeight()).contains(mousePosition)) {
                                currentHp -= getCurrentweapon().getDamage() + adiatt;
                                damageTexts.add(new DamageText(mousePosition.x, mousePosition.y, getCurrentweapon().getDamage() + adiatt));
                                if (currentHp <= 0) {
                                    killedDucks++;
                                    score += superDucks.get(i).getScore();
                                    money += superDucks.get(i).getScore();
                                    InGameData.saveScore(currentEmail, score);

                                    // Remove the duck from the array list.
                                    superDucks.remove(i);
                                    shop.openShop();
                                    gameLevel++;

                                    // We found the duck that player shoot so we can leave the for loop.
                                    break;
                                }
                            }
                        }
                    }
                    for (int i = 0; i < smgDuck.size(); i++) {
                        int smgDuckHp = smgDuck.get(i).getHp();

                        if (new Rectangle(smgDuck.get(i).getX(), smgDuck.get(i).getY(),
                                smgImg.getWidth(), smgImg.getHeight()).contains(mousePosition)) {
                            if (!smgDuck.isEmpty() && new Rectangle(smgDuck.get(i).getX(), smgDuck.get(i).getY(),
                                    smgImg.getWidth(), smgImg.getHeight()).contains(mousePosition)) {
                                 smgDuckHp -= getCurrentweapon().getDamage() + adiatt;
                                damageTexts.add(new DamageText(mousePosition.x, mousePosition.y, getCurrentweapon().getDamage() + adiatt));
                                if (smgDuckHp <= 0) {
                                    killedDucks++;
                                    score += smgDuck.get(i).getScore();
                                    money += smgDuck.get(i).getScore();
                                    InGameData.saveScore(currentEmail, score);

                                    // Remove the duck from the array list.
                                    smgDuck.remove(i);
                                    setCurrentweapon(new Weapon.SMG(smgImg));
                                    setMaxAmmo(getCurrentweapon().maxammo);
                                    setCurrentAmmo(getMaxAmmo());
                                    setIsReloading(false);
                                    setTimeBetweenShots(getCurrentweapon().fireDelay);
                                    weapons.add(new Weapon.SMG(smgImg));

                                    // We found the duck that player shoot so we can leave the for loop.
                                    break;
                                }
                            }
                        }
                    }
                    for (int i = 0; i < rifDuck.size(); i++) {
                        int rifDuckHp = rifDuck.get(i).getHp();

                        if (new Rectangle(rifDuck.get(i).getX(), rifDuck.get(i).getY(),
                                rifImg.getWidth(), rifImg.getHeight()).contains(mousePosition)) {
                            if (!rifDuck.isEmpty() && new Rectangle(rifDuck.get(i).getX(), rifDuck.get(i).getY(),
                                    rifImg.getWidth(), rifImg.getHeight()).contains(mousePosition)) {
                                rifDuckHp -= getCurrentweapon().getDamage() + adiatt;
                                damageTexts.add(new DamageText(mousePosition.x, mousePosition.y, getCurrentweapon().getDamage() + adiatt));
                                if (rifDuckHp <= 0) {
                                    killedDucks++;
                                    score += rifDuck.get(i).getScore();
                                    money += rifDuck.get(i).getScore();
                                    InGameData.saveScore(currentEmail, score);

                                    // Remove the duck from the array list.
                                    rifDuck.remove(i);
                                    setCurrentweapon(new Weapon.Rifle(rifImg));
                                    setMaxAmmo(getCurrentweapon().maxammo);
                                    setCurrentAmmo(getMaxAmmo());
                                    setIsReloading(false);
                                    setTimeBetweenShots(getCurrentweapon().fireDelay);
                                    weapons.add(new Weapon.Rifle(smgImg));

                                    // We found the duck that player shoot so we can leave the for loop.
                                    break;
                                }
                            }
                        }
                    }
                    for (int i = 0; i < sniperDuck.size(); i++) {
                        int sniperDuckHp = sniperDuck.get(i).getHp();

                        if (new Rectangle(sniperDuck.get(i).getX(), sniperDuck.get(i).getY(),
                                snipImg.getWidth(), snipImg.getHeight()).contains(mousePosition)) {
                            if (!sniperDuck.isEmpty() && new Rectangle(sniperDuck.get(i).getX(), sniperDuck.get(i).getY(),
                                    snipImg.getWidth(), snipImg.getHeight()).contains(mousePosition)) {
                                sniperDuckHp -= getCurrentweapon().getDamage() + adiatt;
                                damageTexts.add(new DamageText(mousePosition.x, mousePosition.y, getCurrentweapon().getDamage() + adiatt));
                                if (sniperDuckHp <= 0) {
                                    killedDucks++;
                                    score += sniperDuck.get(i).getScore();
                                    money += sniperDuck.get(i).getScore();
                                    InGameData.saveScore(currentEmail, score);

                                    // Remove the duck from the array list.
                                    sniperDuck.remove(i);
                                    setCurrentweapon(new Weapon.Sniper(snipImg));
                                    setMaxAmmo(getCurrentweapon().maxammo);
                                    setCurrentAmmo(getMaxAmmo());
                                    setIsReloading(false);
                                    setTimeBetweenShots(getCurrentweapon().fireDelay);
                                    weapons.add(new Weapon.Sniper(snipImg));

                                    // We found the duck that player shoot so we can leave the for loop.
                                    break;
                                }
                            }
                        }
                    }
                }
                else if(getCurrentAmmo() == 0){
                    setIsReloading(true);
                    reloadStartTime = System.nanoTime();  // 장전 시작 시간 기록;
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

        if (getCurrentweapon().getName().equals("Pistol")) {
            pistol.update();
        }
        else if (getCurrentweapon().getName().equals("Revolver")) {
            revolver.update();
        }
        else if (getCurrentweapon().getName().equals("SMG")) {
            submachine.update();
        }
        else if (getCurrentweapon().getName().equals("Rifle")) {
            rifle.update();
        }
        else if (getCurrentweapon().getName().equals("Sniper")) {
            sniper.update();
        }

        // When 200 ducks runaway, the game ends.
        if(getRunawayDucks() >= 10) {
            soundPlayer.stop("backgroundMusic");
            Framework.gameState = Framework.GameState.GAMEOVER;
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

        for (int i = 0; i < ducks.size(); i++) ducks.get(i).Draw(g2d);
        for (int i = 0; i < superDucks.size(); i++) superDucks.get(i).Draw(g2d);
        for (int i = 0; i < smgDuck.size(); i++) smgDuck.get(i).Draw(g2d);
        for (int i = 0; i < rifDuck.size(); i++) rifDuck.get(i).Draw(g2d);
        for (int i = 0; i < sniperDuck.size(); i++) sniperDuck.get(i).Draw(g2d);

        g2d.drawImage(backGrassImg, 0, Framework.frameHeight/32 * 10, Framework.frameWidth, backGrassImg.getHeight(), null);

        if (isIsReloading()) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  // 부드러운 텍스트
            g2d.setFont(new Font("Arial", Font.BOLD, 30));  // 폰트 설정
            g2d.setColor(Color.RED);  // 색상 설정
            String reloadMessage = "RELOADING...";
            int stringWidth = g2d.getFontMetrics().stringWidth(reloadMessage);  // 텍스트 가로 길이
            g2d.drawString(reloadMessage, (Framework.frameWidth - stringWidth) / 2, Framework.frameHeight / 2);  // 화면 중앙에 텍스트 표시
        }
        if (shop.isShopOpen()) {
            shop.drawShop(g2d);
        }

        for (DamageText damageText : damageTexts) {
            damageText.draw(g2d);
        }

        g2d.drawImage(grassImg, 0, Framework.frameHeight - grassImg.getHeight(), Framework.frameWidth, grassImg.getHeight(), null);

        g2d.drawImage(sightImg, mousePosition.x - sightImgMiddleWidth, mousePosition.y - sightImgMiddleHeight, null);

        if (getCurrentweapon().getName().equals("Pistol")) {
            g2d.drawImage(pistol.getCurrentFrame(), 0, Framework.frameHeight - (Framework.frameHeight / 4), (Framework.frameHeight / 15) * 8, (Framework.frameHeight / 15) * 5, null);
        }
        else if (getCurrentweapon().getName().equals("Revolver")) {
            g2d.drawImage(revolver.getCurrentFrame(), Framework.frameHeight/10, Framework.frameHeight - (Framework.frameHeight / 3), (Framework.frameHeight / 15) * 8, (Framework.frameHeight / 15) * 5, null);
        }
        else if (getCurrentweapon().getName().equals("SMG")) {
            g2d.drawImage(submachine.getCurrentFrame(), Framework.frameHeight/10, Framework.frameHeight - (Framework.frameHeight / 4), (Framework.frameHeight / 15) * 8, (Framework.frameHeight / 15) * 5, null);
        }
        else if (getCurrentweapon().getName().equals("Rifle")) {
            g2d.drawImage(rifle.getCurrentFrame(), Framework.frameHeight/10, Framework.frameHeight - (Framework.frameHeight / 4), (Framework.frameHeight / 15) * 8, (Framework.frameHeight / 15) * 5, null);
        }
        else if (getCurrentweapon().getName().equals("Sniper")) {
            g2d.drawImage(sniper.getCurrentFrame(), Framework.frameHeight/10, Framework.frameHeight - (Framework.frameHeight / 5), (Framework.frameHeight / 15) * 12, (Framework.frameHeight / 15) * 5 / 2, null);
        }

        g2d.setFont(font);
        g2d.setColor(Color.darkGray);

        g2d.drawString("RUNAWAY: " + getRunawayDucks(), 10, 21);
        g2d.drawString("KILLS: " + killedDucks, 160, 21);
        g2d.drawString("SHOOTS: " + shoots, 299, 21);
        g2d.drawString("SCORE: " + score, 440, 21);
        g2d.drawString("Money: " + money, 560, 21);
        g2d.drawString("LEVEL: " + level, 680, 21);
        g2d.drawString("Weapon: " + getCurrentweapon().getName(), 840, 21);
        g2d.drawString("Boolets: " + getCurrentAmmo(), 840, 42);
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
        g2d.drawString("Your score is " + score + ".", Framework.frameWidth / 2 - 19, (int)(Framework.frameHeight * 0.65) + 1);
        g2d.drawString("kr.jbnu.se.std The game is OVER", Framework.frameWidth / 2 - 149, (int)(Framework.frameHeight * 0.70) + 1);
        g2d.setColor(Color.red);
        g2d.drawString("Your score is " + score + ".", Framework.frameWidth / 2 - 20, (int)(Framework.frameHeight * 0.65));
        g2d.drawString("kr.jbnu.se.std The game is OVER", Framework.frameWidth / 2 - 150, (int)(Framework.frameHeight * 0.70));
    }

    public static Levels getlvdata(){
        if(gameLevel == 1){
            return new Levels.lev1();
        }
        else if(gameLevel == 2){
            return new Levels.lev2();
        }
        else if(gameLevel == 3){
            return new Levels.lev3();
        }
        else if(gameLevel == 4){
            return new Levels.lev4();
        }
        else{
            return new Levels.lev5();
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
        for (int i = 0; i < superDucks.size(); i++) {
            SuperDuck superduck = superDucks.get(i);
            int hp = superduck.getHp();

            hp -= 999;

            // DamageText 추가
            damageTexts.add(new DamageText(superduck.getX(), superduck.getY(), 999));

            if (hp <= 0) {
                superDucks.remove(i);
                i--;
            }
        }

        // smgduck 리스트의 모든 객체 체력을 999 감소
        for (int i = 0; i < smgDuck.size(); i++) {
            Weaponduck.Smgduck smg = smgDuck.get(i);
            int smgDuckHp = smg.getHp();

            smgDuckHp -= 999;

            // DamageText 추가
            damageTexts.add(new DamageText(smg.getX(), smg.getY(), 999));

            if (smgDuckHp <= 0) {
                smgDuck.remove(i);
                i--;
            }
        }

        // rifduck 리스트의 모든 객체 체력을 999 감소
        for (int i = 0; i < rifDuck.size(); i++) {
            Weaponduck.Rifduck rif = rifDuck.get(i);
            int rifHp = rif.getHp();

            rifHp -= 999;

            // DamageText 추가
            damageTexts.add(new DamageText(rif.getX(), rif.getY(), 999));

            if (rifHp <= 0) {
                rifDuck.remove(i);
                i--;
            }
        }

        // sniperduck 리스트의 모든 객체 체력을 999 감소
        for (int i = 0; i < sniperDuck.size(); i++) {
            Weaponduck.Sniperduck sni = sniperDuck.get(i);
            int sniHp = sni.getHp();

            sniHp -= 999;

            // DamageText 추가
            damageTexts.add(new DamageText(sni.getX(), sni.getY(), 999));

            if (sniHp <= 0) {
                sniperDuck.remove(i);
                i--;
            }
        }
    }

    public static int getMoney() { return money; }
    public static void setMoney(int curMoney) { Game.money = curMoney; }

    public static int getAdiatt() { return adiatt; }
    public static void setAdiatt(int curAdiatt) { Game.adiatt = curAdiatt; }

    public static long getReloadDuration() { return reloadDuration; }
    public static void setReloadDuration(long curReloadDuration) { Game.reloadDuration = curReloadDuration; }

    public static int getRedspd() { return redspd; }
    public static void setRedspd(int curRedspd) { Game.redspd = curRedspd; }

    public static int getRubberducksKills() { return rubberduckskills; }
    public static void setRubberduckKills(int curRubberduckskills) { Game.rubberduckskills = curRubberduckskills; }

    public static int getRunawayDucks() { return runawayDucks; }
    public static void setRunawayDucks(int runawayDucks) { Game.runawayDucks = runawayDucks; }
}
