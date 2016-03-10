package com.huntgame.Main;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	public GCMIntentService() {

		super(Utils.GCMSenderId);

	}

	@Override
	protected void onError(Context context, String regId) {
		// TODO Auto-generated method stub
		Log.e("", "error registration id : " + regId);

	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		// TODO Auto-generated method stub
		handleMessage(context, intent);
	}

	@Override
	protected void onRegistered(Context context, String regId) {
		// TODO Auto-generated method stub
		handleRegistration(context, regId);
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	private void handleMessage(Context context, Intent intent) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		Utils.notiTitle = intent.getStringExtra("title");

		Utils.notiGameID = intent.getStringExtra("GameId");
		Utils.pushnotificationTag = intent
				.getStringExtra("PushNotificationTag");

		Utils.notiGameName = intent.getStringExtra("GameName");
		Utils.notistartingDate = intent.getStringExtra("startingDate");
		Utils.notiendingDate = intent.getStringExtra("endingDate");
		// UtilsGcm.category = intent.getStringExtra("category");title
		Utils.notigameType = intent.getStringExtra("gameType");
		Utils.notimoderatorStatus = intent.getStringExtra("moderatorStatus");
		Utils.notilongitude = intent.getStringExtra("longitude");
		Utils.notilatitude = intent.getStringExtra("latitude");
		Utils.notilocation = intent.getStringExtra("location");
		Utils.notistatus = intent.getStringExtra("status");
		Utils.notiuserName = intent.getStringExtra("userName");

		Utils.notiHuntername = intent.getStringExtra("Huntername");
		Utils.notiFujiTiveName = intent.getStringExtra("FujiTiveName");
		Utils.notiHunterId = intent.getStringExtra("HunterId");
		Utils.notiFujitiveId = intent.getStringExtra("FujitiveId");
		if (!(intent.getStringExtra("FujitiveImage") == null))
			Utils.notiFujitiveImage = intent.getStringExtra("FujitiveImage");

		if (!(intent.getStringExtra("HunterImage") == null))
			Utils.notiHunterImage = intent.getStringExtra("HunterImage");
		Utils.notiMessage = intent.getStringExtra("Message");

		if (!(intent.getStringExtra("CapturedImage") == null))
			Utils.notiCapturedImage = intent.getStringExtra("CapturedImage");

		Utils.notiModeratorName = intent.getStringExtra("ModeratorName");

		Utils.notiUserName = intent.getStringExtra("USERName");
		Utils.notiUserImg = intent.getStringExtra("USERImage");
		Utils.notiUserResponse = intent.getStringExtra("USERResponse");

		int icon = R.drawable.ic_launcher; // icon from resources
		CharSequence contentTitle = "hunt";

		if (Utils.pushnotificationTag.equals("Creategame"))

		{
			contentTitle = "New Game Request:" + Utils.notiGameName; // intent.getStringExtra("me");

		}

		if (Utils.pushnotificationTag.equals("nomoderator_capture_information"))

		{
			System.out.println("ccc" + Utils.notiFujitiveImage);
			System.out.println("ccc" + Utils.notiHunterImage);

			contentTitle = Utils.notiGameName + ": " + Utils.notiHuntername
					+ " captured " + Utils.notiFujiTiveName; // intent.getStringExtra("me");

		}

		if (Utils.pushnotificationTag.equals("moderator_capture_information"))

		{
			contentTitle = Utils.notiGameName + ": " + Utils.notiFujiTiveName
					+ " accepted the  capture made by " + Utils.notiHuntername; // intent.getStringExtra("me");

		}

		if (Utils.pushnotificationTag
				.equals("moderator_capture_fujitive_accept_reject"))

		{
			contentTitle = Utils.notiGameName + ": " + Utils.notiHuntername
					+ "claims that you has been captured "
					+ Utils.notiFujiTiveName; // intent.getStringExtra("me");

		}

		if (Utils.pushnotificationTag
				.equals("user_accept_reject_beforegamestarts"))

		{
			if (Utils.notiUserResponse.equals("1")) {
				contentTitle = Utils.notiGameName + ":  " + Utils.notiUserName
						+ "accepted the game"; // intent.getStringExtra("me");
			}
			if (Utils.notiUserResponse.equals("2")) {
				contentTitle = Utils.notiGameName + ":  " + Utils.notiUserName
						+ "rejected the game"; // intent.getStringExtra("me");
			}

		}

		if (Utils.pushnotificationTag.equals("capture_reject_information"))

		{
			contentTitle = Utils.notiGameName + ":  " + Utils.notiUserName
					+ "rejected the capture you made"; // intent.getStringExtra("me");

		}

		if (Utils.pushnotificationTag
				.equals("moderator_capture_reject_information"))

		{
			contentTitle = Utils.notiGameName
					+ ":  "
					+ Utils.notiFujiTiveName
					+ "rejected the capture made by"
					+ Utils.notiHuntername
					+ " .You are the Moderator of this game, Please make the final decision"; // intent.getStringExtra("me");

		}

		long when = System.currentTimeMillis(); // notification time

		// // message title

		NotificationManager notificationManager =

		(NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = new Notification(icon, Utils.notiTitle,
				when);

		Intent notificationIntent = new Intent(context, HomePage.class);

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);

		notification.setLatestEventInfo(context, "HuntGame", contentTitle,
				pendingIntent);

		notification.flags |= notification.FLAG_AUTO_CANCEL;

		notification.defaults |= Notification.DEFAULT_SOUND;

		// notification.vibrate=new long[] {100L};

		notificationManager.notify(1, notification);

		Utils.notificationReceived = true;

		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);

		WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");

		wl.acquire();

	}

	private void handleRegistration(Context context, String regId) {
		// TODO Auto-generated method stub
		Utils.registrationId = regId;
		SavePreferences("PUSHNOT", regId);
		Log.e("", "registration id : " + regId);

		// sendDeviceIdFuction(context,UtilsGcm.registrationId);

	}

	private void SavePreferences(String key, String value) {
		// TODO Auto-generated method stub
		
		SharedPreferences sharedPreferences = getSharedPreferences("myPrefs",
				MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

}
