package com.example.dshalom.kingsgame;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dshalom on 05-Mar-18.
 */

public class SettingsDialog extends Dialog {

    private Context context;
    private ListView level_lvw = null;
    final String[] levelName={"EASY", "MEDIUM", "HARD", "IMPOSSIBLE"};
    ChooseAdapter adapter;
    Button button_ok;

    public SettingsDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public SettingsDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.settings_dialog);
        button_ok = (Button) findViewById(R.id.button_ok);
        initListeners();
        initLevelList();
    }

    private void initListeners() {
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getRadioGroup().clear();
                SettingsDialog.super.dismiss();
            }
        });
    }

    private void initLevelList(){
        level_lvw = (ListView) findViewById(R.id.levelList);
        adapter = new ChooseAdapter(getContext(), levelName);
        level_lvw.setAdapter(adapter);
       /*ArrayList<HashMap<String,Object>> arrayList=new ArrayList<>();
        for (int i=0;i<levelName.length;i++)
        {
            HashMap<String,Object> hashMap=new HashMap<>();//create a hashmap to store the data in key value pair
            hashMap.put("level",levelName[i]);
            hashMap.put("checked",false + "");
            arrayList.add(hashMap);//add the hashmap into arrayList
        }
        String[] from={"level","checked"};//string array
        int[] to={R.id.levelText, R.id.levelRadio};//int array of views id's
        SimpleAdapter simpleAdapter=new SimpleAdapter(context ,arrayList,R.layout.level_item ,from,to);
        level_lvw.setAdapter(simpleAdapter);//sets the adapter for listView*/


    }
}
