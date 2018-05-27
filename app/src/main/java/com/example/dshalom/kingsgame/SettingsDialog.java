package com.example.dshalom.kingsgame;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by dshalom on 05-Mar-18.
 */

public class SettingsDialog extends Dialog {

    private Context context;
    private ListView level_lvw = null;
    private final String[] levelName={"EASY", "MEDIUM", "HARD", "IMPOSSIBLE"};
    private ChooseAdapter adapter;
    Button button_ok;
    CheckBox checkbox_sound_off;
    private boolean sound;
    private int level;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    SettingsDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        preferences = context.getSharedPreferences("dor", Context.MODE_PRIVATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.settings_dialog);
        button_ok = findViewById(R.id.button_ok);
        checkbox_sound_off = findViewById(R.id.checkbox_sound_off);
        if(preferences.contains("sound"))
            checkbox_sound_off.setChecked(!preferences.getBoolean("sound", false));
        initListeners();
        initLevelList();
    }

    private void initListeners() {
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getRadioGroup().clear();
                level = adapter.selectedLevel;
                sound = !checkbox_sound_off.isChecked();
                editor = preferences.edit();
                editor.putInt("level", level);
                editor.putBoolean("sound", sound);
                editor.apply();
                SettingsDialog.super.dismiss();
            }
        });
    }

    private void initLevelList(){
        level_lvw = findViewById(R.id.levelList);
        adapter = new ChooseAdapter(context, levelName);
        level_lvw.setAdapter(adapter);
    }
}
