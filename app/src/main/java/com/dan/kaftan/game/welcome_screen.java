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
import android.widget.ImageView;
import android.widget.TextView;

public class welcome_screen extends AppCompatActivity {

    ImageView iv;
    TextView tv2;
    ImageView icon;
    MediaPlayer startGameMusic;
    static boolean ismulty = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        iv = (ImageView) findViewById(R.id.iv);
        tv2 = (TextView) findViewById(R.id.tv2);
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.mytransition);
        tv2.startAnimation(myanim);
        iv.startAnimation(myanim);
        startGameMusic = MediaPlayer.create(welcome_screen.this, R.raw.start_game_music);
        swichActivity();

        if (ismulty) {
            // iv.setImageResource(R.drawable.multiplication_icon);
        }


    }

    public void swichActivity() {
        final Intent i = new Intent(welcome_screen.this, MainActivity.class);

        Thread timer = new Thread() {
            public void run() {
                try {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {

                    }
                    if (startGameMusic != null) {
                        startGameMusic.start();
                    }
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }


}
