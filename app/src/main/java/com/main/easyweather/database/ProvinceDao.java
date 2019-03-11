package com.main.easyweather.database;

import com.main.easyweather.models.Province;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2019/3/8.
 */

public class ProvinceDao {

    public List<Province> getAllProvince() {
        return DataSupport.findAll(Province.class);
    }

    public boolean findProvince(int iCode) {
        List<Province> provinceList = DataSupport.where("icode = ?", "" + iCode).find(Province.class);
        if (provinceList != null && provinceList.size() > 0) {
            return true;
        }
        return false;
    }

}
