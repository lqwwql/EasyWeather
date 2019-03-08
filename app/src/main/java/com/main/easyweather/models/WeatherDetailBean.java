package com.main.easyweather.models;

/**
 * Created by Administrator on 2019/3/8.
 */

public class WeatherDetailBean {

    private String temp;
    private String condCode;
    private String condTxt;
    private String windDir;
    private String windSc;

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getCondCode() {
        return condCode;
    }

    public void setCondCode(String condCode) {
        this.condCode = condCode;
    }

    public String getCondTxt() {
        return condTxt;
    }

    public void setCondTxt(String condTxt) {
        this.condTxt = condTxt;
    }

    public String getWindDir() {
        return windDir;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }

    public String getWindSc() {
        return windSc;
    }

    public void setWindSc(String windSc) {
        this.windSc = windSc;
    }
}
