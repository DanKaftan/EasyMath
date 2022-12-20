package com.dan.kaftan.game;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import java.util.Locale;

public class Settings extends AppCompatActivity {

    Spinner timerSpinner;
    String timerSeconds = "10";

    String selectedTimerSeconds = "10";
    int score = 0;
    boolean revive;
    boolean mute;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // full screen
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().hide();
        //hide navigation bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        // rotate screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //loadLocale();

        getSelectedTimerSeconds();
        setTimerSpinner();

        Intent muteIntent = getIntent(); // gets the previously created intent
        mute = muteIntent.getBooleanExtra("mute", false);
    }

    public void setTimerSpinner() {

        timerSpinner = (Spinner) findViewById(R.id.spinner);

        ArrayList<String> arraySpinner = new ArrayList(5);

        arraySpinner.add(getString(R.string.unlimitedtime));
        arraySpinner.add("5");
        arraySpinner.add("10");
        arraySpinner.add("15");
        arraySpinner.add("20");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timerSpinner.setAdapter(adapter);
        switch (selectedTimerSeconds) {
            case "Unlimited Time":
                timerSpinner.setSelection(0);
                break;
            case "5":
                timerSpinner.setSelection(1);
                break;
            case "10":
                timerSpinner.setSelection(2);
                break;
            case "15":
                timerSpinner.setSelection(3);
                break;
            case "20":
                timerSpinner.setSelection(4);
                break;
        }

    }

    public void confimOnClick(View view) {
        timerSeconds = timerSpinner.getSelectedItem().toString();
        if (timerSeconds.equals(getString(R.string.unlimitedtime))){
            timerSeconds = "Unlimited time";
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

    private void showChangeLanguageDialog(){
        final String[] ItemList = {"عربى","English","Français","español", "עברית", "हिन्दी", "Türk"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Settings.this);
        mBuilder.setTitle("Choose language");
        mBuilder.setSingleChoiceItems(ItemList, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(i == 0){
                    setLocal("ar");
                    recreate();
                }
                if(i == 1) {
                    setLocal("en");
                    recreate();
                }

                if(i == 2){
                    setLocal("fr");
                    recreate();
                }
                if(i == 3){
                    setLocal("es");
                    recreate();
                }
                if(i == 4){
                    setLocal("iw");
                    recreate();
                }
                if(i == 5){
                    setLocal("hi");
                    recreate();
                }

                if(i == 6){
                    setLocal("tr");
                    recreate();
                }



            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }
    private void setLocal(String lang){
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        getBaseContext().getResources().updateConfiguration(conf, getBaseContext().getResources().getDisplayMetrics());
        invalidateOptionsMenu();
        recreate();


    }

    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocal(language);
    }

    public void changeLanguge(View view) {
        showChangeLanguageDialog();
    }

    public void changeDifOnClick(View view){
        Intent i = new Intent(Settings.this, FirstOpenActivity.class);
        startActivity(i);
    }
}
