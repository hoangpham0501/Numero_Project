package bubble.numero;

import android.graphics.Bitmap;

import game.engine.*;

public class Bubble {
	BubbleTextSprite sprite;
	boolean alive;
	
	int row ;
	int column;
	int number;
	int timer;
	final static Bitmap BITMAP = GameScene.bubbleMap;
	int color;
	boolean checked ;
	public Bubble(int x, int y , int color, int num) {
		sprite = new BubbleTextSprite(BITMAP, String.valueOf(num), GameScene.bubbleWight,
                                        GameScene.bubbleHight, GameScene.bubbleSize/4, GameScene.bubbleSize*3/4);
		number = num;
		Intialize(x, y, color);
	}

    public Bubble Intialize (int x, int y , int color) {
		sprite.moveTo(x, y);
		column = (int) ( (sprite.x+ GameScene.bubbleSize/2) /GameScene.bubbleSize) ;
		row = (int) ( (sprite.y+ GameScene.bubbleSize/2) /GameScene.bubbleSize);
		alive = true;
		this.color = color;
		sprite.setCurrentVerFrame(color);
		timer = 0;
		checked = false ;
		sprite.setCurrentHorFrame(0);
		return this ;
	}

	public int getNumber() {
        return number;
    }

    public boolean isCollition(double x2, double y2) {
        return x2 > sprite.x && x2 < sprite.x + (double)GameScene.bubbleSize*3/2 && y2 > sprite.y && y2 < sprite.y + (double)GameScene.bubbleSize*3/2;
    }
	
	public void updateFrame()
	{
		timer ++ ;
		if (sprite.currentHorFrame == 3)
			{
				GameScene.layer.removeSprite(sprite);
				GameScene.falling.remove(this);
				GameScene.pool.add(this);
			}
		if (timer%5 == 0)
			sprite.nextHorFrame();
	}
	
}
