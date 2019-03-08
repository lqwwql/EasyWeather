package com.main.easyweather.database;

import com.main.easyweather.models.Province;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2019/3/8.
 */

public class ProvinceDao {

    public static List<Province> getAllProvince(){
        return DataSupport.findAll(Province.class);
    }

}
