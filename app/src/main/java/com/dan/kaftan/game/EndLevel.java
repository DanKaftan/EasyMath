package com.dan.kaftan.game;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.analytics.FirebaseAnalytics;

public class EndLevel extends AppCompatActivity {

    ImageButton nextLevelBtn;
    ImageButton menuBtn;
    int levelNum;

    boolean isLevel;
    boolean mute;
    boolean levelPassed;

    ImageView answer_iv;
    ImageView threeStarsIv;
    private FirebaseAnalytics mFirebaseAnalytics;

    MediaPlayer levelupsound;
    boolean isVisible = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_end_level);

        getSupportActionBar().hide();
        //hide navigation bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        // rotate screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        try {
            if (Game.interstitialAd != null && Game.interstitialAd.isLoaded()) {
                Game.interstitialAd.show();
            }
        }catch (Exception e){
            // never mind, it is just an Ad...
        }



        menuBtn = (ImageButton) findViewById(R.id.menu_btn);
        nextLevelBtn = (ImageButton) findViewById(R.id.next_level_btn);
        threeStarsIv = (ImageView)findViewById(R.id.three_stars_iv);

        answer_iv = (ImageView) findViewById(R.id.answer_iv_end_level);



        Intent a = getIntent();
        isLevel = a.getBooleanExtra("isLevel", false);
        mute = a.getBooleanExtra("mute", false);
        levelPassed = a.getBooleanExtra("levelPassed", levelPassed);
        levelNum = a.getIntExtra("levelNum", 0 );

        startMusic();


        if (levelPassed){
            answer_iv.setImageResource(R.drawable.level_passed);
            nextLevelBtn.setImageResource(R.drawable.next_level_btn);
            threeStarsIv.setVisibility(View.VISIBLE);
            modifyLevelPassedToFireBase();
        }
        else {
            answer_iv.setImageResource(R.drawable.level_failed);
            nextLevelBtn.setImageResource(R.drawable.try_again_btn);
            threeStarsIv.setVisibility(View.INVISIBLE);

        }

        if (levelNum == 21){
            answer_iv.setImageResource(R.drawable.end_levels_pic);
            answer_iv.setImageResource(R.drawable.level_finished_txt);
            nextLevelBtn.setVisibility(View.INVISIBLE);
        }

    }

    private void modifyLevelPassedToFireBase() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CHARACTER, "player");
        bundle.putLong(FirebaseAnalytics.Param.LEVEL, levelNum);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LEVEL_UP, bundle);
    }


    public void menuBtnOnClick(View view) {
        Intent i = new Intent(EndLevel.this, MainActivity.class);
        i.putExtra("mute", mute);
        startActivity(i);


    }


    public void nextLevelBtnOnCLick(View view) {

        Intent i = new Intent(EndLevel.this, LevelInterducerActivity.class);
        i.putExtra("isLevel", isLevel);
        i.putExtra("mute", mute);
        i.putExtra("levelNum", levelNum+1);
        if (levelNum <= 21){
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }

    private void startMusic(){
        levelupsound = MediaPlayer.create(EndLevel.this, R.raw.level_up_sound);

        if (!mute) {
            levelupsound.start();
        } else {
            //muteBtn.setBackgroundResource(R.drawable.mute_btn_2);
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        isVisible = hasFocus;
        if (!isVisible) {
            levelupsound.pause();
        } else {
            //mainActivityBackgroud.start();
        }


    }
}
