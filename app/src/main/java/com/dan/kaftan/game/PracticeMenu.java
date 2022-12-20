package com.dan.kaftan.game;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class PracticeMenu extends AppCompatActivity {

    boolean mute = false;
    Map<String, String> diffMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_menu);
        getSupportActionBar().hide();
        //hide navigation bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        diffMap = getOperatorsAndDifficulties();
        setSelectedOperators();
        Intent i = getIntent();
        mute = i.getBooleanExtra("mute", false);

    }

    private void setSelectedOperators(){
         Button addbtn = (Button) findViewById(R.id.add_btn);
         Button subbtn = (Button) findViewById(R.id.sub_btn);
         Button mulbtn = (Button) findViewById(R.id.mul_btn);
         Button divbtn = (Button) findViewById(R.id.div_btn);
         addbtn.setVisibility(View.GONE);
         subbtn.setVisibility(View.GONE);
         mulbtn.setVisibility(View.GONE);
         divbtn.setVisibility(View.GONE);

        for (Map.Entry<String, String> entry : diffMap.entrySet()) {
            String operator = entry.getKey();
            if (operator.equals("+")){
                addbtn.setVisibility(View.VISIBLE);
            }
            if (operator.equals("-")){
                subbtn.setVisibility(View.VISIBLE);
            }
            if (operator.equals("*")){
                mulbtn.setVisibility(View.VISIBLE);
            }
            if (operator.equals("/")){
                divbtn.setVisibility(View.VISIBLE);
            }
            // skip this activity if only one operator chosen
            if (diffMap.entrySet().size() == 1){
                Intent j = getIntent();
                boolean isLevel = j.getBooleanExtra("isLevel", false);
                Intent i;
                if (isLevel){
                    i = new Intent(PracticeMenu.this, LevelsMenu.class);
                } else{
                    i = new Intent(PracticeMenu.this, Game.class);
                }
                i.putExtra("mute", mute);
                i.putExtra("difficulty", diffMap.get(operator));
                i.putExtra("chosenOperator", operator);
                startActivity(i);

            }
        }
    }

    public void operatorOnClick(View view) {
        Button b = (Button)view;
        String operatorStr = b.getText().toString();

        // start game
        Intent i = getIntent();
        boolean isLevel = i.getBooleanExtra("isLevel", false);
        if (isLevel){
            i = new Intent(PracticeMenu.this, LevelsMenu.class);
        } else{
            i = new Intent(PracticeMenu.this, Game.class);
        }
        i.putExtra("mute", mute);
        if (b.getId() == R.id.add_btn){
            i.putExtra("difficulty", diffMap.get("+"));
            i.putExtra("chosenOperator", "+");
        }
        if (b.getId() == R.id.sub_btn){
            i.putExtra("difficulty", diffMap.get("-"));
            i.putExtra("chosenOperator", "-");

        }
        if (b.getId() == R.id.mul_btn){
            i.putExtra("difficulty", diffMap.get("*"));
            i.putExtra("chosenOperator", "*");

        }
        if (b.getId() == R.id.div_btn){
            i.putExtra("difficulty", diffMap.get("/"));
            i.putExtra("chosenOperator", "/");

        }
        startActivity(i);
    }

    public void backOnClick(View view){
        Intent i = new Intent(PracticeMenu.this, MainActivity.class);
        i.putExtra("mute", mute);
        startActivity(i);
    }


    private Map<String, String> getOperatorsAndDifficulties() {
        Map<String, String> diffMap = new HashMap<>();
        FileInputStream fis = null;
        try {
            fis = openFileInput("OPERATORS_AND_DIFF_FILE");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {
                sb.append(text);
                String line = text.toString();
                String[] parts = line.split(",");
                String operator = parts[0];
                String difficulty = parts[1];
                diffMap.put(operator, difficulty);
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

}