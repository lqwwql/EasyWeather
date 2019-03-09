package com.main.easyweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.main.easyweather.R;
import com.main.easyweather.models.WeatherHourBean;
import com.main.easyweather.utils.GlideUtils;

import java.util.List;

/**
 * Created by Administrator on 2019/3/8.
 */

public class HourWeatherRecycleViewAdapter extends RecyclerView.Adapter{

    public HourWeatherViewHolder hourWeatherViewHolder;
    private List<WeatherHourBean> weatherHourBeanList;
    private Context context;

    public HourWeatherRecycleViewAdapter(Context context,List<WeatherHourBean> weatherHourBeanList){
        this.context = context;
        this.weatherHourBeanList = weatherHourBeanList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        hourWeatherViewHolder = new HourWeatherViewHolder(
                LayoutInflater.from(context).inflate(R.layout.rv_item_hour_weather,parent,false)
        );
        return hourWeatherViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HourWeatherViewHolder hourWeatherViewHolder = (HourWeatherViewHolder) holder;
        hourWeatherViewHolder.setData(position);
    }

    @Override
    public int getItemCount() {
        return weatherHourBeanList.size();
    }

    public class HourWeatherViewHolder extends  RecyclerView.ViewHolder {

        public TextView tv_hour,tv_per,tv_temp;
        public ImageView iv_hour_weather;

        public HourWeatherViewHolder(View itemView) {
            super(itemView);
            tv_hour = itemView.findViewById(R.id.tv_hour);
            tv_per = itemView.findViewById(R.id.tv_per);
            tv_temp = itemView.findViewById(R.id.tv_temp);
            iv_hour_weather = itemView.findViewById(R.id.iv_hour_weather);
        }

        public void setData(final int position){
            WeatherHourBean weatherHourBean = weatherHourBeanList.get(position);
            String hour = weatherHourBean.getHour();
            tv_hour.setText(hour.substring(hour.length()-6,hour.length()));
            tv_per.setText(weatherHourBean.getPer()+"%");
            tv_temp.setText(weatherHourBean.getTemp()+"℃");
            int imageUrl = context.getResources().getIdentifier("w"+weatherHourBean.getImage(), "drawable", context.getPackageName());
            GlideUtils.getInstence().setImageCacheResource(imageUrl,context,iv_hour_weather);
        }
    }


}
