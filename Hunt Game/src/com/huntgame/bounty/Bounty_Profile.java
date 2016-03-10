package com.huntgame.bounty;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huntgame.Main.R;
import com.huntgame.UtilitiyFile.JsonClass;
import com.huntgame.UtilitiyFile.StaticValues;
import com.project.LazyLoading.ImageLoader;

public class Bounty_Profile extends Activity {

	TextView UserNameTXT, GameNameTXT, AgeTXT, SexTXT, HaircolorTXT,
			EyeColorTXT, EthinicityTXT, HeightTXT, WeightTXT, Location1TXT,
			Location2TXT, UserCurrentLocationTXT, LocationTXT;
	Animation rotation;
	JsonClass JsonClass_OBJ;
	String Name_S, Age_S, Weight_S, Height_S, Ethinicity_S, Status, Image_S,
			Sex_S, Hair_Colour_S, Eye_Colour_S, Latitude_S, Longitude_S,
			FaceBookName_S, GameName, CurrentLocation;
	ImageView UserPhotoIMGVIW;
	ImageLoader imageloader_obj;
	String UserID, GameID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bounty_profile);

		UserID = getIntent().getExtras().getString("userId");
		GameID = getIntent().getExtras().getString("GameId");
		GameName = getIntent().getExtras().getString("GameName");

		System.out.println("GameID" + GameID);

		UserNameTXT = (TextView) findViewById(R.id.username_txt);
		GameNameTXT = (TextView) findViewById(R.id.gamename_txt);
		AgeTXT = (TextView) findViewById(R.id.age_txt);
		SexTXT = (TextView) findViewById(R.id.sex_txt);
		HeightTXT = (TextView) findViewById(R.id.height_txt);
		WeightTXT = (TextView) findViewById(R.id.weight_txt);
		HaircolorTXT = (TextView) findViewById(R.id.hair_colour_txt);
		EyeColorTXT = (TextView) findViewById(R.id.eye_colour_txt);
		EthinicityTXT = (TextView) findViewById(R.id.ethinicity_txt);
		Location1TXT = (TextView) findViewById(R.id.location1_txt);
		Location2TXT = (TextView) findViewById(R.id.location2_txt);
		LocationTXT = (TextView) findViewById(R.id.locationname_txt);
		UserPhotoIMGVIW = (ImageView) findViewById(R.id.userphoto_imgView);
		JsonClass_OBJ = new JsonClass();
		new loadJsonInnerClass().execute();
	}

	class loadJsonInnerClass extends AsyncTask<Void, Integer, Void>

	{
		Dialog dialog = new Dialog(Bounty_Profile.this);

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			//

			imageloader_obj = new ImageLoader(Bounty_Profile.this);
			this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
			this.dialog.setContentView(R.layout.loadingicon);
			ImageView img = (ImageView) this.dialog
					.findViewById(R.id.progressBar1);
			img.setBackgroundResource(R.drawable.progress_img);

			rotation = AnimationUtils.loadAnimation(Bounty_Profile.this,
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
			System.out.println(StaticValues.UrlLink
					+ "userProfileJson.php?userId=" + UserID);
			JsonClass_OBJ.setAPI(StaticValues.UrlLink
					+ "userProfileJson.php?userId=" + UserID);
			//

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			String S = JsonClass_OBJ.getAPI();
			System.out.println(S);
			JSONObject jobj = null;
			try {
				jobj = new JSONObject(S);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			System.out.println("jobj" + jobj);
			if (jobj.has("Result")) // handle Se_wesam

			{
				try {
					Status = jobj.getString("Result");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (Status.equals("SUCCESS")) {
					try {
						if (jobj.has("userName")) {

							Name_S = jobj.getString("userName");

						}
						if (jobj.has("age")) {

							Age_S = jobj.getString("age");

						}
						if (jobj.has("height")) {

							Height_S = jobj.getString("height");

							Height_S.replace("feet", "'");
							Height_S.replace("inch", "\"");

						}

						if (jobj.has("weight")) {

							Weight_S = jobj.getString("weight");

						}

						if (jobj.has("ethnicity")) {

							Ethinicity_S = jobj.getString("ethnicity");

						}

						if (jobj.has("image")) {

							Image_S = jobj.getString("image");

						}

						if (jobj.has("sex")) {

							Sex_S = jobj.getString("sex");

						}

						if (jobj.has("hair_color")) {

							Hair_Colour_S = jobj.getString("hair_color");

						}

						if (jobj.has("eye_color")) {

							Eye_Colour_S = jobj.getString("eye_color");

							System.out.println("2");
						}

						if (jobj.has("fbName")) {

							FaceBookName_S = jobj.getString("fbName");

							System.out.println("2");
						}

						if (jobj.has("latitude1")) {

							Latitude_S = jobj.getString("latitude1");

							System.out.println("2");
						}

						if (jobj.has("longitude1")) {

							Longitude_S = jobj.getString("longitude1");

							System.out.println("2");
						}
						if (jobj.has("current_location")) {

							CurrentLocation = jobj
									.getString("current_location");

							System.out.println("current" + CurrentLocation);
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					Toast.makeText(getApplicationContext(),
							Status.replace("_", " "), 1500).show();
				}

			}

			if (Name_S.length() > 0) {
				UserNameTXT.setText(Name_S);
			}

			if (FaceBookName_S.length() > 0) {
				UserNameTXT.setText(FaceBookName_S);
			}
			if (Age_S.length() > 0) {

				AgeTXT.setText(Age_S);

			}

			if (Height_S.length() > 0) {
				HeightTXT.setText(Height_S);
			}

			if (Weight_S.length() > 0) {

				WeightTXT.setText(Weight_S);

			}
			if (Sex_S.length() > 0) {

				SexTXT.setText(Sex_S);

			}
			if (Eye_Colour_S.length() > 0) {

				EyeColorTXT.setText(Eye_Colour_S);

			}
			if (Hair_Colour_S.length() > 0) {

				HaircolorTXT.setText(Hair_Colour_S);
			}

			if (Ethinicity_S.length() > 0) {
				EthinicityTXT.setText(Ethinicity_S);

			}

			if (Image_S.length() > 0) {
				// Etinicity_TXT.setText(Ethinicity_S);
				imageloader_obj.clearCache();
				UserPhotoIMGVIW.setImageBitmap(null);

				imageloader_obj.DisplayImage(Image_S, UserPhotoIMGVIW);

			}

			if (Latitude_S.length() > 0) {
				Location1TXT.setText(Latitude_S);
			}

			if (Longitude_S.length() > 0) {
				Location2TXT.setText(Longitude_S);
			}

			if (CurrentLocation.length() > 0) {
				System.out.println("nn" + CurrentLocation);
				LocationTXT.setText(CurrentLocation);
			}

			GameNameTXT.setText(GameName);

			if (this.dialog.isShowing())
				this.dialog.dismiss();

			// TODO Auto-generated method stub

		}
	}

	public void Capture_fugitive_click(View v) {

		Intent in = new Intent(Bounty_Profile.this, Capture_Fugitive.class);
		in.putExtra("fugitiveID", UserID);
		in.putExtra("GameName", GameName);
		in.putExtra("GameId", GameID);
		in.putExtra("fugitiveName", Name_S);

		startActivity(in);

	}
}
