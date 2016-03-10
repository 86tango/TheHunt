package com.huntgame.Main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.huntgame.UtilitiyFile.AppPreferences;

public class SplashScreen extends Activity {
	protected boolean _active = true;
	protected int _splashTime = 2000;
	AppPreferences appPrefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		appPrefs = new AppPreferences(getApplicationContext(), "SplashScreen");

		// appPrefs.SaveData("0", "SESSION_CHECK");////remove this line
		//

		Thread splashTread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while (_active && (waited < _splashTime)) {
						sleep(100);
						if (_active) {
							waited += 100;
						}
					}
				} catch (InterruptedException e) {

				} finally {

					if (appPrefs.getData("SESSION_CHECK").equals("1")) {

						System.out.println("HomePgae");
						startActivity(new Intent(SplashScreen.this,
								HomePage.class));
						finish();

					} else {
						System.out.println("LoginPage");

						startActivity(new Intent(SplashScreen.this,
								LoginPage.class));
						finish();

					}

					// stop();
				}
			}
		};
		splashTread.start();

	}

}
