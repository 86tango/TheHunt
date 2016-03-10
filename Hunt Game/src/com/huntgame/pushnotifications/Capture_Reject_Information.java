package com.huntgame.pushnotifications;

import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huntgame.Main.R;
import com.project.LazyLoading.ImageLoader;

public class Capture_Reject_Information extends Activity {

	TextView Username, GameName, Message;
	ImageView UserImage;
	String Username_S, GameName_S, Image_S, Response_S, Message_S;
	ImageLoader imageloader_obj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_accept_reject_game);

		Username_S = getIntent().getExtras().getString("FugitiveName");
		GameName_S = getIntent().getExtras().getString("GameName");
		Image_S = getIntent().getExtras().getString("FujitiveImage_path");
		Message_S = getIntent().getExtras().getString("description");

		Username = (TextView) findViewById(R.id.UserName);
		GameName = (TextView) findViewById(R.id.GameName);
		Message = (TextView) findViewById(R.id.message);
		UserImage = (ImageView) findViewById(R.id.user_img);
		imageloader_obj = new ImageLoader(Capture_Reject_Information.this);

		Username.setText(Username_S);
		GameName.setText(GameName_S);

		imageloader_obj.DisplayImage(Image_S, UserImage);
		TimeZone tz=new TimeZone() {
			
			@Override
			public boolean useDaylightTime() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void setRawOffset(int offsetMillis) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean inDaylightTime(Date time) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public int getRawOffset() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public int getOffset(int era, int year, int month, int day, int dayOfWeek,
					int timeOfDayMillis) {
				// TODO Auto-generated method stub
				return 0;
			}
		};

	}

	public void close_btn(View v) {
		finish();
	}
}
