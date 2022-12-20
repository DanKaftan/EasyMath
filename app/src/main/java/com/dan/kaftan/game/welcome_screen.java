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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class welcome_screen extends AppCompatActivity {

    ImageView iv;
    TextView tv2;
    MediaPlayer startGameMusic;
    static boolean ismulty = false;
    boolean isFirstVisit = true;
    private static final String FILE_NAME = "best_score.txt";
    private static final String FIRST_VISIT_FILE_NAME = "isFirstVisitFile2.txt";






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //hide navigation bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        checkIsFirstVisitFile();
        if (isFirstVisit){
            createLevelsFile();
            saveIsFirstVisitFile();
            saveBestScore();

        }




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

    private void saveIsFirstVisitFile() {
        FileOutputStream fos = null;


        try {
            fos = openFileOutput(FIRST_VISIT_FILE_NAME, MODE_PRIVATE);
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
            fis = openFileInput(FIRST_VISIT_FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {

                sb.append(text);
                String s = sb.toString();
                isFirstVisit = !s.equals("clicked");
            }
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

    public void swichActivity() {
        Intent i;
        if (isFirstVisit) {
            i = new Intent(welcome_screen.this, FirstOpenActivity.class);

        } else {
            i = new Intent(welcome_screen.this, MainActivity.class);
            saveIsFirstVisitFile();
        }

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

    private void saveBestScore() {

        FileOutputStream fos = null;

        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write("+,upto5,0".getBytes());
            fos.write(10);
            fos.write("+,upto10,0".getBytes());
            fos.write(10);
            fos.write("+,upto20,0".getBytes());
            fos.write(10);
            fos.write("+,upto50,0".getBytes());
            fos.write(10);
            fos.write("+,upto100,0".getBytes());
            fos.write(10);
            fos.write("-,upto5,0".getBytes());
            fos.write(10);
            fos.write("-,upto10,0".getBytes());
            fos.write(10);
            fos.write("-,upto20,0".getBytes());
            fos.write(10);
            fos.write("-,upto50,0".getBytes());
            fos.write(10);
            fos.write("-,upto100,0".getBytes());
            fos.write(10);
            fos.write("*,upto5,0".getBytes());
            fos.write(10);
            fos.write("*,upto10,0".getBytes());
            fos.write(10);
            fos.write("*,upto20,0".getBytes());
            fos.write(10);
            fos.write("*,upto50,0".getBytes());
            fos.write(10);
            fos.write("*,upto100,0".getBytes());
            fos.write(10);
            fos.write("/,upto5,0".getBytes());
            fos.write(10);
            fos.write("/,upto10,0".getBytes());
            fos.write(10);
            fos.write("/,upto20,0".getBytes());
            fos.write(10);
            fos.write("/,upto50,0".getBytes());
            fos.write(10);
            fos.write("/,upto100,0".getBytes());

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

    private void createLevelsFile() {

        FileOutputStream fos = null;

        try {
            fos = openFileOutput("level", MODE_PRIVATE);
            fos.write("+,1".getBytes());
            fos.write(10);
            fos.write("-,1".getBytes());
            fos.write(10);
            fos.write("*,1".getBytes());
            fos.write(10);
            fos.write("/,1".getBytes());


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


}
