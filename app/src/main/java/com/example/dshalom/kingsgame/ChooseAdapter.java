package com.example.dshalom.kingsgame;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by dshalom on 05-Mar-18.
 */

public class ChooseAdapter extends BaseAdapter{

    private String level;
    private String[] levels;
    private LayoutInflater inflater;
    int selectedLevel;
    private SharedPreferences preferences;
    private Context context;

    private VirtualRadioGroup radioGroup = new VirtualRadioGroup();

    ChooseAdapter(Context context, String[] levels) {
        this.context = context;
        preferences = context.getSharedPreferences("dor", Context.MODE_PRIVATE);
        selectedLevel = preferences.getInt("level", 3);
        this.levels = levels;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return levels.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.level_item, null);
        // get the reference of TextView and Button's
        final TextView levelText = view.findViewById(R.id.levelText);
        final RadioButton levelRadio = view.findViewById(R.id.levelRadio);
        if(preferences.contains("level") && i == preferences.getInt("level", 3))
            levelRadio.setChecked(true);
        levelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelRadio.callOnClick();
            }
        });
        levelRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    selectedLevel = i;
            }
        });
        radioGroup.addRadioButton(levelRadio);
        levelText.setText(levels[i]);
        return view;
    }

    public VirtualRadioGroup getRadioGroup() {
        return radioGroup;
    }
}