package com.dan.kaftan.game;

import android.animation.ValueAnimator;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class EndGame extends AppCompatActivity implements RewardedVideoAdListener {


    TextView tvFinalScore;
    TextView tvBestScore;
    TextView finalScoreTV;
    TextView bestScoreTv;

    int score = 0;
    int bestScore = 0;


    Button btnShare;
    Button muteBtn;
    Button rateStarBtn;
    Button btnRevive;
    Button menuBtn;
    Button settingsBtn;
    Button startNewGameBtn;


    boolean revive = false;
    boolean mute;
    boolean isFromSettings = true;

    ImageView gameOverImg;

    int linesCounter = 0;


    private RewardedVideoAd mRewardedVideoAd;
    private static final String FILE_NAME = "best_score.txt";
    private static final String TAG = "MainActivity";
    private AdView mAdView;


    // sounds
    MediaPlayer gameOverSound;
    static MediaPlayer endGameMusic;
    MediaPlayer countingSound;


    boolean isVisible = true;


    GoogleSignInClient googleSignIn;



    int difficulty = 10;
    String operator = "+";

    List<String> bestScoreList = new ArrayList<>();

    InterstitialAd interstitialAd;






    Thread tr1 = new Thread(new Runnable() {
        @Override
        public void run() {
            SystemClock.sleep(4000);
            Animation animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
            Animation animZoomOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    btnRevive.startAnimation(animZoomIn);
                    Thread.sleep(1000);
                    btnRevive.startAnimation(animZoomOut);
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            }
        }
    });


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // full screen
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
        getSupportActionBar().hide();
        //hide navigation bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        operator = getChosenOperator();
        // rotate screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        GoogleSignInOptions  signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                        .build();
        googleSignIn = GoogleSignIn.getClient(this, signInOptions);


        Intent i = getIntent();
        isFromSettings = i.getBooleanExtra("isFromSettings", true);
        mute = i.getBooleanExtra("mute", true);
        difficulty = i.getIntExtra("difficulty", 10);


        gameOverSound = MediaPlayer.create(EndGame.this, R.raw.game_over_sound);
        endGameMusic = MediaPlayer.create(EndGame.this, R.raw.main_activity_music);



        btnRevive = (Button) findViewById(R.id.btnrevive);
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);


        rateStarBtn = (Button) findViewById(R.id.rate_star_btn);
        tvBestScore = (TextView) findViewById(R.id.tvBestScore);
        muteBtn = (Button) findViewById(R.id.mute_btn2);
        menuBtn = (Button) findViewById(R.id.home_btn_end_game);
        gameOverImg = (ImageView) findViewById(R.id.game_over_img);
        finalScoreTV= (TextView) findViewById(R.id.finalscoretv);
        bestScoreTv= (TextView) findViewById(R.id.textView6);
        startNewGameBtn = (Button) findViewById(R.id.btnStartNewGame);
        settingsBtn = (Button) findViewById(R.id.end_game_settings_btn);


        copyReviveFromPrevActivity();
        if (revive) {
            btnRevive.setVisibility(View.GONE);
        }
        copyScoreFromPrevActivity();
        //init();


        setMute();
        setMuteBackground();
        displayFinalScore();
        share();
        getBestScore();

        startSummery();

        if (!mute && !isFromSettings) {
            endGameMusic.setLooping(true);
        }


        if (bestScore < score) {
            try {
                saveBestScore();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            tvBestScore.setText(Integer.toString(score));
        } else {
            tvBestScore.setText(Integer.toString(bestScore));

        }
        if (!revive) {
            btnRevive.setVisibility(View.INVISIBLE);
        }

        rateStarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "https://play.google.com/store/apps/details?id=com.dan.kaftan.game")));

                }
            }
        });

        tr1.start();

        modifyScoreToFireBase();


    }

    // set the text of the final score
    public void displayFinalScore() {

        tvFinalScore = (TextView) findViewById(R.id.tvFinalScore);
        tvFinalScore.setText(Integer.toString(score));
    }


// share app

    public void share() {

        btnShare = (Button) findViewById(R.id.btnshare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "Amazing math game for first graders: https://play.google.com/store/apps/details?id=com.dan.kaftan.game";
                String shareSub = "You might like this app:";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(myIntent, "share using"));

            }
        });


    }


    //start new game
    public void startNewGameOnCLick(View view) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent i = new Intent(EndGame.this, Game.class);
        i.putExtra("revive", false);
        i.putExtra("mute", mute);
        i.putExtra("difficulty", Integer.toString(difficulty));
        i.putExtra("chosenOperator", operator);
        endGameMusic.pause();
        startActivity(i);


    }

    // reward video ad
    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-7775472521601802/3608327197", new AdRequest.Builder().build());
    }


    @Override
    public void onRewardedVideoAdLoaded() {
        mRewardedVideoAd.show();
        System.out.println("onRewardedVideoAdLoaded");

    }

    @Override
    public void onRewardedVideoAdOpened() {
        System.out.println("onRewardedVideoAdOpened");

    }

    @Override
    public void onRewardedVideoStarted() {
        System.out.println("onRewardedVideoStarted");

    }

    @Override
    public void onRewardedVideoAdClosed() {
        System.out.println("onRewardedVideoAdClosed");
        if (revive) {
            Intent i = new Intent(EndGame.this, Game.class);
            i.putExtra("revive", revive);
            i.putExtra("score", score);
            startActivity(i);
        }
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        System.out.println("onRewarded");
        revive = true;

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        System.out.println("onRewardedVideoAdLeftApplication");

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        System.out.println("onRewardedVideoAdFailedToLoad");

    }

    @Override
    public void onRewardedVideoCompleted() {
        System.out.println("onRewardedVideoCompleted");


    }

    @Override
    public void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        revive = false;
        tr1.interrupt();
        super.onDestroy();
    }

    public int getScore() {
        return score;
    }

    public boolean isRevive() {
        return revive;
    }


    public void copyReviveFromPrevActivity() {
        Intent reviveIntent = getIntent(); // gets the previously created intent
        revive = reviveIntent.getBooleanExtra("revive", false);
    }

    public void copyScoreFromPrevActivity() {
        Intent reviveIntent = getIntent(); // gets the previously created intent
        score = reviveIntent.getIntExtra("score", 0);
    }


    private void saveBestScore() throws IOException {
        String str = operator+",upto"+Integer.toString(difficulty)+",";

        FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);

        try {
            for (int i = 0; i<bestScoreList.size(); i++){
                String[] parts = bestScoreList.get(i).split(",");
                String textOperator = parts[0];
                String textDiff = parts[1];
                String textBestScore = parts[2];
                if (textOperator.equals(operator) && textDiff.equals("upto"+ difficulty)){
                    fos.write((str+ Integer.toString(score)).getBytes());
                } else {
                    fos.write((bestScoreList.get(i)).getBytes());
                }
                fos.write(10);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {

                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private void getBestScore() {
        FileInputStream fis = null;
        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            linesCounter = 0;
            while ((text = br.readLine()) != null) {
                bestScoreList.add(text);
                String[] parts = text.split(",");
                String textOperator = parts[0];
                String textDiff = parts[1];
                String textBestScore = parts[2];
                if (textOperator.equals(operator) && textDiff.equals("upto"+ difficulty)){
                    bestScore = Integer.parseInt(textBestScore.toString());
                } else{
                    linesCounter++;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {

                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void settingsBtnEndGame(View view) {
        endGameMusic.pause();
        Intent intent = new Intent(EndGame.this, Settings.class);
        intent.putExtra("isFromEnd", true);
        intent.putExtra("score", score);
        intent.putExtra("revive", revive);
        intent.putExtra("mute", mute);
        startActivity(intent);
    }

    public void setMute() {
        Intent intent = getIntent();
        mute = intent.getBooleanExtra("mute", false);
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void muteOnClick(View view) {
        if (mute) {
            muteBtn.setBackgroundResource(R.drawable.unmute_btn);
            endGameMusic.start();
            mute = false;
        } else {
            muteBtn.setBackgroundResource(R.drawable.mute_btn_2);
            endGameMusic.pause();
            mute = true;
        }
    }


    public void setMuteBackground() {
        if (mute) {
            muteBtn.setBackgroundResource(R.drawable.mute_btn_2);
        } else {
            muteBtn.setBackgroundResource(R.drawable.unmute_btn);
        }
    }

    public void homeBtnOnClick(View view) {
        endGameMusic.stop();
        Intent i = new Intent(EndGame.this, MainActivity.class);
        i.putExtra("mute", mute);
        startActivity(i);
    }


 /*   private void init() {
        manager = ReviewManagerFactory.create(this);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
            @Override
            public void onComplete(@NonNull Task<ReviewInfo> task) {
                if (task.isSuccessful()) {
                    // We can get the ReviewInfo object
                    reviewInfo = task.getResult();
                } else {
                    // There was some problem, continue regardless of the result.
                }
            }
        });


    }*/

    /*public void btnOnclick(View view) {
        Task<Void> flow = manager.launchReviewFlow(EndGame.this, reviewInfo);
        flow.addOnCompleteListener(task -> {
            // The flow has finished. The API does not indicate whether the user
            // reviewed or not, or even whether the review dialog was shown. Thus, no
            // matter the result, we continue our app flow.
        });
    }*/

    public void homeBtnOnClickEndGame(View view) {
        endGameMusic.stop();
        Intent i = new Intent(EndGame.this, MainActivity.class);
        i.putExtra("mute", mute);
        startActivity(i);
    }

    public void btnReviveOnClick(View view) {
        if (!revive) {
            btnRevive.setVisibility(View.INVISIBLE);
            loadRewardedVideoAd();
            mRewardedVideoAd.show();
            revive = true;
        } else {
        }
    }

    private void startSummery(){
        TextView yourFinalScoreTv = (TextView) findViewById(R.id.textView3);
        TextView countingScoreTv = (TextView) findViewById(R.id.textView);
        TextView bestScoreCountingTv = (TextView) findViewById(R.id.textView5);
        ImageView cupIm = (ImageView) findViewById(R.id.imageView);
        // set Visibilty
        //btnShare.setVisibility(View.INVISIBLE);
        btnShare.setVisibility(View.INVISIBLE);
        btnShare.postDelayed(new Runnable() {
            public void run() {
                btnShare.setVisibility(View.VISIBLE);
            }
        }, 4000);
        muteBtn.setVisibility(View.INVISIBLE);
        muteBtn.postDelayed(new Runnable() {
            public void run() {
                muteBtn.setVisibility(View.VISIBLE);
            }
        }, 4000);
        rateStarBtn.setVisibility(View.INVISIBLE);
        rateStarBtn.postDelayed(new Runnable() {
            public void run() {
                rateStarBtn.setVisibility(View.VISIBLE);
            }
        }, 4000);

        btnRevive.setVisibility(View.INVISIBLE);
        btnRevive.postDelayed(new Runnable() {
            public void run() {
                btnRevive.setVisibility(View.VISIBLE);
            }
        }, 4000);
        menuBtn.setVisibility(View.INVISIBLE);
        menuBtn.postDelayed(new Runnable() {
            public void run() {
                menuBtn.setVisibility(View.VISIBLE);
            }
        }, 4000);
        tvFinalScore.setVisibility(View.INVISIBLE);
        tvFinalScore.postDelayed(new Runnable() {
            public void run() {
                tvFinalScore.setVisibility(View.VISIBLE);
            }
        }, 4000);
        tvBestScore.setVisibility(View.INVISIBLE);
        tvBestScore.postDelayed(new Runnable() {
            public void run() {
                tvBestScore.setVisibility(View.VISIBLE);
            }
        }, 4000);
        gameOverImg.setVisibility(View.INVISIBLE);
        gameOverImg.postDelayed(new Runnable() {
            public void run() {
                gameOverImg.setVisibility(View.VISIBLE);
            }
        }, 4000);
        finalScoreTV.setVisibility(View.INVISIBLE);
        finalScoreTV.postDelayed(new Runnable() {
            public void run() {
                finalScoreTV.setVisibility(View.VISIBLE);
            }
        }, 4000);
        bestScoreTv.setVisibility(View.INVISIBLE);
        bestScoreTv.postDelayed(new Runnable() {
            public void run() {
                bestScoreTv.setVisibility(View.VISIBLE);
            }
        }, 4000);
        startNewGameBtn.setVisibility(View.INVISIBLE);
        startNewGameBtn.postDelayed(new Runnable() {
            public void run() {
                startNewGameBtn.setVisibility(View.VISIBLE);
            }
        }, 4000);
        settingsBtn.setVisibility(View.INVISIBLE);
        settingsBtn.postDelayed(new Runnable() {
            public void run() {
                settingsBtn.setVisibility(View.VISIBLE);
            }
        }, 4000);
        bestScoreCountingTv.setVisibility(View.INVISIBLE);
        cupIm.setVisibility(View.INVISIBLE);

        yourFinalScoreTv.setVisibility(View.VISIBLE);
        yourFinalScoreTv.postDelayed(new Runnable() {
            public void run() {
                yourFinalScoreTv.setVisibility(View.INVISIBLE);
            }
        }, 4000);
        countingScoreTv.setVisibility(View.VISIBLE);
        countingScoreTv.postDelayed(new Runnable() {
            public void run() {
                countingScoreTv.setVisibility(View.INVISIBLE);
            }
        }, 4000);
        countingScoreTv.postDelayed(new Runnable() {
            public void run() {
                InterstitialAd mInterstitialAd = new InterstitialAd(EndGame.this);
                mInterstitialAd.setAdUnitId("ca-app-pub-7775472521601802/2796958344");
                AdRequest adRequestInter = new AdRequest.Builder().build();
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        mInterstitialAd.show();
                    }
                    @Override
                    public void onAdClosed()
                    {
                        endGameMusic.start();
                    }
                });
                mInterstitialAd.loadAd(adRequestInter);
            }
        }, 4000);


        //start animation and music
        startCountAnimation();

        // if best score
        if (score > bestScore){
            //SystemClock.sleep(2000);
            bestScoreCountingTv.setVisibility(View.VISIBLE);
            bestScoreCountingTv.postDelayed(new Runnable() {
                public void run() {
                    bestScoreCountingTv.setVisibility(View.INVISIBLE);
                }
            }, 4000);
            cupIm.setVisibility(View.VISIBLE);
            cupIm.postDelayed(new Runnable() {
                public void run() {
                    cupIm.setVisibility(View.INVISIBLE);
                }
            }, 4000);
            countingSound = MediaPlayer.create(EndGame.this, R.raw.level_up_sound);
            if (!mute) {
                countingSound.start();
            } else {
                muteBtn.setBackgroundResource(R.drawable.mute_btn_2);
            }

        }




    }

    private void startCountAnimation() {
        countingSound = MediaPlayer.create(EndGame.this, R.raw.counting_sound);
        if (!mute) {
            countingSound.start();
            //countingSound.setLooping(true);
        } else {
            muteBtn.setBackgroundResource(R.drawable.mute_btn_2);
        }

        TextView textView = (TextView) findViewById(R.id.textView);
        ValueAnimator animator = ValueAnimator.ofInt(0, score); //0 is min number, 600 is max number
        animator.setDuration(1000); //Duration is in milliseconds
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                textView.setText(animation.getAnimatedValue().toString());
            }
        });
        animator.start();
        //SystemClock.sleep(1000);
        //countingSound.stop();



    }
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        isVisible = hasFocus;
        if (!isVisible) {
            countingSound.pause();
        } else {
            //mainActivityBackgroud.start();
        }


    }

    private String getChosenOperator(){
        Intent i = getIntent();
        return i.getStringExtra("chosenOperator");
    }

    private void modifyScoreToFireBase() {
        FirebaseAnalytics mFirebaseAnalytics;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putLong(FirebaseAnalytics.Param.SCORE, score);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.POST_SCORE, bundle);
    }

}




