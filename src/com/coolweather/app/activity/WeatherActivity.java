package com.coolweather.app.activity;

import com.coolweather.app.R;
import com.coolweather.app.R.id;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.app.Activity;
import android.app.DownloadManager.Query;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity{

	private LinearLayout weatherInfoLayout;
	/**
	 * ������ʾ������
	 */
	private TextView cityNameText;
	/**
	 * ������ʾ����ʱ��
	 */
	private TextView publishText;
	/**
	 * ������ʾ������Ϣ
	 */
	private TextView weatherDespText;
	/**
	 * ������ʾ����1
	 */
	private TextView temp1Text;
	/**
	 * ������ʾ����2
	 */
	private TextView temp2Text;
	/**
	 * ������ʾ��ǰ����
	 */
	private TextView currentDateText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		weatherInfoLayout=(LinearLayout)findViewById(R.layout.weather_layout);
		cityNameText=(TextView)findViewById(R.id.city_name);
		publishText=(TextView)findViewById(R.id.publish_text);
		weatherDespText=(TextView)findViewById(R.id.weather_desp);
		temp1Text=(TextView)findViewById(R.id.temp1);
		temp2Text=(TextView)findViewById(R.id.temp2);
		currentDateText=(TextView)findViewById(R.id.current_date);
		String countyCode=getIntent().getStringExtra("county_code");
		if (!TextUtils.isEmpty(countyCode)) {
			//���ؼ������Ǿ�ȥ��ѯ����
			publishText.setText("ͬ����......");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode("countyCode");
		}else{
			//û��ʱ
			showWeather();
		}
	}
	
	/**
	 * ��ѯ�ؼ���������Ӧ����������
	 * @param string
	 */
	private void queryWeatherCode(String countyCode) {
		String address="http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
		queryFromServer(address,"countyCode");
	}
	
	/**
	 * ��ѯ������������Ӧ������
	 * @param string
	 */
	private void queryWeatherInfo(String weatherCode) {
		String address="http://www.weather.com.cn/data/list3/city"+weatherCode+".html";
		queryFromServer(address,"weatherCode");
		
	}
	
	/**
	 * ��ѯ�ؼ���������Ӧ����������
	 * @param string
	 */
	private void queryFromServer(final String address,final String type) {
		 HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){

			@Override
			public void onFinish(String respone) {
				// TODO Auto-generated method stub
				if ("countyCode".equals(type)) {
					if (!TextUtils.isEmpty(respone)) {
						String[] array=respone.split("\\|");
						if (array!=null&&array.length==2) {
							String weatherCode=array[1];
							queryWeatherInfo(weatherCode);
						}
					}
					
				}else if ("weatherCode".equals(type)) {
					Utility.handleWeatherResponse(WeatherActivity.this , respone);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
						showWeather();	
						}
					});
				}
			}

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
					publishText.setText("ͬ��ʧ��");	
					}
				});
			}
			 
		 });
		
	}
	
	/**
	 * ��SharedPrefreences�ļ��ж�ȡ�洢����������Ϣ������ʾ��������
	 */
	private void showWeather() {
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", ""));
		temp1Text.setText(prefs.getString("temp1", ""));
		temp2Text.setText(prefs.getString("temp2", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		currentDateText.setText(prefs.getString("current_text", ""));
		publishText.setText(prefs.getString("publish_time", "")+"����");
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
	}
	
	
}