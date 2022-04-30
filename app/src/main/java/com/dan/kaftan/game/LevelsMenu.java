package com.dan.kaftan.game;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class LevelsMenu extends AppCompatActivity {

    TableLayout tableLayout;
    int buttonsInRow = 7;
    int buttonsInC = 7;
    Button[][] buttonLevelsArr = new Button[buttonsInRow][buttonsInC];

    Boolean mute = false;
    Boolean isLevel = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // rotate screen
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels_menu);

        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Intent intent = getIntent();
        mute = intent.getBooleanExtra("mute", false);
        isLevel = intent.getBooleanExtra("isLevel", false);

        tableLayout = (TableLayout) findViewById(R.id.table_layuot);

        test();
    }


    private void test() {
        TableLayout table = new TableLayout(this);
        for (int row = 0; row < buttonsInC; row++) {
            TableRow currentRow = new TableRow(table.getContext());
            for (int c = 0; c < buttonsInRow; c++) {
                Button currentButton = new Button(this);
                currentButton.setText(Integer.toString(c + row*buttonsInRow +1));
                currentButton.setBackgroundResource(R.drawable.level_passed_btn_background);
                currentButton.setOnClickListener(btnClicked);
                // you can store them
                buttonLevelsArr[row][c] = currentButton;
                // and you have to add them to the TableRow
                currentRow.addView(currentButton);
            }
            // a new row has been constructed -> add to table
            table.addView(currentRow);
        }
// and finally takes that new table and add it to your layout.
        tableLayout.addView(table);
    }

    View.OnClickListener btnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object tag = v.getTag();
            System.out.println("btn clicked");
            Toast.makeText(getApplicationContext(), "clicked button", Toast.LENGTH_SHORT).show();
        }
    };
}