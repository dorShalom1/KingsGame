package com.example.dshalom.kingsgame;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class StartPage extends AppCompatActivity {
    Button newGame;
    Button settings;
    Button tutorial;
    int level = 0;
    boolean soundOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        newGame = (Button) findViewById(R.id.button_new_game);
        settings = (Button) findViewById(R.id.button_settings);
        tutorial = (Button) findViewById(R.id.button_tutorial);
        LinearLayout ll = (LinearLayout)findViewById(R.id.buttons);

        Button resume = new Button(this);
        resume.setText("RESUME");
        ll.addView(resume,0,newGame.getLayoutParams());
        startLiteners();
    }

    private void startLiteners(){
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsDialog();
            }
        });
    }

    public void settingsDialog() {

        SettingsDialog dlg = new SettingsDialog (this );
        dlg.show();
        /*AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(false)
                .setView(R.layout.settings_dialog)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveSettings();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        // Use the Builder class for convenient dialog construction
        final CharSequence[] levels = new CharSequence[] {"EASY", "MEDIUM", "HARD", "IMPOSSIBLE"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setSingleChoiceItems(levels,0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        level = which;
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveSettings();
                    }
                });
        // Create the AlertDialog object and return it
        TextView title = new TextView(this);
        // You Can Customise your Title here
        title.setText("SETTINGS");
        title.setBackgroundColor(Color.BLACK);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(25);
        builder.setCustomTitle(title);
        AlertDialog dialog = builder.create();
        dialog.show();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout parent = (LinearLayout) positiveButton.getParent();
        parent.setGravity(Gravity.CENTER_HORIZONTAL);
        ListView list = dialog.getListView();
        FrameLayout parent1 = (FrameLayout) list.getParent();
        parent1.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        View leftSpacer = parent.getChildAt(1);
        leftSpacer.setVisibility(View.GONE);*/

    }

    private void saveSettings() {
    }
}
