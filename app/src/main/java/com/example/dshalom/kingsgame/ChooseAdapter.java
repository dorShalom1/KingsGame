package com.example.dshalom.kingsgame;

import android.app.Activity;
import android.content.Context;
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
    Context context;

    private VirtualRadioGroup radioGroup = new VirtualRadioGroup();

    public ChooseAdapter(Context applicationContext, String[] levels) {
        this.context = context;
        this.levels = levels;
        inflater = (LayoutInflater.from(applicationContext));
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

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.level_item, null);
        // get the reference of TextView and Button's
        final TextView levelText = (TextView) view.findViewById(R.id.levelText);
        final RadioButton levelRadio = (RadioButton) view.findViewById(R.id.levelRadio);
        radioGroup.addRadioButton(levelRadio);
        levelText.setText(levels[i]);
        return view;
    }

    public VirtualRadioGroup getRadioGroup() {
        return radioGroup;
    }
}