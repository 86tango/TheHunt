package com.huntgame.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.huntgame.UtilitiyFile.AppPreferences;

public class SettingsPage extends Activity {

	ImageView login, push, gps, other;
	AppPreferences appPrefs;
	LocationManager manager;
	int buttonclick = 0;
	RadioGroup RadiusRadioGroup;
	RadioButton RadiusRadioButton_Miles, RadiusRadioButton_Km;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.settings_page);

		appPrefs = new AppPreferences(getApplicationContext(), "SplashScreen");
		manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		login = (ImageView) findViewById(R.id.login_check);
		push = (ImageView) findViewById(R.id.pushnotification_check);
		gps = (ImageView) findViewById(R.id.gps_check);
		other = (ImageView) findViewById(R.id.other_check);
		RadiusRadioGroup = (RadioGroup) findViewById(R.id.radiusradiogroup);
		RadiusRadioButton_Miles = (RadioButton) findViewById(R.id.miles);
		RadiusRadioButton_Km = (RadioButton) findViewById(R.id.km);

		if (appPrefs.getData("Radius").equals("Miles")) {
			RadiusRadioButton_Miles.setChecked(true);
		} else {
			RadiusRadioButton_Km.setChecked(true);
			appPrefs.SaveData("Km", "Radius");

		}

		RadiusRadioButton_Miles.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				appPrefs.SaveData("Miles", "Radius");

			}
		});

		RadiusRadioButton_Km.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				appPrefs.SaveData("Km", "Radius");

			}
		});

		if (appPrefs.getData("logincheck").equals("YES")) {

			login.setImageResource(R.drawable.yes);

		} else {
			login.setImageResource(R.drawable.no);

		}

		if (appPrefs.getData("pushcheck").equals("YES")) {

			push.setImageResource(R.drawable.yes);

		} else {
			push.setImageResource(R.drawable.no);

		}

		if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

			gps.setImageResource(R.drawable.yes);

		} else {
			gps.setImageResource(R.drawable.no);

		}

		if (appPrefs.getData("othercheck").equals("YES")) {

			other.setImageResource(R.drawable.yes);

		} else {
			other.setImageResource(R.drawable.no);

		}
		login.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (appPrefs.getData("logincheck").equals("YES")) {

					appPrefs.SaveData("NO", "logincheck");
					System.out.println(appPrefs.getData("logincheck"));

					login.setImageResource(R.drawable.no);

				} else {
					appPrefs.SaveData("YES", "logincheck");

					System.out.println(appPrefs.getData("logincheck"));
					login.setImageResource(R.drawable.yes);

				}

			}
		});

		push.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (appPrefs.getData("pushcheck").equals("YES")) {

					appPrefs.SaveData("NO", "pushcheck");
					System.out.println(appPrefs.getData("pushcheck"));

					push.setImageResource(R.drawable.no);

				} else {
					appPrefs.SaveData("YES", "pushcheck");
					System.out.println(appPrefs.getData("pushcheck"));

					push.setImageResource(R.drawable.yes);

				}

			}
		});

		gps.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				buttonclick = 1;

				if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

					startActivity(new Intent(
							android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

				} else {

					startActivity(new Intent(
							android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

				}

			}
		});

		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

		}

		other.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (appPrefs.getData("othercheck").equals("YES")) {

					appPrefs.SaveData("NO", "othercheck");
					other.setImageResource(R.drawable.no);

				} else {
					appPrefs.SaveData("YES", "othercheck");
					other.setImageResource(R.drawable.yes);

				}

			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		super.onResume();

		if (buttonclick == 1) {
			buttonclick = 0;
			if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

				gps.setImageResource(R.drawable.yes);

			} else {
				gps.setImageResource(R.drawable.no);

			}
		}
	}
}
