package com.main.easyweather.utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.main.easyweather.database.CacheCityDao;
import com.main.easyweather.models.CacheCity;
import com.main.easyweather.models.WeatherBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/3/10.
 */

public class CacheUtils {

    public static List<WeatherBean> getCacheData() {
        try {
            List<CacheCity> cacheCityList = new CacheCityDao().findCacheCity();
            if (cacheCityList == null || cacheCityList.size() <= 0) {
                return null;
            }
            LogUtils.logInfo("get from cache data");
            String data = cacheCityList.get(0).getCityData();
            List<WeatherBean> weatherBeanList = JSON.parseObject(data, new TypeReference<List<WeatherBean>>() {
            });
            return weatherBeanList;
        } catch (Exception e) {
            LogUtils.logInfo("getCacheData err : ", e);
        }
        return null;
    }

    public static List<CacheCity> getAllCacheCityName() {
        try {
            List<CacheCity> cacheCityList = new CacheCityDao().findCacheCity();
            if (cacheCityList == null || cacheCityList.size() <= 0) {
                cacheCityList = new ArrayList<>();
                CacheCity cacheCity = new CacheCity();
                cacheCity.setCityName("无");
                cacheCityList.add(cacheCity);
                return cacheCityList;
            }
            return cacheCityList;
        } catch (Exception e) {
            LogUtils.logInfo("getCacheData err : ", e);
        }
        return null;
    }

    public static CacheCity getCacheCityName() {
        try {
            List<CacheCity> cacheCityList = new CacheCityDao().findCacheCity();
            if (cacheCityList == null || cacheCityList.size() <= 0) {
                return null;
            }
            LogUtils.logInfo("get from cache cityName");
            return cacheCityList.get(0);
        } catch (Exception e) {
            LogUtils.logInfo("getCacheData err : ", e);
        }
        return null;
    }

    public static void saveToCache(List<WeatherBean> weatherBeanList, String cityName) {
        String data = JSON.toJSONString(weatherBeanList);
        CacheCity cacheCity = new CacheCityDao().findCacheCityByName(cityName);
        if (cacheCity == null) {
            LogUtils.logInfo("cacheCity is null");
            cacheCity = new CacheCity();
        }
        cacheCity.setCityName(cityName);
        cacheCity.setCityData(data);
        cacheCity.save();
    }
}
