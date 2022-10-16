package com.dan.kaftan.game;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class LevelInterducerActivity extends AppCompatActivity {


    int levelNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // rotate screen
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_interducer);

        getSupportActionBar().hide();
        //hide navigation bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // start music
        MediaPlayer nextLevelMusic = MediaPlayer.create(LevelInterducerActivity.this, R.raw.game_over_sound);
        nextLevelMusic.start();



        // start animation
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.mytransition);
        TextView levelTv = (TextView) findViewById(R.id.level_tv_int);
        Intent intent = getIntent();
        levelNum = intent.getIntExtra("levelNum", 0 );
        levelTv.setText("level " + intent.getIntExtra("levelNum", 0 ));
        levelTv.startAnimation(myanim);
        // start level
        switchActivity();

    }


    public void switchActivity() {
        final Intent i = new Intent(LevelInterducerActivity.this, Game.class);

        Thread timer = new Thread() {
            public void run() {
                try {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {

                    }
                    //if (startGameMusic != null) {
                       // startGameMusic.start();
                   // }
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = getIntent();
                    i.putExtra("isLevel", intent.getBooleanExtra("isLevel", false));
                    i.putExtra("mute", intent.getBooleanExtra("mute", false));
                    i.putExtra("chosenLevelNum", levelNum);
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }
}