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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FirstOpenActivity extends AppCompatActivity {

    List<String> chosenOpertatorsList = new ArrayList<>();
    Boolean isFirstNextTap = false;

    RadioButton addRadio;
    RadioButton subRadio;
    RadioButton mulRadio;
    RadioButton divRadio;

    RadioGroup addGroup;
    RadioGroup subGroup;
    RadioGroup mulGroup;
    RadioGroup divGroup;

    boolean isVisible = true;


    Button nextBtn;

    TextView guidTv;

    int selectedNum = 0;
    int radiogroupSelectedNum = 0;

    MediaPlayer firstOpenMusic;

    int addSelectedNum = 0;
    int subSelectedNum = 0;
    int mulSelectedNum = 0;
    int divSelectedNum = 0;


    RadioButton addPrevSelected = null;
    RadioButton subPrevSelected = null;
    RadioButton mulPrevSelected = null;
    RadioButton divPrevSelected = null;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_open);
        getSupportActionBar().hide();
        //hide navigation bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        setButtons();
        setRadioGroups();
        LinearLayout difficultiesLy = (LinearLayout) findViewById(R.id.difficulties_ly);
        difficultiesLy.setVisibility(View.GONE);
        nextBtn.setVisibility(View.GONE);

        guidTv = (TextView) findViewById(R.id.guidtv);

        firstOpenMusic = MediaPlayer.create(FirstOpenActivity.this, R.raw.first_open_sound);
        firstOpenMusic.start();
        firstOpenMusic.setLooping(true);



    }

    private void setButtons(){
        addRadio = (RadioButton) findViewById(R.id.add_radio);
        subRadio = (RadioButton) findViewById(R.id.sub_radio);
        mulRadio = (RadioButton) findViewById(R.id.mul_radio);
        divRadio = (RadioButton) findViewById(R.id.div_radio);
        nextBtn = (Button) findViewById(R.id.next_btn);

    }


    public void nextOnClick(View view) {
        if (!isFirstNextTap){
            isFirstNextTap = true;

            TableLayout chooseOperatorsTl = (TableLayout) findViewById(R.id.choose_operators_tl);

            // get answers
            if (addRadio.isChecked()){
                chosenOpertatorsList.add("add");
            }
            if (subRadio.isChecked()){
                chosenOpertatorsList.add("sub");
            }
            if (mulRadio.isChecked()){
                chosenOpertatorsList.add("mul");
            }
            if (divRadio.isChecked()){
                chosenOpertatorsList.add("div");
            }

            //disappear
            chooseOperatorsTl.setVisibility(View.GONE);
            nextBtn.setVisibility(View.GONE);
            //show next
            LinearLayout difficultyTl = (LinearLayout) findViewById(R.id.difficulties_ly);
            difficultyTl.setVisibility(View.VISIBLE);
            showSelectedOperators();
            //guidTv.setHeight();
            guidTv.setText(getText(R.string.choosedifficulty));
        } else{
            // save operators and difficulties in files
            saveOperatorsAndDiff(getDifficulties());
            // move to MainActivity
            firstOpenMusic.pause();
            modifyChosenOperatorsToFireBase();
            Intent i = new Intent(FirstOpenActivity.this, MainActivity.class);
            startActivity(i);
        }



    }

    private void showSelectedOperators(){

        LinearLayout addDiffLy = (LinearLayout) findViewById(R.id.addition_diff_ly);
        addDiffLy.setVisibility(View.GONE);
        LinearLayout subDiffLy = (LinearLayout) findViewById(R.id.sub_diff_ly);
        subDiffLy.setVisibility(View.GONE);
        LinearLayout mulDiffLy = (LinearLayout) findViewById(R.id.mul_diff_ly);
        mulDiffLy.setVisibility(View.GONE);
        LinearLayout divDiffLy = (LinearLayout) findViewById(R.id.div_diff_ly);
        divDiffLy.setVisibility(View.GONE);




        for (int i =0;i< chosenOpertatorsList.size(); i++){
            if (chosenOpertatorsList.get(i).equals("add")){
                addDiffLy.setVisibility(View.VISIBLE);
            }
            if (chosenOpertatorsList.get(i).equals("sub")){
                subDiffLy.setVisibility(View.VISIBLE);
            }
            if (chosenOpertatorsList.get(i).equals("mul")){
                mulDiffLy.setVisibility(View.VISIBLE);
            }
            if (chosenOpertatorsList.get(i).equals("div")){
                divDiffLy.setVisibility(View.VISIBLE);
            }
        }

    }

    private Map<String, Integer> getDifficulties(){
        Map<String,Integer> diffMap = new HashMap<>();
        RadioGroup addGroup = (RadioGroup) findViewById(R.id.add_radiogroup);
        RadioGroup subGroup = (RadioGroup) findViewById(R.id.sub_radiogroup);
        RadioGroup mulGroup = (RadioGroup) findViewById(R.id.mul_radiogroup);
        RadioGroup divGroup = (RadioGroup) findViewById(R.id.div_radiogroup);
        for (int i =0;i< chosenOpertatorsList.size(); i++){
            if (chosenOpertatorsList.get(i).equals("add")){
                diffMap.put("+",getRadioFromGroup(addGroup,"add"));
            }
            if (chosenOpertatorsList.get(i).equals("sub")){
                diffMap.put("-",getRadioFromGroup(subGroup,"sub"));
            }
            if (chosenOpertatorsList.get(i).equals("mul")){
                diffMap.put("*",getRadioFromGroup(mulGroup,"mul"));
            }
            if (chosenOpertatorsList.get(i).equals("div")){
                diffMap.put("/",getRadioFromGroup(divGroup,"div"));
            }
        }
        return diffMap;

    }




    private int getRadioFromGroup(RadioGroup radioGroup,String operatar){
        int radioButtonID = radioGroup.getCheckedRadioButtonId();

        RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioButtonID);
        String selectedText = (String) radioButton.getText();
        String idName = getResources().getResourceEntryName(radioButton.getId());
        if (idName.equals(operatar+"5")){
            return 5;
        }else if (idName.equals(operatar+"10")){
            return 10;
        }else if (idName.equals(operatar+"20")){
            return 20;
        }else if (idName.equals(operatar+"50")){
            return 50;
        }else if (idName.equals(operatar+"100")){
            return 100;
        } else {
            return 10;
        }
    }


    private void saveOperatorsAndDiff(Map<String, Integer> diffMap) {

        FileOutputStream fos = null;

        try {
            fos = openFileOutput("OPERATORS_AND_DIFF_FILE", MODE_PRIVATE);
            for ( Map.Entry<String, Integer> entry : diffMap.entrySet()) {
                String key = entry.getKey();
                int tab = entry.getValue();
                String str = key + "," + Integer.toString(tab);
                fos.write(str.getBytes());
                fos.write(10);
            }
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

    public void addOnClick(View view) {
        if (!addRadio.isSelected()) {
            addRadio.setBackgroundResource(R.drawable.pressed_blue_button);
            addRadio.setChecked(true);
            addRadio.setSelected(true);
            selectedNum++;
        } else {
            addRadio.setBackgroundResource(R.drawable.blue_button);
            addRadio.setChecked(false);
            addRadio.setSelected(false);
            selectedNum--;
        }
        if (selectedNum > 0){
            nextBtn.setVisibility(View.VISIBLE);
        }
        else {
            nextBtn.setVisibility(View.GONE);
        }
    }

    public void mulOnClick(View view) {
        if (!mulRadio.isSelected()) {
            mulRadio.setBackgroundResource(R.drawable.pressed_yellow_button);
            mulRadio.setChecked(true);
            mulRadio.setSelected(true);
            selectedNum++;

        } else {
            mulRadio.setBackgroundResource(R.drawable.yellow_button);
            mulRadio.setChecked(false);
            mulRadio.setSelected(false);
            selectedNum--;
        }
        if (selectedNum > 0){
            nextBtn.setVisibility(View.VISIBLE);
        }
        else {
            nextBtn.setVisibility(View.GONE);
        }
    }

    public void subOnClick(View view) {
        if (!subRadio.isSelected()) {
            subRadio.setBackgroundResource(R.drawable.pressed_green_button);
            subRadio.setChecked(true);
            subRadio.setSelected(true);
            selectedNum++;
        } else {
            subRadio.setBackgroundResource(R.drawable.green_button);
            subRadio.setChecked(false);
            subRadio.setSelected(false);
            selectedNum--;
        }
        if (selectedNum > 0){
            nextBtn.setVisibility(View.VISIBLE);
        }
        else {
            nextBtn.setVisibility(View.GONE);
        }
    }

    public void divOnClick(View view) {
        if (!divRadio.isSelected()) {
            divRadio.setBackgroundResource(R.drawable.pressed_red_button);
            divRadio.setChecked(true);
            divRadio.setSelected(true);
            selectedNum++;
        } else {
            divRadio.setBackgroundResource(R.drawable.red_button);
            divRadio.setChecked(false);
            divRadio.setSelected(false);
            selectedNum--;
        }
        if (selectedNum > 0){
            nextBtn.setVisibility(View.VISIBLE);
        }
        else {
            nextBtn.setVisibility(View.GONE);
        }
    }

    public void adduptoOnClick(View view){
        earesebacofbuttons(getListOfButtons(addGroup));
        if (!view.isSelected()) {
            if (addPrevSelected != null && addPrevSelected.getId() != view.getId()){
                addPrevSelected.setSelected(false);
            }
            view.setBackgroundResource(R.drawable.selection_rec);
            if (addSelectedNum == 0){
                radiogroupSelectedNum++;
                addSelectedNum++;
            }
            view.setSelected(true);
        } else {
            view.setBackgroundResource(0);
            //view.setChecked(false);
            view.setSelected(false);
            addSelectedNum--;
            radiogroupSelectedNum--;

        }
        RadioGroup addGroup = (RadioGroup) findViewById(R.id.add_radiogroup);
        if (radiogroupSelectedNum >= selectedNum){
            nextBtn.setVisibility(View.VISIBLE);
        } else{
            nextBtn.setVisibility(View.GONE);

        }
        addPrevSelected = (RadioButton) view;
    }
    public void subuptoOnClick(View view){
        earesebacofbuttons(getListOfButtons(subGroup));
        if (!view.isSelected()) {
            view.setBackgroundResource(R.drawable.selection_rec);
            if (subPrevSelected != null && subPrevSelected.getId() != view.getId()){
                subPrevSelected.setSelected(false);
            }

            if (subSelectedNum == 0){
                radiogroupSelectedNum++;
                subSelectedNum++;
            }
            // view.setChecked(true);
            view.setSelected(true);

        } else {
            view.setBackgroundResource(0);
            //view.setChecked(false);
            view.setSelected(false);
            subSelectedNum--;
            radiogroupSelectedNum--;

        }
        RadioGroup addGroup = (RadioGroup) findViewById(R.id.add_radiogroup);
        if (radiogroupSelectedNum == selectedNum){
            nextBtn.setVisibility(View.VISIBLE);
        } else{
            nextBtn.setVisibility(View.GONE);

        }
        subPrevSelected = (RadioButton) view;

    }
    public void muluptoOnClick(View view){
        earesebacofbuttons(getListOfButtons(mulGroup));
        if (!view.isSelected()) {
            view.setBackgroundResource(R.drawable.selection_rec);
            if (mulPrevSelected != null && mulPrevSelected.getId() != view.getId()){
                mulPrevSelected.setSelected(false);
            }

            if (mulSelectedNum == 0){
                radiogroupSelectedNum++;
                mulSelectedNum++;
            }
            view.setSelected(true);

        } else {
            view.setBackgroundResource(0);
            //view.setChecked(false);
            view.setSelected(false);
            mulSelectedNum--;
            radiogroupSelectedNum--;

        }
        RadioGroup addGroup = (RadioGroup) findViewById(R.id.add_radiogroup);
        if (radiogroupSelectedNum == selectedNum){
            nextBtn.setVisibility(View.VISIBLE);
        } else{
            nextBtn.setVisibility(View.GONE);

        }
        mulPrevSelected = (RadioButton) view;

    }
    public void divuptoOnClick(View view){
        earesebacofbuttons(getListOfButtons(divGroup));
        if (!view.isSelected()) {
            view.setBackgroundResource(R.drawable.selection_rec);
            if (divPrevSelected != null && divPrevSelected.getId() != view.getId()){
                divPrevSelected.setSelected(false);
            }

            if (divSelectedNum == 0){
                radiogroupSelectedNum++;
                divSelectedNum++;
            }
            view.setSelected(true);
        } else {
            view.setBackgroundResource(0);
            //view.setChecked(false);
            view.setSelected(false);
            radiogroupSelectedNum--;
        }
        RadioGroup addGroup = (RadioGroup) findViewById(R.id.add_radiogroup);
        if (radiogroupSelectedNum == selectedNum){
            nextBtn.setVisibility(View.VISIBLE);
        } else{
            nextBtn.setVisibility(View.GONE);

        }
        divPrevSelected = (RadioButton) view;

    }

    private void setRadioGroups(){
        addGroup = (RadioGroup) findViewById(R.id.add_radiogroup);
        subGroup = (RadioGroup) findViewById(R.id.sub_radiogroup);
        mulGroup = (RadioGroup) findViewById(R.id.mul_radiogroup);
        divGroup = (RadioGroup) findViewById(R.id.div_radiogroup);

    }


    private ArrayList<RadioButton> getListOfButtons(RadioGroup radioGroup){
        int count = radioGroup.getChildCount();
        ArrayList<RadioButton> listOfRadioButtons = new ArrayList<RadioButton>();
        for (int i=0;i<count;i++) {
            View o = radioGroup.getChildAt(i);
            if (o instanceof RadioButton) {
                listOfRadioButtons.add((RadioButton)o);
            }
        }
        return listOfRadioButtons;
    }
    private void earesebacofbuttons(ArrayList<RadioButton> listOfRadioButtons){
        for (int i=0;i<listOfRadioButtons.size();i++) {
            listOfRadioButtons.get(i).setBackgroundResource(0);

        }
        }

    private void modifyChosenOperatorsToFireBase() {
        FirebaseAnalytics mFirebaseAnalytics;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        for (int i = 0; i < chosenOpertatorsList.size(); i++){
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, chosenOpertatorsList.get(i));
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        }
    }
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        isVisible = hasFocus;
        if (!isVisible) {
            firstOpenMusic.pause();
        } else {
            //mainActivityBackgroud.start();
        }


    }

}