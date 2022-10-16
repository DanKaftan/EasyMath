package com.dan.kaftan.game;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LevelsMenu extends AppCompatActivity {



    Boolean mute = false;
    Boolean isLevel = false;
    GridView gridView;

    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    Button btn6;
    Button btn7;
    Button btn8;
    Button btn9;
    Button btn10;
    Button btn11;
    Button btn12;
    Button btn13;
    Button btn14;
    Button btn15;
    Button btn16;
    Button btn17;
    Button btn18;
    Button btn19;
    Button btn20;
    Button btn21;
    List<Button> buttonList = new ArrayList<Button>();


    int chosenLevelNum = 1;
    int currentLevel = 1;

    MediaPlayer levelBackgroundSound;
    boolean isVisible = true;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // rotate screen
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels_menu);

        getSupportActionBar().hide();
        //hide navigation bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Intent intent = getIntent();
        mute = intent.getBooleanExtra("mute", false);
        isLevel = intent.getBooleanExtra("isLevel", true);
        //gridView = (GridView) findViewById(R.id.gridview);

        createButtonsList();
        getCurrentLevel();
        changeBackgroundOfButtons();
        startMusic();

    }


    private void createButtonsList(){
        btn1 = (Button) findViewById(R.id.level1btn);
        btn2 = (Button) findViewById(R.id.level2btn);
        btn3 = (Button) findViewById(R.id.level3btn);
        btn4 = (Button) findViewById(R.id.level4btn);
        btn5 = (Button) findViewById(R.id.level5btn);
        btn6 = (Button) findViewById(R.id.level6btn);
        btn7 = (Button) findViewById(R.id.level7btn);
        btn8 = (Button) findViewById(R.id.level8btn);
        btn9 = (Button) findViewById(R.id.level9btn);
        btn10 = (Button) findViewById(R.id.level10btn);
        btn11 = (Button) findViewById(R.id.level11btn);
        btn12 = (Button) findViewById(R.id.level12btn);
        btn13 = (Button) findViewById(R.id.level13btn);
        btn14 = (Button) findViewById(R.id.level14btn);
        btn15 = (Button) findViewById(R.id.level15btn);
        btn16 = (Button) findViewById(R.id.level16btn);
        btn17 = (Button) findViewById(R.id.level17btn);
        btn18 = (Button) findViewById(R.id.level18btn);
        btn19 = (Button) findViewById(R.id.level19btn);
        btn20 = (Button) findViewById(R.id.level20btn);
        btn21 = (Button) findViewById(R.id.level21btn);
        buttonList.add(btn1);
        buttonList.add(btn2);
        buttonList.add(btn3);
        buttonList.add(btn4);
        buttonList.add(btn5);
        buttonList.add(btn6);
        buttonList.add(btn7);
        buttonList.add(btn8);
        buttonList.add(btn9);
        buttonList.add(btn10);
        buttonList.add(btn11);
        buttonList.add(btn12);
        buttonList.add(btn13);
        buttonList.add(btn14);
        buttonList.add(btn15);
        buttonList.add(btn16);
        buttonList.add(btn17);
        buttonList.add(btn18);
        buttonList.add(btn19);
        buttonList.add(btn20);
        buttonList.add(btn21);






    }

    public void onClick(View v) {

        try {
            if (v.getId() == R.id.level1btn) {
                chosenLevelNum = 1;
            }
            else if (v.getId() == R.id.level2btn) {
                chosenLevelNum = 2;

            }
            else if (v.getId() == R.id.level3btn) {
                chosenLevelNum = 3;

            }
            else if (v.getId() == R.id.level4btn) {
                chosenLevelNum = 4;

            }
            else if (v.getId() == R.id.level5btn) {
                chosenLevelNum = 5;

            }
            else if (v.getId() == R.id.level6btn) {
                chosenLevelNum = 6;

            }
            else if (v.getId() == R.id.level7btn) {
                chosenLevelNum = 7;

            }
            else if (v.getId() == R.id.level8btn) {
                chosenLevelNum = 8;

            }
            else if (v.getId() == R.id.level9btn) {
                chosenLevelNum = 9;

            }
            else if (v.getId() == R.id.level10btn) {
                chosenLevelNum = 10;

            }
            else if (v.getId() == R.id.level11btn) {
                chosenLevelNum = 11;

            }
            else if (v.getId() == R.id.level12btn) {
                chosenLevelNum = 12;

            }
            else if (v.getId() == R.id.level13btn) {
                chosenLevelNum = 13;

            }
            else if (v.getId() == R.id.level14btn) {
                chosenLevelNum = 14;

            }
            else if (v.getId() == R.id.level15btn) {
                chosenLevelNum = 15;

            }
            else if (v.getId() == R.id.level16btn) {
                chosenLevelNum = 16;

            }
            else if (v.getId() == R.id.level17btn) {
                chosenLevelNum = 17;

            }
            else if (v.getId() == R.id.level18btn) {
                chosenLevelNum = 18;

            }
            else if (v.getId() == R.id.level19btn) {
                chosenLevelNum = 19;

            }
            else if (v.getId() == R.id.level20btn) {
                chosenLevelNum = 20;

            }
            else if (v.getId() == R.id.level21btn) {
                chosenLevelNum = 21;

            }




        } catch (Exception ignored) {


        }

        Intent i = new Intent(LevelsMenu.this, Game.class);
        i.putExtra("mute", mute);
        i.putExtra("isLevel", isLevel);
        i.putExtra("chosenLevelNum", chosenLevelNum);
        if(chosenLevelNum <= currentLevel){
            startActivity(i);
        }

    }


    private void getCurrentLevel() {
        FileInputStream fis = null;
        try {
            fis = openFileInput("level");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {

                sb.append(text);
                currentLevel = Integer.parseInt(sb.toString());


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

    private void changeBackgroundOfButtons(){
        // change background of buttons
        for (int i =1; i<=21; i++){
            if(i<currentLevel){
                buttonList.get(i-1).setBackgroundResource(R.drawable.done_level_btn);
            }
            if(i==currentLevel){
                buttonList.get(i-1).setBackgroundResource(R.drawable.current_level_btn);

            }
            if(i>currentLevel){
                buttonList.get(i-1).setBackgroundResource(R.drawable.lock_level_btn);

            }
        }

    }

    private void startMusic(){
        levelBackgroundSound = MediaPlayer.create(LevelsMenu.this, R.raw.levels_menu_background_sound);

        if (!mute) {
            levelBackgroundSound.start();
            levelBackgroundSound.setLooping(true);
        } else {
            //muteBtn.setBackgroundResource(R.drawable.mute_btn_2);
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        isVisible = hasFocus;
        if (!isVisible) {
            levelBackgroundSound.pause();
        } else {
            //mainActivityBackgroud.start();
        }


    }


    public void levelmuteOnClick(View view) {

        if (!mute) {
            mute = true;
            levelBackgroundSound.pause();
        } else {
            mute = false;
            levelBackgroundSound.start();


        }

    }

    public void homeBtnOnClick(View view) {

        Intent i = new Intent(LevelsMenu.this, MainActivity.class);
        levelBackgroundSound.stop();
        startActivity(i);
    }




}