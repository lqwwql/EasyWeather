package com.main.easyweather.models;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2019/3/8.
 */

public class City extends DataSupport {
    private int iCode;        //城市代码
    private int iProvCode;    //省份代码
    private String sName;   //城市名

    public int getiCode() {
        return iCode;
    }

    public void setiCode(int iCode) {
        this.iCode = iCode;
    }

    public int getiProvCode() {
        return iProvCode;
    }

    public void setiProvCode(int iProvCode) {
        this.iProvCode = iProvCode;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }
}
