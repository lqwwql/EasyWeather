package com.main.easyweather.models;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2019/3/8.
 */

public class Province extends DataSupport{
    private int iCode;        //省份代码
    private String sName;   //省份名称

    public int getiCode() {
        return iCode;
    }

    public void setiCode(int iCode) {
        this.iCode = iCode;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }
}
