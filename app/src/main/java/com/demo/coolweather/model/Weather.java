package com.demo.coolweather.model;

import java.io.Serializable;

public class Weather implements Serializable{
    private String cityName;
    private String time;
    private String date;
    private String info;
    private String temperature;
    private String pm25;
    private String quality;
    private String info2;
    private String temperature2;
    private String date3;
    private String info3;
    private String temperature3;
    private String date4;
    private String info4;
    private String temperature4;
    private String date5;
    private String info5;
    private String temperature5;

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public void setInfo2(String info2) {
        this.info2 = info2;
    }

    public void setTemperature2(String temperature2) {
        this.temperature2 = temperature2;
    }

    public void setDate3(String date3) {
        this.date3 = date3;
    }

    public void setInfo3(String info3) {
        this.info3 = info3;
    }

    public void setTemperature3(String temperature3) {
        this.temperature3 = temperature3;
    }

    public void setDate4(String date4) {
        this.date4 = date4;
    }

    public void setInfo4(String info4) {
        this.info4 = info4;
    }

    public void setTemperature4(String temperature4) {
        this.temperature4 = temperature4;
    }

    public void setDate5(String date5) {
        this.date5 = date5;
    }

    public void setInfo5(String info5) {
        this.info5 = info5;
    }

    public void setTemperature5(String temperature5) {
        this.temperature5 = temperature5;
    }

    public String getCityName() {
        return cityName;
    }

    public String getTime() {
        return time + "发布";
    }

    public String getDate() {
        return date;
    }

    public String getInfo() {
        return info;
    }

    public String getTemperature() {
        return temperature + "℃";
    }

    public String getPm25() {
        return pm25;
    }

    public String getQuality() {
        return quality;
    }

    public String getInfo2() {
        return info2;
    }

    public String getTemperature2() {
        return temperature2 + "℃";
    }

    public String getDate3() {
        return "周" + date3;
    }

    public String getInfo3() {
        return info3;
    }

    public String getTemperature3() {
        return temperature3 + "℃";
    }

    public String getDate4() {
        return "周" + date4;
    }

    public String getInfo4() {
        return info4;
    }

    public String getTemperature4() {
        return temperature4 + "℃";
    }

    public String getDate5() {
        return "周" + date5;
    }

    public String getInfo5() {
        return info5;
    }

    public String getTemperature5() {
        return temperature5 + "℃";
    }
}
