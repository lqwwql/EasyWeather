package com.main.easyweather.database;

import com.main.easyweather.models.City;
import com.main.easyweather.models.County;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2019/3/8.
 */

public class CityDao {

    public static List<City> getCityByProvince(int provCode){
        return DataSupport.where("iProvCode = ?",""+provCode).find(City.class);
    }

    public boolean findCity(int iCode) {
        List<City> cityList = DataSupport.where("icode = ?", "" + iCode).find(City.class);
        if (cityList != null && cityList.size() > 0) {
            return true;
        }
        return false;
    }

}
