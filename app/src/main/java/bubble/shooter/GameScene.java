package bubble.shooter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;

import java.util.LinkedList;
import java.util.Queue;

import bubble.shoot.R;
import game.engine.*;

public class GameScene extends Scene {
    static Point originalPoint; // l point elly bndrb mnha
    static LinkedList<Bubble> BubbleList = new LinkedList<Bubble>();
    static Bitmap bubbleMap;
    static Layer layer;
    static Bubble[][] array;
    static Queue<Bubble> BubblesGroup = new LinkedList<Bubble>();
    static LinkedList<Bubble> falling = new LinkedList<Bubble>();
    static Queue<Bubble> temp = new LinkedList<Bubble>();
    static LinkedList<Bubble> pool = new LinkedList<Bubble>();
    static LinkedList<Bubble> extra = new LinkedList<Bubble>();
    static int bubbleHight;
    static int bubbleWight;
    public Resources resources;
    static int bubbleSize;
    static double shiftDown;
    static int numOfBubble = 0;
    private static int rowsDown = 0; // number of changed rows to modify original Point
    private static int timer = 0;
    static int countdown = 0;
    private static int onesec = 8;
    static double bubbleShitup = 0;
    static Layer blayer;
    static Layer clayer;
    static int score;
    static int downPeriod = 0;
    static int numOfBubbPerRow = 5 + 1; // +1 which will decrease laster in Generator
    static int arrayLenght;
    static int superBubbleColor = 4;
    static int nextNum;

    public GameScene(SurfaceViewHandler surfaceViewHandler) {
        super(surfaceViewHandler, 1);
    }

    public void initialize() {
        gameEnded = false;

        timer = 0;
        countdown = 50 * onesec;
//		System.out.println("LevelSelectMenu.selected = " + LevelSelectMenu.selected);

        nextNum = 1;

        // period bet. downs
        downPeriod = 100;

        // reInitialize all static variable each level
        rowsDown = 0;
        score = 0;
        numOfBubble = 0;
        BubbleList.clear();
        BubblesGroup.clear();
        temp.clear();
        falling.clear();
        pool.clear();
        extra.clear();
        bubbleSize = getWidth() / numOfBubbPerRow;
        shiftDown = (bubbleSize * 2) / 3;
        bubbleMap = BitmapFactory.decodeResource(resources, R.drawable.numero_bubble);

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

        Bitmap wallbitmap = BitmapFactory.decodeResource(resources, R.drawable.wall);
        Sprite wsprite = new Sprite(wallbitmap, wallbitmap.getWidth(), wallbitmap.getHeight());
        wsprite.dispWidth = getWidth();
        wsprite.dispHeight = getHeigth();
        wsprite.moveTo(-bubbleSize / 2, -getHeigth());
        layer.addSprite(wsprite);

        bsprite.dispWidth = getWidth();
        bsprite.dispHeight = getHeigth();
        blayer.addSprite(bsprite);

        // set variables
        arrayLenght = getHeigth() / bubbleSize;

//	    Load the Array
        array = GameActivity.generator.load("SEVEN");
        System.out.println("modified = " + bubbleShitup);

        // Draw the initial Bubbles
        drawGrid();

        int factor = (int) (bubbleSize * 2);
        // Calculate Original Point (Shoteer Point)
        originalPoint = new Point(((getWidth() - bubbleSize) / 2) - bubbleSize / 2, getHeigth() - bubbleSize - factor / 2);

        layer.x = bubbleSize / 2;
        getLayerManager().addLayer(blayer);
        getLayerManager().addLayer(layer);

        clayer = new Layer();
        getLayerManager().addLayer(clayer);

        ts = new TextSprite(null, "TIME REMAINING: " + insertSpace(countdown/onesec) + (countdown/onesec) + "         SCORE: " + score, 0, getHeigth(), Color.GREEN);
        clayer.addSprite(ts);

//	   layer.addSprite(ts);
    }

    TextSprite ts;
    Sprite s;
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
        gameEnded = true;
        timer = 0;
    }

    // Game Looper
    @Override
    public void run() {
        // Check End to back
        if (gameEnded) {
            if (timer++ == 200) {
                timer = 0;
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

        timer++;

        countdown--;
        if (countdown % onesec == 0) {
            // Update countdown
            int cntdwnpersec = countdown / onesec;
            if (cntdwnpersec <= 10) {
                if (cntdwnpersec % 2 == 0) {
                    ts.setColor(Color.RED);
                } else {
                    ts.setColor(Color.GREEN);
                }
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
                    array[i][j].sprite.moveBy(0, i * -bubbleShitup);
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
                            score++;
                        } else {
//                        System.out.println("Bubble number: " + array[i][j].number);
                            layer.removeSprite(array[i][j].sprite);
                            array[i][j] = null;
                            numOfBubble--;
                            countdown += 3 * onesec;
                            nextNum++;
                        }
                        System.out.println("countdown = " + countdown);
                    }
                }
            }
        }
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
