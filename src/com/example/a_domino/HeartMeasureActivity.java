package com.example.a_domino;

import com.physicaloid.lib.Physicaloid;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class HeartMeasureActivity extends Activity {
	VideoView scanVideoView;
	Button btnScan;
	Handler mHandler;
	
	Physicaloid mSerial;
	
	boolean bScanning = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_heart);
		
		mSerial = new Physicaloid(this);
		mHandler = new Handler();
		
		getActionBar().hide();
		
		scanVideoView = (VideoView)findViewById(R.id.scan_video_view_heart);
		
		// 비디오뷰를 커스텀하기 위해서 미디어컨트롤러 객체 생성
		MediaController mediaController = new MediaController(this);
		mediaController.setAnchorView(scanVideoView);
		final Uri video = Uri.parse("android.resource://" + getPackageName() + "/raw/heart_portrait");
		scanVideoView.setVideoURI(video);
		
		
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				scanVideoView.setVideoURI(video);
				scanVideoView.start();
				
			}
		}, 5000);
		
	}
}