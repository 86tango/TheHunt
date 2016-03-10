package com.huntgame.Main;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.Facebook;
import com.facebook.android.SessionStore;
import com.facebook.android.Utility;
import com.google.android.gcm.GCMRegistrar;
import com.huntgame.Friends.FriendsHome_Page;
import com.huntgame.Game.CreateNewGame;
import com.huntgame.Game.CurrentGame;
import com.huntgame.Game.GameRequestPage;
import com.huntgame.Game.PlayerStatus;
import com.huntgame.Gps.TestService;
import com.huntgame.UtilitiyFile.AppPreferences;
import com.huntgame.UtilitiyFile.JsonClass;
import com.huntgame.UtilitiyFile.StaticValues;
import com.huntgame.bounty.Bounties_Main_Page;
import com.huntgame.fb.Facebook_Activity;
import com.huntgame.moderation.ModertionList;
import com.huntgame.pushnotifications.CaptureFugitiveAcceptReject;
import com.huntgame.pushnotifications.CaptureInformation;
import com.huntgame.pushnotifications.Capture_Reject_Information;
import com.huntgame.pushnotifications.ModerationRejectInformation;
import com.huntgame.pushnotifications.UserAcceptRejectGame;

public class HomePage extends Activity {
	public static final String APP_ID = "637669726260011";
	ProgressDialog dialog;
	AppPreferences appPrefs;
	String[] phoneNumber;
	SlidingDrawer slidingDrawer;
	Button slideButton;
	JsonClass JsonClass_OBJ;
	Animation rotation;
	String regId;
	public static Handler mHandler;
	public static String userIDPhone = "";
	TimeZone tz;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_page);
		tz = TimeZone.getDefault();
		// Start service using AlarmManager
		int SDK_INT = android.os.Build.VERSION.SDK_INT;
		if (SDK_INT > 8) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 10);

		Intent intent = new Intent(this, TestService.class);

		PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);

		AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		int i;
		i = 60 * 60;
		alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
				i * 1000, pintent);

		startService(new Intent(getBaseContext(), TestService.class));

		JsonClass_OBJ = new JsonClass();

		appPrefs = new AppPreferences(getApplicationContext(), "SplashScreen");
		rotation = AnimationUtils
				.loadAnimation(this, R.anim.clockwise_rotation);
		rotation.setRepeatCount(Animation.INFINITE);

		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);
		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);

		regId = GCMRegistrar.getRegistrationId(this);

		System.out.println("regId" + regId);

		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);
		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);

		final String regId = GCMRegistrar.getRegistrationId(this);

		if (regId.equals("")) {
			GCMRegistrar.register(this, Utils.GCMSenderId);
		} else {

			Log.v("", "Already registered: " + regId);
		}

		if (Utils.notificationReceived) {
			onNotification();
		}

		new LoadPushNotification().execute();

		slideButton = (Button) findViewById(R.id.slideButton);
		slidingDrawer = (SlidingDrawer) findViewById(R.id.SlidingDrawer);

		Utility.mFacebook = new Facebook(APP_ID);
		Utility.mAsyncRunner = new AsyncFacebookRunner(Utility.mFacebook);
		SessionStore.restore(Utility.mFacebook, this);

		slidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			public void onDrawerOpened() {
				slideButton.setBackgroundResource(R.drawable.down);

				slidingDrawer.setClickable(true);
			}
		});

		slidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			public void onDrawerClosed() {
				slideButton.setBackgroundResource(R.drawable.up);
			}
		});
		// /

		onDestroy();

		if (regId.equals("")) {
			GCMRegistrar.register(this, Utils.GCMSenderId);
		} else {

			Log.v("", "Already registered: " + regId);
		}

		/**
		 * We set this varible true when we recive notification (this code must
		 * be in activity which not showing your applciaiton splash screen).
		 */
		if (Utils.notificationReceived) {
			onNotification();
		}

	}

	class LoadPushNotification extends AsyncTask<Void, Integer, Void> {

		Dialog dialog = new Dialog(HomePage.this);

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// this.dialog.getWindow().setBackgroundDrawable(new
			// ColorDrawable(0));
			// this.dialog.setContentView(R.layout.loadingicon);
			// ImageView img = (ImageView) this.dialog
			// .findViewById(R.id.progressBar1);
			// img.setBackgroundResource(R.drawable.progress_img);
			//
			// rotation = AnimationUtils.loadAnimation(HomePage.this,
			// R.anim.clockwise_rotation);
			// rotation.setRepeatCount(Animation.INFINITE);
			//
			// this.dialog.show();
			// img.startAnimation(rotation);
			// this.dialog.setCanceledOnTouchOutside(false);

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			String s = tz.getID();

			System.out.println("timezone" + s);
			try {
				s = URLEncoder.encode(tz.getID(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JsonClass_OBJ.setAPI(StaticValues.UrlLink
					+ "device_register_json.php?userId="
					+ appPrefs.getData("USER_ID") + "&dev_token=" + regId
					+ "&timezone=" + s);

			System.out.println("DEVICEAPI2" + StaticValues.UrlLink
					+ "device_register_json.php?userId="
					+ appPrefs.getData("USER_ID") + "&dev_token=" + regId
					+ "&timezone=" + s);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			// if (this.dialog.isShowing())
			// this.dialog.dismiss();

		}

	}

	private class LogoutRequestListener extends BaseRequestListener {

		public void onComplete(String response) {
			// TODO Auto-generated method stub

			// dialog.dismiss();
			SessionStore.clear(getApplicationContext());

			finish();
			// mHandler.post(new Runnable() {
			//
			// public void run() {
			// SessionEvents.onLogoutFinish();
			// }
			// });
		}
	}

	public void Profile_click(View v) {

		startActivity(new Intent(HomePage.this, UserProfile.class));
	}

	public void playerstatus_click(View v) {

		startActivity(new Intent(HomePage.this, PlayerStatus.class));
	}

	public void facebookLogin_Click(View v) {

		if (Utility.mFacebook.isSessionValid()) {

			AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
			builder.setCancelable(true);
			builder.setTitle("Log out facebook");
			builder.setInverseBackgroundForced(true);
			builder.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog_on, int which) {

							dialog_on.dismiss();
							dialog = ProgressDialog.show(HomePage.this, "",
									"Logging out from Hunt Game", true, true);
							// SessionEvents.onLogoutBegin();
							AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(
									Utility.mFacebook);
							asyncRunner.logout(getApplicationContext(),
									new LogoutRequestListener());
							// finish();

						}
					});
			builder.setNegativeButton("No",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog_on, int which) {
							dialog_on.dismiss();
						}
					});
			AlertDialog alert = builder.create();
			alert.show();

			// appPrefs.SaveData("0", "SESSION_CHECK");
			//
			// AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(
			// Utility.mFacebook);
			// asyncRunner.logout(getApplicationContext(),
			// new LogoutRequestListener());

		} else if (appPrefs.getData("SESSION_CHECK").equals("1")) {
			appPrefs.SaveData("0", "SESSION_CHECK");
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case (1):
			if (resultCode == Activity.RESULT_OK) {
				Uri contactData = data.getData(); // has the uri for picked
													// contact
				Cursor c = getContentResolver().query(contactData, null, null,
						null, null); // creates the contact cursor with the
										// returned uri
				if (c.moveToFirst()) {
					String name = c.getString(c
							.getColumnIndex(PhoneLookup.DISPLAY_NAME));
					String number = c.getString(c
							.getColumnIndex(PhoneLookup.NUMBER));
					Toast.makeText(this, name + " has number " + number,
							Toast.LENGTH_LONG).show();
				}
			}
			break;
		}
	}

	public void showSelectedNumber(int type, String number) {
		Toast.makeText(this, type + ": " + number, Toast.LENGTH_LONG).show();
	}

	private String getPhoneNumber(long id) {
		String phone = null;
		Cursor phonesCursor = null;
		phonesCursor = queryPhoneNumbers(id);
		if (phonesCursor == null || phonesCursor.getCount() == 0) {
			// No valid number
			// signalError();
			return null;
		} else if (phonesCursor.getCount() == 1) {
			// only one number, call it.
			phone = phonesCursor.getString(phonesCursor
					.getColumnIndex(Phone.NUMBER));
		} else {
			phonesCursor.moveToPosition(-1);
			while (phonesCursor.moveToNext()) {

				// Found super primary, call it.
				phone = phonesCursor.getString(phonesCursor
						.getColumnIndex(Phone.NUMBER));
				break;

			}
		}

		return phone;
	}

	private Cursor queryPhoneNumbers(long contactId) {
		ContentResolver cr = getContentResolver();
		Uri baseUri = ContentUris.withAppendedId(
				ContactsContract.Contacts.CONTENT_URI, contactId);
		Uri dataUri = Uri.withAppendedPath(baseUri,
				ContactsContract.Contacts.Data.CONTENT_DIRECTORY);

		Cursor c = cr.query(dataUri, new String[] { Phone._ID, Phone.NUMBER,
				Phone.IS_SUPER_PRIMARY, RawContacts.ACCOUNT_TYPE, Phone.TYPE,
				Phone.LABEL }, Data.MIMETYPE + "=?",
				new String[] { Phone.CONTENT_ITEM_TYPE }, null);
		if (c != null && c.moveToFirst()) {
			return c;
		}
		return null;
	}

	public void settings(View v) {

	}

	public void Friends_Click(View v) {
		appPrefs.SaveData("friendslist", "FriendsHome_Page");
		startActivity(new Intent(HomePage.this, FriendsHome_Page.class));

	}

	public void menu_settings_click(View v) {
		startActivity(new Intent(HomePage.this, SettingsPage.class));

		slidingDrawer.close();

	}

	public void invitefriends_click(View v) {

		// startActivity(new Intent(HomePage.this, Facebook_Activity.class));
		appPrefs.SaveData("invitefriends", "FriendsHome_Page");
		startActivity(new Intent(HomePage.this, FriendsHome_Page.class));

	}

	public void CreateGame_Click(View v) {

		startActivity(new Intent(HomePage.this, CreateNewGame.class));

	}

	public void currentgame_click(View v) {

		startActivity(new Intent(HomePage.this, CurrentGame.class));

	}

	public void store(View v) {

		startActivity(new Intent(HomePage.this, MainActivity.class));

		slidingDrawer.close();

	}

	public void moderation_click(View v) {
		startActivity(new Intent(HomePage.this, ModertionList.class));
		slidingDrawer.close();

	}

	public void bounties_Click(View v) {
		startActivity(new Intent(HomePage.this, Bounties_Main_Page.class));
		slidingDrawer.close();

	}

	public void invite_frnds(View v) {
		startActivity(new Intent(HomePage.this, Facebook_Activity.class));
		slidingDrawer.close();

	}

	public void tutorial(View v) {
		startActivity(new Intent(HomePage.this, Tutorial.class));
		slidingDrawer.close();

	}

	// ///////////push notification

	@Override
	protected void onDestroy() {

		GCMRegistrar.onDestroy(getApplicationContext());
		super.onDestroy();

	}

	public void onNotification() {

		Utils.notificationReceived = false;

		PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);

		WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.ON_AFTER_RELEASE, "TAG");

		wl.acquire();

		Toast.makeText(getApplicationContext(), Utils.pushnotificationTag, 2000)
				.show();
		if (Utils.notiHunterImage.equals("null")) {
			Utils.notiHunterImage = "";
		}
		if (Utils.notiFujitiveImage.equals("null")) {
			Utils.notiFujitiveImage = "";
		}
		if (Utils.notiCapturedImage.equals("null")) {
			Utils.notiCapturedImage = "";
		}

		System.out.println("tagggg" + Utils.pushnotificationTag);
		if (Utils.pushnotificationTag.equals("Creategame")) {

			Intent i = new Intent(HomePage.this, GameRequestPage.class);
			Bundle b = new Bundle();
			b.putString("Title", Utils.notiTitle);
			b.putString("GameName", Utils.notiGameName);
			b.putString("GameID", Utils.notiGameID);

			b.putString("StartingDate", Utils.notistartingDate);
			b.putString("EndingDate", Utils.notiendingDate);
			b.putString("GameType", Utils.notigameType);

			b.putString("Latitude", Utils.notilatitude);
			b.putString("Longitude", Utils.notilongitude);
			b.putString("ModeratorSatus", Utils.notimoderatorStatus);

			b.putString("Location", Utils.notilocation);
			b.putString("Status", Utils.notistatus);
			b.putString("UserName", Utils.notiuserName);

			i.putExtras(b);
			startActivity(i);
		}

		else if (Utils.pushnotificationTag
				.equals("nomoderator_capture_information")) {
			Intent i = new Intent(HomePage.this, CaptureInformation.class);
			Bundle b = new Bundle();
			b.putString("GameName", Utils.notiGameName);
			b.putString("HunterName", Utils.notiHuntername);
			b.putString("FugitiveName", Utils.notiFujiTiveName);
			b.putString("Message", Utils.notiMessage);
			b.putString("HunterImage_path", Utils.notiHunterImage);
			b.putString("FujitiveImage_path", Utils.notiFujitiveImage);
			b.putString("CaptureImage_path", Utils.notiCapturedImage);
			b.putString("description", "Hunter caught the Fugative");

			i.putExtras(b);
			startActivity(i);
		} else if (Utils.pushnotificationTag
				.equals("capture_reject_information")) {
			Intent i = new Intent(HomePage.this,
					Capture_Reject_Information.class);
			Bundle b = new Bundle();
			b.putString("GameName", Utils.notiGameName);
			b.putString("FugitiveName", Utils.notiFujiTiveName);
			b.putString("FujitiveImage_path", Utils.notiFujitiveImage);

			b.putString("description",
					"Fugitive rejected the capture made by the you. Game Moderator, "
							+ Utils.notiModeratorName
							+ " will take the final desicion");
			i.putExtras(b);
			startActivity(i);
		}

		else if (Utils.pushnotificationTag
				.equals("moderator_capture_fujitive_accept_reject")) {
			Intent i = new Intent(HomePage.this,
					CaptureFugitiveAcceptReject.class);
			Bundle b = new Bundle();
			b.putString("GameName", Utils.notiGameName);
			b.putString("HunterName", Utils.notiHuntername);
			b.putString("FugitiveName", Utils.notiFujiTiveName);
			b.putString("Message", Utils.notiMessage);
			b.putString("HunterImage", Utils.notiHunterImage);
			b.putString("FugitiveImage", Utils.notiFujitiveImage);
			b.putString("CaptureImage", Utils.notiCapturedImage);

			b.putString("ModeratorName", Utils.notiModeratorName);
			b.putString("HunterID", Utils.notiHunterId);
			b.putString("FugitiveID", Utils.notiFujitiveId);
			b.putString("GameID", Utils.notiGameID);

			i.putExtras(b);
			startActivity(i);
		}

		else if (Utils.pushnotificationTag
				.equals("user_accept_reject_beforegamestarts")) {

			System.out.println("ttttttt");
			Intent i = new Intent(HomePage.this, UserAcceptRejectGame.class);
			Bundle b = new Bundle();
			b.putString("gamename", Utils.notiGameName);
			b.putString("username", Utils.notiUserName);
			b.putString("userimage", Utils.notiUserImg);
			b.putString("response", Utils.notiUserResponse);

			i.putExtras(b);
			startActivity(i);
		}

		else if (Utils.pushnotificationTag
				.equals("moderator_capture_reject_information")) {

			System.out.println("ttttttt");
			Intent i = new Intent(HomePage.this,
					ModerationRejectInformation.class);
			Bundle b = new Bundle();
			b.putString("HunterName", Utils.notiHuntername);
			b.putString("HunterID", Utils.notiHunterId);
			b.putString("HunterImage", Utils.notiHunterImage);

			b.putString("FugitiveName", Utils.notiFujiTiveName);
			b.putString("FugitiveID", Utils.notiFujitiveId);
			b.putString("FugitiveImage", Utils.notiFujitiveImage);

			b.putString("GameName", Utils.notiGameName);
			b.putString("GameID", Utils.notiGameID);

			b.putString("userimage", Utils.notiUserImg);
			b.putString("Message", Utils.notiMessage);
			b.putString("CaptureImage", Utils.notiCapturedImage);

			i.putExtras(b);
			startActivity(i);
		}

	}
}
