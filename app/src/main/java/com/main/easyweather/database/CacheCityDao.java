package com.main.easyweather.database;

import android.util.Log;

import com.main.easyweather.models.CacheCity;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2019/3/10.
 */

public class CacheCityDao {

    public List<CacheCity> findCacheCity() {
        try {
            List<CacheCity> cacheCityList = DataSupport.findAll(CacheCity.class);
            if (cacheCityList == null || cacheCityList.size() <= 0) {
                return null;
            }
            return cacheCityList;
        }catch (Exception e){
            Log.i("weather","findCacheCity err = ",e);
        }
        return null;
    }

    public CacheCity findCacheCityByName(String cityName){
        try {
            List<CacheCity> cacheCityList = DataSupport.where("cityname = ?",cityName).find(CacheCity.class);
            if (cacheCityList == null || cacheCityList.size() <= 0) {
                return null;
            }
            return cacheCityList.get(0);
        }catch (Exception e){
            Log.i("weather","findCacheCityByName err = ",e);
        }
        return null;
    }


}
