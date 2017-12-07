package bubble.numero;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;

import bubble.shoot.R;
import game.engine.*;

public class GameActivity extends Activity {

    GameScene scene;
    public static InputStream inputStream;
    public static GameActivity NumeroActivity ;
    public static MediaPlayer hitPlayer;
    public static MediaPlayer fallPlayer;
    public static MediaPlayer winPlayer;
    public static MediaPlayer losePlayer;

    static Generate generator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        inputStream = getResources().openRawResource(R.raw.file);
        NumeroActivity = this;
        SurfaceViewHandler handler = (SurfaceViewHandler)findViewById(R.id.surface);
        scene = new GameScene(handler);
        scene.resources = getResources();

        try {
            generator = new Generate();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        //create Players
        hitPlayer = MediaPlayer.create(this,R.raw.hit);
        losePlayer = MediaPlayer.create(this,R.raw.lose);
        fallPlayer = MediaPlayer.create(this,R.raw.fall);
        winPlayer = MediaPlayer.create(this,R.raw.win);
    }

    @Override
    protected void onPause() {
//    	call Garbage collector
        System.gc();
        scene.pause();
        super.onPause();
    }
    @Override
    protected void onResume() {
//    	System.out.println("resume Scene:" + scene);
        if(scene != null){
            scene.resume();
        }
        super.onResume();
    }
    @Override
    protected void onDestroy() {

        // release all Players
        hitPlayer.release();
        fallPlayer.release();
        winPlayer.release();
        losePlayer.release();
        // TODO Auto-generated method stub
        System.out.println("destroyed");
        if(scene != null){
            scene.started = false;
            scene.getSurfaceViewHandler().started = false;
        }

        //call Garbage collector
        System.gc();
        super.onDestroy();
    }
}
