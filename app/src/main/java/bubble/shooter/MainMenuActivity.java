package bubble.shooter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import bubble.shoot.R;

public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void classicalButton (View v){
        Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
        startActivity(intent);
    }

    public void challengeButton (View v){

    }
}
