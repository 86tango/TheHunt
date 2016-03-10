package com.huntgame.bounty;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.huntgame.Main.HomePage;
import com.huntgame.Main.R;
import com.huntgame.UtilitiyFile.AppPreferences;
import com.huntgame.UtilitiyFile.JsonClass;

public class Capture_Fugitive extends Activity {
	Uri imageUri;
	private static final int TAKE_PICTURE = 1;
	ImageView Photo_imgView;
	ProgressBar progressBarImageedit;
	File photopath;
	String Unedited_Img_Path;
	String Unedited_Img_Name;
	Bitmap Main_Bitmap;
	ConnectivityManager conMgr;
	URL link;
	Animation rotation;
	String fugitiveID, GameName, GameID, fugitiveName;
	AppPreferences appPrefs;
	JsonClass JsonClass_OBJ;
	TextView gamenametxt, usernametxt;
	EditText megedt;
	String msg = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.capture_fugitive);

		Photo_imgView = (ImageView) findViewById(R.id.photoview);
		gamenametxt = (TextView) findViewById(R.id.GameName);
		usernametxt = (TextView) findViewById(R.id.UserName);
		megedt = (EditText) findViewById(R.id.msg_edt);
		appPrefs = new AppPreferences(getApplicationContext(), "SplashScreen");
		fugitiveID = getIntent().getExtras().getString("fugitiveID");
		GameName = getIntent().getExtras().getString("GameName");
		GameID = getIntent().getExtras().getString("GameId");
		fugitiveName = getIntent().getExtras().getString("fugitiveName");

		gamenametxt.setText(GameName);
		usernametxt.setText(fugitiveName);
		Unedited_Img_Name = GameID + "_" + fugitiveID
				+ String.valueOf(System.currentTimeMillis()) + ".jpg";

		File dir = new File(Environment.getExternalStorageDirectory().getPath()
				+ "/Hunt/");
		try {
			dir.mkdir();
		} catch (Exception e) {
			e.printStackTrace();

		}
		Unedited_Img_Path = Environment.getExternalStorageDirectory().getPath()
				+ "/Hunt/" + Unedited_Img_Name;
		photopath = new File(Unedited_Img_Path);

		// Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		// intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION,
		// ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photopath));
		// imageUri = Uri.fromFile(photopath);
		// startActivityForResult(intent, TAKE_PICTURE);
		//
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

		intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION,
				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photopath));
		imageUri = Uri.fromFile(photopath);
		startActivityForResult(intent, TAKE_PICTURE);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("onActivity" + requestCode);
		switch (requestCode) {

		case TAKE_PICTURE:
			if (resultCode == Activity.RESULT_OK) {

				try {

					BitmapFactory.Options options = new BitmapFactory.Options();
					// will results in a much smaller image than the original
					options.inSampleSize = 12;

					Main_Bitmap = BitmapFactory.decodeFile(Unedited_Img_Path,
							options);

					Photo_imgView.setImageBitmap(Main_Bitmap);

				} catch (Exception e) {
					Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
							.show();
					Log.e("Camera", e.toString());
				}

			} else {
				finish();
			}

		}
	}

	public void sendbutton(View v) {

		msg = megedt.getText().toString();
		msg = msg.replace(" ", "%20");
		new UploadImage_InnerClass(this).execute();

	}

	class UploadImage_InnerClass extends AsyncTask<Void, Integer, Void> {
		Context context;
		Dialog dialog = new Dialog(Capture_Fugitive.this);

		public UploadImage_InnerClass(Context c) {
			// TODO Auto-generated constructor stub

			this.context = c;

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
			this.dialog.setContentView(R.layout.loadingicon);
			ImageView img = (ImageView) this.dialog
					.findViewById(R.id.progressBar1);
			img.setBackgroundResource(R.drawable.progress_img);

			rotation = AnimationUtils.loadAnimation(Capture_Fugitive.this,
					R.anim.clockwise_rotation);
			rotation.setRepeatCount(Animation.INFINITE);

			this.dialog.show();
			img.startAnimation(rotation);
			this.dialog.setCanceledOnTouchOutside(false);
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub

			if (this.dialog.isShowing()) {
				this.dialog.dismiss();

			}

			Toast.makeText(getApplicationContext(), "Captured image send", 1000)
					.show();

			Intent intent = new Intent(getApplicationContext(), HomePage.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			this.dialog.setCanceledOnTouchOutside(false);

			doFileUpload(Unedited_Img_Path);

			return null;

		}
	}

	// ///////////////////// network check

	public Boolean checkconnection() {
		NetworkInfo i = conMgr.getActiveNetworkInfo();
		if (i == null)
			return false;
		if (!i.isConnected())
			return false;
		if (!i.isAvailable())
			return false;
		return true;
	}

	private void doFileUpload(String path) {

		System.out.println("doupload");
		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;
		DataInputStream inputStream = null;
		String pathToOurFile = path;
		String urlServer = "http://sicsglobal.com/projects/App_projects/hunt/caught_fujitive_list.php?hunterId="
				+ appPrefs.getData("USER_ID")
				+ "&fujitiveId="
				+ fugitiveID
				+ "&gameId="
				+ GameID
				+ "&message="
				+ msg
				+ "&uploadImage="
				+ Unedited_Img_Name;

		System.out
				.println("http://sicsglobal.com/projects/App_projects/hunt/caught_fujitive_list.php?hunterId="
						+ appPrefs.getData("USER_ID")
						+ "&fujitiveId="
						+ fugitiveID
						+ "&gameId="
						+ GameID
						+ "&message="
						+ msg
						+ "&uploadImage=" + Unedited_Img_Name);

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;

		try {

			System.out.println("one");
			FileInputStream fileInputStream = new FileInputStream(new File(
					pathToOurFile));

			URL url = new URL(urlServer);
			connection = (HttpURLConnection) url.openConnection();
			System.out.println("two");
			// Allow Inputs & Outputs
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			System.out.println("three");
			// Enable POST method
			connection.setRequestMethod("POST");

			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			System.out.println("four");
			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream
					.writeBytes("Content-Disposition: form-data; name=\"uploadImage\";filename=\""
							+ pathToOurFile + "\"" + lineEnd);
			outputStream.writeBytes(lineEnd);
			System.out.println("five");
			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// Read file
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0) {
				System.out.println("six");
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens
					+ lineEnd);
			System.out.println("seven");
			// Responses from the server (code and message)
			int serverResponseCode = connection.getResponseCode();
			String serverResponseMessage = connection.getResponseMessage();

			System.out.println("test   -----" + serverResponseCode);
			System.out.println("test-----" + serverResponseMessage);

			fileInputStream.close();
			outputStream.flush();
			outputStream.close();
		} catch (Exception ex) {
			// Exception handling
		}
	}
}
