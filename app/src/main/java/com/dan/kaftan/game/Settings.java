package com.dan.kaftan.game;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Settings extends AppCompatActivity {

    Spinner timerSpinner;
    Spinner difficultySpinner;
    Switch timerSwitch;
    String timerSeconds = "10";
    int maxAnswer = 10;
    int selectedMaxAnswer = 10;
    String selectedTimerSeconds = "10";
    int score = 0;
    boolean revive;
    boolean mute;

    TextView timerTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // full screen
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().hide();

        // rotate screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        getSelectedMaxAnswer();
        getSelectedTimerSeconds();
        setTimerSpinner();
        setdifficultySpinner();

        timerSwitch = (Switch) findViewById(R.id.timer_switch);
        timerTV = (TextView) findViewById(R.id.timer_tv);
        if (selectedTimerSeconds.equals("unlimited time")) {
            timerSwitch.setChecked(false);
            timerSpinner.setVisibility(View.INVISIBLE);
            timerTV.setVisibility(View.INVISIBLE);
        } else {
            timerSwitch.setChecked(true);
            timerSpinner.setVisibility(View.VISIBLE);
            timerTV.setVisibility(View.VISIBLE);
        }

        Intent muteIntent = getIntent(); // gets the previously created intent
        mute = muteIntent.getBooleanExtra("mute", false);
    }

    public void setTimerSpinner() {

        timerSpinner = (Spinner) findViewById(R.id.spinner);

        ArrayList<String> arraySpinner = new ArrayList(5);

        arraySpinner.add("5");
        arraySpinner.add("10");
        arraySpinner.add("15");
        arraySpinner.add("20");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timerSpinner.setAdapter(adapter);
        switch (selectedTimerSeconds) {
            case "5":
                timerSpinner.setSelection(0);
                break;
            case "10":
                timerSpinner.setSelection(1);
                break;
            case "15":
                timerSpinner.setSelection(2);
                break;
            case "20":
                timerSpinner.setSelection(3);
                break;
        }

    }


    public void setdifficultySpinner() {

        difficultySpinner = (Spinner) findViewById(R.id.difficulty_spinner);

        ArrayList<String> arraySpinner = new ArrayList(5);

        if (!welcome_screen.ismulty) {
            arraySpinner.add("up to 5");
        }
        arraySpinner.add("up to 10");
        arraySpinner.add("up to 20");
        arraySpinner.add("up to 50");
        arraySpinner.add("up to 100");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(adapter);

        int offset = welcome_screen.ismulty ? -1 : 0;
        switch (selectedMaxAnswer) {
            case 5:
                difficultySpinner.setSelection(0);
                break;
            case 10:
                difficultySpinner.setSelection(1 + offset);
                break;
            case 20:
                difficultySpinner.setSelection(2 + offset);
                break;
            case 50:
                difficultySpinner.setSelection(3 + offset);
                break;
            case 100:
                difficultySpinner.setSelection(4 + offset);
                break;
        }

    }

    private void setMaxAnswer() {
        switch (difficultySpinner.getSelectedItem().toString()) {

            case "up to 5":
                maxAnswer = 5;
                break;
            case "up to 10":
                maxAnswer = 10;
                break;
            case "up to 20":
                maxAnswer = 20;
                break;
            case "up to 50":
                maxAnswer = 50;
                break;
            case "up to 100":
                maxAnswer = 100;
                break;
            case "up to 1000":
                maxAnswer = 1000;
                break;


        }

    }

    public void confimOnClick(View view) {
        setMaxAnswer();
        saveDifficulty();
        timerSeconds = timerSpinner.getSelectedItem().toString();
        if (!timerSwitch.isChecked()) {
            timerSeconds = "unlimited time";
        }
        saveTimerSeconds();

        Intent intent = getIntent();
        boolean isFromEnd = intent.getBooleanExtra("isFromEnd", false);
        if (isFromEnd) {
            getVariableFromPrevActivity();
            System.out.println("came from isFromEnd = true");
            Intent i = new Intent(Settings.this, EndGame.class);
            i.putExtra("score", score);
            i.putExtra("revive", revive);
            i.putExtra("mute", mute);
            i.putExtra("isFromSettings", true);
            startActivity(i);

        } else {
            System.out.println("came from isFromEnd = false");
            Intent i = new Intent(Settings.this, MainActivity.class);
            i.putExtra("mute", mute);
            i.putExtra("isFromSettings", true);
            startActivity(i);

        }


    }


    private void saveTimerSeconds() {

        FileOutputStream fos = null;


        try {
            fos = openFileOutput("settings_timer_seconds", MODE_PRIVATE);
            fos.write(timerSeconds.getBytes());
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

    private void saveDifficulty() {

        FileOutputStream fos = null;


        try {
            fos = openFileOutput("settings_difficulty", MODE_PRIVATE);
            fos.write(Integer.toString(maxAnswer).getBytes());
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


    private void getSelectedMaxAnswer() {
        FileInputStream fis = null;
        try {
            fis = openFileInput("settings_difficulty");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {

                sb.append(text);
                selectedMaxAnswer = Integer.parseInt(sb.toString());
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

    private void getSelectedTimerSeconds() {
        FileInputStream fis = null;
        try {
            fis = openFileInput("settings_timer_seconds");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {

                sb.append(text);
                selectedTimerSeconds = sb.toString();
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

    private void getVariableFromPrevActivity() {
        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        revive = intent.getBooleanExtra("revive", false);
        mute = intent.getBooleanExtra("mute", false);

    }


    public void timerOfOnOnClick(View view) {
        if (timerSwitch.isChecked()) {
            timerTV.setVisibility(View.VISIBLE);
            timerSpinner.setVisibility(View.VISIBLE);
        } else {
            timerTV.setVisibility(View.INVISIBLE);
            timerSpinner.setVisibility(View.INVISIBLE);
        }
    }
}
