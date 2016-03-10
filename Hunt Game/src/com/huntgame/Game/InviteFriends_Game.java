package com.huntgame.Game;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huntgame.Main.HomePage;
import com.huntgame.Main.R;
import com.huntgame.UtilitiyFile.AppPreferences;
import com.huntgame.UtilitiyFile.JsonClass;
import com.huntgame.UtilitiyFile.StaticValues;
import com.project.LazyLoading.ImageLoader;

public class InviteFriends_Game extends Activity {

	ListView list_userslist;
	JsonClass JsonClass_OBJ;
	Animation rotation;
	AppPreferences appPrefs;
	TextView GameName_Txt;

	String Status, userList_S, GameID, GameName;
	AdapterClass AdapterClass_Obj;
	ArrayList<String> UserName = new ArrayList<String>();
	ArrayList<String> UserID = new ArrayList<String>();

	ArrayList<String> UserImagePath = new ArrayList<String>();
	ArrayList<String> SelectedBox = new ArrayList<String>();
	ArrayList<String> checkboxstate = new ArrayList<String>();
	ArrayList<String> UserId_Selected = new ArrayList<String>();
	int send_btn_flg = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.invite_friends_game);
		GameName = getIntent().getExtras().getString("GameName");

		GameID = getIntent().getExtras().getString("GameID");
		GameName_Txt = (TextView) findViewById(R.id.gamename_txt);
		GameName_Txt.setText("Game : " + GameName);

		list_userslist = (ListView) findViewById(R.id.list_userslists);
		JsonClass_OBJ = new JsonClass();

		appPrefs = new AppPreferences(getApplicationContext(), "SplashScreen");

		rotation = AnimationUtils
				.loadAnimation(this, R.anim.clockwise_rotation); // progress
		new LoadUsersJson().execute();

	}

	class LoadUsersJson extends AsyncTask<Void, Void, Void> {
		Dialog dialog = new Dialog(InviteFriends_Game.this);

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

			rotation = AnimationUtils.loadAnimation(InviteFriends_Game.this,
					R.anim.clockwise_rotation);
			rotation.setRepeatCount(Animation.INFINITE);

			this.dialog.show();
			img.startAnimation(rotation);
			this.dialog.setCanceledOnTouchOutside(false);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			this.dialog.setCanceledOnTouchOutside(false);

			if (send_btn_flg == 1) {
				JsonClass_OBJ.setAPI(StaticValues.UrlLink
						+ "insert_players_json.php?gameId=" + GameID
						+ "&userid=" + userList_S);

				System.out.println("ddh" + StaticValues.UrlLink
						+ "insert_players_json.php?gameId=" + GameID
						+ "&userid=" + userList_S);
			} else {
				JsonClass_OBJ.setAPI(StaticValues.UrlLink
						+ "viewFriendJson.php?userId="
						+ appPrefs.getData("USER_ID"));

			}

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

			if (send_btn_flg == 1) {
				send_btn_flg = 0;
				Toast.makeText(getApplicationContext(), "Game request send",
						1000).show();

				Intent intent = new Intent(getApplicationContext(),
						HomePage.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);

			} else {
				System.out.println("neeee");
				calAdapter();

			}

		}
	}

	public void calAdapter() {
		AdapterClass_Obj = new AdapterClass(this,
				R.layout.invite_friends_game_adapter, UserName);

		list_userslist.setAdapter(AdapterClass_Obj);
		System.out.println("nexaceee" + UserName);

	}

	public void getData() {

		try {
			JSONObject jobj = new JSONObject(JsonClass_OBJ.getAPI());

			JSONObject subObj;

			JSONArray array = new JSONArray();

			if (jobj.has("Result")) {

				Status = jobj.getString("Result");

				if (Status.equals("SUCCESS")) {

					if (jobj.has("friends")) // handle Se_wesam

					{

						array = jobj.getJSONArray("friends");

						for (int i = 0; i < array.length(); i++) {
							subObj = array.getJSONObject(i);
							HashMap<String, String> map = new HashMap<String, String>();
							if (subObj.has("userName")) {

								UserName.add(subObj.getString("userName"));

							}

							if (subObj.has("image")) {

								UserImagePath.add(subObj.getString("image"));

							}

							if (subObj.has("userId")) {

								UserID.add(subObj.getString("userId"));

							}

						}

					}

				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	class AdapterClass extends ArrayAdapter<String> {

		private final Context context;
		private LayoutInflater vi = null;
		ImageLoader imageLoader;

		CheckBox checkbox;
		ArrayList<String> UserName = new ArrayList<String>();

		public AdapterClass(Context context, int textViewResourceId,
				ArrayList<String> UserName) {
			super(context, textViewResourceId, UserName);
			this.context = context;
			this.UserName = UserName;

			imageLoader = new ImageLoader(context);
			vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			    System.out.println("inside adapter_cont");

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				v = vi.inflate(R.layout.invite_friends_game_adapter, null);
			}

			System.out.println("inside adapter_view");

			TextView Username_txt = (TextView) v.findViewById(R.id.usernames);

			checkbox = (CheckBox) v.findViewById(R.id.checkBox_page);
			imageLoader.DisplayImage(UserImagePath.get(position),
					(ImageView) v.findViewById(R.id.userpics));
			System.out.println(UserName.get(position));
			Username_txt.setText(UserName.get(position));
			final int i = position;

			checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked) {
						checkboxstate.add(Integer.valueOf(i).toString());
					} else {
						checkboxstate.remove(Integer.valueOf(i).toString());
					}
				}
			});

			return v;
		}

	}

	public void send_request(View v) {
		UserId_Selected.clear();
		send_btn_flg = 1;
		for (int element = 0; element < checkboxstate.size(); element++) {
			UserId_Selected.add(UserID.get(Integer.valueOf(checkboxstate
					.get(element))));

		}

		userList_S = UserId_Selected.toString().replace("[", "")
				.replace("]", "").replace(" ", "");
		System.out.println(userList_S);
		new LoadUsersJson().execute();
	}

}
