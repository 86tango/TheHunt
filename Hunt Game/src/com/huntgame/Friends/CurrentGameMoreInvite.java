package com.huntgame.Friends;

import java.util.ArrayList;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huntgame.Main.R;
import com.huntgame.UtilitiyFile.AppPreferences;
import com.huntgame.UtilitiyFile.JsonClass;
import com.huntgame.UtilitiyFile.StaticValues;

public class CurrentGameMoreInvite extends Activity {
	Animation rotation;
	JsonClass JsonClass_OBJ;
	ArrayList<String> GameName_Arr = new ArrayList<String>();
	ArrayList<String> GameIDArr = new ArrayList<String>();
	ArrayList<String> GamePlayerStatusArr = new ArrayList<String>();
	ArrayList<String> GameStatusArr = new ArrayList<String>();

	// www.sicsglobal.com/projects/App_projects/hunt/insert_players_json.php?gameId=3&userid=17

	AppPreferences appPrefs;
	ListView lv;
	String NewUserId_S, NewUserName_S;

	// 8086531555
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.current_game);

		lv = (ListView) findViewById(R.id.game_type);
		JsonClass_OBJ = new JsonClass();
		appPrefs = new AppPreferences(getApplicationContext(), "SplashScreen");

		rotation = AnimationUtils
				.loadAnimation(this, R.anim.clockwise_rotation);
		rotation.setRepeatCount(Animation.INFINITE);
		NewUserId_S = getIntent().getExtras().getString("NewUserID");
		NewUserName_S = getIntent().getExtras().getString("NewUserName");

		new LoadDataJson().execute();
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent i = new Intent(CurrentGameMoreInvite.this,
						ViewGameDetails_MoreInvite.class);

				System.out.println("int" + arg2);
				Bundle b = new Bundle();
				b.putString("NewUserID", NewUserId_S);
				b.putString("NewUserName", NewUserName_S);
				b.putString("GameID", GameIDArr.get(arg2));
				i.putExtras(b);
				startActivity(i);

			}
		});

	}

	class LoadDataJson extends AsyncTask<Void, Integer, Void> {

		Dialog dialog = new Dialog(CurrentGameMoreInvite.this);

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
			this.dialog.setContentView(R.layout.loadingicon);
			ImageView img = (ImageView) this.dialog
					.findViewById(R.id.progressBar1);
			img.setBackgroundResource(R.drawable.progress_img);

			rotation = AnimationUtils.loadAnimation(CurrentGameMoreInvite.this,
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
					+ "view_game_name.php?userId=1");

			JsonClass_OBJ.setAPI(StaticValues.UrlLink
					+ "view_game_name.php?userid="
					+ appPrefs.getData("USER_ID"));
			getdata();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			lv.setAdapter(new Myadpter(getApplicationContext(), GameName_Arr,
					null));
			if (this.dialog.isShowing())
				this.dialog.dismiss();

		}

	}

	class Myadpter extends ArrayAdapter<String> {
		ArrayList<String> GameName = new ArrayList<String>();

		Context context;

		public Myadpter(Context context, ArrayList<String> GameName,
				ArrayList<Integer> listImgs) {
			super(context, R.layout.currentgame_adapter, GameName);
			// TODO Auto-generated constructor stub
			this.GameName = GameName;

			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View rowView = convertView;
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.currentgame_adapter, parent,
					false);
			View v = (View) rowView.findViewById(R.id.view_left);

			if (position % 2 == 0) {
				v.setVisibility(View.GONE);
			} else {
				v.setVisibility(View.VISIBLE);
			}

			notifyDataSetChanged();

			TextView txt_type = (TextView) rowView.findViewById(R.id.txt);

			if (GamePlayerStatusArr.get(position).equals("Pending")) {
				txt_type.setText(GamePlayerStatusArr.get(position) + " : "
						+ GameName.get(position));

			} else {
				txt_type.setText(GameStatusArr.get(position) + " : "
						+ GameName.get(position));

			}

			return rowView;
		}
	}

	public void getdata() {

		try {

			JSONArray array = new JSONArray(JsonClass_OBJ.getAPI());

			System.out.println(JsonClass_OBJ.getAPI());
			JSONObject jobjsub = new JSONObject();

			for (int i = 0; i < array.length(); i++) {
				jobjsub = array.getJSONObject(i);

				if (jobjsub.has("status")) {

					if (jobjsub.getString("status").equals("owner")) {
						GameStatusArr.add(jobjsub.getString("status"));
						if (jobjsub.has("title")) {

							GameName_Arr.add(jobjsub.getString("title"));

						}
						if (jobjsub.has("game_id")) {

							GameIDArr.add(jobjsub.getString("game_id"));

						}
						if (jobjsub.has("request_status")) {

							GamePlayerStatusArr.add(jobjsub
									.getString("request_status"));

						}

					}

				}

			}

			System.out.println(GameName_Arr);

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
