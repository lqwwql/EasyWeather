package com.main.easyweather.database;

import com.main.easyweather.models.City;
import com.main.easyweather.models.County;
import com.main.easyweather.models.Province;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2019/3/10.
 */

public class CountyDao {

    public static List<County> getCityByProvince(int cityCode){
        return DataSupport.where("icity = ?",""+cityCode).find(County.class);
    }

    public boolean findCounty(int iCode) {
        List<County> countyList = DataSupport.where("icode = ?", "" + iCode).find(County.class);
        if (countyList != null && countyList.size() > 0) {
            return true;
        }
        return false;
    }

}
