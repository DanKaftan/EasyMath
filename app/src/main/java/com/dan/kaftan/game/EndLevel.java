package com.dan.kaftan.game;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;

public class EndLevel extends AppCompatActivity {

    Button nextLevelBtn;
    Button menuBtn;
    int levelNum;

    boolean isLevel;
    boolean mute;
    boolean levelPassed;

    TextView answer_tv;
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

        menuBtn = (Button) findViewById(R.id.menu_btn);
        nextLevelBtn = (Button) findViewById(R.id.next_level_btn);
        threeStarsIv = (ImageView)findViewById(R.id.three_stars_iv);

        answer_tv = (TextView) findViewById(R.id.answer_iv_end_level);

        showAd();


        Intent a = getIntent();
        isLevel = a.getBooleanExtra("isLevel", false);
        mute = a.getBooleanExtra("mute", false);
        levelPassed = a.getBooleanExtra("levelPassed", levelPassed);
        levelNum = a.getIntExtra("levelNum", 0 );


        if (levelPassed){
            answer_tv.setText(R.string.levelpassed);
            answer_tv.setTextColor(Color.GREEN);
            nextLevelBtn.setText(R.string.nextlevel);
            threeStarsIv.setVisibility(View.VISIBLE);
            modifyLevelPassedToFireBase();
        }
        else {
            answer_tv.setText(R.string.levelfaild);
            answer_tv.setTextColor(Color.RED);
            nextLevelBtn.setText(R.string.tryagain);
            threeStarsIv.setVisibility(View.INVISIBLE);

        }

        if (levelNum == 21){
            answer_tv.setText(R.string.alllevelsdone);
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
        i.putExtra("levelPassed", levelPassed);
        i.putExtra("chosenOperator", getChosenOperator());
        System.out.println(getChosenOperator());
        i.putExtra("difficulty", getDifficulty());
        if (levelPassed){
            i.putExtra("levelNum", levelNum+1);

        }
        else{
            i.putExtra("levelNum", levelNum);
        }
        if (levelNum <= 21){
            startActivity(i);
        }
    }

    private String getChosenOperator(){
        Intent i = getIntent();
        return i.getStringExtra("chosenOperator");
    }

    private String getDifficulty(){
        Intent i = getIntent();
        return i.getStringExtra("difficulty");
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

    private void showAd(){
        InterstitialAd mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7775472521601802/6183325504");
        AdRequest adRequestInter = new AdRequest.Builder().build();
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mInterstitialAd.show();
            }
            @Override
            public void onAdClosed()
            {
                startMusic();
            }
        });
        mInterstitialAd.loadAd(adRequestInter);
    }
}
