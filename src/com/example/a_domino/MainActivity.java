package com.example.a_domino;

import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.physicaloid.lib.Physicaloid;
import com.physicaloid.lib.usb.driver.uart.ReadLisener;

public class MainActivity extends Activity {

    private static final int MENU_ID_HEART_MEASURE  = 0;
    private static final int MENU_ID_DHT11   	    = 1;
    private static final int MENU_ID_CALORIE		= 2;
	
	ImageButton btSkin, btHeart, btCalorie;
	ImageView mainView;
	Physicaloid mPhysicaloid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mainView = (ImageView)findViewById(R.id.main);
		btSkin = (ImageButton)findViewById(R.id.skin);
		btHeart = (ImageButton)findViewById(R.id.heart);
		btCalorie = (ImageButton)findViewById(R.id.calorie);
		
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
	
	public void calorieButton(View v){
		Intent cintent = new Intent(this, CalorieActivity.class);
		startActivityForResult(cintent, 1);
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
            	Intent dintent = new Intent(this, Dht11.class);
            	startActivityForResult(dintent, 1);
                return true;
            case MENU_ID_HEART_MEASURE:
            	Intent hintent = new Intent(this, HeartMeasureActivity.class);
            	startActivityForResult(hintent, 1);
                return true;
            case MENU_ID_CALORIE:
            	Intent cintetn = new Intent(this, CalorieActivity.class);
            	startActivityForResult(cintetn, 1);
            default:
                return false;
        }
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
