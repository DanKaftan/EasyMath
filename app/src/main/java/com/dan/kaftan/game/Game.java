package com.dan.kaftan.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.dan.kaftan.game.targil.BankOfTargils;
import com.dan.kaftan.game.targil.Targil;
import com.dan.kaftan.game.targil.TargilAdd;
import com.dan.kaftan.game.targil.TargilDiv;
import com.dan.kaftan.game.targil.TargilMultiply;
import com.dan.kaftan.game.targil.TargilSub;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.reward.RewardedVideoAd;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class Game extends AppCompatActivity {


    private AdView mAdView;

    List<Integer> answers = new ArrayList<>();

    int fakeAnswer1 = 0;
    int fakeAnswer2 = 0;
    int fakeAnswer3 = 0;
    int trueAnswer = 0;
    int invalidationCounter = 0;
    int score = 0;
    int timerSeconds = 15;
    int maxAnswer = welcome_screen.ismulty ? 100 : 10;
    int maxResult = Integer.MIN_VALUE;
    int minResult = Integer.MAX_VALUE;
    int levelNum = 1;
    int currentExNum = 0;
    int firstNum = 0;
    int secondNum = 0;
    int entersCounter = 0;
    int maxLevel = 1;

    String timerSecondsOnString = "10";
    String operatorSymbol = "+";

    Button homeBtn;
    Button muteBtn;



    TextView tv;
    TextView tva1;
    TextView tva2;
    TextView tva3;
    TextView tva4;
    TextView timer;
    TextView tvScore;
    TextView levelTv;
    TextView trueAnsTv;

    ImageView iv;
    ImageView hiv1;
    ImageView hiv2;
    ImageView hiv3;

    Random rand = new Random();

    private CountDownTimer mcountDownTimer;
    private CountDownTimer viewResultTimer;

    boolean answerCheck = false;
    boolean revive = false;
    boolean mute;
    boolean isLevel;
    boolean isFirstVisit;
    boolean isRightAnswer = false;
    // for disabling sound
    boolean isVisible = true;
    boolean gameOver = false;
    boolean levelPassed;

    boolean isClicked = false;


    // sounds
    MediaPlayer correctSound;
    MediaPlayer falseSound;
    MediaPlayer threeSecondsSound;
    MediaPlayer gameSound;
    //MediaPlayer lestMinSound;

    private static final String TAG = "MainActivity";



    // this holds the targilim we want to run
    BankOfTargils bankOfTargils = new BankOfTargils();
    private RewardedVideoAd mRewardedVideoAd;

    static InterstitialAd mInterstitialAd;

    int[] timeArray = new int[48];
    int[] exNumArray = new int[48];
    int[] maxResultArray = new int[48];

    List<ImageView> circle_list = new ArrayList<ImageView>();

    GridLayout gridLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // full screen
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().hide();
        //hide navigation bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        // rotate screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setViews();
        levelMode();
        setEntersCounter();
        Intent i = getIntent();
        mute = i.getBooleanExtra("mute", false);
        muteBtn = (Button) findViewById(R.id.game_mute_btn);
        if (mute) {
            muteBtn.setBackgroundResource(R.drawable.mute_btn_2);
        } else {
            muteBtn.setBackgroundResource(R.drawable.unmute_btn);

        }
        entersCounter++;

        copyReviveFromPrevActivity();
        copyScoreFromPrevActivity(revive);
        if (!isLevel) {
            tvScore.setText(getString(R.string.score) + ": " + Integer.toString(score));
            levelTv.setVisibility(View.INVISIBLE);
            gridLayout.setVisibility(View.INVISIBLE);
        } else {
            tvScore.setText(currentExNum + "/" + exNumArray[levelNum - 1]);
            //levelTv.setText("level: " + levelNum);
            levelTv.setVisibility(View.VISIBLE);
            gridLayout.setVisibility(View.VISIBLE);
        }


        getDifficulty();
        getTimerSeconds();

        String chosenOperator = i.getStringExtra("chosenOperator");
        //מה יהיה סוג התרגילים
        if (chosenOperator.equals("*")) {
            operatorSymbol = "x";
            switch (maxAnswer) {
                case 5:
                    initTargilim(5, 5, maxAnswer, true, "x");
                    break;
                case 10:
                    initTargilim(5, 5, maxAnswer, true, "x");
                    break;
                case 20:
                case 50:
                case 100:
                    initTargilim(10, 10, maxAnswer, true, "x");
                    break;
                case 1000:
                    initTargilim((int) Math.sqrt(maxAnswer), (int) Math.sqrt(maxAnswer), maxAnswer, true, "x");
                    break;
            }
        } else if (chosenOperator.equals("+")){
            operatorSymbol = "+";
            switch (maxAnswer) {
                case 5:
                    initTargilim(maxAnswer - 1, maxAnswer - 1, maxAnswer, true, "+");
                    break;
                case 10:
                    initTargilim(maxAnswer - 1, maxAnswer - 1, maxAnswer, true, "+");
                    break;
                case 20:
                    initTargilim(10, 10, maxAnswer, true, "+");
                    break;
                case 50:
                    initTargilim(25, 25, maxAnswer, true, "+");
                    break;
                case 100:
                    initTargilim(50, 50, maxAnswer, true, "+");
                    break;
                case 1000:
                    initTargilim(500, 500, maxAnswer, true, "+");
                    break;
            }

        }

        else if (chosenOperator.equals("-")){
            operatorSymbol = "-";
            switch (maxAnswer) {
                case 5:
                    initTargilim(5, 5, maxAnswer, true, "-");
                    break;
                case 10:
                    initTargilim(maxAnswer - 1, maxAnswer - 1, maxAnswer, true, "-");
                    break;
                case 20:
                    initTargilim(10, 10, maxAnswer, true, "-");
                    break;
                case 50:
                    initTargilim(25, 25, maxAnswer, true, "-");
                    break;
                case 100:
                    initTargilim(50, 50, maxAnswer, true, "-");
                    break;
                case 1000:
                    initTargilim(500, 500, maxAnswer, true, "-");
                    break;
            }

        }

        else if (chosenOperator.equals("/")){
            operatorSymbol = "/";
            switch (maxAnswer) {
                case 5:
                    initTargilim(maxAnswer - 1, maxAnswer - 1, maxAnswer, true, "/");
                    break;
                case 10:
                    initTargilim(maxAnswer - 1, maxAnswer - 1, maxAnswer, true, "/");
                    break;
                case 20:
                    initTargilim(10, 10, maxAnswer, true, "/");
                    break;
                case 50:
                    initTargilim(25, 25, maxAnswer, true, "/");
                    break;
                case 100:
                    initTargilim(50, 50, maxAnswer, true, "/");
                    break;
                case 1000:
                    initTargilim(500, 500, maxAnswer, true, "/");
                    break;
            }

        }


        setSound();

        setMute();

        setGame();



    }


    //  Game setter

    public void setGame() {

        trueAnsTv.setVisibility(View.INVISIBLE);

        initGameView();

        chooseTargil();

        chooseFakeAnswers();

        chooseLocationForAnswers();

        setTimerForAnswer();
        if (isLevel){
            update_level_bar();

        }
    }


    // choose the location of the true answer
    private void chooseLocationForAnswers() {

        List<TextView> tvaList = Arrays.asList(tva1, tva2, tva3, tva4);
        Collections.shuffle(tvaList);
        for (TextView tva : tvaList) {
            tva.setText(Integer.toString(answers.remove(0)));
        }
    }


    // Get fake answers
    private void chooseFakeAnswers() {

        int minFakeResult;
        int maxFakeResult;

        if (trueAnswer - 10 < minResult) {
            minFakeResult = minResult;
        } else {
            minFakeResult = trueAnswer - 10;
        }

        if (trueAnswer + 10 > maxResult) {
            maxFakeResult = maxResult;
        } else {
            maxFakeResult = trueAnswer + 10;
        }

        int fakeRange = maxFakeResult - minFakeResult + 1;

        fakeAnswer1 = rand.nextInt(fakeRange) + minFakeResult;
        while (fakeAnswer1 == trueAnswer) {
            fakeAnswer1 = rand.nextInt(fakeRange) + minFakeResult;
        }
        answers.add(fakeAnswer1);


        fakeAnswer2 = rand.nextInt(fakeRange) + minFakeResult;
        while (fakeAnswer2 == trueAnswer || fakeAnswer2 == fakeAnswer1) {
            fakeAnswer2 = rand.nextInt(fakeRange) + minFakeResult;
        }
        answers.add(fakeAnswer2);


        fakeAnswer3 = rand.nextInt(fakeRange) + minFakeResult;
        while (fakeAnswer3 == trueAnswer || fakeAnswer3 == fakeAnswer1 || fakeAnswer3 == fakeAnswer2) {
            fakeAnswer3 = rand.nextInt(fakeRange) + minFakeResult;
        }
        answers.add(fakeAnswer3);
    }


    // Get the question and calc the true answer
    @NonNull
    private void chooseTargil() {

        // take random targil
        Targil targil = bankOfTargils.removeRandomTargil();

        // to display it
        tv.setText(targil.getTargilAsString());
        firstNum = targil.getFirstNum();
        secondNum = targil.getSecondNum();

        // calc the targil result
        trueAnswer = targil.calc();

        answers.add(trueAnswer);
    }


    // Change the design to the true/false answer view
    private void initGameView() {

        iv.setImageResource(R.drawable.a);
        tva1.setVisibility(View.VISIBLE);
        tva2.setVisibility(View.VISIBLE);
        tva3.setVisibility(View.VISIBLE);
        tva4.setVisibility(View.VISIBLE);
        tv.setVisibility(View.VISIBLE);
        tvScore.setVisibility(View.VISIBLE);
        timer.setVisibility(View.VISIBLE);
        gridLayout.setVisibility(View.VISIBLE);
    }


    // set all the questions in a list
    private void initTargilim(int maxFirstNum, int maxSecondNum, int maxExpectedResult, boolean resultLimit, String operator) {

        // take targilim form bank of targilim
        List<Targil> targilim = bankOfTargils.getTarglilim();

        // fill it with new targilim
        for (int firstNum = 1; firstNum < maxFirstNum; firstNum++) {
            for (int secondNum = 1; (secondNum < maxSecondNum) && (resultLimit && operator.equals("+") ? firstNum + secondNum <= maxExpectedResult : resultLimit && operator.equals("x") ? firstNum * secondNum <= maxExpectedResult : secondNum < maxSecondNum); secondNum++) {
                // generate the targil
                Targil targil = newTargil(firstNum, secondNum, operator);
                // add it to bank
                targilim.add(targil);
                // calc it
                int targilResult = targil.calc();
                // update max result if needed
                if (targilResult > maxResult) {
                    maxResult = targilResult;
                }
                // update min result if needed
                if (targilResult < minResult) {
                    minResult = targilResult;
                }
            }
        }
    }

    private Targil newTargil(int firstNum, int secondNum, String operator) {
        Targil targil;
        switch (operator) {
            case "+":
                targil = new TargilAdd(firstNum, secondNum, operator);
                break;
            case "x":
                targil = new TargilMultiply(firstNum, secondNum, operator);
                break;
            case "-":
                if(firstNum < secondNum){
                    int flag = firstNum;
                    firstNum = secondNum;
                    secondNum = flag;
                }
                targil = new TargilSub(firstNum, secondNum, operator);
                break;
            case "/":
                if(firstNum % secondNum !=0){
                    if(firstNum < secondNum){
                        int flag = firstNum;
                        firstNum = secondNum;
                        secondNum = flag;
                    }
                    int sherit = firstNum % secondNum;
                    firstNum = firstNum - sherit;
                }
                targil = new TargilDiv(firstNum, secondNum, operator);
                break;

            default:
                throw new UnsupportedOperationException();
        }
        return targil;
    }


    // check the answer right in case of on click

    public void tva1OnClick(View v) throws InterruptedException {
        handleClick(v, tva1, false);
    }

    public void tva2OnClick(View v) throws InterruptedException {
        handleClick(v, tva2, false);
    }

    public void tva3OnClick(View v) throws InterruptedException {
        handleClick(v, tva3, false);
    }

    public void tva4OnClick(View v) throws InterruptedException {
        handleClick(v, tva4, false);
    }


    public void handleClick(View v, TextView tva, boolean timeOut) throws InterruptedException {
        try {
            // first, cancel timer as the user clicked
            if (mcountDownTimer != null) {
                stopTimer();
            }

            int tvaNum = 0;
            if (tva != null) {
                tvaNum = Integer.parseInt(tva.getText().toString());
            }
            tva1.setVisibility(View.INVISIBLE);
            tva2.setVisibility(View.INVISIBLE);
            tva3.setVisibility(View.INVISIBLE);
            tva4.setVisibility(View.INVISIBLE);
            gridLayout.setVisibility(View.INVISIBLE);
            tv.setVisibility(View.INVISIBLE);
            //tvScore.setVisibility(View.INVISIBLE);
            timer.setVisibility(View.INVISIBLE);
            threeSecondsSound.pause();

            if (!timeOut && tvaNum == trueAnswer) {
                isRightAnswer = true;
                iv.setImageResource(R.drawable.vi);
                currentExNum++;

                // do not disturb with sounds if not visible
                if (isVisible && !mute) {
                    correctSound.start();
                }
                //   correctSoundEffect(context);
                score = score + 10;
                answerCheck = true;

                if (isLevel) {

                }
                //result();
            } else {
                isRightAnswer = false;
                iv.setImageResource(R.drawable.x);
                if (operatorSymbol.equals("/")) {
                    trueAnsTv.setText( firstNum + " "+ ":" + " " + secondNum + " = " + trueAnswer);
                }else{
                    trueAnsTv.setText( firstNum + " "+ operatorSymbol + " " + secondNum + " = " + trueAnswer);
                }
                trueAnsTv.setVisibility(View.VISIBLE);

                // do not disturb with sounds if not visible
                if (isVisible && !mute) {
                    falseSound.start();
                }
                answerCheck = false;
                invalidationCounter = invalidationCounter + 1;
                //     result();

                if (invalidationCounter == 1) {
                    hiv3.setVisibility(View.INVISIBLE);
                }
                if (invalidationCounter == 2) {
                    hiv2.setVisibility(View.INVISIBLE);
                }
                if (invalidationCounter == 3) {
                    hiv1.setVisibility(View.INVISIBLE);
                }
            }
            if (!isLevel) {
                tvScore.setText(getString(R.string.score)+": " + Integer.toString(score));
            } else {
                tvScore.setText(currentExNum + "/" + exNumArray[levelNum - 1]);

            }
            if (invalidationCounter != 3) {
                setTimerForViewResult();


                if ((exNumArray[levelNum - 1] == currentExNum) && (isLevel)) {

                    //levelNum++;
                    if (levelNum == 47) {
                    }
                    levelPassed = true;
                    setTimerForGameOver();

                    if (invalidationCounter == 3) {
                        levelPassed = false;
                        setTimerForGameOver();
                    }
                }
            } else {
                setTimerForGameOver();

            }

        } catch (Exception e) {

        }


        isClicked = false;
    }

    //מגיעים לפונקציה שהמשחק נגמר

    public void gameOver() throws IOException {


        System.out.println("levelNum" + levelNum);
        saveEntersCounter();


        if (isVisible) {

            Intent a;
            if (!isLevel) {
                a = new Intent(Game.this, EndGame.class);
            } else {
                saveLevel();
                a = new Intent(Game.this, EndLevel.class);

            }

            a.putExtra("score", score);
            a.putExtra("isLevel", isLevel);
            a.putExtra("revive", revive);
            a.putExtra("mute", mute);
            a.putExtra("levelNum", levelNum);
            a.putExtra("levelPassed", levelPassed);
            a.putExtra("isFromSettings", false);
            a.putExtra("difficulty", maxAnswer);
            a.putExtra("operator", operatorSymbol);
            a.putExtra("chosenOperator", getChosenOperator());
            startActivity(a);
        } else {
            gameOver = true;
        }
    }


    public void setTimerForAnswer() {
        mcountDownTimer = new CountDownTimer((timerSeconds + 1) * 1000, 1000) { //40000 milli seconds is total time, 1000 milli seconds is time interval
            int timerNum = timerSeconds;

            public void onTick(long millisUntilFinished) {
                // if the time is limited show it
                if (timerSeconds != 10000) {
                    timer.setText(Integer.toString(timerNum));
                    timer.setVisibility(View.VISIBLE);
                } else {
                    timer.setVisibility(View.INVISIBLE);
                }

                timerNum = timerNum - 1;

                if (!mute) {
                    //gameSound.start();
                }
                if (!isVisible) {
                    gameSound.pause();
                }


            }

            public void onFinish() {
                try {
                    handleClick(null, null, true);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }.start();
    }

    private void stopTimer() {
        mcountDownTimer.cancel();
    }

    private void startTimer() {
        int timerNum = timerSeconds;
        mcountDownTimer.start();
    }

    public void setTimerForViewResult() {
        if (isRightAnswer) {
            viewResultTimer = new CountDownTimer(2000, 1000) { //40000 milli seconds is total time, 1000 milli seconds is time interval
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    setGame();
                }
            }.start();
        } else {
            viewResultTimer = new CountDownTimer(3000, 1000) { //40000 milli seconds is total time, 1000 milli seconds is time interval
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    setGame();
                }
            }.start();
        }

    }

    public void setTimerForGameOver() {
        viewResultTimer = new CountDownTimer(2000, 1000) { //40000 milli seconds is total time, 1000 milli seconds is time interval
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                try {
                    gameOver();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }


    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        isVisible = hasFocus;

        // full screen
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }


        if (gameSound != null) {
            gameSound.pause();
            threeSecondsSound.pause();
        }
        if (isVisible && gameOver) {
            gameOver = false;
            try {
                gameOver();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void copyReviveFromPrevActivity() {
        Intent reviveIntent = getIntent(); // gets the previously created intent
        revive = reviveIntent.getBooleanExtra("revive", false);
    }

    public void copyScoreFromPrevActivity(boolean revive) {
        if (revive) {
            Intent reviveIntent = getIntent(); // gets the previously created intent
            score = reviveIntent.getIntExtra("score", 0);
        }


    }

    private void getTimerSeconds() {
        FileInputStream fis = null;
        try {
            fis = openFileInput("settings_timer_seconds");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {

                sb.append(text);
                if (isLevel) {
                    timerSeconds = timeArray[levelNum - 1];
                } else {
                    timerSecondsOnString = sb.toString();
                    if (timerSecondsOnString.equals("Unlimited time")) {
                        timerSeconds = 10000;
                    } else {
                        timerSeconds = Integer.parseInt(timerSecondsOnString);
                    }
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

    // שליפת סוג התרגילים שהמשתמש ביקש מקובץ טקסט והשמה שלהם במשתנים המתאימים
    private void getDifficulty() {
        Intent i = getIntent();
        String maxAnswerStr = i.getStringExtra("difficulty");
        if (maxAnswerStr == null){
            maxAnswerStr = "10";
        }
        maxAnswer = Integer.parseInt(maxAnswerStr);
    }

    public void setMute() {
        Intent intent = getIntent();
        mute = intent.getBooleanExtra("mute", false);


    }

    // ביטול כפתור החזרה אחורה
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    public void levelMode() {



        Intent intent = getIntent();
        isLevel = intent.getBooleanExtra("isLevel", false);
        if (isLevel) {
            setLevel();
            setLevelsArrays();
            tvScore.setVisibility(View.INVISIBLE);
//            initTargilim(maxAnswer-1,maxAnswer-1, maxResultArray[levelNum-1], true,"x");

//            initTargilim(maxAnswer-1,maxAnswer-1, maxResultArray[levelNum-1], true,"+");
            setLevelBar();

        }



    }

    private int getSavedLevel() {
        Map<String, String> levelsData = getLevelsData();
         return Integer.parseInt(levelsData.get(getChosenOperator()));
    }
    private String getChosenOperator(){
        Intent i = getIntent();
        return i.getStringExtra("chosenOperator");
    }

    private Map<String, String> getLevelsData() {
        Map<String, String> diffMap = new HashMap<>();
        FileInputStream fis = null;
        try {
            fis = openFileInput("level");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {
                sb.append(text);
                String line = text.toString();
                String[] parts = line.split(",");
                String operator = parts[0];
                String levelNum = parts[1];
                diffMap.put(operator, levelNum);
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
        return diffMap;
    }


    private void setLevel() {
        Intent i = getIntent();
        levelNum = i.getIntExtra("chosenLevelNum", 1);
        levelTv.setText(getString(R.string.Level)+ ": " + levelNum);
        i = getIntent();
        levelNum = i.getIntExtra("chosenLevelNum", 1);
        levelTv.setText(getString(R.string.Level)+ ": " + levelNum);
        if (getSavedLevel() > levelNum){
            maxLevel = getSavedLevel();
        }
        else{
            maxLevel = levelNum;
        }

    }

    private List<String> getLevelsList(){
        List<String> levelsList = new ArrayList<>();
        FileInputStream fis = null;
        try {
            fis = openFileInput("level");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {
                levelsList.add(text);
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
        return levelsList;
    }

    private void saveLevel() throws IOException {
        List<String> levelsList = getLevelsList();

        FileOutputStream fos = openFileOutput("level", MODE_PRIVATE);

        try {
            for (int i = 0; i<levelsList.size(); i++){
                String[] parts = levelsList.get(i).split(",");
                String textOperator = parts[0];
                if (textOperator.equals(getChosenOperator())){
                    if (levelPassed && levelNum==maxLevel){
                        fos.write((textOperator+","+Integer.toString(maxLevel+1)).getBytes());
                    }
                    else{
                        fos.write((textOperator+","+Integer.toString(maxLevel)).getBytes());
                    }
                } else {
                    fos.write((levelsList.get(i)).getBytes());
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


    // get and set the enters number of the user

    private void setEntersCounter() {
        FileInputStream fis = null;
        try {
            fis = openFileInput("counter");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {

                sb.append(text);
                entersCounter = Integer.parseInt(sb.toString());
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
                entersCounter = 0;
            }
        }
    }



    private void saveEntersCounter() {
        FileOutputStream fos = null;


        try {
            fos = openFileOutput("counter", MODE_PRIVATE);
            fos.write(Integer.toString(entersCounter).getBytes());
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


    private void setViews() {
        tv = (TextView) findViewById(R.id.tv);
        tva1 = (TextView) findViewById(R.id.tva1);
        tva2 = (TextView) findViewById(R.id.tva2);
        tva3 = (TextView) findViewById(R.id.tva3);
        tva4 = (TextView) findViewById(R.id.tva4);
        tvScore = (TextView) findViewById(R.id.score);
        timer = (TextView) findViewById(R.id.timer);
        levelTv = (TextView) findViewById(R.id.level_tv);
        trueAnsTv = (TextView)findViewById(R.id.true_ans_tv);
        iv = (ImageView) findViewById(R.id.iv);
        hiv1 = (ImageView) findViewById(R.id.hiv1);
        hiv2 = (ImageView) findViewById(R.id.hiv2);
        hiv3 = (ImageView) findViewById(R.id.hiv3);
        //mAdView = findViewById(R.id.adView);
        homeBtn = (Button) findViewById(R.id.home_btn_game);
        gridLayout = (GridLayout) findViewById(R.id.grid_level_bar);

    }

    private void setSound() {
        correctSound = MediaPlayer.create(Game.this, R.raw.correct);
        falseSound = MediaPlayer.create(Game.this, R.raw.wrong_answer_sound);
        threeSecondsSound = MediaPlayer.create(Game.this, R.raw.timer_sound);
        gameSound = MediaPlayer.create(Game.this, R.raw.game_sound);
    }


    public void homeClick(View view) {
        Intent i = new Intent(Game.this, MainActivity.class);
        startActivity(i);
    }


    private void setLevelsArrays() {


        // read levels details from file
        Intent i = getIntent();
        isFirstVisit = i.getBooleanExtra("isFirstVisit", true);
        if(isFirstVisit){

        }



        timeArray[0] = 20;
        timeArray[1] = 20;
        timeArray[2] = 15;
        timeArray[3] = 15;
        timeArray[4] = 10;
        timeArray[5] = 10;
        timeArray[6] = 20;
        timeArray[7] = 20;
        timeArray[8] = 15;
        timeArray[9] = 10;
        timeArray[10] = 20;
        timeArray[11] = 15;
        timeArray[12] = 8;
        timeArray[13] = 8;
        timeArray[14] = 8;
        timeArray[15] = 7;
        timeArray[16] = 7;
        timeArray[17] = 5;
        timeArray[18] = 5;
        timeArray[19] = 5;
        timeArray[20] = 7;


        exNumArray[0] = 3;
        exNumArray[1] = 5;
        exNumArray[2] = 3;
        exNumArray[3] = 5;
        exNumArray[4] = 3;
        exNumArray[5] = 5;
        exNumArray[6] = 7;
        exNumArray[7] = 9;
        exNumArray[8] = 9;
        exNumArray[9] = 10;
        exNumArray[10] = 10;
        exNumArray[11] = 10;
        exNumArray[12] = 3;
        exNumArray[13] = 3;
        exNumArray[14] = 5;
        exNumArray[15] = 7;
        exNumArray[16] = 10;
        exNumArray[17] = 3;
        exNumArray[18] = 5;
        exNumArray[19] = 10;
        exNumArray[20] = 15;



        for (int j = 0; j <= 47; j++) {
            maxResultArray[j] = 10;

        }

    }

    public void homeBtnOnClick(View view) {
        Intent intent = new Intent(Game.this, MainActivity.class);
        intent.putExtra("mute", mute);
        startActivity(intent);
    }

    public void muteGameBtnOnClick(View view) {
        if (!mute) {
            muteBtn.setBackgroundResource(R.drawable.mute_btn_2);
        } else {
            muteBtn.setBackgroundResource(R.drawable.unmute_btn);
        }
        mute = !mute;
    }


    private void setLevelBar(){
        initateLevelBar();
        // create ImageView
        GridLayout layout = (GridLayout) findViewById(R.id.grid_level_bar);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width = layout.getMeasuredWidth();
                int height = layout.getMeasuredHeight();
                setLevelViews(height, width);

            }
        });

    }
    private void setLevelViews(int layoutHeight, int layoutWidth) {

        int width = 100;
        int height = 100;
        //ArrayList<String> data = new ArrayList<>();
        //data.clear();
        gridLayout.setColumnCount(exNumArray[levelNum - 1]);
        gridLayout.removeAllViews();
        for (int i = 0; i < exNumArray[levelNum - 1]; i++) {
            ImageView imageView = circle_list.get(i);
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;
            imageView.setLayoutParams(layoutParams);
            imageView.setImageResource(R.drawable.level_bar_not_finished);
            gridLayout.addView(imageView, i);
        }

    }
    private void update_level_bar(){
        for (int i = 0; i < exNumArray[levelNum - 1]-1; i++) {
            if(i< currentExNum){
                circle_list.get(i).setImageResource(R.drawable.level_bar_finished);
            }
            else{
                circle_list.get(i).setImageResource(R.drawable.level_bar_not_finished);

            }
        }
    }

    private void initateLevelBar(){
        for (int i = 0; i < exNumArray[levelNum - 1]+1; i++) {
            ImageView imageView = new ImageView(this);
            circle_list.add(imageView);
        }
    }


}










