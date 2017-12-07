package bubble.numero;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import bubble.shoot.R;
import game.engine.Layer;
import game.engine.Scene;
import game.engine.Sprite;
import game.engine.SurfaceViewHandler;
import game.engine.TextSprite;

public class GameScene extends Scene {
    static Bitmap bubbleMap;
    static Layer layer;
    static Bubble[][] array;
    static int bubbleHight;
    static int bubbleWight;
    public Resources resources;
    static int bubbleSize;
    static int numOfBubble = 0;
    private static int gameEndedTimer = 0;
    static int countdown = 0;
    private static int onesec = 8;
    static Layer blayer;
    static Layer clayer;
    static int score;
    static int downPeriod = 0;
    static int numOfBubbPerRow = 6;
    static int arrayLenght;
    static int nextNum;
    static int idxNextNum;

    public GameScene(SurfaceViewHandler surfaceViewHandler) {
        super(surfaceViewHandler, 1);
    }

    public void initialize() {
        gameEnded = false;

        gameEndedTimer = 0;
        countdown = 50 * onesec;
//		System.out.println("LevelSelectMenu.selected = " + LevelSelectMenu.selected);

        nextNum = 1;
        idxNextNum = 0;

        // period bet. downs
        downPeriod = 100;

        // reInitialize all static variable each level
        score = 0;
        numOfBubble = 0;
        bubbleSize = getWidth() / numOfBubbPerRow;
        bubbleMap = BitmapFactory.decodeResource(resources, R.drawable.numero_bubble3);

        bubbleHight = bubbleMap.getHeight() / 5;
        bubbleWight = bubbleMap.getWidth() / 4;

        System.out.println("sizes = " + bubbleHight + " , " + bubbleWight);

        // create Objects
        layer = new Layer();
        blayer = new Layer();
        Bitmap background;
        if (false) {
            background = BitmapFactory.decodeResource(resources, R.drawable.numero_gameplay_bg);
        } else {
            background = BitmapFactory.decodeResource(resources, R.drawable.numero_gameplay_bg_level_7);
        }

        Sprite bsprite = new Sprite(background, background.getWidth(), background.getHeight());

//        Bitmap wallbitmap = BitmapFactory.decodeResource(resources, R.drawable.wall);
//        Sprite wsprite = new Sprite(wallbitmap, wallbitmap.getWidth(), wallbitmap.getHeight());
//        wsprite.dispWidth = getWidth();
//        wsprite.dispHeight = getHeigth();
//        wsprite.moveTo(-bubbleSize / 2, -getHeigth());
//        layer.addSprite(wsprite);

        bsprite.dispWidth = getWidth();
        bsprite.dispHeight = getHeigth();
        blayer.addSprite(bsprite);

        // set variables
        arrayLenght = getHeigth() / bubbleSize;

//	    Load the Array
        array = GameActivity.generator.load("SEVEN");

        // Draw the initial Bubbles
        drawGrid();

        layer.x = bubbleSize / 2;
        getLayerManager().addLayer(blayer);
        getLayerManager().addLayer(layer);

        clayer = new Layer();
        getLayerManager().addLayer(clayer);

        ts = new TextSprite(null, "TIME REMAINING: " + insertSpace(countdown/onesec) + (countdown/onesec) + "         SCORE: " + score, 30, getHeigth()-20, Color.BLACK);
        clayer.addSprite(ts);

//	   layer.addSprite(ts);
    }

    TextSprite ts;
    Sprite s;
    TextSprite yourScore;
    Bitmap winLose;
    static boolean gameEnded = false;

    //	Handle Win/Lose Action
    public void winlose(boolean win) {
        if (win) {
            winLose = BitmapFactory.decodeResource(resources, R.drawable.win);
            if (Options.soundBoolean)
                GameActivity.winPlayer.start();
        } else {
            winLose = BitmapFactory.decodeResource(resources, R.drawable.lose);
            if (Options.soundBoolean)
                GameActivity.losePlayer.start();
        }
        s = new Sprite(winLose, winLose.getWidth(), winLose.getHeight());

        s.dispWidth = getWidth() / 2;
        s.dispHeight = (int) ((((double) winLose.getHeight()) / ((double) winLose.getWidth())) * s.dispWidth);
        s.x = getWidth() / 4;
        s.y = -s.dispHeight;
        clayer.addSprite(s);
        s.animateTo(getWidth() / 4, (getHeigth() / 2) - s.dispHeight, 20);

        yourScore = new TextSprite(null, "YOUR SCORE: " + score, 0, winLose.getHeight(), Color.BLACK, 60);
        yourScore.dispWidth = getWidth() / 2;
        yourScore.dispHeight = (int) ((((double) winLose.getHeight()) / ((double) winLose.getWidth())) * yourScore.dispWidth);
        yourScore.x = getWidth() / 4;
        yourScore.y = -s.dispHeight;
        clayer.addSprite(yourScore);
        yourScore.animateTo(getWidth() / 4, (getHeigth() / 2) - yourScore.dispHeight, 20);

        gameEnded = true;
    }


    // Game Looper
    @Override
    public void run() {
        // Check End to back
        if (gameEnded) {
            gameEndedTimer++;
            if (gameEndedTimer == 70) {
                gameEndedTimer = 0;
                GameActivity.NumeroActivity.finish();
            }
            super.run();
            return;
        }

        // Check Winning
        if (numOfBubble == 0) {
            score += countdown / onesec;
            winlose(true);
        }

        countdown--;
        if (countdown % onesec == 0) {
            // Update countdown
            int cntdwnpersec = countdown / onesec;
            ts.setColor(Color.BLACK);
            if (cntdwnpersec <= 10 && cntdwnpersec % 2 == 0) {
                ts.setColor(Color.RED);
            }
            ts.text = "TIME REMAINING: " + insertSpace(cntdwnpersec) + "" + (cntdwnpersec) + "         SCORE: " + score;
        }
        if (countdown <= 0) {
            winlose(false);
        }

        super.run();
    }

    // draw initial Bubbles
    private void drawGrid() {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                if (array[i][j] != null) {
                    numOfBubble++;
                    array[i][j].sprite.dispWidth = GameScene.bubbleSize;
                    array[i][j].sprite.dispHeight = GameScene.bubbleSize;
                    if (i % 2 == 1)
                        array[i][j].sprite.moveBy(bubbleSize / 2, 0);
                    layer.addSprite(array[i][j].sprite);
                }
            }
        }

    }

    public static void checkCollition(double x2, double y2) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                if (array[i][j] != null) {
                    if (array[i][j].isCollition(x2, y2) && !gameEnded) {
                        if (array[i][j].getNumber() != nextNum) {
                            countdown -= 3 * onesec;
                        } else {
                            GameActivity.popPlayer.start();
//                        System.out.println("Bubble number: " + array[i][j].number);
                            layer.removeSprite(array[i][j].sprite);
                            array[i][j] = null;
                            numOfBubble--;
                            countdown += 5 * onesec;
                            CalNextNum();
                            score++;
                        }
                        System.out.println("countdown = " + countdown);
                    }
                }
            }
        }
    }

    static void CalNextNum() {
        idxNextNum++;
        nextNum = Generate.intArray[idxNextNum];
        System.out.println("idxNextNum = " + idxNextNum + " || nextNum = " + nextNum);
    }

    public String insertSpace(int num) {
        int digit = 0;
        while (num > 0) {
            digit++;
            num /= 10;
        }
        if (digit == 3) return "";
        if (digit == 2) return " ";
        if (digit == 1) return "  ";
        else return "";
    }
}
