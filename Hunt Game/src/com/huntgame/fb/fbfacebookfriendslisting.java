package com.huntgame.fb;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseDialogListener;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.Utility;
import com.huntgame.Main.R;
import com.huntgame.UtilitiyFile.AppPreferences;
import com.project.LazyLoading.ImageLoader;

public class fbfacebookfriendslisting extends Activity {
	protected static JSONArray jsonArray;
	String graph_or_fql = "fql";
	ArrayList<String> user_name = new ArrayList<String>();
	ArrayList<String> image = new ArrayList<String>();
	ArrayList<String> fbuserid = new ArrayList<String>();
	ArrayList<String> fbinstaplanid = new ArrayList<String>();
	ArrayList<String> fbinstaplanuser = new ArrayList<String>();
	ArrayList<Boolean> fbboolean = new ArrayList<Boolean>();
	ListView contactlist;
	Fbfriends fbfriends;
	String friendsname = "";
	AppPreferences appPrefs;
	private Handler mRunOnUi = new Handler();
	// CheckBox check;
	String sam_fbid;
	ToggleButton togg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fb_friendspost);
		Bundle extras = getIntent().getExtras();
		String apiResponse = extras.getString("API_RESPONSE");
		String graph_or_fql = extras.getString("METHOD");


		appPrefs = new AppPreferences(getApplicationContext(), "SplashScreen");
	
		contactlist = (ListView) findViewById(R.id.friendslist_ids);
		togg = (ToggleButton) findViewById(R.id.toggle);

		fbfriends = new Fbfriends(fbfacebookfriendslisting.this, user_name,
				image, fbuserid, fbboolean);
		contactlist.setAdapter(fbfriends);

		togg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub

				if (isChecked) {
					for (int i = 0; i < fbuserid.size(); i++) {
						fbboolean.set(i, true);
					}

					fbfriends.notifyDataSetChanged();

				} else {

					for (int i = 0; i < fbuserid.size(); i++) {
						fbboolean.set(i, false);
					}

					fbfriends.notifyDataSetChanged();
				}

			}
		});
		try {
			if (graph_or_fql.equals("graph")) {
				jsonArray = new JSONObject(apiResponse).getJSONArray("data");
			} else {
				jsonArray = new JSONArray(apiResponse);

				JSONObject jsonObject = null;

				for (int i = 0; i < jsonArray.length(); i++) {
					jsonObject = jsonArray.getJSONObject(i);

					image.add(jsonObject.getString("pic_square"));
					user_name.add(jsonObject.getString("name"));
					fbuserid.add(jsonObject.getString("uid"));
					if (fbinstaplanid.contains(jsonObject.getString("uid"))) {
						fbboolean.add(true);
					} else {
						fbboolean.add(false);
					}

				}

			}
		} catch (JSONException e) {
			// showToast("Error: " + e.getMessage());
			return;
		}

		fbfriends.notifyDataSetChanged();

	}

	public void back(View v) {
		finish();
	}

	class Fbfriends extends ArrayAdapter<String> {
		// TextView label;
		Context contxt;
		public ImageLoader imageLoader;
		ArrayList<String> fbname = new ArrayList<String>();
		ArrayList<String> fbpic = new ArrayList<String>();
		ArrayList<String> fbid = new ArrayList<String>();
		ArrayList<Boolean> ckbool = new ArrayList<Boolean>();

		public Fbfriends(Context context,

		ArrayList<String> fbname, ArrayList<String> fbpic,
				ArrayList<String> fbid, ArrayList<Boolean> ckbool) {
			super(context, android.R.layout.simple_list_item_1, fbname);

			this.contxt = context;
			this.fbname = fbname;
			this.fbpic = fbpic;
			this.fbid = fbid;
			this.ckbool = ckbool;
			imageLoader = new ImageLoader(context);

		}

		public int getCount() {
			return fbname.size();
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) contxt
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.fbfriends_row, parent,
					false);
			LinearLayout linear = (LinearLayout) rowView
					.findViewById(R.id.linear);

			if (position % 2 == 0) {
				linear.setBackgroundColor(Color.parseColor("#A4A4A4"));
			}

			TextView events = (TextView) rowView.findViewById(R.id.eventnames);

			events.setText(fbname.get(position));

			ImageView email = (ImageView) rowView.findViewById(R.id.friendpics);

			Resources res = getResources();
			final int item = position;
			imageLoader.DisplayImage(fbpic.get(position), email);
			System.out.println("####" + fbid + "----------------"
					+ fbinstaplanid);
			// final CheckBox repeatChkBx = (CheckBox) rowView
			// .findViewById(R.id.checkBox1);

			final ToggleButton repeatChkBx = (ToggleButton) rowView
					.findViewById(R.id.checkBox1);

			repeatChkBx
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (isChecked) {
								System.out.println("prevaddedfriends3");

								// perform logic

								for (int i = 0; i < fbid.size(); i++) {
									if (!(fbinstaplanid.contains(fbid.get(i)))) {
										if (!fbinstaplanid.contains(fbid
												.get(repeatChkBx.getId()))) {
											fbinstaplanid.add(fbid
													.get(repeatChkBx.getId()));
											fbinstaplanuser.add(fbname
													.get(repeatChkBx.getId()));
											int position = fbuserid
													.indexOf(fbid
															.get(repeatChkBx
																	.getId()));

											sam_fbid = fbid.get(repeatChkBx
													.getId());

											System.out
													.println("------------ shaaajjjjiiiii------"
															+ sam_fbid);

											postToFacebook(sam_fbid);

											fbboolean.set(position, true);
										}

									}

								}

							} else {

								for (int i = 0; i < fbinstaplanid.size(); i++) {
									if ((fbid.contains(fbinstaplanid.get(i)))) {
										fbinstaplanid.remove(fbid
												.get(repeatChkBx.getId()));
										fbinstaplanuser.remove(fbname
												.get(repeatChkBx.getId()));
										int position = fbuserid.indexOf(fbid
												.get(repeatChkBx.getId()));

										fbboolean.set(position, false);

									}

								}

								ckbool.set(repeatChkBx.getId(), false);

							}
							System.out.println("fb back----" + fbinstaplanid
									+ " fb user " + fbinstaplanuser);

						}
					});
			repeatChkBx.setId(position);
			repeatChkBx.setChecked(ckbool.get(position));

			return rowView;
		}
	}

	public void post(View v) {

		Toast.makeText(getApplicationContext(), "Please wait.....", 1000)
				.show();
		if (!Utility.mFacebook.isSessionValid()) {

		} else {
			System.out.println("sssssssssssssssssssssssssss"
					+ fbinstaplanuser.size());
			for (int i = 0; fbinstaplanuser.size() > i; i++) {

				if (i == fbinstaplanuser.size() - 1) {

					friendsname = friendsname + fbinstaplanuser.get(i);
				} else {
					friendsname = friendsname + fbinstaplanuser.get(i) + ",";
				}
			}

			postToFacebook("", "", "", "");

		}
	}

	void postToFacebook(String news_title, String news_publisher,
			String news_content, String news_imagepath) {

		AsyncFacebookRunner mAsyncFbRunner = new AsyncFacebookRunner(
				Utility.mFacebook);

		Bundle params = new Bundle();

		// params.putString("message", "some type of data");
		System.out.println("-------------" + friendsname);
		params.putString(
				"caption",
				"Entrada a todos los Clubs,  Eventos en Bares y Ofertas en Restaurantes en la palma de tu mano. Do u party? WhatsClub");
		params.putString("link", "http://www.whatsclub.com/");
		params.putString("description", "powered by WhatsClub");
		params.putString("picture",
				"https://si0.twimg.com/profile_images/2452385074/2bp32814pdpu3xmf68d5.jpeg");
		params.putString("name", friendsname);
		// params.putByteArray("photo", bitMapData);
		mAsyncFbRunner.request("me/feed", params, "POST",
				new WallPostListener());
	}

	public void postToFacebook(String valId) {
		Bundle params = new Bundle();

		params.putString("to", valId);
		params.putString("caption", getString(R.string.app_name));
		params.putString("description", "Test description for Hunt Game");
		params.putString("picture", Utility.HACK_ICON_URL);
		params.putString("name", "THE HUNT GAME");
		Utility.mFacebook.dialog(fbfacebookfriendslisting.this, "feed", params,
				new PostDialogListener());
	}

	final class WallPostListener extends BaseRequestListener {
		public void onComplete(final String response) {
			mRunOnUi.post(new Runnable() {

				public void run() {
					// mProgress.cancel();
					System.out.println("----------------- completed---"
							+ response);

				}
			});
		}
	}

	/*
	 * Callback after the message has been posted on friend's wall.
	 */
	public class PostDialogListener extends BaseDialogListener {

		public void onComplete(Bundle values) {
			final String postId = values.getString("post_id");
			if (postId != null) {
				Toast.makeText(getApplicationContext(),
						"Message posted on the wall.", 1).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"No message posted on the wall.", 1).show();

			}
		}
	}

}
