package com.huntgame.Main;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.project.LazyLoading.ImageLoader;

public class SeeCapture extends Activity {
	String ImagePath_S;
	ImageLoader imgloader_obj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seecapture);
		ImagePath_S = getIntent().getExtras().getString("FullImagePath");

		System.out.println("imageath" + ImagePath_S);
		imgloader_obj = new ImageLoader(this);
		imgloader_obj.DisplayImage(ImagePath_S,
				(ImageView) findViewById(R.id.capture_img));

	}

}
