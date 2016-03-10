package com.huntgame.Friends;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huntgame.Main.R;
import com.huntgame.UtilitiyFile.AppPreferences;
import com.huntgame.UtilitiyFile.JsonClass;
import com.huntgame.UtilitiyFile.StaticValues;
import com.project.LazyLoading.ImageLoader;

public class FriendsHome_Page extends Activity {

	ListView list_userslist;
	JsonClass JsonClass_OBJ;
	Animation rotation;

	ArrayList<String> UserName_Arr = new ArrayList<String>();
	ArrayList<String> UserImagePath_Arr = new ArrayList<String>();
	ArrayList<String> UserID_Arr = new ArrayList<String>();

	ArrayList<String> Sort_UserName_Arr = new ArrayList<String>();
	ArrayList<String> Sort_UserImagePath_Arr = new ArrayList<String>();
	ArrayList<String> Sort_UserID_Arr = new ArrayList<String>();

	String Status;
	ImageLoader imageLoader;
	myAdapter MyAdapter;
	AppPreferences appPrefs;
	RelativeLayout btn_heading, search_heading;
	EditText searchtxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friendshome_page);

		list_userslist = (ListView) findViewById(R.id.list_userslist);
		btn_heading = (RelativeLayout) findViewById(R.id.btn_heading_lay);
		search_heading = (RelativeLayout) findViewById(R.id.textsearch_heading_lay);
		searchtxt = (EditText) findViewById(R.id.searchtext);
		appPrefs = new AppPreferences(getApplicationContext(), "SplashScreen");

		if (appPrefs.getData("FriendsHome_Page").equals("friendslist")) {
			btn_heading.setVisibility(View.GONE);
		} else {
			search_heading.setVisibility(View.GONE);
		}
		JsonClass_OBJ = new JsonClass();

		rotation = AnimationUtils
				.loadAnimation(this, R.anim.clockwise_rotation); // progress
		new asynctask().execute();

		searchtxt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				SortingData(searchtxt.getText().toString());
			}
		});

		list_userslist.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(),
						Sort_UserName_Arr.get(arg2), 1000).show();

				Intent i = new Intent(FriendsHome_Page.this,
						CurrentGameMoreInvite.class);

				System.out.println("int" + arg2);
				Bundle b = new Bundle();
				b.putString("NewUserID", UserID_Arr.get(arg2));
				b.putString("NewUserName", UserName_Arr.get(arg2));

				i.putExtras(b);
				startActivity(i);

			}
		});

	}

	public void friends_requests_click(View v) {

		startActivity(new Intent(FriendsHome_Page.this,
				Friends_Request_List.class));
	}

	public void users_click(View v) {

		startActivity(new Intent(FriendsHome_Page.this, Users_List.class));
	}

	class asynctask extends AsyncTask<Void, Void, Void> {

		Dialog dialog = new Dialog(FriendsHome_Page.this);

		public asynctask() {
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

			rotation = AnimationUtils.loadAnimation(FriendsHome_Page.this,
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

			JsonClass_OBJ.setAPI(StaticValues.UrlLink
					+ "viewFriendJson.php?userId="
					+ appPrefs.getData("USER_ID"));
			System.out.println(StaticValues.UrlLink
					+ "viewFriendJson.php?userId="
					+ appPrefs.getData("USER_ID"));

			String S = JsonClass_OBJ.getAPI();
			System.out.println(S);
			test();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			loadadapter();
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}

		}

	}

	public void loadadapter() {
		MyAdapter = new myAdapter(this, R.layout.users_list_adapter,
				UserName_Arr, UserImagePath_Arr);
		list_userslist.setAdapter(MyAdapter);

	}

	public void test() {

		try {
			JSONObject jobj = new JSONObject(JsonClass_OBJ.getAPI());

			JSONObject subObj;

			JSONArray array = new JSONArray();
			JSONObject jobjsub = new JSONObject();

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

								UserName_Arr.add(subObj.getString("userName"));

							}

							if (subObj.has("image")) {

								UserImagePath_Arr
										.add(subObj.getString("image"));

							}

							if (subObj.has("userId")) {

								UserID_Arr.add(subObj.getString("userId"));

							}

						}

					}

				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		Sort_UserName_Arr.addAll(UserName_Arr);
		Sort_UserImagePath_Arr.addAll(UserImagePath_Arr);
		Sort_UserID_Arr.addAll(UserID_Arr);

	}

	class myAdapter extends ArrayAdapter<String> {

		private final Context context;
		private LayoutInflater vi = null;
		ImageLoader imageLoader;
		ArrayList<String> Name_Arr = new ArrayList<String>();
		ArrayList<String> ImagePath = new ArrayList<String>();

		public myAdapter(Context context, int textViewResourceId,
				ArrayList<String> Name_Arr, ArrayList<String> ImagePath) {
			super(context, textViewResourceId, Name_Arr);
			this.context = context;
			this.Name_Arr = Name_Arr;
			this.ImagePath = ImagePath;

			imageLoader = new ImageLoader(context);

			vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				v = vi.inflate(R.layout.users_list_adapter, null);
			}
			int Imagecheck_flag = 0;

			TextView title = (TextView) v.findViewById(R.id.heading_txt);
			// TextView ImageUser = (TextView)
			// vi.findViewById(R.id.list_images);

			title.setText(Name_Arr.get(position));

			// email.setText(lists.get("description"));

			imageLoader.DisplayImage(ImagePath.get(position),
					(ImageView) v.findViewById(R.id.list_images));

			if (position % 2 == 0) {
				v.setBackgroundColor(Color.parseColor("#55d3d3d3"));
			} else {
				v.setBackgroundColor(Color.parseColor("#99ffffff"));
			}

			return v;
		}

	}

	private void SortingData(CharSequence s) {
		Sort_UserName_Arr.clear();
		Sort_UserImagePath_Arr.clear();
		// TODO Auto-generated method stub

		for (int i = 0; i < UserName_Arr.size(); i++) {

			if (UserName_Arr.get(i).toLowerCase()
					.contains(s.toString().toLowerCase())) {
				Sort_UserName_Arr.add(UserName_Arr.get(i));
				Sort_UserImagePath_Arr.add(UserImagePath_Arr.get(i));

			}

		}
		MyAdapter = new myAdapter(this, R.layout.users_list_adapter,
				Sort_UserName_Arr, Sort_UserImagePath_Arr);
		list_userslist.setAdapter(MyAdapter);

	}
}
