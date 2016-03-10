package com.huntgame.pushnotifications;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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

public class CaptureFugitiveAcceptReject extends Activity {

	TextView HunterNameTXT, FugitiveNameTXT, GameNameTXT, ModeratorNameTXT,
			MessaageTXT;
	ImageView HunterImage, FugitiveImage;

	String HunterName_S, FugitiveName_S, GameName_S, HunterImage_S,
			FugitiveImage_S, CaptureImage_S, ModeratorName_S, HunterID_S,
			GameID_S, FugitiveID_S, Message_S;
	ImageLoader imageloader_obj1, imageloader_obj2;
	Animation rotation;

	JsonClass JsonClass_OBJ;
	int response;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		rotation = AnimationUtils
				.loadAnimation(this, R.anim.clockwise_rotation);
		rotation.setRepeatCount(Animation.INFINITE);
		JsonClass_OBJ = new JsonClass();

		setContentView(R.layout.capture_fugitive_accept_reject);
		HunterNameTXT = (TextView) findViewById(R.id.hunterUserName);
		GameNameTXT = (TextView) findViewById(R.id.GameName);
		FugitiveNameTXT = (TextView) findViewById(R.id.fugitivUserName);
		HunterImage = (ImageView) findViewById(R.id.hunter_img);
		FugitiveImage = (ImageView) findViewById(R.id.fugitiveimg);
		ModeratorNameTXT = (TextView) findViewById(R.id.moderatorname);

		HunterName_S = getIntent().getExtras().getString("HunterName");
		GameName_S = getIntent().getExtras().getString("GameName");
		FugitiveName_S = getIntent().getExtras().getString("FugitiveName");
		HunterImage_S = getIntent().getExtras().getString("HunterImage");
		FugitiveImage_S = getIntent().getExtras().getString("FugitiveImage");
		CaptureImage_S = getIntent().getExtras().getString("CaptureImage");
		Message_S = getIntent().getExtras().getString("Message");

		ModeratorName_S = getIntent().getExtras().getString("ModeratorName");
		HunterID_S = getIntent().getExtras().getString("HunterID");
		FugitiveID_S = getIntent().getExtras().getString("FugitiveID");
		GameID_S = getIntent().getExtras().getString("GameID");

		HunterNameTXT.setText(HunterName_S);
		FugitiveNameTXT.setText(FugitiveName_S);
		GameNameTXT.setText(GameName_S);
		ModeratorNameTXT.setText("Game is moderated by " + ModeratorName_S);
		imageloader_obj1 = new ImageLoader(CaptureFugitiveAcceptReject.this);
		imageloader_obj2 = new ImageLoader(CaptureFugitiveAcceptReject.this);

		imageloader_obj1.DisplayImage(HunterImage_S, HunterImage);
		imageloader_obj2.DisplayImage(FugitiveImage_S, FugitiveImage);

	}

	public void seecapture_click(View v) {

		Intent i = new Intent(CaptureFugitiveAcceptReject.this,
				SeeCapture.class);
		Bundle b = new Bundle();
		b.putString("FullImagePath", CaptureImage_S);
		i.putExtras(b);
		startActivity(i);

	}

	public void Acceptbtn(View v) {
		response = 1;
		new LoadDataJson().execute();
	}

	public void Rejectbtn(View v) {
		response = 0;
		alertbox();

	}

	class LoadDataJson extends AsyncTask<Void, Integer, Void> {

		Dialog dialog = new Dialog(CaptureFugitiveAcceptReject.this);

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
			this.dialog.setContentView(R.layout.loadingicon);
			ImageView img = (ImageView) this.dialog
					.findViewById(R.id.progressBar1);
			img.setBackgroundResource(R.drawable.progress_img);

			rotation = AnimationUtils
					.loadAnimation(CaptureFugitiveAcceptReject.this,
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
							+ GameID_S
							+ "&hunterId="
							+ HunterID_S
							+ "&fujitiveId="
							+ FugitiveID_S
							+ "&response="
							+ response);

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

	public void alertbox() {

		AlertDialog.Builder altDialog = new AlertDialog.Builder(this);
		altDialog.setMessage("Final decision made by the modertor"); // here
																		// add
																		// your
																		// message
		altDialog.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						new LoadDataJson().execute();
					}
				});

		altDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		altDialog.show();

	}

}
