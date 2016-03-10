package com.huntgame.pushnotifications;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huntgame.Main.HomePage;
import com.huntgame.Main.R;
import com.huntgame.Main.SeeCapture;
import com.project.LazyLoading.ImageLoader;

public class CaptureInformation extends Activity {

	TextView HunterName_txt, FugitiveName_txt, GameName_txt, Message_txt,
			description_txt;
	ImageView HunterImage, FugitiveImage;
	String GameName_S, HunterName_S, FugitiveName_S, Message_S,
			HunterImage_path, FugitiveImage_path, CaptureImage_path,
			description_S;
	ImageLoader imageloader_obj1, imageloader_obj2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.capture_information);

		GameName_S = getIntent().getExtras().getString("GameName");
		HunterName_S = getIntent().getExtras().getString("HunterName");
		FugitiveName_S = getIntent().getExtras().getString("FugitiveName");
		Message_S = getIntent().getExtras().getString("Message");
		description_S = getIntent().getExtras().getString("description");

		
		
		HunterImage_path = getIntent().getExtras()
				.getString("HunterImage_path");
		FugitiveImage_path = getIntent().getExtras().getString(
				"FujitiveImage_path");
		CaptureImage_path = getIntent().getExtras().getString(
				"CaptureImage_path");

		HunterName_txt = (TextView) findViewById(R.id.hunterUserName);
		FugitiveName_txt = (TextView) findViewById(R.id.fugitivUserName);
		GameName_txt = (TextView) findViewById(R.id.gameName);
		Message_txt = (TextView) findViewById(R.id.msg_txt);
		description_txt = (TextView) findViewById(R.id.description_txt);

		HunterImage = (ImageView) findViewById(R.id.hunter_img);
		FugitiveImage = (ImageView) findViewById(R.id.fugitiveimg);

		GameName_txt.setText(GameName_S);
		HunterName_txt.setText(HunterName_S);
		FugitiveName_txt.setText(FugitiveName_S);
		Message_txt.setText(Message_S);
		description_txt.setText(description_S);

		imageloader_obj1 = new ImageLoader(CaptureInformation.this);
		imageloader_obj2 = new ImageLoader(CaptureInformation.this);

		imageloader_obj1.DisplayImage(HunterImage_path, HunterImage);
		imageloader_obj2.DisplayImage(FugitiveImage_path, FugitiveImage);

	}

	public void close_btn(View v) {
		Intent intent = new Intent(getApplicationContext(), HomePage.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	public void seecapture_click(View v) {

		Intent i = new Intent(CaptureInformation.this, SeeCapture.class);
		Bundle b = new Bundle();
		b.putString("FullImagePath", CaptureImage_path);
		i.putExtras(b);
		startActivity(i);

	}
}
