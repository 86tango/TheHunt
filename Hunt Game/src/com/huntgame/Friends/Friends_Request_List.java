package com.huntgame.Friends;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huntgame.Main.R;
import com.huntgame.UtilitiyFile.AppPreferences;
import com.huntgame.UtilitiyFile.JsonClass;
import com.huntgame.UtilitiyFile.StaticValues;
import com.project.LazyLoading.ImageLoader;

public class Friends_Request_List extends Activity {

	ListView list_userslist;
	myAdapter MyAdapter;
	Animation rotation;
	ImageLoader imageLoader;
	JsonClass JsonClass_OBJ;
	String Status;
	AppPreferences appPrefs;

	ArrayList<String> UserID_Arr = new ArrayList<String>();
	ArrayList<String> UserName_Arr = new ArrayList<String>();
	ArrayList<String> ImagePath_Arr = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.users_list);

		list_userslist = (ListView) findViewById(R.id.list_userslist);
		appPrefs = new AppPreferences(getApplicationContext(), "SplashScreen");

		JsonClass_OBJ = new JsonClass();
		rotation = AnimationUtils
				.loadAnimation(this, R.anim.clockwise_rotation); // progress

		new asynctask().execute();

		list_userslist.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parentView, View v,
					int position, long arg3) {

			}
		});

	}

	public void LoadMainAsync() {

		UserName_Arr.clear();
		UserID_Arr.clear();
		ImagePath_Arr.clear();
		new asynctask().execute();
	}

	class asynctask extends AsyncTask<Void, Void, Void> {

		Dialog dialog = new Dialog(Friends_Request_List.this);

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

			rotation = AnimationUtils.loadAnimation(Friends_Request_List.this,
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
					+ "viewFriendRequestJson.php?userId="
					+ appPrefs.getData("USER_ID"));

			String S = JsonClass_OBJ.getAPI();
			System.out.println(S);
			test();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub

			MyAdapter = new myAdapter(Friends_Request_List.this,
					R.layout.friendrequests_list_adapter, UserID_Arr);

			list_userslist.setAdapter(MyAdapter);

			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
		}

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

					if (jobj.has("friendRequest")) // handle Se_wesam

					{

						array = jobj.getJSONArray("friendRequest");

						for (int i = 0; i < array.length(); i++) {
							subObj = array.getJSONObject(i);
							if (subObj.has("userId")) {

								UserID_Arr.add(subObj.getString("userId"));

							}

							if (subObj.has("userName")) {

								if (subObj.getString("userName").length() > 0) {
									UserName_Arr.add(subObj
											.getString("userName"));

								} else {

									UserName_Arr.add("no data");

								}

							}

							if (subObj.has("image")) {

								if (subObj.getString("image").length() > 0) {
									ImagePath_Arr
											.add(subObj.getString("image"));

								} else {
									ImagePath_Arr.add("no data");
								}

							}

						}

					}

				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public class myAdapter extends ArrayAdapter<String> {
		// Context c= Raw;
		private Activity activity;
		String ID;
		// Bitmap mIcon11 = null;
		// ImageLoader imageLoader;
		private LayoutInflater inflater = null;
		ArrayList<String> userid = new ArrayList<String>();

		public myAdapter(Context context, int textViewResourceId,
				ArrayList<String> userid) {
			super(context, textViewResourceId, userid);
			this.userid = userid;

			imageLoader = new ImageLoader(context);
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {// Set
			// values
			// in
			// hotel
			// name,price
			// offerprice
			// TODO Auto-generated method stub
			View vi = convertView;
			if (convertView == null)
				vi = inflater.inflate(R.layout.friendrequests_list_adapter,
						null);

			TextView title = (TextView) vi.findViewById(R.id.heading_txt);

			Button Accept_request_btn = (Button) vi
					.findViewById(R.id.acceptrequest_btn);
			Button Reject_request_btn = (Button) vi
					.findViewById(R.id.rejectrequest_btn);
			// TextView ImageUser = (TextView)
			// vi.findViewById(R.id.list_images);

			title.setText(UserName_Arr.get(position));
			// email.setText(lists.get("description"));

			imageLoader.DisplayImage(ImagePath_Arr.get(position),
					(ImageView) vi.findViewById(R.id.list_images));

			// Accept_request_btn.setId(position);
			Accept_request_btn.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {

					// TODO Auto-generated method stub
					ID = UserID_Arr.get(position);
					new AcceptRejectasynctask(ID, "1").execute();
				}
			});

			// Reject_request_btn.setId(position);
			Reject_request_btn.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {

					// TODO Auto-generated method stub
					ID = UserID_Arr.get(position);

					new AcceptRejectasynctask(ID, "0").execute();
				}
			});

			return vi;
		}

	}

	class AcceptRejectasynctask extends AsyncTask<Void, Void, Void> {

		Dialog dialog = new Dialog(Friends_Request_List.this);
		String friendrequestid, Statusvalue;

		public AcceptRejectasynctask(String frienid, String status) {
			// TODO Auto-generated constructor stub
			friendrequestid = frienid;
			Statusvalue = status;

			System.out.println("send requestid" + frienid);
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

			rotation = AnimationUtils.loadAnimation(Friends_Request_List.this,
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

			System.out.println(StaticValues.UrlLink
					+ "respondFriendJson.php?userId="
					+ appPrefs.getData("USER_ID") + "&friendId="
					+ friendrequestid + "&status=" + Statusvalue);

			JsonClass_OBJ.setAPI(StaticValues.UrlLink
					+ "respondFriendJson.php?userId="
					+ appPrefs.getData("USER_ID") + "&friendId="
					+ friendrequestid + "&status=" + Statusvalue);

			String S = JsonClass_OBJ.getAPI();
			System.out.println(S);
			test();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub

			MyAdapter = new myAdapter(Friends_Request_List.this,
					R.layout.friendrequests_list_adapter, UserID_Arr);

			list_userslist.setAdapter(MyAdapter);

			if (this.dialog.isShowing()) {
				this.dialog.dismiss();

			}

			LoadMainAsync();
		}

	}

}