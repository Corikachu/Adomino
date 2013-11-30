package com.example.a_domino;

import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.physicaloid.lib.Physicaloid;
import com.physicaloid.lib.usb.driver.uart.ReadLisener;

public class MainActivity extends Activity {

    private static final int MENU_ID_HEART_MEASURE  = 0;
    private static final int MENU_ID_DHT11   	    = 1;
	
	ImageButton btSkin, btHeart;
	ImageView mainView;
	Physicaloid mPhysicaloid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mainView = (ImageView)findViewById(R.id.main);
		btSkin = (ImageButton)findViewById(R.id.skin);
		btHeart = (ImageButton)findViewById(R.id.heart);
		
		mPhysicaloid = new Physicaloid(this);
	}
    

	public void skinButton(View v){
		Intent intent = new Intent(this, Dht11.class);
    	startActivityForResult(intent, 1);
	}
	
	public void heartButton(View v){
		Intent mintent = new Intent(this, HeartMeasureActivity.class);
    	startActivityForResult(mintent, 1);
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	     menu.add(Menu.NONE, MENU_ID_DHT11, Menu.NONE, "온습도 센서");
	     menu.add(Menu.NONE, MENU_ID_HEART_MEASURE, Menu.NONE, "Heart rate graph");

	     return super.onCreateOptionsMenu(menu);
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ID_DHT11:
            	Intent intent = new Intent(this, Dht11.class);
            	startActivityForResult(intent, 1);
                return true;
            case MENU_ID_HEART_MEASURE:
            	Intent mintent = new Intent(this, HeartMeasureActivity.class);
            	startActivityForResult(mintent, 1);
                return true;
            default:
                return false;
        }
    }
	

	
	Handler mHandler = new Handler();
	private void tvAppend(TextView tv, CharSequence text){
		final TextView ftv = tv;
		final CharSequence ftext = text;
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				ftv.append(ftext);
			}
		});
		
	}
	

	
	@Override
	public void onPause(){
		mPhysicaloid.close();
		super.onPause();
	}
	
	@Override
	public void onDestroy(){
		mPhysicaloid.close();
		super.onDestroy();
	}
	
	@Override
	public void onStop(){
		mPhysicaloid.close();
		super.onStop();
	}
}
