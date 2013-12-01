package com.example.a_domino;

import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.physicaloid.lib.Physicaloid;
import com.physicaloid.lib.usb.driver.uart.ReadLisener;

public class CalorieActivity extends Activity {

	Button btStart, btStop, btReset;
	TextView goalCalroie, curCalroie, achivCalroie;
	
	Physicaloid mSerial;
	
	long endTime = 0;
	long startTime = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_cal);
	    
	    btStart = (Button)findViewById(R.id.startButton);
	    btStop = (Button)findViewById(R.id.stopButton);
	    btReset = (Button)findViewById(R.id.resetButton);
	    
	    goalCalroie = (TextView)findViewById(R.id.goalCalorie);
	    curCalroie = (TextView)findViewById(R.id.currentCalorie);
	    achivCalroie = (TextView)findViewById(R.id.achievementRate);
	    
	    mSerial = new Physicaloid(this);
	}
	
	public void buttonStartCal(View v){
		setText3(goalCalroie, 1);
		
		if(mSerial.open()){
			
			startTime = System.currentTimeMillis();
			
			mSerial.addReadListener(new ReadLisener() {
				String readStr;
				
				@Override
				public void onRead(int size) {
					byte[] buf = new byte[size];
					
					mSerial.read(buf, size);
					try{
						readStr = new String(buf, "UTF-8");
					} catch(UnsupportedEncodingException e){
						Log.e("TAG", e.toString());
						return;
					}
					
					int integ = Integer.parseInt(readStr);
					
					if(integ/1000 >= 1){
						setText1(curCalroie, integ%100);
						setText2(achivCalroie, integ%100);
					}
				}
			});
		}
	}
	
	public void buttonStopCal(View v){
		mSerial.close();
	}
	
	public void buttonResetCal(View v){
		resetText(goalCalroie, curCalroie, achivCalroie);
	}
	
	Handler mhandler = new Handler();
	private void setText1(TextView v, int i){
		final TextView fv = v;
		
		if(i<23){
			i=23;
		}
		
		final int fi = i;
		endTime = System.currentTimeMillis();
		mhandler.post(new Runnable() {
			@Override
			public void run() {
				fv.setText("ÇöÀç Ä®·Î¸® : "+ (int)((40+(fi - 23)*18)*((endTime-startTime)/1000))+"cal");
			}
		});
	}
	
	private void setText2(TextView v, int i){
		final TextView fv = v;
		if(i<23){
			i=23;
		}
		final int fi = i;
		endTime = System.currentTimeMillis();
		mhandler.post(new Runnable() {
			@Override
			public void run() {
				fv.setText("´Þ¼º·®  : " + (int)(((40+(fi - 23)*18)*((endTime-startTime)/1000)/200000)*100) +"%");
			}
		});
	}
	
	private void setText3(TextView v, int i){
		final TextView fv = v;
		mhandler.post(new Runnable() {
			@Override
			public void run() {
				fv.setText("¸ñÇ¥ Ä®·Î¸® : 2000kcal");
			}
		});
	}
	
	private void resetText(TextView v1, TextView v2, TextView v3){
		final TextView fv1 = v1;
		final TextView fv2 = v2;
		final TextView fv3 = v3;
		
		mhandler.post(new Runnable() {
			
			@Override
			public void run() {
				fv1.setText("Çï½º±â¾î¸¦ ¿¬°áÇÏ°í");
				fv2.setText("START ¹öÆ°À» ´­·¯ÁÖ¼¼¿ä");
				fv3.setText("Èå±ØÈå±Ø");
			}
		});
	}
}
