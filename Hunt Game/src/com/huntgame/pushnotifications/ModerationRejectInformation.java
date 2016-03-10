package com.huntgame.pushnotifications;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.huntgame.Main.HomePage;
import com.huntgame.Main.R;
import com.huntgame.Main.SeeCapture;
import com.huntgame.UtilitiyFile.JsonClass;
import com.project.LazyLoading.ImageLoader;

public class ModerationRejectInformation extends Activity {

	TextView HunterNameTXT, FugitiveNameTXT, GameNameTXT, MessageTXT;
	ImageView HunterImage, FugitiveImage;

	String HunterName_S, FugitiveName_S, GameName_S, HunterImage_S,
			FugitiveImage_S, CaptureImage_S, HunterID, FugitiveID, GameID,
			Message;
	ImageLoader imageloader_obj;
	Animation rotation;
	String response;
	JsonClass JsonClass_OBJ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.moderation_reject_information);

		rotation = AnimationUtils
				.loadAnimation(this, R.anim.clockwise_rotation);
		rotation.setRepeatCount(Animation.INFINITE);
		JsonClass_OBJ = new JsonClass();

		HunterNameTXT = (TextView) findViewById(R.id.hunterUserName);
		GameNameTXT = (TextView) findViewById(R.id.game_Name);
		FugitiveNameTXT = (TextView) findViewById(R.id.fugitivUserName);
		HunterImage = (ImageView) findViewById(R.id.hunter_img);
		FugitiveImage = (ImageView) findViewById(R.id.fugitiveimg);
		MessageTXT = (TextView) findViewById(R.id.msg_from_fugitive_txt);

		HunterName_S = getIntent().getExtras().getString("HunterName");
		HunterImage_S = getIntent().getExtras().getString("HunterImage");
		HunterID = getIntent().getExtras().getString("HunterID");

		GameName_S = getIntent().getExtras().getString("GameName");
		GameID = getIntent().getExtras().getString("GameID");

		FugitiveName_S = getIntent().getExtras().getString("FugitiveName");
		FugitiveImage_S = getIntent().getExtras().getString("FugitiveImage");
		FugitiveID = getIntent().getExtras().getString("FugitiveID");

		CaptureImage_S = getIntent().getExtras().getString("CaptureImage");

		Message = getIntent().getExtras().getString("CaptureImage");

		HunterNameTXT.setText(HunterName_S);
		FugitiveNameTXT.setText(FugitiveName_S);
		GameNameTXT.setText(GameName_S);
		MessageTXT.setText(Message);

		imageloader_obj = new ImageLoader(ModerationRejectInformation.this);

		imageloader_obj.DisplayImage(HunterImage_S, HunterImage);
		imageloader_obj.DisplayImage(FugitiveImage_S, FugitiveImage);

	}

	public void seecapture(View v) {
		Intent i = new Intent(ModerationRejectInformation.this,
				SeeCapture.class);
		Bundle b = new Bundle();
		b.putString("FullImagePath", CaptureImage_S);
		i.putExtras(b);
		startActivity(i);

	}

	class LoadDataJson extends AsyncTask<Void, Integer, Void> {

		Dialog dialog = new Dialog(ModerationRejectInformation.this);

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
			this.dialog.setContentView(R.layout.loadingicon);
			ImageView img = (ImageView) this.dialog
					.findViewById(R.id.progressBar1);
			img.setBackgroundResource(R.drawable.progress_img);

			rotation = AnimationUtils
					.loadAnimation(ModerationRejectInformation.this,
							R.anim.clockwise_rotation);
			rotation.setRepeatCount(Animation.INFINITE);
			
			this.dialog.show();
			img.startAnimation(rotation);
			this.dialog.setCanceledOnTouchOutside(false);

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			// JsonClass_OBJ.setAPI(StaticValues.UrlLink
			// + "view_game_name.php?userId=1");

			JsonClass_OBJ
					.setAPI("http://www.sicsglobal.com/projects/App_projects/hunt/accept_caughted.php?gameId="
							+ GameID
							+ "&hunterId="
							+ HunterID
							+ "&fujitiveId="
							+ FugitiveID + "&response=" + response);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (this.dialog.isShowing())
				this.dialog.dismiss();

			Intent intent = new Intent(getApplicationContext(), HomePage.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

		}

	}

	public void CaptureInvalid(View v) {
		response = "1";
		new LoadDataJson().execute();

	}

	public void CaptureStands(View v) {
		response = "0";

		new LoadDataJson().execute();

	}

}
