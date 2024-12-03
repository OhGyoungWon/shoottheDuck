package kr.jbnu.se.std;


import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
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

    private String bgm = "backgroundMusic";
    private String boxURL = "/images/gunbox.png";

    /**
     * Array list of the ducks.
     */
    private static ArrayList<Duck> ducks;
    private static ArrayList<Superduck> superDuck;
    private static ArrayList<Weaponduck.WeaponBox> smgDuck;
    private static ArrayList<Weaponduck.WeaponBox> rifDuck;
    private static ArrayList<Weaponduck.WeaponBox> sniperDuck;

    /**
     * How many ducks leave the screen alive?
     */
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
    /**
     * 리더보드 출력
     */

    private static Weapon currentweapon;

    private static Levels lvData;
    private static int gameLevel;
    private static int money;

    private static int ExDamage;
    private static int ExAttackSpeed;
    private static int reduceSpeed;

    private static int rubberduckskills;
    private static int wineskills;
    private static boolean nuclearSwitch;

    private static boolean isReloading = false;  // 장전 중인지 여부
    private static int currentAmmo;  // 현재 남은 탄약 수
    private static int maxAmmo;  // 탄창 크기
    private long reloadStartTime;  // 장전 시작 시간
    private static long reloadDuration;  // 장전 시간 1.5초

    private static ArrayList<DamageText> damageTexts;
    private static ArrayList<DisplayText> displayTexts;

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

                soundPlayer.play(bgm);

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
        superDuck = new ArrayList<>();
        smgDuck = new ArrayList<>();
        rifDuck = new ArrayList<>();
        sniperDuck = new ArrayList<>();

        runawayDucks = 0;
        killedDucks = 0;
        score = 0;
        money = 0;
        shoots = 0;
        nuclearSwitch = false;
        reloadDuration = 1_500_000_000L;
        isReloading = false;

        ExDamage = 0;
        reduceSpeed = 0;
        ExAttackSpeed = 0;
        gameLevel = 1;
        lvData = getLvData();
        currentweapon = new Weapon.Revolver(revImg);
        weapons = new ArrayList<>();
        weapons.add(new Weapon.Revolver(revImg));
        lastTimeShoot = 0;
        if(currentweapon.fireDelay > reduceSpeed){
            timeBetweenShots = currentweapon.fireDelay - reduceSpeed;
        }
        else{
            timeBetweenShots = 50_000_000;
        }
        damageTexts = new ArrayList<>();
        displayTexts = new ArrayList<>();

        maxAmmo = currentweapon.maxammo;
        currentAmmo = maxAmmo;

        rubberduckskills = 5;
        wineskills = 5;

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
            backGrassImg = ImageIO.read(backgrassImgUrl);

            URL duckImgUrl = this.getClass().getResource("/images/duck.png");
            duckImg = ImageIO.read(duckImgUrl);

            URL superduckImgUrl = this.getClass().getResource("/images/superduck.png");
            superDuckImg = ImageIO.read(superduckImgUrl);

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
            submachine = new DrawGun(submachineImg, 12, 15_000_000L);

            URL rifleImgUrl = this.getClass().getResource("/images/rifle.png");
            BufferedImage rifleImg = ImageIO.read(rifleImgUrl);
            rifle = new DrawGun(rifleImg, 12, 20_000_000L);

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
            soundPlayer.loadSound(bgm, backgroundMusicUrl);

            URL revImgUrl = this.getClass().getResource(boxURL);
            revImg = ImageIO.read(revImgUrl);

            URL smgImgUrl = this.getClass().getResource(boxURL);
            smgImg = ImageIO.read(smgImgUrl);

            URL rifImgUrl = this.getClass().getResource(boxURL);
            rifImg = ImageIO.read(rifImgUrl);

            URL snipImgUrl = this.getClass().getResource(boxURL);
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
        superDuck.clear();
        smgDuck.clear();
        rifDuck.clear();
        sniperDuck.clear();
        damageTexts.clear();
        displayTexts.clear();
        weapons.clear();
        currentweapon = new Weapon.Revolver(revImg);
        weapons.add(new Weapon.Revolver(revImg));
        
        // We set last duck time to zero.
        Duck.lastDuckTime = 0;

        runawayDucks = 0;
        killedDucks = 0;
        score = 0;
        money = 0;
        shoots = 0;
        gameLevel = 1;
        currentweapon = new Weapon.Revolver(revImg);
        ExDamage = 0;
        reduceSpeed = 0;

        lastTimeShoot = 0;
        rubberduckskills = 0;
        nuclearSwitch = false;
        wineskills = 0;
    }

    
    /**
     * Update game logic.
     * 
     * @param gameTime gameTime of the game.
     * @param mousePosition current mouse position.
     */
    public void UpdateGame(long gameTime, Point mousePosition) {
        String currentEmail = LoginUI.getUserEmail();

        if (handleShopInteraction(mousePosition)) return;

        if (isReloading) handleReloading();

        if (nuclearSwitch) {
            reduceHealthOfAllObjects();
        }

        spawnEntities();

        UpdateAllDucks();

        handleShooting(mousePosition, currentEmail);

        ShowDamageTexts();

        updateCurrentWeapon();

        checkGameOver();

        ShowDamageTexts();

        ShowDisplayTexts();
    }

    // Handles shop interactions
    private boolean handleShopInteraction(Point mousePosition) {
        if (shop.isShopOpen()) {
            if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {
                shop.handleClick(mousePosition);  // 상점 클릭 처리
            }
            return true;
        }
        return false;
    }

    // Spawns all necessary entities
    private void spawnEntities() {
        SpawnDucks();
        SpawnSuperDuck();
        spawnWeaponDucks();
    }

    // Spawns weapon ducks based on killed ducks
    private void spawnWeaponDucks() {
        if (killedDucks == 30 && smgDuck.isEmpty()) {
            SpawnWeapon(smgDuck, smgImg);
        }
        if (killedDucks == 50 && rifDuck.isEmpty()) {
            SpawnWeapon(rifDuck, rifImg);
        }
        if (killedDucks == 70 && sniperDuck.isEmpty()) {
            SpawnWeapon(sniperDuck, snipImg);
        }
    }

    // Handles shooting logic
    private void handleShooting(Point mousePosition, String currentEmail) {
        if (Canvas.mouseButtonState(MouseEvent.BUTTON1) && !isReloading && System.nanoTime() - lastTimeShoot >= timeBetweenShots) {
            if (currentAmmo > 0) {
                performShooting(mousePosition, currentEmail);
            } else {
                initiateReloading();
            }
            lastTimeShoot = System.nanoTime();
        }
    }


    // Performs the actual shooting actions
    private void performShooting(Point mousePosition, String currentEmail) {
        shoots++;
        currentAmmo--;
        PlayAllMotion();  // 총 모션 재생
        CheckAllShoot(mousePosition, currentEmail);  // 체크 모든 쏘기
    }

    // Initiates the reloading process
    private void initiateReloading() {
        isReloading = true;
        reloadStartTime = System.nanoTime();  // 장전 시작 시간 기록
    }




    // Updates the current weapon
    private void updateCurrentWeapon() {
        switch (currentweapon.getName()) {
            case "Pistol":
                pistol.update();
                break;
            case "Revolver":
                revolver.update();
                break;
            case "SMG":
                submachine.update();
                break;
            case "Rifle":
                rifle.update();
                break;
            case "Sniper":
                sniper.update();
                break;
            default:
                break;
        }
    }

    // Checks if the game is over
    private void checkGameOver() {
        if (runawayDucks >= 20) {
            soundPlayer.stop(bgm);
            Framework.gameState = Framework.GameState.GAMEOVER;
        }
    }


    private void handleReloading() {
        if (System.nanoTime() - reloadStartTime >= reloadDuration) {  // 1.5초 장전 시간
            isReloading = false;  // 장전 완료
            currentAmmo = maxAmmo;  // 탄창 재충전
        }
    }

    private void CheckAllShoot(Point mousePosition, String currentEmail) {
        CheckDuckShoot(mousePosition, currentEmail);
        CheckSuperDuckShoot(mousePosition, currentEmail);
        GunBoxShoot(smgDuck, smgImg, mousePosition, currentEmail);
        GunBoxShoot(rifDuck, rifImg, mousePosition, currentEmail);
        GunBoxShoot(sniperDuck, snipImg, mousePosition, currentEmail);
    }

    private void CheckDuckShoot(Point mousePosition, String currentEmail) {
            for (int i = 0; i < ducks.size(); i++) {
                // We check, if the mouse was over ducks head or body, when player has shot.
                if (new Rectangle(ducks.get(i).getX() + 11, ducks.get(i).getY(), 44, 50).contains(mousePosition) ||
                        new Rectangle(ducks.get(i).getX() + 15, ducks.get(i).getY() + 39, 64, 44).contains(mousePosition)) {
                    ducks.get(i).reduceHp(currentweapon.getDamage() + ExDamage);
                    damageTexts.add(new DamageText(mousePosition.x, mousePosition.y, currentweapon.getDamage() + ExDamage));
                    if (ducks.get(i).getHp() <= 0) {
                    killedDucks++;
                    score += ducks.get(i).getScore();
                    money += ducks.get(i).getScore();
                    InGameData.saveScore(currentEmail, score);
                    // Remove the duck from the array list.
                    ducks.remove(i);

                    // We found the duck that player shoot so we can leave the for loop.
                    break;
                }
            }
        }
    }

    private void CheckSuperDuckShoot(Point mousePosition, String currentEmail) {
        for (int i = 0; i < superDuck.size(); i++) {
            if (!superDuck.isEmpty() && new Rectangle(superDuck.get(i).getX(), superDuck.get(i).getY(), superDuckImg.getWidth(), superDuckImg.getHeight()).contains(mousePosition)) {
                superDuck.get(i).reduceHp(currentweapon.getDamage() + ExDamage);
                damageTexts.add(new DamageText(mousePosition.x, mousePosition.y, currentweapon.getDamage() + ExDamage));
                if (superDuck.get(i).getHp() <= 0) {
                    killedDucks++;
                    score += superDuck.get(i).getScore();
                    money += superDuck.get(i).getScore();
                    InGameData.saveScore(currentEmail, score);

                    // Remove the duck from the array list.
                    superDuck.remove(i);
                    shop.openShop();
                    gameLevel++;
                    displayTexts.add(new DisplayText(Framework.frameWidth*0.4, Framework.frameHeight*0.3, "Level up to " + gameLevel));
                    // We found the duck that player shoot so we can leave the for loop.
                    break;
                }
            }
        }
    }

    private void GunBoxShoot(ArrayList<Weaponduck.WeaponBox> gun, BufferedImage img, Point mousePosition, String currentEmail) {
        for (int i = 0; i < gun.size(); i++) {
            if (new Rectangle(gun.get(i).getX(), gun.get(i).getY(), img.getWidth(), img.getHeight()).contains(mousePosition)) {
                gun.get(i).reduceHp(currentweapon.getDamage() + ExDamage);
                damageTexts.add(new DamageText(mousePosition.x, mousePosition.y, currentweapon.getDamage() + ExDamage));
                if (gun.get(i).getHp() <= 0) {
                    killedDucks++;
                    score += gun.get(i).getScore();
                    money += gun.get(i).getScore();
                    InGameData.saveScore(currentEmail, score);

                    // Remove the duck from the array list.
                    gun.remove(i);
                    if (img.equals(smgImg)) {
                        currentweapon = new Weapon.SMG(img);
                        maxAmmo = currentweapon.maxammo;
                        currentAmmo = maxAmmo;
                        isReloading = false;
                        timeBetweenShots = currentweapon.fireDelay;
                        weapons.add(new Weapon.SMG(img));
                    } else if (img.equals(rifImg)) {
                        currentweapon = new Weapon.Rifle(img);
                        maxAmmo = currentweapon.maxammo;
                        currentAmmo = maxAmmo;
                        isReloading = false;
                        timeBetweenShots = currentweapon.fireDelay;
                        weapons.add(new Weapon.Rifle(img));
                    } else if (img.equals(snipImg)) {
                        currentweapon = new Weapon.Sniper(img);
                        maxAmmo = currentweapon.maxammo;
                        currentAmmo = maxAmmo;
                        isReloading = false;
                        timeBetweenShots = currentweapon.fireDelay;
                        weapons.add(new Weapon.Sniper(img));
                    }
                    // We found the duck that player shoot so we can leave the for loop.
                    break;
                }
            }
        }
    }

    private void SpawnDucks() {
        if(System.nanoTime() - Duck.lastDuckTime >= lvData.sumdly && superDuck.isEmpty())
        {
            // Here we create new duck and add it to the array list.
            ducks.add(new Duck(Duck.duckLines[Duck.nextDuckLines][0] + random.nextInt(200),
                    Duck.duckLines[Duck.nextDuckLines][1], duckImg));

            // Here we increase nextDuckLines so that next duck will be created in next line.
            Duck.nextDuckLines++;
            if(Duck.nextDuckLines >= Duck.duckLines.length)
                Duck.nextDuckLines = 0;

            Duck.lastDuckTime = System.nanoTime();
        }
    }

    private void SpawnSuperDuck() {
        if(killedDucks % 20 == 0 && killedDucks != 0 && superDuck.isEmpty()){
            superDuck.add(new Superduck(Duck.duckLines[Duck.nextDuckLines][0] + random.nextInt(200),
                    (int) (Framework.frameHeight*0.6), superDuckImg));
            displayTexts.add(new DisplayText(Framework.frameWidth*0.4, Framework.frameHeight*0.3, "B  O  S  S"));
        }
    }

    private void SpawnWeapon(ArrayList<Weaponduck.WeaponBox> box, BufferedImage img) {
        box.add(new Weaponduck.WeaponBox(Framework.frameWidth + random.nextInt(200),
                (int) (Framework.frameHeight*0.25), lvData.speed, lvData.ducksc*2, lvData.duckhp*2, img));
    }

    private void UpdateAllDucks() {
        UpdateDuck();
        UpdateSuperDuck();
        UpdateWeaponBox(smgDuck, smgImg);
        UpdateWeaponBox(rifDuck, rifImg);
        UpdateWeaponBox(sniperDuck, snipImg);
    }

    private void UpdateDuck() {
        for(int i = 0; i < ducks.size(); i++) {
            // Move the duck.
            ducks.get(i).Update();

            // Checks if the duck leaves the screen and remove it if it does.
            if(ducks.get(i).getX() < 0 - duckImg.getWidth())
            {
                ducks.remove(i);
                runawayDucks++;
            }
        }
    }

    private void UpdateSuperDuck() {
        for(int i = 0; i < superDuck.size(); i++){
            superDuck.get(i).Update();

            // Checks if the duck leaves the screen and remove it if it does.
            if(superDuck.get(i).getX() < 0 - superDuckImg.getWidth())
            {
                superDuck.remove(i);
                runawayDucks = 999;
            }
        }
    }

    private void UpdateWeaponBox(ArrayList<Weaponduck.WeaponBox> box, BufferedImage img) {
        for(int i = 0; i < box.size(); i++){
            box.get(i).Update();

            // Checks if the duck leaves the screen and remove it if it does.
            if(box.get(i).getX() < 0 - img.getWidth())
            {
                box.remove(i);
            }
        }
    }

    private void PlayAllMotion() {
        MotionPlayer(revolver);
        MotionPlayer(submachine);
        MotionPlayer(rifle);
        MotionPlayer(sniper);
    }

    private void MotionPlayer(DrawGun gun) {
        if (!gun.isShooting()) {
            currentweapon.playSound(soundPlayer);
            gun.startShooting();
        }
    }

    private void ShowDamageTexts() {

        // Safely remove the element
        damageTexts.removeIf(damageText -> !damageText.update());
    }

    private void ShowDisplayTexts() {
        for(int i = 0; i < displayTexts.size(); i++) {
            if(!displayTexts.get(i).update()) {
                displayTexts.remove(i);
                i--;
            }
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
        for(int i = 0; i < superDuck.size(); i++){
            superDuck.get(i).Draw(g2d);
        }
        for(int i = 0; i < smgDuck.size(); i++){
            smgDuck.get(i).Draw(g2d);
        }
        for(int i = 0; i < rifDuck.size(); i++){
            rifDuck.get(i).Draw(g2d);
        }
        for(int i = 0; i < sniperDuck.size(); i++){
            sniperDuck.get(i).Draw(g2d);
        }

        g2d.drawImage(backGrassImg, 0, Framework.frameHeight/32 * 10, Framework.frameWidth, backGrassImg.getHeight(), null);

        if (isReloading) {
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
        for(DisplayText displayText: displayTexts) {
            displayText.draw(g2d);
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
        g2d.drawString("MONEY: " + money, 560, 21);
        g2d.drawString("LEVEL: " + gameLevel, 680, 21);
        g2d.drawString("WEAPON: " + currentweapon.getName(), 840, 21);
        g2d.drawString("BULLETS: " + currentAmmo, 840, 42);
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

    public static Levels getLvData(){
        if(gameLevel == 1){
            return new Levels.Lev1();
        }
        else if(gameLevel == 2){
            return new Levels.Lev2();
        }
        else if(gameLevel == 3){
            return new Levels.Lev3();
        }
        else if(gameLevel == 4){
            return new Levels.Lev4();
        }
        else{
            return new Levels.Lev5();
        }
    }
    public static void reduceHealthOfAllObjects() {
        displayTexts.add(new DisplayText(Framework.frameWidth*0.4, Framework.frameHeight*0.3, "NUCLEAR FIRED"));
        reduceHealth(ducks);
        reduceHealth(superDuck);
        reduceHealth(smgDuck);
        reduceHealth(rifDuck);
        reduceHealth(sniperDuck);
    }
    public static void slowAllObjects(){
        displayTexts.add(new DisplayText(Framework.frameWidth*0.4, Framework.frameHeight*0.45, "WINE SKILL ACTIVATED"));
        slowDownObject(ducks);
        slowDownObject(superDuck);
        slowDownObject(smgDuck);
        slowDownObject(rifDuck);
        slowDownObject(sniperDuck);
    }

    public static <T extends Damageable> void slowDownObject(ArrayList<T> objects){
        for (int i = 0; i < objects.size(); i++) {
            T obj = objects.get(i);
            obj.setSpeed(-1);
        }
    }

    private static <T extends Damageable> void reduceHealth(ArrayList<T> objects) {
        Iterator<T> iterator = objects.iterator();

        while (iterator.hasNext()) {
            T obj = iterator.next();
            obj.reduceHp(999);

            damageTexts.add(new DamageText(obj.getX(), obj.getY(), 999));

            if (obj.getHp() <= 0) {
                iterator.remove(); // Safely remove the object from the list
            }
        }
    }


    public static void changeWeapon(Weapon weapon) {
        Game.setCurrentWeapon(weapon);
        Game.setTimeBetweenShots(weapon);
        Game.setMaxAmmo(weapon);
        Game.setCurrentAmmo(weapon);
        Game.setIsReloading();
    }

    public static ArrayList<Weapon> getWeapons(){ return weapons; }
    public static void setCurrentWeapon(Weapon weapon){ currentweapon = weapon; }
    public static void setTimeBetweenShots(Weapon weapon){ timeBetweenShots = weapon.fireDelay; }
    public static void setMaxAmmo(Weapon weapon){ maxAmmo = weapon.maxammo; }
    public static void setCurrentAmmo(Weapon weapon){ currentAmmo = weapon.currentammo; }
    public static void setIsReloading(){ isReloading = false; }

    public static int getRubberDucksKill(){ return rubberduckskills; }
    public static void setRubberDucksKill(int n){ rubberduckskills += n; }

    public static int getWineskills(){ return wineskills; }
    public static void setWineskills(int n){ wineskills = n; }

    public static int getMoney(){ return money; }
    public static void reduceMoney(int n){ money -= n;}

    public static int getRunawayDucks(){ return runawayDucks; }
    public static void reduceRunawayDucks(int n){ runawayDucks -= n; }

    public static int getExDamage(){ return ExDamage; }
    public static void setExDamage(int n){ ExDamage = n; }

    public static long getReloadDuration(){ return reloadDuration; }
    public static void setReloadDuration(long n){ reloadDuration = n; }

    public static int getReduceSpeed(){ return reduceSpeed; }
    public static void setReduceSpeed(int n){ reduceSpeed = n; }
}
