package com.main.easyweather.models;

import java.util.List;

/**
 * Created by Administrator on 2019/3/8.
 */

public class WeatherBean {

    private WeatherDetailBean weatherDetailBean;
    private List<WeatherHourBean> weatherHourBeanList;
    private List<WeatherDayBean> weatherDayBeanList;
    private WeatherTipBean weatherTipBean;
    private int Type;

    public WeatherDetailBean getWeatherDetailBean() {
        return weatherDetailBean;
    }

    public void setWeatherDetailBean(WeatherDetailBean weatherDetailBean) {
        this.weatherDetailBean = weatherDetailBean;
    }

    public List<WeatherHourBean> getWeatherHourBeanList() {
        return weatherHourBeanList;
    }

    public void setWeatherHourBeanList(List<WeatherHourBean> weatherHourBeanList) {
        this.weatherHourBeanList = weatherHourBeanList;
    }

    public List<WeatherDayBean> getWeatherDayBeanList() {
        return weatherDayBeanList;
    }

    public void setWeatherDayBeanList(List<WeatherDayBean> weatherDayBeanList) {
        this.weatherDayBeanList = weatherDayBeanList;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public WeatherTipBean getWeatherTipBean() {
        return weatherTipBean;
    }

    public void setWeatherTipBean(WeatherTipBean weatherTipBean) {
        this.weatherTipBean = weatherTipBean;
    }
}
