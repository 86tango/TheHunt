package com.huntgame.Friends;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huntgame.Main.R;
import com.huntgame.UtilitiyFile.AppPreferences;
import com.huntgame.UtilitiyFile.JsonClass;
import com.huntgame.UtilitiyFile.StaticValues;
import com.project.LazyLoading.ImageLoader;

public class Users_List extends Activity {

	ListView list_userslist;
	RSSItemAdapter adapter;
	Animation rotation;
	ImageLoader imageLoader;
	JsonClass JsonClass_OBJ;
	String Status;
	AppPreferences appPrefs;

	ArrayList<String> UserID_Arr = new ArrayList<String>();
	ArrayList<String> UserName_Arr = new ArrayList<String>();
	ArrayList<String> ImagePath_Arr = new ArrayList<String>();
	ArrayList<String> Status_Arr = new ArrayList<String>();
	ArrayList<String> Sort_UserID_Arr = new ArrayList<String>();
	ArrayList<String> Sort_UserName_Arr = new ArrayList<String>();
	ArrayList<String> Sort_ImagePath_Arr = new ArrayList<String>();
	ArrayList<String> Sort_Status_Arr = new ArrayList<String>();

	EditText Searchtxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.users_list);
		appPrefs = new AppPreferences(getApplicationContext(), "SplashScreen");

		list_userslist = (ListView) findViewById(R.id.list_userslist);
		Searchtxt = (EditText) findViewById(R.id.searchtext);
		Searchtxt.addTextChangedListener(new TextWatcher() {

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
				SortingData(Searchtxt.getText().toString());
			}
		});

		JsonClass_OBJ = new JsonClass();
		rotation = AnimationUtils
				.loadAnimation(this, R.anim.clockwise_rotation); // progress

		LoadMainAsync();

		list_userslist.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parentView, View v,
					int position, long arg3) {

			}
		});

	}

	public void LoadMainAsync() {

		UserID_Arr.clear();
		UserName_Arr.clear();
		ImagePath_Arr.clear();
		Status_Arr.clear();

		new asynctask().execute();
	}

	class asynctask extends AsyncTask<Void, Void, Void> {

		Dialog dialog = new Dialog(Users_List.this);

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

			rotation = AnimationUtils.loadAnimation(Users_List.this,
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
					+ "view_friends_relation.php?userId="
					+ appPrefs.getData("USER_ID"));

			String S = JsonClass_OBJ.getAPI();
			System.out.println(S);
			test();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub

			adapter = new RSSItemAdapter(Users_List.this,
					R.layout.sendrequest_list_adapter, UserName_Arr,
					UserID_Arr, ImagePath_Arr, Status_Arr);

			list_userslist.setAdapter(adapter);

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

					if (jobj.has("users")) // handle Se_wesam

					{

						array = jobj.getJSONArray("users");

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

							if (subObj.has("Status")) {

								if (subObj.getString("Status").length() > 0) {
									Status_Arr.add(subObj.getString("Status"));

								} else {
									Status_Arr.add("no data");
								}

							}

						}

					}

				}

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Sort_UserID_Arr.addAll(UserID_Arr);
		Sort_UserName_Arr.addAll(UserName_Arr);
		Sort_ImagePath_Arr.addAll(ImagePath_Arr);
		Sort_Status_Arr.addAll(Status_Arr);

	}

	// /////////////////

	class Sendrequestasynctask extends AsyncTask<Void, Void, Void> {

		Dialog dialog = new Dialog(Users_List.this);
		String sendrequestid;

		public Sendrequestasynctask(String frienid) {
			// TODO Auto-generated constructor stub
			sendrequestid = frienid;

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

			rotation = AnimationUtils.loadAnimation(Users_List.this,
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

			System.out.println(StaticValues.UrlLink + "addFriend.php?userId="
					+ appPrefs.getData("USER_ID") + "&friendId="
					+ sendrequestid);

			JsonClass_OBJ.setAPI(StaticValues.UrlLink + "addFriend.php?userId="
					+ appPrefs.getData("USER_ID") + "&friendId="
					+ sendrequestid);

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

			LoadMainAsync();
		}
	}

	public void loadadapter() {
		adapter = new RSSItemAdapter(Users_List.this,
				R.layout.sendrequest_list_adapter, UserName_Arr, UserID_Arr,
				ImagePath_Arr, Status_Arr);
		list_userslist.setAdapter(adapter);

	}

	class RSSItemAdapter extends ArrayAdapter<String> {

		private final Context context;
		private LayoutInflater vi = null;
		ImageLoader imageLoader;
		ArrayList<String> UserName_Arr_a = new ArrayList<String>();
		ArrayList<String> UserID_Arr_a = new ArrayList<String>();
		ArrayList<String> Userimg_Arr_a = new ArrayList<String>();
		ArrayList<String> Userstatus_Arr_a = new ArrayList<String>();

		public RSSItemAdapter(Context context, int textViewResourceId,
				ArrayList<String> UserName_Arr_a,
				ArrayList<String> UserID_Arr_a,
				ArrayList<String> Userimg_Arr_a,
				ArrayList<String> Userstatus_Arr_a) {
			super(context, textViewResourceId, UserName_Arr_a);
			this.context = context;
			this.UserID_Arr_a = UserID_Arr_a;
			this.UserName_Arr_a = UserName_Arr_a;
			this.Userimg_Arr_a = Userimg_Arr_a;
			this.Userstatus_Arr_a = Userstatus_Arr_a;

			imageLoader = new ImageLoader(context);
			vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				v = vi.inflate(R.layout.sendrequest_list_adapter, null);
			}
			int Imagecheck_flag = 0;
			TextView UserName_txt = (TextView) v.findViewById(R.id.heading_txt);

			Button Send_request_btn = (Button) v
					.findViewById(R.id.sendrequest_btn);

			// Send_request_btn.setId(position);

			if (Userstatus_Arr_a.get(position).equals("No")) {
				Send_request_btn.setVisibility(View.VISIBLE);
				Send_request_btn.setText("Invite");

				Send_request_btn.setEnabled(true);

			} else if (Userstatus_Arr_a.get(position).equals("Pending")) {
				Send_request_btn.setVisibility(View.VISIBLE);
				Send_request_btn.setText("Pending");
				Send_request_btn.setEnabled(false);

			} else {
				Send_request_btn.setEnabled(true);
				Send_request_btn.setVisibility(View.INVISIBLE);
			}

			UserName_txt.setText(UserName_Arr_a.get(position));
			imageLoader.DisplayImage(Userimg_Arr_a.get(position),
					(ImageView) v.findViewById(R.id.list_images));

			System.out.println("s" + position);
			Send_request_btn.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {

					// TODO Auto-generated method stub
					// ID =
					// UsersList_Json_Details.get(arg0.getId()).get("userId");

					new Sendrequestasynctask(UserID_Arr_a.get(position))
							.execute();
					Toast.makeText(context, "" + position, 1).show();
				}
			});
			if (position % 2 == 0) {
				v.setBackgroundColor(Color.parseColor("#55d3d3d3"));
			} else {
				v.setBackgroundColor(Color.parseColor("#99ffffff"));
			}

			return v;
		}
	}

	private void SortingData(CharSequence s) {
		Sort_UserID_Arr.clear();
		Sort_UserName_Arr.clear();
		Sort_ImagePath_Arr.clear();
		Sort_Status_Arr.clear();

		// TODO Auto-generated method stub

		for (int i = 0; i < UserName_Arr.size(); i++) {

			if (UserName_Arr.get(i).toLowerCase()
					.contains(s.toString().toLowerCase())) {
				Sort_UserName_Arr.add(UserName_Arr.get(i));
				Sort_UserID_Arr.add(UserID_Arr.get(i));
				Sort_ImagePath_Arr.add(ImagePath_Arr.get(i));
				Sort_Status_Arr.add(Status_Arr.get(i));

			}

		}
		adapter = new RSSItemAdapter(Users_List.this,
				R.layout.sendrequest_list_adapter, Sort_UserName_Arr,
				Sort_UserID_Arr, Sort_ImagePath_Arr, Sort_Status_Arr);
		list_userslist.setAdapter(adapter);

	}
}