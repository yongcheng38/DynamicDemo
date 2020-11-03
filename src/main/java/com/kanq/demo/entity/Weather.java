package com.kanq.demo.entity;


import java.sql.Timestamp;

/**
 * @author yyc
 */
public class Weather {

    private Timestamp ts;

    private int temperature;

    private float humidity;

    private String noString;

    public String getNoString() {
        return noString;
    }

    public void setNoString(String noString) {
        this.noString = noString;
    }

    public Timestamp getTs() {
        return ts;
    }

    public void setTs(Timestamp ts) {
        this.ts = ts;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }
}
