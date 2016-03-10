package com.huntgame.Gps;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.IBinder;

import com.huntgame.UtilitiyFile.AppPreferences;

public class TestService extends Service {
	String latitude;
	String longitude;
	String Addresslocation;
	AppPreferences appPrefs;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.

		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		appPrefs = new AppPreferences(getApplicationContext(), "SplashScreen");
		// Toast.makeText(getApplicationContext(), "Service Created", 1).show();
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		// Toast.makeText(getApplicationContext(), "Service Destroy", 1).show();
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		// Toast.makeText(getApplicationContext(), "Service Running ",
		// 1).show();
		location();
		return super.onStartCommand(intent, flags, startId);
	}

	private void category() {

		Addresslocation = Addresslocation.replaceAll(" ", "%20");

		// TODO Auto-generated method stub
		String sublink = "http://www.sicsglobal.com/projects/App_projects/hunt/update_location.php?userId="
				+ appPrefs.getData("USER_ID")
				+ "&lattitude="
				+ latitude
				+ "&longitude=" + longitude + "&location=" + Addresslocation;
		System.out.println("locaion" + sublink);
		URL obj_URL;
		try {
			obj_URL = new URL(sublink);

			InputStream in = obj_URL.openStream();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			StringBuilder sb = new StringBuilder();
			String line = reader.readLine();
			while (line != null) {
				sb.append(line);
				line = reader.readLine();
			}
			reader.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void location() {
		// TODO Auto-generated method stub
		GPSTracker mGPS = new GPSTracker(getApplicationContext());
		if (mGPS.canGetLocation) {
			double mLat = mGPS.getLatitude();
			double mLong = mGPS.getLongitude();
			latitude = mLat + "";
			longitude = mLong + "";
			Addresslocation = "";
			Geocoder geocoder = new Geocoder(getApplicationContext(),
					Locale.ENGLISH);
			try {
				List<Address> addresses = geocoder.getFromLocation(mLat, mLong,
						1);
				if (addresses != null) {
					Address returnedAddress = addresses.get(0);
					Addresslocation = returnedAddress.getSubLocality() + ","
							+ returnedAddress.getSubAdminArea() + ","
							+ returnedAddress.getCountryName();
					category();
				} else {
					System.out.println("No Address returned!");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Canont get Address!");
			}
			// Toast.makeText(getApplicationContext(),
			// Addresslocation + " " + latitude + " " + longitude, 1)
			// .show();
		} else {
			// Toast.makeText(getApplicationContext(), "No location found", 1)
			// .show();
		}
	}
}
