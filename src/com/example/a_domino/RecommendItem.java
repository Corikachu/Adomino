package com.example.a_domino;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class RecommendItem extends Activity {

	ImageView cos1, cos2, cos3, cos4, cos5;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.recommed_cos);
	    
	    cos1 = (ImageView)findViewById(R.id.cos1);
	    cos2 = (ImageView)findViewById(R.id.cos2);
	    cos3 = (ImageView)findViewById(R.id.cos3);
	    cos4 = (ImageView)findViewById(R.id.cos4);
	    cos5 = (ImageView)findViewById(R.id.cos5);
	    
	    int intentActivity = getIntent().getExtras().getInt("intentActivity");
	    
//	    Log.d("TAG", intentActivity);
	    
	    if(intentActivity == 1){
	    	cos1.setVisibility(View.VISIBLE);
	    	ImageClickLisnter(cos1, "http://www.nots.co.kr/product.nots?productId=40");
	    	
	    }
	    else if(intentActivity == 2){
	    	cos2.setVisibility(View.VISIBLE);
	    	ImageClickLisnter(cos2, "http://www.ellotte.com/selectitem/goodsDetail.ldpm?goodsNo=10017339&dispNo=X01A01A02&isdPathCd=02&isdPathDtlNo=X01A01A02#");
	    	
	    }
	    else if(intentActivity == 3){
	    	cos3.setVisibility(View.VISIBLE);
	    	ImageClickLisnter(cos3, "");
	    	
	    }
	    else if(intentActivity == 4){
	    	cos4.setVisibility(View.VISIBLE);
	    	ImageClickLisnter(cos4, "");
	    	
	    }
	    else if(intentActivity == 5){
	    	cos5.setVisibility(View.VISIBLE);
	    	ImageClickLisnter(cos5, "");
	
	    }
	}
	
	Handler mHandle = new Handler();
	private void ImageClickLisnter(ImageView v, String iv){
		final ImageView fv = v;
		final String webUrl = iv;
		mHandle.post(new Runnable() {
			@Override
			public void run() {
				fv.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl)));
					}
				});
			}
		});

	}
}
