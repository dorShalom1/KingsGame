package com.example.dshalom.kingsgame;

/**
 * Created by dshalom on 06-Mar-18.
 */


import android.view.View;
import android.widget.RadioButton;

import java.util.ArrayList;

public class VirtualRadioGroup {

    // Global variables
    private ArrayList<RadioButton> radioButtonList;
    private int firstCheckedItemId;
    public static int countRadioButtons;

    public VirtualRadioGroup() {
        radioButtonList = new ArrayList<>();
    }

    public VirtualRadioGroup(ArrayList<RadioButton> radioButtons) {
        radioButtonList = radioButtons;
        firstCheckedItemId = getCheckedItemId();
        countRadioButtons = radioButtonList.size();
        onCheck();
    }

    private void onCheck() {
        for (RadioButton aRb : radioButtonList) {
            aRb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View radio) {
                    final RadioButton myRb = (RadioButton) radio;
                    for (RadioButton aRb : radioButtonList) {
                        if (aRb.equals(myRb)) {
                            aRb.setChecked(true);
                        } else aRb.setChecked(false);
                    }
                }
            });
        }
    }

    public void addRadioButton(RadioButton radioButton){
        radioButtonList.add(radioButton);
        onCheck();
    }

    public void clear(){
        radioButtonList.clear();
    }
    
    public int getRadioButtonsCount() {
        return countRadioButtons;
    }

    public int getCheckedItemId() {
        int id = -1;
        for (RadioButton aRb : radioButtonList) {
            if (aRb.isChecked())
                id = aRb.getId();
        }
        return id;
    }

    public RadioButton getCheckedItem() {
        RadioButton RB = null;
        for (RadioButton aRb : radioButtonList) {
            if (aRb.isChecked())
                RB = aRb;
        }
        return RB;
    }

    public void resetButtons() {
        RadioButton iRb = getCheckedItem();
        if (iRb != null)
            iRb.setChecked(false);
        if (firstCheckedItemId != -1)
            getChildById(firstCheckedItemId).setChecked(true);
    }

    private RadioButton getChildById(int id) {
        RadioButton iRb = null;
        for (RadioButton aRb : radioButtonList) {
            if (aRb.getId() == id) {
                iRb = aRb;
                break;
            }
        }
        return iRb;
    }

    public RadioButton getChildAt(int position) {
        return radioButtonList.get(position);
    }
}
