package com.example.a_domino;

import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.physicaloid.lib.Physicaloid;
import com.physicaloid.lib.usb.driver.uart.ReadLisener;

public class Dht11 extends Activity {

	private static final int MENU_ID_RECOMMEND = 1;
	
	Button btOpen, btClose;
	TextView tvTemp, tvHumid;
	ImageView face1, face2, face3, face4, face5;
	Vibrator vibrator;
	NotificationManager nm;
	Notification notif;
	
	Physicaloid mSerial;
	
	static int intentActvity;
	long vibratorTime=500;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.dht11);
	    
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	    
	    btOpen = (Button)findViewById(R.id.btOpen1);
	    btClose = (Button)findViewById(R.id.btClose1);
	    tvTemp = (TextView)findViewById(R.id.tvTemp);
	    tvHumid = (TextView)findViewById(R.id.tvHumid);
	    face1 = (ImageView)findViewById(R.id.face1);
	    face2 = (ImageView)findViewById(R.id.face2);
	    face3 = (ImageView)findViewById(R.id.face3);
	    face4 = (ImageView)findViewById(R.id.face4);
	    face5 = (ImageView)findViewById(R.id.face5);
	    
	    mSerial = new Physicaloid(this);
	    
	    setEnabledUi(false);
	}
	
	//menu open device
	public void OnClickOpen1(View v){
		if(mSerial.open()){
			setEnabledUi(true);
			
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
						sText(tvTemp, ""+ integ%100 + "¡É");
						sText(tvHumid, ""+ ((integ-integ%100)/100) + "%");
						sDiscomfortable(integ);
						
						if(integ%100 >= 32){
							nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
							notif = new NotificationCompat.Builder(getApplicationContext())
									.setContentTitle("asdf")
									.setContentText("ffff")
									.setTicker("¾Ë¸²")
									.setAutoCancel(true)
									.build();
						}
					}
				}
			});
		}
	}

	//Change TextView
	Handler mHandler = new Handler();
	private void sText(TextView tv, CharSequence text){
		final TextView ftv = tv;
		final CharSequence ftext = text;
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				ftv.setText(ftext);
			}
		});
		
	}

	//calc Discomfort index
	private void sDiscomfortable(int i){
		int Td = i%100;
		int humid = ((i-i%100)/100);

		final int fi = (int)(0.81*Td + 0.01*humid*(0.99*Td - 14.3) + 46.3);
		mHandler.post(new Runnable() {
		
			@Override
			public void run() {
				if(fi>=65 && fi<=70){
					//1
					intentActvity = 1;
					face1.setVisibility(View.VISIBLE);
					face2.setVisibility(View.INVISIBLE);
					face3.setVisibility(View.INVISIBLE);
					face4.setVisibility(View.INVISIBLE);
					face5.setVisibility(View.INVISIBLE);
					
				} else if((fi>=60 && fi<65) || (fi>70 && fi<=75)){
					//2
					intentActvity = 2;
					face1.setVisibility(View.INVISIBLE);
					face2.setVisibility(View.VISIBLE);
					face3.setVisibility(View.INVISIBLE);
					face4.setVisibility(View.INVISIBLE);
					face5.setVisibility(View.INVISIBLE);
				} else if(fi>75 && fi<80){
					//3
					intentActvity = 3;
					face1.setVisibility(View.INVISIBLE);
					face2.setVisibility(View.INVISIBLE);
					face3.setVisibility(View.VISIBLE);
					face4.setVisibility(View.INVISIBLE);
					face5.setVisibility(View.INVISIBLE);
				} else if((fi>55 && fi<60) || (fi>=80 && fi<85)){
					//4
					intentActvity = 4;
					face1.setVisibility(View.INVISIBLE);
					face2.setVisibility(View.INVISIBLE);
					face3.setVisibility(View.INVISIBLE);
					face4.setVisibility(View.VISIBLE);
					face5.setVisibility(View.INVISIBLE);
				} else if(fi<=55 || fi>=85 ){
					//5
					intentActvity = 5;
					vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
					vibrator.vibrate(vibratorTime);
					face1.setVisibility(View.INVISIBLE);
					face2.setVisibility(View.INVISIBLE);
					face3.setVisibility(View.INVISIBLE);
					face4.setVisibility(View.INVISIBLE);
					face5.setVisibility(View.VISIBLE);
				}
				
			}
		});
	}
	
	public void OnClickClose1(View v){
		setEnabledUi(false);
		mSerial.close();
	}
	
	private void setEnabledUi(Boolean on){
		if(on){
			btOpen.setEnabled(false);
			btClose.setEnabled(true);
		} else {
			btOpen.setEnabled(true);
			btClose.setEnabled(false);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 menu.add(Menu.NONE, MENU_ID_RECOMMEND, Menu.NONE, "cosmetics");

	     
	     return super.onCreateOptionsMenu(menu);
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ID_RECOMMEND:
            	Intent intent = new Intent(this, RecommendItem.class);
            	intent.putExtra("intentActivity", intentActvity);
            	startActivityForResult(intent, 1);
                return true;

            default:
                return false;
        }
    }
	
	@Override
	public void onPause(){
		mSerial.close();
		setEnabledUi(false);
		super.onPause();
	}
	
	@Override
	public void onDestroy(){
		mSerial.close();
		super.onDestroy();
	}
	
	@Override
	public void onStop(){
		mSerial.close();
		setEnabledUi(false);
		super.onStop();
	}
	
	@Override
	public void onResume(){
		btOpen = (Button)findViewById(R.id.btOpen1);
	    btClose = (Button)findViewById(R.id.btClose1);
		super.onResume();
	}
	
	public void onStart(){
		btOpen = (Button)findViewById(R.id.btOpen1);
	    btClose = (Button)findViewById(R.id.btClose1);
		super.onStart();
	}
}
