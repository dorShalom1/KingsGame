package com.example.dshalom.kingsgame;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by dshalom on 14-Feb-18.
 */

public class Place {
    ImageView image;
    private boolean isHighlighted;
    private boolean isOccupied;
    private int index;

    public Place(ImageView image, int index) {
        this.image = image;
        this.index = index;
    }

    public ImageView getImage() {
        return image;
    }

    public int getImageId() {
        return (int) image.getTag();
    }

    public void setImage(int imageId) {
        image.setImageResource(imageId);
        image.setTag(imageId);
    }

    public synchronized boolean isHighlighted() {
        return isHighlighted;
    }

    public synchronized void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
        int color = isHighlighted ? Color.CYAN : Color.TRANSPARENT;
        image.setBackgroundDrawable(new ColorDrawable(color));
    }

    public synchronized boolean isOccupied() {
        return isOccupied;
    }

    public synchronized void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public int getIndex() {
        return index;
    }
}
