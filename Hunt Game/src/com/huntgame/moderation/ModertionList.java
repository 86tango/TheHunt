package com.huntgame.moderation;

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

public class ModertionList extends Activity {
	Animation rotation;
	JsonClass JsonClass_OBJ;
	ArrayList<String> HunterNameArr = new ArrayList<String>();
	ArrayList<String> HunterIDArr = new ArrayList<String>();
	ArrayList<String> HunterImgArr = new ArrayList<String>();

	ArrayList<String> FugitiveNameArr = new ArrayList<String>();
	ArrayList<String> FugitiveIDArr = new ArrayList<String>();
	ArrayList<String> FugitiveImgArr = new ArrayList<String>();

	ArrayList<String> GameNameArr = new ArrayList<String>();
	ArrayList<String> GameIDArr = new ArrayList<String>();

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
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
//				Intent i = new Intent(ModertionList.this, ModerationRejectInformation.class);
//
//				Bundle b = new Bundle();
//				b.putString("HunterName", HunterNameArr.get(arg2));
//				b.putString("HunterImage", HunterImgArr.get(arg2));
//				b.putString("FugitiveName", FugitiveNameArr.get(arg2));
//				b.putString("FugitiveImage", FugitiveImgArr.get(arg2));
//				b.putString("GameName", GameNameArr.get(arg2));
//				b.putString("CaptureImage", "capture imagepath");
//
//				i.putExtras(b);
//				startActivity(i);

			}
		});

	}

	class LoadDataJson extends AsyncTask<Void, Integer, Void> {

		Dialog dialog = new Dialog(ModertionList.this);

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
			this.dialog.setContentView(R.layout.loadingicon);
			ImageView img = (ImageView) this.dialog
					.findViewById(R.id.progressBar1);
			img.setBackgroundResource(R.drawable.progress_img);

			rotation = AnimationUtils.loadAnimation(ModertionList.this,
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
					+ "view_bounty_each_json.php?userId="
					+ appPrefs.getData("USER_ID"));

			JsonClass_OBJ.setAPI(StaticValues.UrlLink
					+ "view_bounty_each_json.php?userId="
					+ appPrefs.getData("USER_ID"));
			getdata();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			lv.setAdapter(new Myadpter(getApplicationContext(), GameIDArr, null));
			if (this.dialog.isShowing())
				this.dialog.dismiss();

		}

	}

	class Myadpter extends ArrayAdapter<String> {
		ArrayList<String> GameName = new ArrayList<String>();

		Context context;

		public Myadpter(Context context, ArrayList<String> GameName,
				ArrayList<Integer> listImgs) {
			super(context, R.layout.moderation_list_adapter, GameName);
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
			rowView = inflater.inflate(R.layout.moderation_list_adapter,
					parent, false);
			View v = (View) rowView.findViewById(R.id.view_left);

			if (position % 2 == 0) {
				v.setVisibility(View.GONE);
			} else {
				v.setVisibility(View.VISIBLE);
			}

			notifyDataSetChanged();

			TextView txt_type = (TextView) rowView.findViewById(R.id.txt);

			txt_type.setText(HunterNameArr.get(position) + " vs "
					+ FugitiveNameArr.get(position));

			return rowView;
		}
	}

	public void getdata() {

		try {

			JSONArray array = new JSONArray(JsonClass_OBJ.getAPI());

			System.out.println(JsonClass_OBJ.getAPI());
			JSONObject jobj = new JSONObject();
			JSONObject jobj_sub_sender, jobj_sub_reciver;

			for (int i = 0; i < array.length(); i++) {
				jobj = array.getJSONObject(i);

				if (jobj.has("sender"))

				{

					jobj_sub_sender = new JSONObject(jobj.getString("sender"));
					HunterNameArr.add(jobj_sub_sender.getString("userName"));
					HunterIDArr.add(jobj_sub_sender.getString("userId"));
					HunterImgArr.add(jobj_sub_sender.getString("image"));
					GameNameArr.add(jobj_sub_sender.getString("gameName"));
					GameIDArr.add(jobj_sub_sender.getString("gameId"));

				}

				if (jobj.has("reciver"))

				{
					jobj_sub_reciver = new JSONObject(jobj.getString("reciver"));
					FugitiveNameArr.add(jobj_sub_reciver.getString("userName"));
					FugitiveIDArr.add(jobj_sub_reciver.getString("userId"));
					FugitiveImgArr.add(jobj_sub_reciver.getString("image"));

				}

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
