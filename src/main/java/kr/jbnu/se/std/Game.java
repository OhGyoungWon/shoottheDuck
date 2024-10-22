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
    private ArrayList<Superduck> superducks;
    private ArrayList<Weaponduck.Smgduck> smgduck;
    private ArrayList<Weaponduck.Rifduck> rifduck;
    private ArrayList<Weaponduck.Sniperduck> sniperduck;

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
    private BufferedImage backgrassImg;
    
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

    public static Weapon currentweapon;

    public static Levels lvdata;
    public static int gamelevel;
    public static int money;

    public static int adiatt;
    public static int atspdpls;
    public static int redspd;

    public static int rubberduckskills;
    public static boolean nuclearswitch;

    public static boolean isReloading = false;  // 장전 중인지 여부
    public static int currentAmmo;  // 현재 남은 탄약 수
    public static int maxAmmo;  // 탄창 크기
    private long reloadStartTime;  // 장전 시작 시간
    public static long reloadDuration;  // 장전 시간 1.5초

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
        sniperduck = new ArrayList<>();

        runawayDucks = 0;
        killedDucks = 15;
        score = 0;
        money = 0;
        shoots = 0;
        level = 1;
        nuclearswitch = false;
        reloadDuration = 1_500_000_000L;
        isReloading = false;

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

        maxAmmo = currentweapon.maxammo;
        currentAmmo = maxAmmo;

    }

    /**
     * Load game files - images, sounds, ...
     */
    private void LoadContent()
    {
        try
        {
            URL backgroundImgUrl = this.getClass().getResource("/images/background.png");
            backgroundImg = ImageIO.read(backgroundImgUrl);
            
            URL grassImgUrl = this.getClass().getResource("/images/grass.png");
            grassImg = ImageIO.read(grassImgUrl);

            URL backgrassImgUrl = this.getClass().getResource("/images/backgrass.png");
            backgrassImg = ImageIO.read(backgrassImgUrl);

            URL duckImgUrl = this.getClass().getResource("/images/duck.png");
            duckImg = ImageIO.read(duckImgUrl);

            URL superduckImgUrl = this.getClass().getResource("/images/superduck.png");
            superduckImg = ImageIO.read(superduckImgUrl);

            URL sightImgUrl = this.getClass().getResource("/images/sight.png");
            sightImg = ImageIO.read(sightImgUrl);
            sightImgMiddleWidth = sightImg.getWidth() / 2;
            sightImgMiddleHeight = sightImg.getHeight() / 2;

            URL pistolImgUrl = this.getClass().getResource("/images/pistol.png");
            BufferedImage pistolImg = ImageIO.read(pistolImgUrl);
            pistol = new DrawGun(pistolImg, 11, 10_000_000L);  // 11프레임, 프레임당 100ms

            URL revolverImgUrl = this.getClass().getResource("/images/revolver.png");
            BufferedImage revolverImg = ImageIO.read(revolverImgUrl);
            revolver = new DrawGun(revolverImg, 10, 30_000_000L);  // 10프레임, 프레임당 300ms

            URL submachineImgUrl = this.getClass().getResource("/images/submachine.png");
            BufferedImage submachineImg = ImageIO.read(submachineImgUrl);
            submachine = new DrawGun(submachineImg, 12, 8_000_000L);

            URL rifleImgUrl = this.getClass().getResource("/images/rifle.png");
            BufferedImage rifleImg = ImageIO.read(rifleImgUrl);
            rifle = new DrawGun(rifleImg, 12, 10_000_000L);

            URL sniperImgUrl = this.getClass().getResource("/images/sniper.png");
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

            URL revImgUrl = this.getClass().getResource("/images/gunbox.png");
            revImg = ImageIO.read(revImgUrl);

            URL smgImgUrl = this.getClass().getResource("/images/gunbox.png");
            smgImg = ImageIO.read(smgImgUrl);

            URL rifImgUrl = this.getClass().getResource("/images/gunbox.png");
            rifImg = ImageIO.read(rifImgUrl);

            URL snipImgUrl = this.getClass().getResource("/images/gunbox.png");
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
        superducks.clear();
        smgduck.clear();
        rifduck.clear();
        sniperduck.clear();
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
        if (isReloading) {
            if (System.nanoTime() - reloadStartTime >= reloadDuration) {  // 1.5초 장전 시간
                isReloading = false;  // 장전 완료
                currentAmmo = maxAmmo;  // 탄창 재충전
            }
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
                    (int) (Framework.frameHeight*0.25), lvdata.speed, lvdata.ducksc*2, lvdata.duckhp*2, smgImg ));
        }
        if(killedDucks == 50 && rifduck.isEmpty()){
            rifduck.add(new Weaponduck.Rifduck(Framework.frameWidth + random.nextInt(200),
                    (int) (Framework.frameHeight*0.25), lvdata.speed, lvdata.ducksc*2, lvdata.duckhp*2, rifImg ));
        }
        if(killedDucks == 70 && sniperduck.isEmpty()){
            sniperduck.add(new Weaponduck.Sniperduck(Framework.frameWidth + random.nextInt(200),
                    (int) (Framework.frameHeight*0.25), lvdata.speed, lvdata.ducksc*2, lvdata.duckhp*2, snipImg));
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
        for(int i = 0; i < sniperduck.size(); i++){
            sniperduck.get(i).Update();

            // Checks if the duck leaves the screen and remove it if it does.
            if(sniperduck.get(i).x < 0 - snipImg.getWidth())
            {
                sniperduck.remove(i);
                runawayDucks++;
            }
        }

        // Does player shoots?
        if(Canvas.mouseButtonState(MouseEvent.BUTTON1) && !isReloading)
        {
            // Checks if it can shoot again.
            if(System.nanoTime() - lastTimeShoot >= timeBetweenShots)
            {

                if(currentAmmo > 0) {
                    shoots++;
                    currentAmmo--;
// 총 사운드 재생

                    // 총 모션 재생
                    if (!pistol.isShooting() && currentweapon.getName().equals("Pistol")) {
                        pistol.startShooting();
                    } else if (!revolver.isShooting() && currentweapon.getName().equals("Revolver")) {
                        soundPlayer.play("gunshot");
                        revolver.startShooting();
                    } else if (!submachine.isShooting() && currentweapon.getName().equals("SMG")) {
                        soundPlayer.play("smg");
                        submachine.startShooting();
                    } else if (!rifle.isShooting() && currentweapon.getName().equals("Rifle")) {
                        soundPlayer.play("rifle");
                        rifle.startShooting();
                    } else if (!sniper.isShooting() && currentweapon.getName().equals("Sniper")) {
                        soundPlayer.play("sniper");
                        sniper.startShooting();
                    }

                    // We go over all the ducks and we look if any of them was shoot.
                    for (int i = 0; i < ducks.size(); i++) {
                        // We check, if the mouse was over ducks head or body, when player has shot.
                        if (new Rectangle(ducks.get(i).x + 11, ducks.get(i).y, 44, 50).contains(mousePosition) ||
                                new Rectangle(ducks.get(i).x + 15, ducks.get(i).y + 39, 64, 44).contains(mousePosition)) {
                            ducks.get(i).hp -= currentweapon.getDamage() + adiatt;
                            damageTexts.add(new DamageText(mousePosition.x, mousePosition.y, currentweapon.getDamage() + adiatt));
                            if (ducks.get(i).hp <= 0) {
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
                    for (int i = 0; i < superducks.size(); i++) {
                        if (new Rectangle(superducks.get(i).x, superducks.get(i).y,
                                superduckImg.getWidth(), superduckImg.getHeight()).contains(mousePosition)) {
                            if (!superducks.isEmpty() && new Rectangle(superducks.get(i).x, superducks.get(i).y,
                                    superduckImg.getWidth(), superduckImg.getHeight()).contains(mousePosition)) {
                                superducks.get(i).hp -= currentweapon.getDamage() + adiatt;
                                damageTexts.add(new DamageText(mousePosition.x, mousePosition.y, currentweapon.getDamage() + adiatt));
                                if (superducks.get(i).hp <= 0) {
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
                    for (int i = 0; i < smgduck.size(); i++) {
                        if (new Rectangle(smgduck.get(i).x, smgduck.get(i).y,
                                smgImg.getWidth(), smgImg.getHeight()).contains(mousePosition)) {
                            if (!smgduck.isEmpty() && new Rectangle(smgduck.get(i).x, smgduck.get(i).y,
                                    smgImg.getWidth(), smgImg.getHeight()).contains(mousePosition)) {
                                smgduck.get(i).hp -= currentweapon.getDamage() + adiatt;
                                damageTexts.add(new DamageText(mousePosition.x, mousePosition.y, currentweapon.getDamage() + adiatt));
                                if (smgduck.get(i).hp <= 0) {
                                    killedDucks++;
                                    score += smgduck.get(i).score;
                                    money += smgduck.get(i).score;

                                    // Remove the duck from the array list.
                                    smgduck.remove(i);
                                    currentweapon = new Weapon.SMG(smgImg);
                                    maxAmmo = currentweapon.maxammo;
                                    currentAmmo = maxAmmo;
                                    isReloading = false;
                                    timeBetweenShots = currentweapon.fireDelay;
                                    weapons.add(new Weapon.SMG(smgImg));

                                    // We found the duck that player shoot so we can leave the for loop.
                                    break;
                                }
                            }
                        }
                    }
                    for (int i = 0; i < rifduck.size(); i++) {
                        if (new Rectangle(rifduck.get(i).x, rifduck.get(i).y,
                                rifImg.getWidth(), rifImg.getHeight()).contains(mousePosition)) {
                            if (!rifduck.isEmpty() && new Rectangle(rifduck.get(i).x, rifduck.get(i).y,
                                    rifImg.getWidth(), rifImg.getHeight()).contains(mousePosition)) {
                                rifduck.get(i).hp -= currentweapon.getDamage() + adiatt;
                                damageTexts.add(new DamageText(mousePosition.x, mousePosition.y, currentweapon.getDamage() + adiatt));
                                if (rifduck.get(i).hp <= 0) {
                                    killedDucks++;
                                    score += rifduck.get(i).score;
                                    money += rifduck.get(i).score;

                                    // Remove the duck from the array list.
                                    rifduck.remove(i);
                                    currentweapon = new Weapon.Rifle(rifImg);
                                    maxAmmo = currentweapon.maxammo;
                                    currentAmmo = maxAmmo;
                                    isReloading = false;
                                    timeBetweenShots = currentweapon.fireDelay;
                                    weapons.add(new Weapon.Rifle(smgImg));

                                    // We found the duck that player shoot so we can leave the for loop.
                                    break;
                                }
                            }
                        }
                    }
                    for (int i = 0; i < sniperduck.size(); i++) {
                        if (new Rectangle(sniperduck.get(i).x, sniperduck.get(i).y,
                                snipImg.getWidth(), snipImg.getHeight()).contains(mousePosition)) {
                            if (!sniperduck.isEmpty() && new Rectangle(sniperduck.get(i).x, sniperduck.get(i).y,
                                    snipImg.getWidth(), snipImg.getHeight()).contains(mousePosition)) {
                                sniperduck.get(i).hp -= currentweapon.getDamage() + adiatt;
                                damageTexts.add(new DamageText(mousePosition.x, mousePosition.y, currentweapon.getDamage() + adiatt));
                                if (sniperduck.get(i).hp <= 0) {
                                    killedDucks++;
                                    score += sniperduck.get(i).score;
                                    money += sniperduck.get(i).score;

                                    // Remove the duck from the array list.
                                    sniperduck.remove(i);
                                    currentweapon = new Weapon.Sniper(snipImg);
                                    maxAmmo = currentweapon.maxammo;
                                    currentAmmo = maxAmmo;
                                    isReloading = false;
                                    timeBetweenShots = currentweapon.fireDelay;
                                    weapons.add(new Weapon.Sniper(snipImg));

                                    // We found the duck that player shoot so we can leave the for loop.
                                    break;
                                }
                            }
                        }
                    }
                }
                else if(currentAmmo == 0){
                    isReloading = true;
                    reloadStartTime = System.nanoTime();  // 장전 시작 시간 기록;
                    System.out.println("RELOADING...");
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

        if (currentweapon.getName().equals("Pistol")) {
            pistol.update();
        }
        else if (currentweapon.getName().equals("Revolver")) {
            revolver.update();
        }
        else if (currentweapon.getName().equals("SMG")) {
            submachine.update();
        }
        else if (currentweapon.getName().equals("Rifle")) {
            rifle.update();
        }
        else if (currentweapon.getName().equals("Sniper")) {
            sniper.update();
        }

        // When 200 ducks runaway, the game ends.
        if(runawayDucks >= 100) {
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
        for(int i = 0; i < sniperduck.size(); i++){
            sniperduck.get(i).Draw(g2d);
        }

        g2d.drawImage(backgrassImg, 0, Framework.frameHeight/32 * 10, Framework.frameWidth, backgrassImg.getHeight(), null);

        if (isReloading) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  // 부드러운 텍스트
            g2d.setFont(new Font("Arial", Font.BOLD, 30));  // 폰트 설정
            g2d.setColor(Color.RED);  // 색상 설정
            String reloadMessage = "RELOADING...";
            System.out.println("왜안돼");
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

        if (currentweapon.getName().equals("Pistol")) {
            g2d.drawImage(pistol.getCurrentFrame(), 0, Framework.frameHeight - (Framework.frameHeight / 4), (Framework.frameHeight / 15) * 8, (Framework.frameHeight / 15) * 5, null);
        }
        else if (currentweapon.getName().equals("Revolver")) {
            g2d.drawImage(revolver.getCurrentFrame(), Framework.frameHeight/10, Framework.frameHeight - (Framework.frameHeight / 3), (Framework.frameHeight / 15) * 8, (Framework.frameHeight / 15) * 5, null);
        }
        else if (currentweapon.getName().equals("SMG")) {
            g2d.drawImage(submachine.getCurrentFrame(), Framework.frameHeight/10, Framework.frameHeight - (Framework.frameHeight / 4), (Framework.frameHeight / 15) * 8, (Framework.frameHeight / 15) * 5, null);
        }
        else if (currentweapon.getName().equals("Rifle")) {
            g2d.drawImage(rifle.getCurrentFrame(), Framework.frameHeight/10, Framework.frameHeight - (Framework.frameHeight / 4), (Framework.frameHeight / 15) * 8, (Framework.frameHeight / 15) * 5, null);
        }
        else if (currentweapon.getName().equals("Sniper")) {
            g2d.drawImage(sniper.getCurrentFrame(), Framework.frameHeight/10, Framework.frameHeight - (Framework.frameHeight / 5), (Framework.frameHeight / 15) * 12, (Framework.frameHeight / 15) * 5 / 2, null);
        }

        g2d.setFont(font);
        g2d.setColor(Color.darkGray);
        
        g2d.drawString("RUNAWAY: " + runawayDucks, 10, 21);
        g2d.drawString("KILLS: " + killedDucks, 160, 21);
        g2d.drawString("SHOOTS: " + shoots, 299, 21);
        g2d.drawString("SCORE: " + score, 440, 21);
        g2d.drawString("Money: " + money, 560, 21);
        g2d.drawString("LEVEL: " + level, 680, 21);
        g2d.drawString("Weapon: " + currentweapon.getName(), 840, 21);
        g2d.drawString("Boolets: " + currentAmmo, 840, 42);
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
        else if(gamelevel == 4){
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

        // sniperduck 리스트의 모든 객체 체력을 999 감소
        for (int i = 0; i < sniperduck.size(); i++) {
            Weaponduck.Sniperduck sni = sniperduck.get(i);
            sni.hp -= 999;

            // DamageText 추가
            damageTexts.add(new DamageText(sni.x, sni.y, 999));

            if (sni.hp <= 0) {
                sniperduck.remove(i);
                i--;
            }
        }
    }
}
