package com.huntgame.Game;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.Toast;

import com.huntgame.Main.HomePage;
import com.huntgame.Main.R;
import com.huntgame.UtilitiyFile.AppPreferences;
import com.huntgame.UtilitiyFile.JsonClass;
import com.huntgame.UtilitiyFile.StaticValues;

public class GameRequestPage extends Activity {

	String GameName, Title, GameID, StartingDate, EndingDate, GameType,
			Latitude, Longitude, ModeratorSatus, Location, Status, UserName;
	TextView GameNameTXT, StartDateTXT, EndDateTXT, StartTimeTXT, EndTimeTXT,
			GameTypeTXT, GameLocationTXT, ModeratorName;
	Animation rotation;
	AppPreferences appPrefs;

	JsonClass JsonClass_OBJ;
	String Result;
	String ResposeFlag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamerequestpage);
		appPrefs = new AppPreferences(getApplicationContext(), "SplashScreen");

		rotation = AnimationUtils
				.loadAnimation(this, R.anim.clockwise_rotation);
		rotation.setRepeatCount(Animation.INFINITE);

		JsonClass_OBJ = new JsonClass();

		GameName = getIntent().getExtras().getString("GameName");
		Title = getIntent().getExtras().getString("Title");
		GameID = getIntent().getExtras().getString("GameID");
		StartingDate = getIntent().getExtras().getString("StartingDate");
		EndingDate = getIntent().getExtras().getString("EndingDate");
		GameType = getIntent().getExtras().getString("GameType");
		Latitude = getIntent().getExtras().getString("Latitude");
		Longitude = getIntent().getExtras().getString("Longitude");
		ModeratorSatus = getIntent().getExtras().getString("ModeratorSatus");
		Location = getIntent().getExtras().getString("Location");
		Status = getIntent().getExtras().getString("Status");
		UserName = getIntent().getExtras().getString("UserName");

		GameNameTXT = (TextView) findViewById(R.id.gameName_txt);
		StartDateTXT = (TextView) findViewById(R.id.start_date_txt);
		EndDateTXT = (TextView) findViewById(R.id.end_date_txt);
		StartTimeTXT = (TextView) findViewById(R.id.start_time_txt);
		EndTimeTXT = (TextView) findViewById(R.id.end_time_txt);
		GameTypeTXT = (TextView) findViewById(R.id.gametype_txt);
		GameLocationTXT = (TextView) findViewById(R.id.location_txt);
		ModeratorName = (TextView) findViewById(R.id.game_modertor_txt);

		GameNameTXT.setText(GameName);
		GameTypeTXT.setText(GameType);
		GameLocationTXT.setText(Location);
		StartDateTXT.setText(StartingDate.subSequence(0, 10));

		EndDateTXT.setText(EndingDate.subSequence(0, 10));

		StartTimeTXT.setText(StartingDate.subSequence(11, 19));
		EndTimeTXT.setText(EndingDate.subSequence(11, 19));
		ModeratorName.setText("This Game is being moderated by " + UserName);

	}

	class LoadDataJson extends AsyncTask<Void, Integer, Void> {

		Dialog dialog = new Dialog(GameRequestPage.this);

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
			this.dialog.setContentView(R.layout.loadingicon);
			ImageView img = (ImageView) this.dialog
					.findViewById(R.id.progressBar1);
			img.setBackgroundResource(R.drawable.progress_img);

			rotation = AnimationUtils.loadAnimation(GameRequestPage.this,
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

			System.out.println(StaticValues.UrlLink
					+ "accept_reject_request_json.php?userId="
					+ appPrefs.getData("USER_ID") + "&gameId=" + GameID
					+ "&response=" + ResposeFlag);

			JsonClass_OBJ.setAPI(StaticValues.UrlLink
					+ "accept_reject_request_json.php?userId="
					+ appPrefs.getData("USER_ID") + "&gameId=" + GameID
					+ "&response=" + ResposeFlag);
			// http://www.sicsglobal.com/projects/App_projects/hunt/accept_reject_request_json.php?userId=1&gameId=1&response=1

			// getdata();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (this.dialog.isShowing())
				this.dialog.dismiss();

			if (ResposeFlag.equals("1")) {
				Toast.makeText(getApplicationContext(), "Game Added", 1000)
						.show();
				Intent intent = new Intent(getApplicationContext(),
						HomePage.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);

			}
			if (ResposeFlag.equals("2")) {
				Toast.makeText(getApplicationContext(), "Game Rejected", 1000)
						.show();
				Intent intent = new Intent(getApplicationContext(),
						HomePage.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);

			}
		}

	}

	public void getdata() {

		try {

			JSONObject jobjsub = new JSONObject(JsonClass_OBJ.getAPI());

			if (jobjsub.has("title")) {

				Result = jobjsub.getString("title");

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public void Accept_btn(View v) {

		ResposeFlag = "1";
		new LoadDataJson().execute();
	}

	public void cancel_btn(View v) {
		ResposeFlag = "2";
		new LoadDataJson().execute();
	}
}
