package com.huntgame.Game;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.huntgame.Main.R;
import com.huntgame.UtilitiyFile.JsonClass;
import com.huntgame.UtilitiyFile.StaticValues;

public class ViewGameDetails extends Activity {
	String GameID;
	Animation rotation;
	JsonClass JsonClass_OBJ;

	String GameName, Radius, Title, StartingDate, EndingDate, GameType,
			Latitude, Longitude, ModeratorSatus, Location, Status, UserName;
	TextView UserStatusTXT, gameRadius_TXT, GameNameTXT, StartDateTXT,
			EndDateTXT, StartTimeTXT, EndTimeTXT, GameTypeTXT, GameLocationTXT,
			ModeratorName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewgamedetails);
		GameID = getIntent().getExtras().getString("GameID");
		Status = getIntent().getExtras().getString("GameStatus");

		System.out.println("GameID" + GameID);
		JsonClass_OBJ = new JsonClass();

		rotation = AnimationUtils
				.loadAnimation(this, R.anim.clockwise_rotation);
		rotation.setRepeatCount(Animation.INFINITE);

		UserStatusTXT = (TextView) findViewById(R.id.userstatus_txt);
		GameNameTXT = (TextView) findViewById(R.id.gamename_txt);
		gameRadius_TXT = (TextView) findViewById(R.id.gameRadius_TXT);
		StartDateTXT = (TextView) findViewById(R.id.start_date_txt);
		EndDateTXT = (TextView) findViewById(R.id.end_date_txt);
		StartTimeTXT = (TextView) findViewById(R.id.start_time_txt);
		EndTimeTXT = (TextView) findViewById(R.id.end_time_txt);
		GameTypeTXT = (TextView) findViewById(R.id.gametype_txt);
		GameLocationTXT = (TextView) findViewById(R.id.location_txt);
		ModeratorName = (TextView) findViewById(R.id.game_modertor_txt);

		new LoadDataJson().execute();

	}

	class LoadDataJson extends AsyncTask<Void, Integer, Void> {

		Dialog dialog = new Dialog(ViewGameDetails.this);

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
			this.dialog.setContentView(R.layout.loadingicon);
			ImageView img = (ImageView) this.dialog
					.findViewById(R.id.progressBar1);
			img.setBackgroundResource(R.drawable.progress_img);

			rotation = AnimationUtils.loadAnimation(ViewGameDetails.this,
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
					+ "view_game_details_json.php?gameid=" + GameID);

			JsonClass_OBJ.setAPI(StaticValues.UrlLink
					+ "view_game_details_json.php?gameid=" + GameID);
			getdata();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			GameNameTXT.setText(GameName);
			gameRadius_TXT.setText(Radius);
			UserStatusTXT.setText(Status);

			StartDateTXT.setText(StartingDate.subSequence(0, 10));

			EndDateTXT.setText(EndingDate.subSequence(0, 10));

			StartTimeTXT.setText(StartingDate.subSequence(11, 19));
			EndTimeTXT.setText(EndingDate.subSequence(11, 19));

			GameLocationTXT.setText(Location);
			GameTypeTXT.setText(GameType);
			if (Status.equals("owner")) {
				ModeratorName.setVisibility(View.INVISIBLE);
			}

			if (this.dialog.isShowing())
				this.dialog.dismiss();

		}
	}

	public void getdata() {

		try {

			System.out.println(JsonClass_OBJ.getAPI());
			JSONObject jobjsub = new JSONObject(JsonClass_OBJ.getAPI());

			if (jobjsub.has("title")) {

				GameName = jobjsub.getString("title");

			}
			if (jobjsub.has("Radius")) {

				Radius = jobjsub.getString("Radius");

			}

			if (jobjsub.has("startingDate")) {

				StartingDate = jobjsub.getString("startingDate");

			}
			if (jobjsub.has("endingDate")) {

				EndingDate = jobjsub.getString("endingDate");

			}
			if (jobjsub.has("gameType")) {

				GameType = jobjsub.getString("gameType");

			}
			if (jobjsub.has("moderatorStatus")) {

				ModeratorSatus = jobjsub.getString("moderatorStatus");

			}
			if (jobjsub.has("status")) {

				// Status = jobjsub.getString("status");

			}
			if (jobjsub.has("userName")) {

				UserName = jobjsub.getString("userName");

			}
			if (jobjsub.has("location")) {

				Location = jobjsub.getString("location");

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
