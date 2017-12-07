package bubble.numero;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import bubble.shoot.R;

public class MainMenuActivity extends Activity {

    static String gameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void classicalButton (View v){
        gameMode = "Classical";
        Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
        startActivity(intent);
    }

    public void challengeButton (View v){
        gameMode = "Challenge";
        Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
        startActivity(intent);
    }
}
