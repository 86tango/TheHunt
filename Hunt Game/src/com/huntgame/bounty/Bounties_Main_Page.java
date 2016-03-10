package com.huntgame.bounty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.huntgame.Main.R;
import com.huntgame.UtilitiyFile.AppPreferences;

public class Bounties_Main_Page extends Activity {

	ArrayList<HashMap<String, String>> category_map = new ArrayList<HashMap<String, String>>();
	GridView gridView;
	AppPreferences appPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bounties_main_page);
		gridView = (GridView) findViewById(R.id.gridview);
		appPrefs = new AppPreferences(getApplicationContext(), "SplashScreen");

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent in = new Intent(Bounties_Main_Page.this,
						Bounty_Profile.class);
				in.putExtra("userId", category_map.get(arg2).get("userId"));
				in.putExtra("GameName", category_map.get(arg2).get("title"));
				in.putExtra("GameId", category_map.get(arg2).get("game_id"));
				startActivity(in);
			}
		});
		new Item().execute();
	}

	public class Item extends AsyncTask<Object, Integer, Void> {

		public Item() {
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Object... arg0) {
			category();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			gridView.setAdapter(new Bounties_Main_Page_Adapter(
					Bounties_Main_Page.this, category_map));
		}

	}

	public void category() {
		// TODO Auto-generated method stub
		category_map.clear();
		String sublink = "http://www.sicsglobal.com/projects/App_projects/hunt/view_all_bounties.php?userId="
				+ appPrefs.getData("USER_ID");
		System.out.println("ppp" + sublink);
		URL obj_URL;
		try {
			obj_URL = new URL(sublink);
			try {
				InputStream in = obj_URL.openStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in));
				StringBuilder sb = new StringBuilder();
				String line = reader.readLine();
				while (line != null) {
					sb.append(line);
					line = reader.readLine();
				}
				reader.close();
				JSONArray jsonObject = new JSONArray(sb.toString());
				for (int i = 0; i < jsonObject.length(); i++) {
					JSONObject s = (JSONObject) jsonObject.getJSONObject(i);
					String game_id = s.getString("game_id");
					String title = s.getString("title");
					String userId = s.getString("userId");
					String userName = s.getString("userName");
					String image = s.getString("image");
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("image", image);
					map.put("userName", userName);
					map.put("title", title);
					map.put("userId", userId);
					map.put("game_id", game_id);
					category_map.add(map);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
