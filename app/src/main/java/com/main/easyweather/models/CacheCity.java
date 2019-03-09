package com.main.easyweather.models;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2019/3/9.
 */

public class CacheCity extends DataSupport {

    private String cityName;
    private String cityData;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityData() {
        return cityData;
    }

    public void setCityData(String cityData) {
        this.cityData = cityData;
    }
}
