package com.huntgame.Main;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionEvents;
import com.facebook.android.SessionEvents.AuthListener;
import com.facebook.android.SessionEvents.LogoutListener;
import com.facebook.android.SessionStore;
import com.facebook.android.Utility;
import com.huntgame.UtilitiyFile.AppPreferences;
import com.huntgame.UtilitiyFile.JsonClass;
import com.huntgame.UtilitiyFile.StaticValues;

public class LoginPage extends Activity {

	String[] permissions = { "offline_access", "publish_stream", "user_photos",
			"publish_checkins", "photo_upload", "email" };
	public static final String APP_ID = "637669726260011";
	Facebook sa = new Facebook(APP_ID);
	private Handler mHandler;
	ProgressDialog dialog;
	JsonClass JsonClass_OBJ;
	Animation rotation;
	String Name_S, Email_S, fb_ID_S, Status, Password_S, FB_Imagepath;
	AppPreferences appPrefs;
	EditText Email_edttxt, Password_edttxt;
	int fb_flag = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_page);

		Email_edttxt = (EditText) findViewById(R.id.username_edttxt);
		Password_edttxt = (EditText) findViewById(R.id.password_edttxt);

		mHandler = new Handler();

		appPrefs = new AppPreferences(getApplicationContext(), "SplashScreen");
		JsonClass_OBJ = new JsonClass();

		rotation = AnimationUtils
				.loadAnimation(this, R.anim.clockwise_rotation);
		rotation.setRepeatCount(Animation.INFINITE);

		/* Fb controls */
		Utility.mFacebook = new Facebook(APP_ID);
		Utility.mAsyncRunner = new AsyncFacebookRunner(Utility.mFacebook);
		SessionStore.restore(Utility.mFacebook, this);

		SessionEvents.addAuthListener(new FbAPIsAuthListener());
		SessionEvents.addLogoutListener(new FbAPIsLogoutListener());

	}

	public void Register_Click(View v) {

		startActivity(new Intent(LoginPage.this, RegistrationPage.class));
	}

	public void SignIn_Click(View v) {
		Email_S = Email_edttxt.getText().toString();
		Password_S = Password_edttxt.getText().toString();

		if ((Email_S.length() == 0) || (Password_S.length() == 0))

		{
			Toast.makeText(getApplicationContext(), "All fields are mandatory",
					1500).show();

		} else {
			Loaddata_Method();
		}

	}

	public void facebookLogin_Click(View v) {
		if (isNetworkAvailable(getApplicationContext())) {

			fb_flag = 1;

			if (!Utility.mFacebook.isSessionValid()) {

				// Util.showAlert(this, "Warning",
				// "You must first log in.");

				AlertDialog.Builder builder = new AlertDialog.Builder(
						LoginPage.this);
				builder.setCancelable(true);

				builder.setTitle("Login Hunt Game Using Facebook");
				builder.setInverseBackgroundForced(true);
				builder.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {

								Utility.mFacebook.authorize(LoginPage.this,
										permissions, -1,
										new FbLoginDialogListener());
								dialog.dismiss();
							}
						});
				builder.setNegativeButton("No",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				AlertDialog alert = builder.create();
				alert.show();

			} else {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						LoginPage.this);
				builder.setCancelable(true);

				builder.setTitle("Log out facebook");
				builder.setInverseBackgroundForced(true);
				builder.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog_on,
									int which) {

								dialog_on.dismiss();
								dialog = ProgressDialog.show(LoginPage.this,
										"", "Logging out from Hunt Game", true,
										true);
								// SessionEvents.onLogoutBegin();
								AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(
										Utility.mFacebook);
								asyncRunner.logout(getApplicationContext(),
										new LogoutRequestListener());

							}
						});
				builder.setNegativeButton("No",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog_on,
									int which) {
								dialog_on.dismiss();
							}
						});
				AlertDialog alert = builder.create();
				alert.show();

			}
		} else {

			Toast.makeText(getApplicationContext(), "No Internet Connection",
					3000).show();

		}
	}

	// /////////////////////fb data///////////////////////////
	public class FbAPIsAuthListener implements AuthListener {

		public void onAuthSucceed() {
			requestUserData();
		}

		public void onAuthFail(String error) {

		}
	}

	public void requestUserData() {

		Bundle params = new Bundle();
		params.putString("fields", "name, picture,email");
		Utility.mAsyncRunner.request("me", params, new UserRequestListener());

	}

	private final class FbLoginDialogListener implements DialogListener {
		public void onComplete(Bundle values) {
			SessionStore.save(Utility.mFacebook, LoginPage.this);
			requestUserData();

		}

		public void onFacebookError(FacebookError error) {

		}

		public void onError(DialogError error) {

		}

		public void onCancel() {

		}
	}

	public class UserRequestListener extends BaseRequestListener {

		public void onComplete(String response) {
			// TODO Auto-generated method stub
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(response);

				System.out.println("jsonObject" + jsonObject);
				JSONObject picobj = new JSONObject(
						jsonObject.getString("picture"));

				JSONObject dataobj = new JSONObject(picobj.getString("data"));

				FB_Imagepath = dataobj.getString("url").replace(" ", "%20");

				Name_S = jsonObject.getString("name").replace(" ", "%20");

				Email_S = jsonObject.getString("email").replace(" ", "%20");

				fb_ID_S = jsonObject.getString("id").replace(" ", "%20");

				System.out.println("id" + fb_ID_S);

				Utility.userUID = jsonObject.getString("id");

				mHandler.post(new Runnable() {

					public void run() {

						Loaddata_Method();
					}
				});

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void Loaddata_Method() {

		new loadJsonInnerClass().execute();
	}

	private class LogoutRequestListener extends BaseRequestListener {

		public void onComplete(String response) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			SessionStore.clear(getApplicationContext());
			// mHandler.post(new Runnable() {
			//
			// public void run() {
			// SessionEvents.onLogoutFinish();
			// }
			// });
		}
	}

	public class FbAPIsLogoutListener implements LogoutListener {

		public void onLogoutBegin() {

		}

		public void onLogoutFinish() {

		}
	}

	// //////////////////////Check InternetStatus////////////////////////////

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	class loadJsonInnerClass extends AsyncTask<Void, Integer, Void>

	{
		Dialog dialog = new Dialog(LoginPage.this);

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			//
			this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
			this.dialog.setContentView(R.layout.loadingicon);
			ImageView img = (ImageView) this.dialog
					.findViewById(R.id.progressBar1);
			img.setBackgroundResource(R.drawable.progress_img);

			rotation = AnimationUtils.loadAnimation(LoginPage.this,
					R.anim.clockwise_rotation);
			rotation.setRepeatCount(Animation.INFINITE);

			this.dialog.show();
			img.startAnimation(rotation);
			this.dialog.setCanceledOnTouchOutside(false);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			// this.dialog.setCanceledOnTouchOutside(false);

			if (fb_flag == 1) {
				JsonClass_OBJ.setAPI(StaticValues.UrlLink
						+ "fbLoginJson.php?fbId=" + fb_ID_S + "&fbName="
						+ Name_S + "&email=" + Email_S + "&imagename="
						+ FB_Imagepath);

				System.out.println(StaticValues.UrlLink
						+ "fbLoginJson.php?fbId=" + fb_ID_S + "&fbName="
						+ Name_S + "&email=" + Email_S + "&imagename="
						+ FB_Imagepath);
			} else {
				JsonClass_OBJ.setAPI(StaticValues.UrlLink
						+ "loginJson.php?email=" + Email_S + "&password="
						+ Password_S);
			}
			//
			// TODO Auto-generated method stub
			String S = JsonClass_OBJ.getAPI();
			System.out.println(S);
			JSONObject jobj = null;
			try {
				jobj = new JSONObject(S);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (jobj.has("Result")) // handle Se_wesam

			{
				try {
					Status = jobj.getString("Result");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (Status.equals("SUCCESS") || Status.equals("EXISTS")
						|| Status.equals("INSERTED")) {

					if (jobj.has("userId")) {

						try {

							appPrefs.SaveData(jobj.getString("userId"),
									"USER_ID");

							System.out.println(jobj.getString("userId"));

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			if (Status.equals("SUCCESS") || Status.equals("EXISTS")
					|| Status.equals("INSERTED")) {

				appPrefs.SaveData("1", "SESSION_CHECK");
				startActivity(new Intent(LoginPage.this, HomePage.class));
				finish();

			}

			else {
				Toast.makeText(getApplicationContext(),
						"Incorrect userName/Password", 1500).show();

			}

			if (this.dialog.isShowing()) {
				this.dialog.dismiss();

			}

		}
	}
}
