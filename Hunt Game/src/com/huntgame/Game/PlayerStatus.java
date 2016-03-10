package com.huntgame.Game;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huntgame.Main.R;
import com.huntgame.UtilitiyFile.AppPreferences;
import com.huntgame.UtilitiyFile.JsonClass;

public class PlayerStatus extends Activity {
	Animation rotation;
	JsonClass JsonClass_OBJ;
	ArrayList<String> GameNameArr = new ArrayList<String>();
	ArrayList<String> EndingDateArr = new ArrayList<String>();
	ArrayList<String> PlayerStatusArr = new ArrayList<String>();
	ArrayList<String> PlayerNameArr = new ArrayList<String>();

	AppPreferences appPrefs;
	ListView lv;

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

		new LoadDataJson().execute();
		// lv.setOnItemClickListener(new OnItemClickListener() {
		//
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3) {
		// Intent i;
		// Bundle b = new Bundle();
		// if (GamePlayerStatusArr.get(arg2).equals("Pending")) {
		// i = new Intent(PlayerStatus.this, ViewGameRequestPage.class);
		//
		// } else {
		//
		// i = new Intent(PlayerStatus.this, ViewGameDetails.class);
		// b.putString("GameStatus", GameStatusArr.get(arg2));
		// }
		//
		// b.putString("GameID", GameIDArr.get(arg2));
		//
		// i.putExtras(b);
		// startActivity(i);
		//
		// }
		// });

	}

	class LoadDataJson extends AsyncTask<Void, Integer, Void> {

		Dialog dialog = new Dialog(PlayerStatus.this);

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
			this.dialog.setContentView(R.layout.loadingicon);
			ImageView img = (ImageView) this.dialog
					.findViewById(R.id.progressBar1);
			img.setBackgroundResource(R.drawable.progress_img);

			rotation = AnimationUtils.loadAnimation(PlayerStatus.this,
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

			System.out
					.println("view game"
							+ "http://www.sicsglobal.com/projects/App_projects/hunt/ViewPlayerStatus.php?userId="
							+ appPrefs.getData("USER_ID"));
			JsonClass_OBJ
					.setAPI("http://www.sicsglobal.com/projects/App_projects/hunt/ViewPlayerStatus.php?userId="
							+ appPrefs.getData("USER_ID"));
			getdata();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			System.out.println("one");
			lv.setAdapter(new Myadpter(getApplicationContext(), GameNameArr,
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
			super(context, R.layout.playerstatus_adapter, GameName);
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
			rowView = inflater.inflate(R.layout.playerstatus_adapter, parent,
					false);
			System.out.println("test");
			// notifyDataSetChanged();

			TextView gamename_txt = (TextView) rowView
					.findViewById(R.id.gamename_txt);
			TextView Enddate_txt = (TextView) rowView
					.findViewById(R.id.calender_date_end);
			TextView Endtime_txt = (TextView) rowView
					.findViewById(R.id.clock_time_end);
			TextView playerstatus_txt = (TextView) rowView
					.findViewById(R.id.playerstatus_txt);
			//
			gamename_txt.setText(GameNameArr.get(position));
			Enddate_txt.setText(EndingDateArr.get(position).subSequence(0, 10));
			Endtime_txt
					.setText(EndingDateArr.get(position).subSequence(11, 19));
			playerstatus_txt.setText(PlayerStatusArr.get(position));

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

				if (jobjsub.has("GameName")) {

					GameNameArr.add(jobjsub.getString("GameName"));

				}
				if (jobjsub.has("EndingDate")) {

					EndingDateArr.add(jobjsub.getString("EndingDate"));

				}
				if (jobjsub.has("PlayerName")) {

					PlayerNameArr.add(jobjsub.getString("PlayerName"));

				}

				if (jobjsub.has("PlayerStatus")) {

					PlayerStatusArr.add(jobjsub.getString("PlayerStatus"));

				}

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
