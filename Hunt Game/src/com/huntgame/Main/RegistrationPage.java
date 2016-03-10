package com.huntgame.Main;

import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.huntgame.UtilitiyFile.AppPreferences;
import com.huntgame.UtilitiyFile.JsonClass;
import com.huntgame.UtilitiyFile.StaticValues;

public class RegistrationPage extends Activity {

	EditText Username_EDTTXT, Email_EDTTXT, Password_EDTTXT,
			ConfirmPassword_EDTTXT;
	String Username_S, Email_S, Password_S, ConfirmPassword_S;
	String Status;
	JsonClass JsonClass_OBJ;
	Animation rotation;
	AppPreferences appPrefs;
	JSONObject jobj;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration_page);
		appPrefs = new AppPreferences(getApplicationContext(), "SplashScreen");

		JsonClass_OBJ = new JsonClass();
		Username_EDTTXT = (EditText) findViewById(R.id.UserName_edttxt);
		Email_EDTTXT = (EditText) findViewById(R.id.Email_edttxt);
		Password_EDTTXT = (EditText) findViewById(R.id.password_edttxt);
		ConfirmPassword_EDTTXT = (EditText) findViewById(R.id.confirmPassword_edttxt);

		rotation = AnimationUtils
				.loadAnimation(this, R.anim.clockwise_rotation);
		rotation.setRepeatCount(Animation.INFINITE);
	}

	public void Register_btn(View v) {

		Username_S = Username_EDTTXT.getText().toString();
		Email_S = Email_EDTTXT.getText().toString();
		Password_S = Password_EDTTXT.getText().toString();
		ConfirmPassword_S = ConfirmPassword_EDTTXT.getText().toString();

		if (Username_S.length() == 0 || Email_S.length() == 0
				|| Password_S.length() == 0 || ConfirmPassword_S.length() == 0) {
			Toast.makeText(getApplicationContext(), "Al fileds are mandatory",
					1000).show();
		} else if (!checkEmail(Email_S)) {
			Toast.makeText(getApplicationContext(), "Enter valid Email id",
					1000).show();
		} else if (!Password_S.equals(ConfirmPassword_S)) {
			Toast.makeText(getApplicationContext(), "Password mismatch", 1000)
					.show();

		} else {

			new loadJsonInnerClass().execute();
		}

	}

	private boolean checkEmail(String email) {
		return EMAIL_PATTERN.matcher(email).matches();
	}

	public final Pattern EMAIL_PATTERN = Pattern
			.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

	class loadJsonInnerClass extends AsyncTask<Void, Integer, Void>

	{
		Dialog dialog = new Dialog(RegistrationPage.this);

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
			this.dialog.setContentView(R.layout.loadingicon);
			ImageView img = (ImageView) this.dialog
					.findViewById(R.id.progressBar1);
			img.setBackgroundResource(R.drawable.progress_img);

			rotation = AnimationUtils.loadAnimation(RegistrationPage.this,
					R.anim.clockwise_rotation);
			rotation.setRepeatCount(Animation.INFINITE);

			this.dialog.show();
			img.startAnimation(rotation);
			this.dialog.setCanceledOnTouchOutside(false);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			this.dialog.setCanceledOnTouchOutside(false);

			JsonClass_OBJ.setAPI(StaticValues.UrlLink
					+ "registerJson.php?userName=" + Username_S + "&email="
					+ Email_S + "&password=" + Password_S);

			String S = JsonClass_OBJ.getAPI();
			System.out.println("JsonObj" + S);
			jobj = null;
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

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			if (Status.equals("INSERTED")) {

				if (jobj.has("userId")) {

					finish();

				}

			} else {

				System.out.println("Status" + Status);
				Toast.makeText(getApplicationContext(),
						Status.replace("_", " "), 1500).show();
			}

			if (this.dialog.isShowing()) {
				this.dialog.dismiss();

			}
			// TODO Auto-generated method stub

		}
	}

}
