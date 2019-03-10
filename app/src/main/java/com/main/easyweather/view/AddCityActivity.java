package com.main.easyweather.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.main.easyweather.R;
import com.main.easyweather.adapter.PCCAdapter;
import com.main.easyweather.common.Common;
import com.main.easyweather.models.City;
import com.main.easyweather.models.County;
import com.main.easyweather.models.Province;
import com.main.easyweather.network.HttpConnectionManager;
import com.main.easyweather.utils.LogUtils;

import java.util.List;

public class AddCityActivity extends AppCompatActivity {

    private Button btn_check;
    private Spinner sp_province, sp_city, sp_county;
    private List<Province> provincelists;
    private List<City> citylists;
    private List<County> countylists;
    private HttpConnectionManager httpConnectionManager;
    private String result;
    private TextView tv_title;
    private ImageView iv_turnback;
    private int iProvCode, iCityCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        httpConnectionManager = HttpConnectionManager.getInstance();
        init();
    }

    private void init() {
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("添加城市/地区");
        iv_turnback = findViewById(R.id.iv_turnback);
        btn_check = findViewById(R.id.btn_check);
        sp_province = findViewById(R.id.sp_province);
        sp_city = findViewById(R.id.sp_city);
        sp_county = findViewById(R.id.sp_county);
        initProvince();

        iv_turnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (result == null || result.equals("")) {
                    new AlertDialog.Builder(AddCityActivity.this).setTitle("温馨提示")
                            .setMessage("请选择城市或地区")
                            .setPositiveButton("确定", null).show();
                } else {
                    Intent intent = new Intent(AddCityActivity.this, WeatherActivity.class);
                    intent.putExtra("cityName", result);
                    startActivity(intent);
                }
            }
        });
        //所支持的中国所有省份
        sp_province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (provincelists != null && provincelists.size() > 0 && position > 0) {
                    iProvCode = provincelists.get(position).getiCode();
                    result = provincelists.get(position).getsName();
                    LogUtils.logInfo("select --> "+result);
                    initCity(iProvCode);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //对应省份所有城市和地区
        sp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (citylists != null && citylists.size() > 0) {
                    iCityCode = citylists.get(position).getiCode();
                    result = citylists.get(position).getsName();
                    LogUtils.logInfo("select --> "+result);
                    initCounty(iCityCode, iProvCode);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //对应城市地区对应的县级市
        sp_county.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (countylists != null && countylists.size() > 0) {
                    result = countylists.get(position).getsName();
                    LogUtils.logInfo("select --> "+result);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initProvince() {
        LogUtils.logInfo("initProvince");
        httpConnectionManager.getProvince(Common.URL, new HttpConnectionManager.GetProvinceCallBack() {
            @Override
            public void onResponse(int result, List<Province> provList) {
                provincelists = provList;
                if (provincelists != null && provincelists.size() > 0) {
                    PCCAdapter<Province> provincePCCAdapter = new PCCAdapter<Province>(AddCityActivity.this, provList, R.layout.spinner_item) {
                        @Override
                        public void convert(SpinnerViewHolder spinnerViewHolder, Province province) {
                            spinnerViewHolder.setTitle(R.id.tv_spinner, province.getsName());
                        }
                    };
                    provincePCCAdapter.setDropDownViewResource(R.layout.down_style);
                    sp_province.setAdapter(provincePCCAdapter);
                }
            }
        });
    }

    private void initCity(int iProvCode) {
        httpConnectionManager.getCity(Common.URL, iProvCode, new HttpConnectionManager.GetCityCallBack() {
            @Override
            public void onResponse(int result, List<City> cityList) {
                citylists = cityList;
                if (cityList != null && cityList.size() > 0) {
                    PCCAdapter<City> cityPCCAdapter = new PCCAdapter<City>(AddCityActivity.this, cityList, R.layout.spinner_item) {
                        @Override
                        public void convert(SpinnerViewHolder spinnerViewHolder, City city) {
                            spinnerViewHolder.setTitle(R.id.tv_spinner, city.getsName());
                        }
                    };
                    cityPCCAdapter.setDropDownViewResource(R.layout.down_style);
                    sp_city.setAdapter(cityPCCAdapter);
                }else{
                    sp_city.setAdapter(null);
                }
            }
        });
    }

    private void initCounty(int iCityCode, int iProvCode) {
        httpConnectionManager.getCounty(Common.URL, iCityCode, iProvCode, new HttpConnectionManager.GetCountyCallBack() {
            @Override
            public void onResponse(int result, List<County> countyList) {
                countylists = countyList;
                if (countylists != null && countylists.size() > 0) {
                    PCCAdapter<County> countyPCCAdapter = new PCCAdapter<County>(AddCityActivity.this, countyList, R.layout.spinner_item) {
                        @Override
                        public void convert(SpinnerViewHolder spinnerViewHolder, County county) {
                            spinnerViewHolder.setTitle(R.id.tv_spinner, county.getsName());
                        }
                    };
                    countyPCCAdapter.setDropDownViewResource(R.layout.down_style);
                    sp_county.setAdapter(countyPCCAdapter);
                }else{
                    sp_county.setAdapter(null);
                }
            }
        });
    }

}
