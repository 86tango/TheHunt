package com.huntgame.fb;

import java.util.Timer;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionStore;
import com.facebook.android.Utility;
import com.huntgame.InviteFriends.SendMessagesActivity;
import com.huntgame.Main.R;
import com.huntgame.UtilitiyFile.AlertDialogManager;
import com.huntgame.UtilitiyFile.AppPreferences;
import com.huntgame.UtilitiyFile.ConnectionDetector;

public class Facebook_Activity extends Activity {

	TextView ids;

	String[] main_items = { "Update Status", "App Requests", "Get Friends",
			"Upload Photo", "Place Check-in", "Run FQL Query",
			"Graph API Explorer", "email", "publish_stream",
			"user_mobile_phone", "user_birthday", "user_location" };

	String[] permissions = { "offline_access", "publish_stream", "user_photos",
			"publish_checkins", "photo_upload", "email", "user_mobile_phone",
			"user_birthday", "user_location" };

	public static final String APP_ID = "637669726260011";
	@SuppressWarnings("unused")
	private Handler fb_mHandler;

	// Fb dialog flag for to avoid Window managing exception
	boolean fb_flag = true;

	// Alert Dialog Manager
	AlertDialogManager trivia_alert = new AlertDialogManager();

	// Internet Connection detector
	private ConnectionDetector trivia_connectiondectection;

	String gender, username, fb_id, fb_email, profile_picture, fb_phone,
			fb_dob, fb_place;
	String regId;

	String graph_or_fql = "fql";
	Handler mRunOnUi = new Handler();
	String friendsname = "";

	ProgressDialog dialog;
	Timer timer;

	AppPreferences appPrefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.invitefriends);

		ids = (TextView) findViewById(R.id.rel_game_length);

		appPrefs = new AppPreferences(getApplicationContext(), "SplashScreen");

		Utility.mFacebook = new Facebook("637669726260011");

		Utility.mAsyncRunner = new AsyncFacebookRunner(Utility.mFacebook);
		SessionStore.restore(Utility.mFacebook, this);
		trivia_connectiondectection = new ConnectionDetector(
				getApplicationContext());
	}

	public void fb_click(View v) {

		// Check if Internet present
		if (trivia_connectiondectection.isConnectingToInternet()) {

			// Check if fb Session is active
			if (!Utility.mFacebook.isSessionValid()) {

				Utility.mFacebook.authorize(Facebook_Activity.this,
						permissions, -1, new FbLoginDialogListener());

			} else {
				dialog = ProgressDialog.show(Facebook_Activity.this, "",
						"Getting friends list", true, true);

				graph_or_fql = "fql";
				String query = "select name, current_location, uid, pic_square from user where uid in (select uid2 from friend where uid1=me()) order by name";
				Bundle params = new Bundle();
				params.putString("method", "fql.query");
				params.putString("query", query);
				Utility.mAsyncRunner.request(null, params,
						new FriendsRequestListener());
			}
		} else {
			trivia_alert.showAlertDialog(Facebook_Activity.this,
					"Connection Error",
					"You need a working internet connection to proceed", false);

		}

	}

	// FB Listners
	private final class FbLoginDialogListener implements DialogListener {
		public void onComplete(Bundle values) {

			SessionStore.save(Utility.mFacebook, Facebook_Activity.this);

			Toast.makeText(getApplicationContext(), "please wait...", 3000)
					.show();

			requestUserData();

		}

		public void onFacebookError(FacebookError error) {

		}

		public void onError(DialogError error) {

		}

		public void onCancel() {

		}
	}

	public class FriendsRequestListener extends BaseRequestListener {

		public void onFacebookError(FacebookError error) {

			Toast.makeText(getApplicationContext(),
					"Facebook Error: " + error.getMessage(), Toast.LENGTH_SHORT)
					.show();
		}

		public void onComplete(String response) {
			// TODO Auto-generated method stub
			final String res = response;
			if (dialog.isShowing()) {
				dialog.dismiss();
			}

			Facebook_Activity.this.runOnUiThread(new Runnable() {

				public void run() {
					// TODO Auto-generated method stub
					System.out.println(res);
					Intent myIntent = new Intent(getApplicationContext(),
							fbfacebookfriendslisting.class);
					myIntent.putExtra("API_RESPONSE", res);
					myIntent.putExtra("METHOD", graph_or_fql);
					Bundle temp_addedfriends = new Bundle();

					myIntent.putExtras(temp_addedfriends);

					startActivity(myIntent);

				}
			});

		}
	}

	public void requestUserData() {

		// Bundle params = new Bundle();
		// params.putString("fields", "name, picture");
		Utility.mAsyncRunner.request("me", new UserRequestListener());
	}

	public class UserRequestListener extends BaseRequestListener {

		public void onComplete(final String response) {
			// TODO Auto-generated method stub

			System.out.println(response);

			Facebook_Activity.this.runOnUiThread(new Runnable() {

				public void run() {
					// TODO Auto-generated method stub
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(response);
						appPrefs.SaveData("facebukId",
								jsonObject.getString("id"));
						System.out.println("---------xxxxxxxxx----"
								+ jsonObject.getString("id"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						System.out.println("----------------eeeee" + e);
						e.printStackTrace();
					}
					dialog = ProgressDialog.show(Facebook_Activity.this, "",
							"Getting friends list", true, true);
					graph_or_fql = "fql";
					String query = "select name, current_location, uid, pic_square from user where uid in (select uid2 from friend where uid1=me()) order by name";
					Bundle params = new Bundle();
					params.putString("method", "fql.query");
					params.putString("query", query);
					Utility.mAsyncRunner.request(null, params,
							new FriendsRequestListener());

				}
			});

		}

	}

	public void invite_contacts(View v) {
		startActivity(new Intent(Facebook_Activity.this,
				SendMessagesActivity.class));
		finish();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		super.onBackPressed();
	}

	public void Submit(View v) {
		String recepients = ids.getText().toString();
		System.out.println("recepients:" + recepients);
		// TODO Auto-generated method stub
		Intent i = new Intent(Intent.ACTION_SEND);
		// i.setType("text/plain"); // use this line for testing in
		// the emulator
		i.setType("message/rfc822"); // use from live device
		i.putExtra(Intent.EXTRA_EMAIL, new String[] { recepients });
		i.putExtra(Intent.EXTRA_SUBJECT, "HUNT Game Invitaition");
		// i.putExtra(Intent.EXTRA_TEXT, "Hi,");
		startActivity(Intent.createChooser(i, "Select email application."));

	}

}
