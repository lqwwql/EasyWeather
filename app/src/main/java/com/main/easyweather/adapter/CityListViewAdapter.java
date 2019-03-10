package com.main.easyweather.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.main.easyweather.R;
import com.main.easyweather.listener.CityListViewItemClickListener;
import com.main.easyweather.models.CacheCity;
import com.main.easyweather.view.WeatherActivity;

import java.util.List;

/**
 * Created by Administrator on 2019/3/9.
 */

public class CityListViewAdapter extends BaseAdapter {

    private Activity activity;
    private List<CacheCity> cityList;
    private CityListViewItemClickListener cityListViewItemClickListener;

    public CityListViewAdapter(Activity activity, List<CacheCity> cityList,CityListViewItemClickListener cityListViewItemClickListener){
        this.activity = activity;
        this.cityList = cityList;
        this.cityListViewItemClickListener = cityListViewItemClickListener;
    }

    @Override
    public int getCount() {
        return cityList.size();
    }

    @Override
    public Object getItem(int position) {
        return cityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CityListViewHolder cityListViewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(activity).inflate(R.layout.lv_item_my_city,parent,false);
            cityListViewHolder = new CityListViewHolder(convertView);
            convertView.setTag(cityListViewHolder);
        }else{
            cityListViewHolder = (CityListViewHolder) convertView.getTag();
        }
        cityListViewHolder.tv_city_name.setText(cityList.get(position).getCityName());
        cityListViewHolder.iv_remove_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityListViewItemClickListener.onItemClickListener(position);
            }
        });
        cityListViewHolder.ll_turn_to_wd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,WeatherActivity.class);
                intent.putExtra("cityName",cityList.get(position).getCityName());
                activity.startActivity(intent);
                activity.finish();
            }
        });
        return convertView;
    }

    class CityListViewHolder{
        private TextView tv_city_name;
        private ImageView iv_remove_city;
        private LinearLayout ll_turn_to_wd;
        public CityListViewHolder(View view){
            tv_city_name = view.findViewById(R.id.tv_city_name);
            iv_remove_city = view.findViewById(R.id.iv_remove_city);
            ll_turn_to_wd = view.findViewById(R.id.ll_turn_to_wd);
        }
    }

}
