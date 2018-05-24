package com.example.dshalom.kingsgame;

/**
 * Created by dshalom on 22-Feb-18.
 */

public class GameStep {
    public enum Type {
        ADD,
        REMOVE,
        CHANGE_STATE;
    }

    private String card;
    private Type type;
    private int place;

    public GameStep(Type stepType, int placeId, String card){
        this.card = card;
        this.type = stepType;
        this.place = placeId;
    }

    public GameStep(Type stepType){
        this.type = stepType;
    }

    public String getCard() {
        return card;
    }

    public Type getType() {
        return type;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }
}
