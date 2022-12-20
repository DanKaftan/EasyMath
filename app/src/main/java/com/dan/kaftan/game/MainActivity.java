package com.dan.kaftan.game;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.play.core.review.ReviewManagerFactory;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {


    Button settingsBtn;
    Button muteBtn;
    int i = 0;
    private static final String TAG = "MainActivity";
    MediaPlayer mainActivityBackgroud;
    MediaPlayer click;
    boolean isVisible = true;
    boolean mute = false;
    boolean isLevel = false;
    boolean isFirstVisit = true;
    ImageView menuIv;

    GoogleSignInClient mGoogleSignInClient;
    // Request code used to invoke sign in user interactions.




    @Override
    protected void onCreate(Bundle savedInstanceState) {


        // full screen
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        //hide navigation bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);


        // rotate screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        MobileAds.initialize(this, "ca-app-pub-7775472521601802~2555137966");





        settingsBtn = (Button) findViewById(R.id.settings_btn_main);
        muteBtn = (Button) findViewById(R.id.mute_btn);

        Intent muteIntent = getIntent(); // gets the previously created intent
        mute = muteIntent.getBooleanExtra("mute", false);


        mainActivityBackgroud = MediaPlayer.create(MainActivity.this, R.raw.main_activity_music);
        click = MediaPlayer.create(MainActivity.this, R.raw.click_main);

        if (!mute) {
            mainActivityBackgroud.start();
            mainActivityBackgroud.setLooping(true);
        } else {
            muteBtn.setBackgroundResource(R.drawable.mute_btn_2);
        }



        makeLevelFile();

        if (welcome_screen.ismulty) {

            // menuIv.setImageResource(R.drawable.multiplication_icon);
        }

        checkIsFirstVisitFile();
        if (!isFirstVisit) {
        } else {
            saveIsFirstVisitFile();
        }

        InAppReview inAppReview = new InAppReview(this);
        inAppReview.startReview();

    }

    public void startGameClick(View view) {
        mainActivityBackgroud.pause();
        if (!mute) {
            click.start();
        }
        Intent i = new Intent(MainActivity.this, PracticeMenu.class);
        i.putExtra("mute", mute);
        startActivity(i);

    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        isVisible = hasFocus;
        if (!isVisible) {
            mainActivityBackgroud.pause();
        } else {
            //mainActivityBackgroud.start();
        }


    }

    public void settingsMainClick(View view) {
        mainActivityBackgroud.pause();
        Intent intent = new Intent(MainActivity.this, Settings.class);
        intent.putExtra("mute", mute);
        startActivity(intent);

    }


    public void muteOnClick(View view) {

        if (!mute) {
            mute = true;
            muteBtn.setBackgroundResource(R.drawable.mute_btn_2);
            mainActivityBackgroud.pause();
        } else {
            mute = false;
            muteBtn.setBackgroundResource(R.drawable.unmute_btn);
            mainActivityBackgroud.start();


        }

    }

    public void levelOnClick(View view) {

        isLevel = true;
        if (!mute) {
            click.start();
        }
        mainActivityBackgroud.stop();
        Intent i = new Intent(MainActivity.this, PracticeMenu.class);
        i.putExtra("mute", mute);
        i.putExtra("isLevel", isLevel);
        i.putExtra("isFirstVisit", isFirstVisit);
        startActivity(i);
    }


    private void makeLevelFile() {

        File file = new File(getApplicationContext().getFilesDir(), "level");
        if (!file.exists()) {


            FileOutputStream fos = null;


            try {
                fos = openFileOutput("level", MODE_PRIVATE);
            } catch (FileNotFoundException e) {
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
    }



    private void saveIsFirstVisitFile() {
        FileOutputStream fos = null;


        try {
            fos = openFileOutput("isFirstVisitFile", MODE_PRIVATE);
            fos.write("clicked".getBytes());
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

    private void checkIsFirstVisitFile() {
        FileInputStream fis = null;
        try {
            fis = openFileInput("isFirstVisitFile");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {

                sb.append(text);
                String s = sb.toString();
                isFirstVisit = !s.equals("clicked");
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
            } else {
            }
        }
    }

    //how many times the user entered the app
    private void makeCounterFile() {

        File file = new File(getApplicationContext().getFilesDir(), "counter");
        if (!file.exists()) {


            FileOutputStream fos = null;


            try {
                fos = openFileOutput("counter", MODE_PRIVATE);
            } catch (FileNotFoundException e) {
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
    }


    public void settingsBtnMain(View view) {
        if (isFirstVisit) {
            saveIsFirstVisitFile();
        }
    }



}


