package com.huntgame.pushnotifications;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huntgame.Main.R;
import com.project.LazyLoading.ImageLoader;

public class UserAcceptRejectGame extends Activity {

	TextView Username, GameName, Message;
	ImageView UserImage;
	String Username_S, GameName_S, Image_S, Response_S;
	ImageLoader imageloader_obj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_accept_reject_game);

		Username_S = getIntent().getExtras().getString("username");
		GameName_S = getIntent().getExtras().getString("gamename");
		Image_S = getIntent().getExtras().getString("userimage");
		Response_S = getIntent().getExtras().getString("response");

		Username = (TextView) findViewById(R.id.UserName);
		GameName = (TextView) findViewById(R.id.GameName);
		Message = (TextView) findViewById(R.id.message);
		UserImage = (ImageView) findViewById(R.id.user_img);
		imageloader_obj = new ImageLoader(UserAcceptRejectGame.this);

		Username.setText(Username_S);
		GameName.setText(GameName_S);

		if (Response_S.equals("1"))
			Message.setText(Username_S + " accepted the game request");

		if (Response_S.equals("2"))
			Message.setText(Username_S + " rejected the game request");

		imageloader_obj.DisplayImage(Image_S, UserImage);

	}

	public void close_btn(View v) {
		finish();
	}
	

}
