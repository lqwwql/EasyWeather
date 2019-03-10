package com.main.easyweather.models;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2019/3/10.
 */

public class County extends DataSupport {

    private int iCode;        //县级代码
    private int iCity;    //市级代码
    private String sName;     //县区名

    public int getiCode() {
        return iCode;
    }

    public void setiCode(int iCode) {
        this.iCode = iCode;
    }

    public int getiCity() {
        return iCity;
    }

    public void setiCity(int iCity) {
        this.iCity = iCity;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    @Override
    public String toString() {
        return sName;
    }
}
