package com.huntgame.Friends;

import org.json.JSONArray;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huntgame.Main.R;
import com.huntgame.UtilitiyFile.JsonClass;
import com.huntgame.UtilitiyFile.StaticValues;

public class ViewGameDetails_MoreInvite extends Activity {
	String GameID;
	Animation rotation;
	JsonClass JsonClass_OBJ;

	String GameName, Title, StartingDate, EndingDate, GameType, Latitude,
			Longitude, ModeratorSatus, Location, Status, UserName;
	TextView UserStatusTXT, GameNameTXT, StartDateTXT, EndDateTXT,
			StartTimeTXT, EndTimeTXT, GameTypeTXT, GameLocationTXT,
			ModeratorName;
	Button invitefrnd_btn;
	String NewUserId_S, NewUserName_S;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewgamedetails);
		GameID = getIntent().getExtras().getString("GameID");
		System.out.println("GameID" + GameID);
		JsonClass_OBJ = new JsonClass();

		NewUserId_S = getIntent().getExtras().getString("NewUserID");
		NewUserName_S = getIntent().getExtras().getString("NewUserName");

		rotation = AnimationUtils
				.loadAnimation(this, R.anim.clockwise_rotation);
		rotation.setRepeatCount(Animation.INFINITE);

		invitefrnd_btn = (Button) findViewById(R.id.invitefrnd_btn);
		invitefrnd_btn.setVisibility(View.VISIBLE);
		UserStatusTXT = (TextView) findViewById(R.id.userstatus_txt);
		GameNameTXT = (TextView) findViewById(R.id.gamename_txt);
		StartDateTXT = (TextView) findViewById(R.id.start_date_txt);
		EndDateTXT = (TextView) findViewById(R.id.end_date_txt);
		StartTimeTXT = (TextView) findViewById(R.id.start_time_txt);
		EndTimeTXT = (TextView) findViewById(R.id.end_time_txt);
		GameTypeTXT = (TextView) findViewById(R.id.gametype_txt);
		GameLocationTXT = (TextView) findViewById(R.id.location_txt);
		ModeratorName = (TextView) findViewById(R.id.game_modertor_txt);

		invitefrnd_btn.setText("Invite " + NewUserName_S);

		new LoadDataJson().execute();

	}

	class LoadDataJson extends AsyncTask<Void, Integer, Void> {

		Dialog dialog = new Dialog(ViewGameDetails_MoreInvite.this);

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
			this.dialog.setContentView(R.layout.loadingicon);
			ImageView img = (ImageView) this.dialog
					.findViewById(R.id.progressBar1);
			img.setBackgroundResource(R.drawable.progress_img);

			rotation = AnimationUtils.loadAnimation(
					ViewGameDetails_MoreInvite.this, R.anim.clockwise_rotation);
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

				Status = jobjsub.getString("status");

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

	public void invitefrnd_btn(View v) {
		new LoadUsersJson().execute();
	}

	class LoadUsersJson extends AsyncTask<Void, Void, Void> {
		Dialog dialog = new Dialog(ViewGameDetails_MoreInvite.this);

		public LoadUsersJson() {
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onPreExecute() {

			System.out.println("sss");
			// TODO Auto-generated method stub
			this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
			this.dialog.setContentView(R.layout.loadingicon);
			ImageView img = (ImageView) this.dialog
					.findViewById(R.id.progressBar1);
			img.setBackgroundResource(R.drawable.progress_img);

			rotation = AnimationUtils.loadAnimation(
					ViewGameDetails_MoreInvite.this, R.anim.clockwise_rotation);
			rotation.setRepeatCount(Animation.INFINITE);

			this.dialog.show();
			img.startAnimation(rotation);
			this.dialog.setCanceledOnTouchOutside(false);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			this.dialog.setCanceledOnTouchOutside(false);

			JsonClass_OBJ.setAPI(StaticValues.UrlLink
					+ "insert_players_json.php?gameId=" + GameID + "&userid="
					+ NewUserId_S);

			System.out.println("ddh" + StaticValues.UrlLink
					+ "insert_players_json.php?gameId=" + GameID + "&userid="
					+ NewUserId_S);

			String S = JsonClass_OBJ.getAPI();
			System.out.println(S);

			getData();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub

			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}

			if (Status.equals("Already Exists")) {
				Toast.makeText(getApplicationContext(),
						UserName + " was already added to this game", 1000)
						.show();
			} else {
				Toast.makeText(getApplicationContext(), Status, 1000).show();
			}

		}
	}

	public void getData() {

		try {
			JSONObject jobj = new JSONObject(JsonClass_OBJ.getAPI());

			JSONArray array = new JSONArray();

			if (jobj.has("Result")) {

				Status = jobj.getString("Result");

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
