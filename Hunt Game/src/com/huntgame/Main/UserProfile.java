package com.huntgame.Main;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.huntgame.UtilitiyFile.AppPreferences;
import com.huntgame.UtilitiyFile.JsonClass;
import com.huntgame.UtilitiyFile.StaticValues;
import com.project.LazyLoading.ImageLoader;

public class UserProfile extends Activity {

	InputStream imageStream;
	Bitmap bitmap, bmp;
	ImageView Userphoto_ImageView;
	String path, extensions;
	JsonClass JsonClass_OBJ;
	Animation rotation;
	String Name_S, Age_S, Weight_S, Height_S, Status, Image_S, Sex_S,
			Hair_Colour_S, Eye_Colour_S, Latitude_S, Longitude_S,
			FaceBookName_S;

	String urlServer;
	AppPreferences appPrefs;
	ImageLoader imageloader_obj;
	int newimage_selected_checkflag = 0;
	TextView Username_TXT;

	MyAdapter myadapter_sex_obj, myadapter_eyecolour_obj,
			myadapter_haircolour_obj, myadapter_age_obj, myadapter_weight_obj,
			myadapter_height_obj;

	Spinner Sex_spinner, Eye_Colour_Spinner, Hair_Colour_Spinner, Age_Spinner,
			Weight_Spinner, Height_Spinner;
	TextView add_changephoto_txt;

	ArrayList<String> Sex_Arr = new ArrayList<String>() {
		{

			add("-Select-");
			add("Male");
			add("Female");
		}
	};
	ArrayList<String> Eye_Colour_Arr = new ArrayList<String>() {
		{
			add("-Select-");
			add("Amber");
			add("Blue");
			add("Brown");
			add("Gray");
			add("Green");
			add("Hazel");

		}
	};

	ArrayList<String> Hair_Colour_Arr = new ArrayList<String>() {
		{
			add("-Select-");
			add("Black");
			add("Brown");
			add("Blond");
			add("Auburn");
			add("Chestnut");
			add("Gray and white");

		}
	};

	ArrayList<String> Height_Arr = new ArrayList<String>();
	ArrayList<String> Age_Arr = new ArrayList<String>();
	ArrayList<String> Weight_Arr = new ArrayList<String>();

	public ArrayAdapter<String> adapter, adapter2;
	public AutoCompleteTextView Location1_AutoTextView, Location2_AutoTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.user_profile);

		appPrefs = new AppPreferences(getApplicationContext(), "SplashScreen");
		JsonClass_OBJ = new JsonClass();

		Username_TXT = (TextView) findViewById(R.id.txt_username);

		Sex_spinner = (Spinner) findViewById(R.id.sex_spinner);
		Eye_Colour_Spinner = (Spinner) findViewById(R.id.eye_colour_spinner);
		Hair_Colour_Spinner = (Spinner) findViewById(R.id.hair_colour_spinner);
		Age_Spinner = (Spinner) findViewById(R.id.age_spinner);
		Weight_Spinner = (Spinner) findViewById(R.id.weight_spinner);
		Height_Spinner = (Spinner) findViewById(R.id.height_spinner);
		add_changephoto_txt = (TextView) findViewById(R.id.add_changephoto_txt);
		Location1_AutoTextView = (AutoCompleteTextView) findViewById(R.id.location1_Autocomplete);
		Location2_AutoTextView = (AutoCompleteTextView) findViewById(R.id.location2_Autocomplete);
		adapter = new ArrayAdapter<String>(this, R.layout.item_list);
		adapter2 = new ArrayAdapter<String>(this, R.layout.item_list);
		rotation = AnimationUtils
				.loadAnimation(this, R.anim.clockwise_rotation);
		rotation.setRepeatCount(Animation.INFINITE);
		imageloader_obj = new ImageLoader(UserProfile.this);
		Userphoto_ImageView = (ImageView) findViewById(R.id.userphoto_imgView);

		File dir = new File(Environment.getExternalStorageDirectory().getPath()
				+ "/The Hunt/");
		try {
			dir.mkdir();
		} catch (Exception e) {
			e.printStackTrace();

		}

		//

		Height_Arr.add("-Select-");
		Age_Arr.add("-Select-");
		Weight_Arr.add("-Select-");

		for (int i = 4; i <= 100; i++) {

			Age_Arr.add(Integer.valueOf(i).toString());
		}
		for (int i = 50; i <= 350; i++) {

			Weight_Arr.add(Integer.valueOf(i).toString());
		}

		for (int i = 2; i < 8; i++) {

			for (int j = 0; j < 12; j++) {

				Height_Arr.add(i + "\'" + j + "\"");

			}
		}
		new loadJsonInnerClass().execute();

		myadapter_sex_obj = new MyAdapter(UserProfile.this,
				R.layout.spinneradapter, Sex_Arr);
		myadapter_eyecolour_obj = new MyAdapter(UserProfile.this,
				R.layout.spinneradapter, Eye_Colour_Arr);

		myadapter_haircolour_obj = new MyAdapter(UserProfile.this,
				R.layout.spinneradapter, Hair_Colour_Arr);
		myadapter_age_obj = new MyAdapter(UserProfile.this,
				R.layout.spinneradapter, Age_Arr);
		myadapter_weight_obj = new MyAdapter(UserProfile.this,
				R.layout.spinneradapter, Weight_Arr);
		myadapter_height_obj = new MyAdapter(UserProfile.this,
				R.layout.spinneradapter, Height_Arr);

		Sex_spinner.setAdapter(myadapter_sex_obj);
		Eye_Colour_Spinner.setAdapter(myadapter_eyecolour_obj);
		Hair_Colour_Spinner.setAdapter(myadapter_haircolour_obj);
		Age_Spinner.setAdapter(myadapter_age_obj);
		Weight_Spinner.setAdapter(myadapter_weight_obj);
		Height_Spinner.setAdapter(myadapter_height_obj);

		Sex_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		Eye_Colour_Spinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						// TODO Auto-generated method stub

					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		Hair_Colour_Spinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						// TODO Auto-generated method stub

					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		Weight_Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		Height_Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		adapter.setNotifyOnChange(true);
		Location1_AutoTextView.setAdapter(adapter);
		Location1_AutoTextView.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (count % 3 == 1) {
					adapter.clear();
					GetPlaces task = new GetPlaces();

					// now pass the argument in the textview to the task
					task.execute(Location1_AutoTextView.getText().toString());
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			public void afterTextChanged(Editable s) {

			}
		});

		Location1_AutoTextView
				.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						Toast.makeText(UserProfile.this, " selected",
								Toast.LENGTH_LONG).show();

					}
				});

		adapter2.setNotifyOnChange(true);
		Location2_AutoTextView.setAdapter(adapter2);
		Location2_AutoTextView.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (count % 3 == 1) {
					adapter2.clear();
					GetPlaces1 task = new GetPlaces1();

					// now pass the argument in the textview to the task
					task.execute(Location2_AutoTextView.getText().toString());
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			public void afterTextChanged(Editable s) {

			}
		});

		Location2_AutoTextView
				.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						Toast.makeText(UserProfile.this, " selected",
								Toast.LENGTH_LONG).show();

					}
				});

	}

	public class MyAdapter extends ArrayAdapter<String> {

		ArrayList<String> Adapter_Arr = new ArrayList<String>();

		public MyAdapter(Context context, int textViewResourceId,
				ArrayList<String> country_id_List) {

			super(context, textViewResourceId, country_id_List);

			this.Adapter_Arr = country_id_List;
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		public View getCustomView(int position, View convertView,
				ViewGroup parent) {

			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.spinneradapter, parent, false);
			TextView label = (TextView) row.findViewById(R.id.languageName);

			label.setText(Adapter_Arr.get(position));

			return row;
		}

	}

	public void Addphoto_click(View v)

	{

		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, 1111);

	}

	public void Save_click(View v)

	{
		Name_S = Username_TXT.getText().toString();
		Age_S = Age_Spinner.getSelectedItem().toString();
		Weight_S = Weight_Spinner.getSelectedItem().toString();
		Height_S = Height_Spinner.getSelectedItem().toString();
		Sex_S = Sex_spinner.getSelectedItem().toString();
		Hair_Colour_S = Hair_Colour_Spinner.getSelectedItem().toString();
		Eye_Colour_S = Eye_Colour_Spinner.getSelectedItem().toString();
		Latitude_S = Location1_AutoTextView.getText().toString()
				.replaceAll(" ", "%20");
		Longitude_S = Location2_AutoTextView.getText().toString()
				.replaceAll(" ", "%20");

		if (Name_S.length() == 0) {
			Toast.makeText(getApplicationContext(), "UserName mandatory", 1000)
					.show();
		}

		new UploadImage_InnerClass().execute();

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1111) {
			if (resultCode == RESULT_OK) {
				System.out.println("haiiiiii");
				newimage_selected_checkflag = 1;
				Uri selectedImage = data.getData();
				System.out.println("SHARTHAHAHAHhs");
				try {

					imageStream = getContentResolver().openInputStream(
							selectedImage);

					path = getRealPathFromURI(selectedImage);
					String filenameArray[] = path.split("\\.");
					extensions = filenameArray[filenameArray.length - 1];

					try {

						FileOutputStream out;
						if (extensions.equals("jpg")) {
							System.out.println("inside jpg");

							out = new FileOutputStream(new File(Environment
									.getExternalStorageDirectory().getPath()
									+ "/The Hunt/huntprofilepic_"
									+ appPrefs.getData("USER_ID") + ".jpg"));
							Bitmap b = decodeFile(new File(path));
							b.compress(Bitmap.CompressFormat.JPEG, 50, out);
							out.flush();
							out.close();
							path = Environment.getExternalStorageDirectory()
									.getPath()
									+ "/The Hunt/huntprofilepic_"
									+ appPrefs.getData("USER_ID") + ".jpg";
							// newpath = "/sdcard/huntprofilepic.jpg";
							//
							// String paths = getRealPathFromURI(selectedImage);
							// // Toast.makeText(getApplicationContext(),
							// // "path=" + paths, Toast.LENGTH_LONG).show();
							// System.out.println("paths" + paths);
							//
							// String[] s = paths.split("/");
							// System.out.println(s[s.length - 1]);
							// imageName1 = s[s.length - 1];
							// imageName1 = imageName1.replace(" ", "");
							// System.out.println("imagename" + imageName1);

							Userphoto_ImageView
									.setImageBitmap(decodeFile(new File(path)));

						}
						if (extensions.equals("png")) {
							System.out.println("inside png");
							out = new FileOutputStream(new File(Environment
									.getExternalStorageDirectory().getPath()
									+ "/The Hunt/huntprofilepic_"
									+ appPrefs.getData("USER_ID") + ".jpg"));
							Bitmap b = decodeFile(new File(path));
							b.compress(Bitmap.CompressFormat.PNG, 50, out);
							out.flush();
							out.close();
							path = Environment.getExternalStorageDirectory()
									.getPath()
									+ "/The Hunt/huntprofilepic_"
									+ appPrefs.getData("USER_ID") + ".jpg";
							// newpath = "/sdcard/huntprofilepic.png";
							//
							// String paths = getRealPathFromURI(selectedImage);
							// System.out.println("paths" + paths);
							//
							// String[] s = paths.split("/");
							// System.out.println(s[s.length - 1]);
							// imageName1 = s[s.length - 1];
							// imageName1 = imageName1.replace(" ", "");
							// System.out.println("imagename" + imageName1);

							Userphoto_ImageView
									.setImageBitmap(decodeFile(new File(path)));

						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				} catch (FileNotFoundException e) {

					e.printStackTrace();
				}

			} else {
				System.out.println("erorrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
			}

		}

		// if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
		// BitmapFactory.Options options = new BitmapFactory.Options();
		// // will results in a much smaller image than the original
		// options.inSampleSize = 12;
		//
		// Main_Bitmap = BitmapFactory.decodeFile(Unedited_Img_Path,
		// optiwssdons);
		//
		// seletedimg_view.setImageBitmap(Main_Bitmap);
		//
		// System.out.println("Unedited_Img_Path" + Unedited_Img_Path);
		// path = Unedited_Img_Path;
		//
		// }

		// if (requestCode == 1111 && resultCode == RESULT_OK) {
		// newimage_selected_checkflag = 1;
		//
		// Uri selectedImage = data.getData();
		//
		// try {
		// imageStream = getContentResolver().openInputStream(
		// selectedImage);
		//
		// } catch (FileNotFoundException e) {
		//
		// e.printStackTrace();
		// }
		//
		// try
		//
		// {
		// String[] filePathColumn = { MediaStore.Images.Media.DATA };
		// Cursor cursor = getContentResolver().query(selectedImage,
		// filePathColumn, null, null, null);
		// cursor.moveToFirst();
		// int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		// String filePath = cursor.getString(columnIndex);
		// cursor.close();
		//
		// bmp = BitmapFactory.decodeFile(filePath);
		//
		// Userphoto_ImageView.setImageBitmap(bmp);
		//
		// } catch (Exception c) {
		// }
		//
		// path = getRealPathFromURI(selectedImage);
		//
		// }
		//
	}

	// /convert image to string
	public String getRealPathFromURI(Uri contentUri) {

		// can post image
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, // Which columns to
														// return
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();

		return cursor.getString(column_index);

	}

	class loadJsonInnerClass extends AsyncTask<Void, Integer, Void>

	{
		Dialog dialog = new Dialog(UserProfile.this);

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			//
			this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
			this.dialog.setContentView(R.layout.loadingicon);
			ImageView img = (ImageView) this.dialog
					.findViewById(R.id.progressBar1);
			img.setBackgroundResource(R.drawable.progress_img);

			rotation = AnimationUtils.loadAnimation(UserProfile.this,
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

			JsonClass_OBJ.setAPI(StaticValues.UrlLink
					+ "userProfileJson.php?userId="
					+ appPrefs.getData("USER_ID"));
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
				Username_TXT.setText(Name_S);
			}

			if (FaceBookName_S.length() > 0) {
				Username_TXT.setText(FaceBookName_S);
			}
			if (Age_S.length() > 0) {

				int i = Age_Arr.indexOf(Age_S);
				Age_Spinner.setSelection(i);
			}

			if (Height_S.length() > 0) {
				int i = Height_Arr.indexOf(Height_S);
				Height_Spinner.setSelection(i);
			}

			if (Weight_S.length() > 0) {

				int i = Weight_Arr.indexOf(Weight_S);
				Weight_Spinner.setSelection(i);

			}
			if (Sex_S.length() > 0) {

				int i = Sex_Arr.indexOf(Sex_S);
				Sex_spinner.setSelection(i);

			}
			if (Eye_Colour_S.length() > 0) {

				int i = Eye_Colour_Arr.indexOf(Eye_Colour_S);
				Eye_Colour_Spinner.setSelection(i);

			}
			if (Hair_Colour_S.length() > 0) {

				int i = Hair_Colour_Arr.indexOf(Hair_Colour_S);
				Hair_Colour_Spinner.setSelection(i);

			}

			if (Image_S.length() > 0) {
				// Etinicity_TXT.setText(Ethinicity_S);
				imageloader_obj.clearCache();
				Userphoto_ImageView.setImageBitmap(null);

				imageloader_obj.DisplayImage(Image_S, Userphoto_ImageView);
				add_changephoto_txt.setText("Add User Photo");
			} else if (Image_S.equals("null")) {
				add_changephoto_txt.setText("Add User Photo");

			} else {
				add_changephoto_txt.setText("Change User Photo");

			}

			if (Latitude_S.length() > 0) {
				Location1_AutoTextView.setText(Latitude_S);
			}

			if (Longitude_S.length() > 0) {
				Location2_AutoTextView.setText(Longitude_S);
			}

			if (this.dialog.isShowing())
				this.dialog.dismiss();

			// TODO Auto-generated method stub

		}
	}

	class UploadImage_InnerClass extends AsyncTask<Void, Integer, Void> {
		Context context;
		Dialog dialog = new Dialog(UserProfile.this);

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
			this.dialog.setContentView(R.layout.loadingicon);
			ImageView img = (ImageView) this.dialog
					.findViewById(R.id.progressBar1);
			img.setBackgroundResource(R.drawable.progress_img);

			rotation = AnimationUtils.loadAnimation(UserProfile.this,
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

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			this.dialog.setCanceledOnTouchOutside(false);

			if (newimage_selected_checkflag == 1) {
				newimage_selected_checkflag = 0;
				doFileUpload(path);
			} else {

				JsonClass_OBJ.setAPI(StaticValues.UrlLink
						+ "updateRegisterJson.php?userId="
						+ appPrefs.getData("USER_ID") + "&age=" + Age_S
						+ "&height=" + Height_S + "&weight=" + Weight_S
						+ "&latitude1=" + Latitude_S + "&longitude1="
						+ Longitude_S
						+ "&latitude2=12.33&longitude2=-133.56&ethnicity="
						+ "none" + "&sex=" + Sex_S + "&eye_color="
						+ Eye_Colour_S + "&hair_color="
						+ Hair_Colour_S.replaceAll(" ", "%20"));
				System.out.println(StaticValues.UrlLink
						+ "updateRegisterJson.php?userId="
						+ appPrefs.getData("USER_ID") + "&age=" + Age_S
						+ "&height=" + Height_S + "&weight=" + Weight_S
						+ "&latitude1=" + Latitude_S + "&longitude1="
						+ Longitude_S
						+ "&latitude2=12.33&longitude2=-133.56&ethnicity="
						+ "none" + "&sex=" + Sex_S + "&eye_color="
						+ Eye_Colour_S + "&hair_color="
						+ Hair_Colour_S.replaceAll(" ", "%20"));
			}
			return null;

		}
	}

	private void doFileUpload(String path) {

		System.out.println("doupload" + path);
		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;

		String pathToOurFile = path;

		urlServer = StaticValues.UrlLink + "updateRegisterJson.php?userId="
				+ appPrefs.getData("USER_ID") + "&age=" + Age_S + "&height="
				+ "3inch2feet" + "&weight=" + Weight_S + "&latitude1="
				+ Latitude_S + "&longitude1=" + Longitude_S
				+ "&latitude2=12.33&longitude2=-133.56&ethnicity=" + "none"
				+ "&sex=" + Sex_S + "&eye_color=" + Eye_Colour_S
				+ "&hair_color=" + Hair_Colour_S.replaceAll(" ", "%20");

		System.out.println("url-----" + urlServer);

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;

		try {

			System.out.println("onee");
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
					.writeBytes("Content-Disposition: form-data; name=\"userfile\";filename=\""
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

			System.out.println("Exception" + ex);
		}
	}

	// public void eye_colour_click(View v) {
	// System.out.println("onee");
	// }

	private Bitmap decodeFile(File f) {
		Bitmap b = null;
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;

			FileInputStream fis = new FileInputStream(f);
			BitmapFactory.decodeStream(fis, null, o);

			fis.close();

			int scale = 3;
			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			fis = new FileInputStream(f);
			b = BitmapFactory.decodeStream(fis, null, o2);
			fis.close();
		} catch (IOException e) {

		}
		return b;
	}

	// //getting location////////////////////////////////////////////

	class GetPlaces extends AsyncTask<String, Void, ArrayList<String>> {

		@Override
		// three dots is java for an array of strings
		protected ArrayList<String> doInBackground(String... args) {

			Log.d("gottaGo", "doInBackground");

			ArrayList<String> predictionsArr = new ArrayList<String>();

			try {

				URL googlePlaces = new URL(
				// URLEncoder.encode(url,"UTF-8");
						"https://maps.googleapis.com/maps/api/place/autocomplete/json?input="
								+ URLEncoder.encode(args[0], "UTF-8")
								+ "&types=geocode&language=en&sensor=true&key=AIzaSyBzWACfUEIB8O2FjSUWXR9nDZqw5ynkvJ4");
				URLConnection tc = googlePlaces.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(
						tc.getInputStream()));

				String line;
				StringBuffer sb = new StringBuffer();
				// take Google's legible JSON and turn it into one big
				// string.
				while ((line = in.readLine()) != null) {
					sb.append(line);
				}
				// turn that string into a JSON object
				JSONObject predictions = new JSONObject(sb.toString());
				// now get the JSON array that's inside that object
				JSONArray ja = new JSONArray(
						predictions.getString("predictions"));

				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					// add each entry to our array
					predictionsArr.add(jo.getString("description"));
				}
			} catch (IOException e) {

				Log.e("YourApp", "GetPlaces : doInBackground", e);

			} catch (JSONException e) {

				Log.e("YourApp", "GetPlaces : doInBackground", e);

			}

			return predictionsArr;

		}

		// then our post

		@Override
		protected void onPostExecute(ArrayList<String> result) {

			Log.d("YourApp", "onPostExecute : " + result.size());
			// update the adapter
			adapter = new ArrayAdapter<String>(getBaseContext(),
					R.layout.item_list);
			adapter.setNotifyOnChange(true);
			// attach the adapter to

			Location1_AutoTextView.setAdapter(adapter);

			for (String string : result) {

				Log.d("YourApp", "onPostExecute : result = " + string);
				adapter.add(string);
				adapter.notifyDataSetChanged();

			}

			Log.d("YourApp",
					"onPostExecute : autoCompleteAdapter" + adapter.getCount());

		}

	}

	class GetPlaces1 extends AsyncTask<String, Void, ArrayList<String>> {

		@Override
		// three dots is java for an array of strings
		protected ArrayList<String> doInBackground(String... args) {

			Log.d("gottaGo", "doInBackground");

			ArrayList<String> predictionsArr = new ArrayList<String>();

			try {

				URL googlePlaces = new URL(
				// URLEncoder.encode(url,"UTF-8");
						"https://maps.googleapis.com/maps/api/place/autocomplete/json?input="
								+ URLEncoder.encode(args[0], "UTF-8")
								+ "&types=geocode&language=en&sensor=true&key=AIzaSyBzWACfUEIB8O2FjSUWXR9nDZqw5ynkvJ4");
				URLConnection tc = googlePlaces.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(
						tc.getInputStream()));

				String line;
				StringBuffer sb = new StringBuffer();
				// take Google's legible JSON and turn it into one big
				// string.
				while ((line = in.readLine()) != null) {
					sb.append(line);
				}
				// turn that string into a JSON object
				JSONObject predictions = new JSONObject(sb.toString());
				// now get the JSON array that's inside that object
				JSONArray ja = new JSONArray(
						predictions.getString("predictions"));

				for (int i = 0; i < ja.length(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					// add each entry to our array
					predictionsArr.add(jo.getString("description"));
				}
			} catch (IOException e) {

				Log.e("YourApp", "GetPlaces : doInBackground", e);

			} catch (JSONException e) {

				Log.e("YourApp", "GetPlaces : doInBackground", e);

			}

			return predictionsArr;

		}

		// then our post

		@Override
		protected void onPostExecute(ArrayList<String> result) {

			Log.d("YourApp", "onPostExecute : " + result.size());
			// update the adapter
			adapter = new ArrayAdapter<String>(getBaseContext(),
					R.layout.item_list);
			adapter.setNotifyOnChange(true);
			// attach the adapter to

			Location2_AutoTextView.setAdapter(adapter);

			for (String string : result) {

				Log.d("YourApp", "onPostExecute : result = " + string);
				adapter.add(string);
				adapter.notifyDataSetChanged();

			}

			Log.d("YourApp",
					"onPostExecute : autoCompleteAdapter" + adapter.getCount());

		}

	}

}
