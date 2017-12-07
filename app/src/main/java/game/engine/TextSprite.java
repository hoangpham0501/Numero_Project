package game.engine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

public class TextSprite extends Sprite {
	public String text;

	public TextSprite(Bitmap image, String text, int width, int height, int color) {
		super(image, width, height);
		this.text = text;
		paint.setColor(color);
		paint.setTextSize(40);
		paint.setShadowLayer(6, 0, 0, Color.WHITE);
		// TODO Auto-generated constructor stub
	}

	public TextSprite(Bitmap image, String text, int width, int height, int color, int txtSize) {
		super(image, width, height);
		this.text = text;
		paint.setColor(color);
		paint.setTextSize(txtSize);
		paint.setShadowLayer(6, 0, 0, Color.WHITE);
		// TODO Auto-generated constructor stub
	}

	public void setColor (int color) {
        paint.setColor(color);
    }

	@Override
	public void render(Canvas c) {
        if(steps-- > 0){
//		   System.out.println("x: "+x+" y: "+y+" deltaX: "+deltaX+" deltaY: "+deltaY);
            x+=deltaX;
            y+=deltaY;
        }
//		c.drawText(text,(int) x, (int)y, (float)x+dispWidth, (float)y+dispHeight,paint);
//		paint.setColor(Color.MAGENTA);
//		System.out.println("gere");
//		System.out.println(text+" "+x+" "+y+" "+paint);
		c.drawText(text,(float)(x+width),(float)(y+height) , paint);
		
	}

	@Override
    public void animateTo(double x,double y,int steps){
        deltaX = ( x - this.x) /steps;
        deltaY = ( y - this.y) /steps;
        this.steps = steps;
    }
	
}
