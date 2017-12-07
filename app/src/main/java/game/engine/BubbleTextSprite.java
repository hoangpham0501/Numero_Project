package game.engine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

public class BubbleTextSprite extends Sprite {
    public String text;
    public int deltaBubbleSizeX;
    public int deltaBubbleSizeY;
    int sizePerChar = 7;

    public BubbleTextSprite(Bitmap image, String text, int width, int height, int bbSizeX, int bbSizeY) {
        super(image, width, height);
        this.text = text;
        this.deltaBubbleSizeX = bbSizeX;
        this.deltaBubbleSizeY = bbSizeY;
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        paint.setShadowLayer(6, 0, 0, Color.WHITE);
        // TODO Auto-generated constructor stub
    }
    @Override
    public void render(Canvas c) {
        if(steps-- > 0){
//		   System.out.println("x: "+x+" y: "+y+" deltaX: "+deltaX+" deltaY: "+deltaY);
            x+=deltaX;
            y+=deltaY;
        }
        src.left = currentHorFrame*width;
        src.top = (int) (currentVerFrame*height);
        src.right = currentHorFrame*width + width;
        src.bottom = (int) (currentVerFrame*height +height);
        dist.left = (int)x;
        dist.top = (int) y;
        dist.bottom = (int)y+ dispHeight;
        dist.right =  (int)x+dispWidth ;
        c.drawBitmap(image,src,dist, paint);
        c.drawText(text,(float)(x+deltaX+(deltaBubbleSizeX-sizePerChar*text.length()/2)),
                        (float)(y+deltaY+deltaBubbleSizeY-sizePerChar*2) , paint);

    }

}
