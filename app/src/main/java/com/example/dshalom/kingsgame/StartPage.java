package com.example.dshalom.kingsgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class StartPage extends AppCompatActivity {
    Button newGame;
    Button settings;
    Button tutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        newGame = findViewById(R.id.button_new_game);
        settings = findViewById(R.id.button_settings);
        tutorial = findViewById(R.id.button_tutorial);
        LinearLayout ll = findViewById(R.id.buttons);
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
    }
}
