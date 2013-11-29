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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.physicaloid.lib.Physicaloid;
import com.physicaloid.lib.usb.driver.uart.ReadLisener;

public class MainActivity extends Activity {

    private static final int MENU_ID_HEART_MEASURE  = 0;
    private static final int MENU_ID_DHT11   	    = 1;
	
	Button btOpen, btClose, btRead;
	TextView tvRead;
	ScrollView mSvText;

	Physicaloid mPhysicaloid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btOpen = (Button)findViewById(R.id.btOpen);
		btClose = (Button)findViewById(R.id.btClose);
		btRead = (Button)findViewById(R.id.btRead);
		tvRead = (TextView)findViewById(R.id.tvRead);
		mSvText = (ScrollView)findViewById(R.id.svText);
		
		
		setEnabledUi(false);
		
		mPhysicaloid = new Physicaloid(this);
	}
    

	
	public void onClickOpen(View v){
		if(mPhysicaloid.open()){
			setEnabledUi(true);
			
			mPhysicaloid.addReadListener(new ReadLisener() {
				String readStr;
				
				@Override
				public void onRead(int size) {
					byte[] buf = new byte[size];
					
					mPhysicaloid.read(buf, size);
					try{
						readStr = new String(buf, "UTF-8");
					} catch(UnsupportedEncodingException e){
						Log.e("TAG", e.toString());
						return;
					}
					tvAppend(tvRead, readStr);
					mSvText.fullScroll(ScrollView.FOCUS_DOWN);
				}
			});
		}
	}

	public void onClickClose(View v){
		if(mPhysicaloid.close()){
			setEnabledUi(false);
		}
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
	
    private void openUsbSerial() {
        if(mPhysicaloid == null) {
            Toast.makeText(this, "cannot open", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!mPhysicaloid.isOpened()) {
            }
            if (!mPhysicaloid.open()) {
                Toast.makeText(this, "cannot open", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();
                setEnabledUi(true);
        			
        		mPhysicaloid.addReadListener(new ReadLisener() {
        			String readStr;
        				
	        		@Override
	        		public void onRead(int size) {
	        			byte[] buf = new byte[size];
	        					
	        			mPhysicaloid.read(buf, size);
	        			try{
	        				readStr = new String(buf, "UTF-8");
	        			} catch(UnsupportedEncodingException e){
	        				Log.e("TAG", e.toString());
	        				return;
	        			}
	       				tvAppend(tvRead, readStr);
	       				mSvText.fullScroll(ScrollView.FOCUS_DOWN);
	        		}	
        		});
        		
            }
    }
    
    private void closeUsbSerial() {
        setEnabledUi(false);
       
        mPhysicaloid.close();
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
	
	private void setEnabledUi(boolean on){
		if(on){
			btOpen.setEnabled(false);
			btClose.setEnabled(true);
			btRead.setEnabled(true);
			tvRead.setEnabled(true);
		} else{
			btOpen.setEnabled(true);
			btClose.setEnabled(false);
			btRead.setEnabled(false);
			tvRead.setEnabled(false);
		}
	}
	
}
