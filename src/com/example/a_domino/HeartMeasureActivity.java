package com.example.a_domino;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class HeartMeasureActivity extends Activity {
	VideoView scanVideoView;
	Button btnScan;
	
	boolean bScanning = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_heart);
		
		getActionBar().hide();
		
		scanVideoView = (VideoView)findViewById(R.id.scan_video_view_heart);
		
		// 비디오뷰를 커스텀하기 위해서 미디어컨트롤러 객체 생성
		MediaController mediaController = new MediaController(this);
		mediaController.setAnchorView(scanVideoView);
		final Uri video = Uri.parse("android.resource://" + getPackageName() + "/raw/heart_portrait");
		scanVideoView.setVideoURI(video);
		
		btnScan = (Button)findViewById(R.id.btn_heart_start);
		btnScan.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				bScanning = !bScanning;
				if(bScanning) {
					btnScan.setText("측정 종료");
					scanVideoView.setVideoURI(video);
					scanVideoView.start();
				} else {
					btnScan.setText("측정 시작");
					scanVideoView.stopPlayback();
					
					HeartMeasureActivity.this.finish();
				}
			}
		});
		
		
	}


}