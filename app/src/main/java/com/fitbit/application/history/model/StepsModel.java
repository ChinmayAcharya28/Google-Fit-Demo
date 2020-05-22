package com.fitbit.application.history.model;

public class StepsModel {

    String date;
    int value;

    public StepsModel(String date, int value) {
        this.date = date;
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public int getValue() {
        return value;
    }

}
