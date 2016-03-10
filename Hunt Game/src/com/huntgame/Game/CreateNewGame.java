package com.huntgame.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.huntgame.Main.R;
import com.huntgame.UtilitiyFile.AppPreferences;
import com.huntgame.UtilitiyFile.JsonClass;
import com.huntgame.UtilitiyFile.StaticValues;

public class CreateNewGame extends Activity {

	private TextView startDateDisplay, startTimeDisplay, GameRadiusTxt,
			DurationTXT;
	private TextView endDateDisplay, endTimeDisplay;
	private RelativeLayout startPickDate, startTime;
	private RelativeLayout endPickDate, endTime;
	ImageView Moderator_btn;
	private Calendar startDate;
	private Calendar endDate;
	Animation rotation;
	AppPreferences appPrefs;

	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;

	private TextView activeDateDisplay;
	private Calendar activeDate;

	String StartTime = "not_selected", EndTime = "not_selected",
			StartDate = "not_selected", EndDate = "not_selected", Moderator;
	String modertor_flag = "0";

	AutoCompleteTextView location_autocomplte;
	public ArrayAdapter<String> adapter;
	JsonClass JsonClass_OBJ;
	String Status, GameName_S = "not_selected", GameRadius_S = "not_selected";
	Spinner GameType_Spinner;
	String GameID;
	EditText GameName, GameRadius;
	ArrayList<String> GameType_Arr = new ArrayList<String>() {
		{

			add("-Select-");
			add("Original Game");
			add("Free For All");
			add("Double Jeopardy");
			add("Last Stand");

		}
	};
	String GameType_S, Location_S = "not_selected";
	MyAdapter myadapter_gametype_obj;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_new_game);
		JsonClass_OBJ = new JsonClass();

		rotation = AnimationUtils
				.loadAnimation(this, R.anim.clockwise_rotation); // progress

		appPrefs = new AppPreferences(getApplicationContext(), "SplashScreen");

		location_autocomplte = (AutoCompleteTextView) findViewById(R.id.location_autocomplte);
		adapter = new ArrayAdapter<String>(this, R.layout.item_list);
		GameName = (EditText) findViewById(R.id.gameName_edt);
		GameRadius = (EditText) findViewById(R.id.gameRadius_edt);
		GameRadiusTxt = (TextView) findViewById(R.id.gameradiustext);
		GameType_Spinner = (Spinner) findViewById(R.id.gametype_spinner);
		DurationTXT = (TextView) findViewById(R.id.game_length_txt);

		myadapter_gametype_obj = new MyAdapter(CreateNewGame.this,
				R.layout.spinneradapter, GameType_Arr);

		GameType_Spinner.setAdapter(myadapter_gametype_obj);

		if (appPrefs.getData("Radius").equals("Miles")) {
			GameRadiusTxt.setText("Game Radius (Miles)");
		} else {
			GameRadiusTxt.setText("Game Radius (Km)");
			appPrefs.SaveData("Km", "Radius");

		}

		GameType_Spinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						// TODO Auto-generated method stub

					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		/* capture our View elements for the start date function */
		startDateDisplay = (TextView) findViewById(R.id.calender_date);
		startPickDate = (RelativeLayout) findViewById(R.id.b_startdate);
		/* get the current date */
		startDate = Calendar.getInstance();

		/* add a click listener to the button */
		startPickDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDateDialog(startDateDisplay, startDate);
			}
		});

		/* capture our View elements for the end date function */
		endDateDisplay = (TextView) findViewById(R.id.calender_date_end);
		endPickDate = (RelativeLayout) findViewById(R.id.b_enddate);

		/* get the current date */
		endDate = Calendar.getInstance();

		/* add a click listener to the button */
		endPickDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDateDialog(endDateDisplay, endDate);
			}
		});
		startTimeDisplay = (TextView) findViewById(R.id.clock_time);
		startTime = (RelativeLayout) findViewById(R.id.b_starttime);

		startTime.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				showTimeDialog(startTimeDisplay, endDate);
			}
		});
		endTimeDisplay = (TextView) findViewById(R.id.clock_time_end);

		endTime = (RelativeLayout) findViewById(R.id.b_endtime);

		endTime.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				showTimeDialog(endTimeDisplay, endDate);
			}
		});
		/* display the current date (this method is below) */
		// updateDisplay(startDateDisplay, startDate);
		// updateDisplay(endDateDisplay, endDate);

		Moderator_btn = (ImageView) findViewById(R.id.user_moderator_btn);

		Moderator_btn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (modertor_flag.equals("1")) {
					modertor_flag = "0";

					Moderator_btn.setImageResource(R.drawable.no);

				} else {
					modertor_flag = "1";

					Moderator_btn.setImageResource(R.drawable.yes);

				}
			}
		});

		adapter.setNotifyOnChange(true);
		location_autocomplte.setAdapter(adapter);
		location_autocomplte.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (count % 3 == 1) {
					adapter.clear();
					GetPlaces task = new GetPlaces();

					// now pass the argument in the textview to the task
					task.execute(location_autocomplte.getText().toString());
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			public void afterTextChanged(Editable s) {

			}
		});

		location_autocomplte.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(CreateNewGame.this, " selected",
						Toast.LENGTH_LONG).show();

			}
		});

	}

	private void updateDisplay(TextView dateDisplay, Calendar date) {

		dateDisplay.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(date.get(Calendar.MONTH) + 1).append("-")
				.append(date.get(Calendar.DAY_OF_MONTH)).append("-")
				.append(date.get(Calendar.YEAR)).append(" "));

		String mon = Integer.valueOf(date.get(Calendar.MONTH) + 1).toString();
		String day = Integer.valueOf(date.get(Calendar.DAY_OF_MONTH))
				.toString();
		String year = Integer.valueOf(date.get(Calendar.YEAR)).toString();

		System.out.println("mon" + mon.length());

		if (mon.length() == 1) {
			mon = "0" + mon;
		}
		if (day.length() == 1) {
			day = "0" + day;
		}

		if (dateDisplay.getId() == startDateDisplay.getId()) {

			StartDate = year + "-" + mon + "-" + day;

			System.out.println("start1" + StartDate);

		}
		if (dateDisplay.getId() == endDateDisplay.getId()) {

			EndDate = year + "-" + mon + "-" + day;

		}

	}

	private void updateTimeDisplay(TextView dateDisplay, Calendar date) {
		int time = 0;
		String amorpm = null;
		if (date.get(Calendar.AM_PM) == 1) {
			amorpm = "PM";
		} else {
			amorpm = "AM";
		}
		if (date.get(Calendar.HOUR_OF_DAY) > 12) {
			time = date.get(Calendar.HOUR_OF_DAY) - 12;
		} else {
			time = date.get(Calendar.HOUR_OF_DAY);
		}
		if (time == 0) {
			time = 12;
		}

		String hr = Integer.valueOf(date.get(Calendar.HOUR_OF_DAY)).toString();
		String min = Integer.valueOf(date.get(Calendar.MINUTE)).toString();

		if (hr.length() == 1) {
			hr = "0" + hr;
		}

		System.out.println("min" + min.length());
		if (min.length() == 1) {
			min = "0" + min;
		}

		if (dateDisplay.getId() == startTimeDisplay.getId()) {

			StartTime = hr + ":" + min + ":00";

		}

		if (dateDisplay.getId() == endTimeDisplay.getId()) {
			EndTime = hr + ":" + min + ":00";

			final String dateStart = StartDate + " "
					+ startTimeDisplay.getText().toString();
			final String dateStop = EndDate + " "
					+ endTimeDisplay.getText().toString();
			final DateTimeFormatter format = DateTimeFormat
					.forPattern("yyyy-mm-dddd hh:mm:ss a");
			final DateTime dt2 = format.parseDateTime(dateStart);
			final DateTime dt1 = format.parseDateTime(dateStop);

			System.out.print(Days.daysBetween(dt1, dt2).getDays() + " days, ");
			System.out.print(Hours.hoursBetween(dt1, dt2).getHours() % 24
					+ " hours, ");
			System.out.print(Minutes.minutesBetween(dt1, dt2).getMinutes() % 60
					+ " minutes, ");
			System.out.print(Seconds.secondsBetween(dt1, dt2).getSeconds() % 60
					+ " seconds.");
			DurationTXT.setText(Days.daysBetween(dt1, dt2).getDays()
					+ " days, " + Hours.hoursBetween(dt1, dt2).getHours() % 24
					+ " hr, " + Minutes.minutesBetween(dt1, dt2).getMinutes()
					% 60 + " min, ");
		}

		dateDisplay.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(time).append(":").append(min).append(" ")
				.append(amorpm).append(" "));

	}

	public void showDateDialog(TextView dateDisplay, Calendar date) {
		activeDateDisplay = dateDisplay;
		activeDate = date;
		showDialog(DATE_DIALOG_ID);
	}

	public void showTimeDialog(TextView dateDisplay, Calendar date) {
		activeDateDisplay = dateDisplay;
		activeDate = date;
		showDialog(TIME_DIALOG_ID);
	}

	private OnDateSetListener dateSetListener = new OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			activeDate.set(Calendar.YEAR, year);
			activeDate.set(Calendar.MONTH, monthOfYear);
			activeDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateDisplay(activeDateDisplay, activeDate);
			unregisterDateDisplay();
		}
	};
	private OnTimeSetListener timeSetListener = new OnTimeSetListener() {
		// @Override
		// public void onDateSet(DatePicker view, int year, int monthOfYear,
		// int dayOfMonth) {
		// activeDate.set(Calendar.YEAR, year);
		// activeDate.set(Calendar.MONTH, monthOfYear);
		// activeDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		// updateDisplay(activeDateDisplay, activeDate);
		// unregisterDateDisplay();
		//

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			activeDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
			activeDate.set(Calendar.MINUTE, minute);

			updateTimeDisplay(activeDateDisplay, activeDate);
			unregisterTimeDateDisplay();
		}
	};

	private void unregisterDateDisplay() {
		activeDateDisplay = null;
		activeDate = null;
	}

	private void unregisterTimeDateDisplay() {
		activeDateDisplay = null;
		activeDate = null;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, dateSetListener,
					activeDate.get(Calendar.YEAR),
					activeDate.get(Calendar.MONTH),
					activeDate.get(Calendar.DAY_OF_MONTH));
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, timeSetListener,
					activeDate.get(Calendar.HOUR),
					activeDate.get(Calendar.MINUTE), false);
		}

		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		super.onPrepareDialog(id, dialog);
		switch (id) {
		case DATE_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(
					activeDate.get(Calendar.YEAR),
					activeDate.get(Calendar.MONTH),
					activeDate.get(Calendar.DAY_OF_MONTH));
			break;
		}
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

			location_autocomplte.setAdapter(adapter);

			for (String string : result) {

				Log.d("YourApp", "onPostExecute : result = " + string);
				adapter.add(string);
				adapter.notifyDataSetChanged();

			}

			Log.d("YourApp",
					"onPostExecute : autoCompleteAdapter" + adapter.getCount());

		}

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

	public void save_click(View v) {

		System.out.println("qqq" + GameRadius_S);

		System.out.println("moderator" + modertor_flag);

		System.out.println("start" + StartTime);
		System.out.println("end" + EndTime);

		System.out.println("startdat" + StartDate);
		System.out.println("enddat" + EndDate);

		System.out.println("GameType_S" + GameType_S);

		GameName_S = GameName.getText().toString().replaceAll(" ", "%20");
		GameRadius_S = GameRadius.getText().toString().replaceAll(" ", "%20");

		GameType_S = GameType_Spinner.getSelectedItem().toString();
		Location_S = location_autocomplte.getText().toString()
				.replaceAll(" ", "%20");

		System.out.println("l" + Location_S);

		if (GameName_S.equals("")) {
			Toast.makeText(getApplicationContext(), "Enter Game Name", 1000)
					.show();

		} else if (StartDate.equals("not_selected")) {
			Toast.makeText(getApplicationContext(), "Enter Start Date", 1000)
					.show();

		} else if (StartTime.equals("not_selected")) {
			Toast.makeText(getApplicationContext(), "Enter Start Time", 1000)
					.show();

		} else if (EndDate.equals("not_selected")) {
			Toast.makeText(getApplicationContext(), "Enter End Date", 1000)
					.show();

		} else if (EndTime.equals("not_selected")) {
			Toast.makeText(getApplicationContext(), "Enter End Time", 1000)
					.show();

		}

		else if (GameRadius_S.equals("")) {
			Toast.makeText(getApplicationContext(), "Enter Game Radius", 1000)
					.show();

		}
		// /

		else if (GameType_S.equals("-Select-")) {
			Toast.makeText(getApplicationContext(), "Select a Game Type", 1000)
					.show();
		}

		else if (Location_S.length() == 0) {
			Toast.makeText(getApplicationContext(), "Select Game Location",
					1000).show();
		}

		else if (appPrefs.getData("Radius").equals("Miles")) {

			if (Float.parseFloat(GameRadius_S) < .5
					|| Float.parseFloat(GameRadius_S) > 25) {
				Toast.makeText(getApplicationContext(),
						"Radius must be between .5 and .25 Miles", 1000).show();

			}

			else if (Float.parseFloat(GameRadius_S) < 2
					|| Float.parseFloat(GameRadius_S) > 5) {
				alertbox();
			}

		}

		else if (appPrefs.getData("Radius").equals("Km")) {

			if (Float.parseFloat(GameRadius_S) < 1
					|| Float.parseFloat(GameRadius_S) > 40) {
				Toast.makeText(getApplicationContext(),
						"Radius must be between 1 and 40 Km", 1000).show();

			}

			else if (Float.parseFloat(GameRadius_S) < 3
					|| Float.parseFloat(GameRadius_S) > 8) {
				alertbox();
			}

		}

		else {
			new AcceptRejectasynctask().execute();
		}
	}

	class AcceptRejectasynctask extends AsyncTask<Void, Void, Void> {

		Dialog dialog = new Dialog(CreateNewGame.this);
		String friendrequestid, Statusvalue;

		public AcceptRejectasynctask() {
			// TODO Auto-generated constructor stub

		}

		@Override
		protected void onPreExecute() {

			System.out.println("sss");
			// TODO Auto-generated method stub
			this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
			this.dialog.setContentView(R.layout.loadingicon);
			ImageView img = (ImageView) this.dialog
					.findViewById(R.id.progressBar1);
			img.setBackgroundResource(R.drawable.progress_img);

			rotation = AnimationUtils.loadAnimation(CreateNewGame.this,
					R.anim.clockwise_rotation);
			rotation.setRepeatCount(Animation.INFINITE);

			this.dialog.show();
			img.startAnimation(rotation);
			this.dialog.setCanceledOnTouchOutside(false);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			this.dialog.setCanceledOnTouchOutside(false);
			GameType_S = GameType_S.replace(" ", "");

			GameName_S = GameName_S.replace(" ", "20");
			System.out.println(StaticValues.UrlLink
					+ "createGameJson.php?userId="
					+ appPrefs.getData("USER_ID") + "&title=" + GameName_S
					+ "&gameType=" + GameType_S + "&startingDate=" + StartDate
					+ "%20" + StartTime + "&endingDate=" + EndDate + "%20"
					+ EndTime + "&moderatorStatus=" + modertor_flag
					+ "&location=" + Location_S + "&radius=" + GameRadius_S);
			JsonClass_OBJ.setAPI(StaticValues.UrlLink
					+ "createGameJson.php?userId="
					+ appPrefs.getData("USER_ID") + "&title=" + GameName_S
					+ "&gameType=" + GameType_S + "&startingDate=" + StartDate
					+ "%20" + StartTime + "&endingDate=" + EndDate + "%20"
					+ EndTime + "&moderatorStatus=" + modertor_flag
					+ "&location=" + Location_S + "&radius=" + GameRadius_S);

			String S = JsonClass_OBJ.getAPI();
			System.out.println(S);
			test();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub

			System.out.println("sqsaqs" + Status);

			if (Status.equals("SUCCESS")) {

				Intent i = new Intent(CreateNewGame.this,
						InviteFriends_Game.class);
				Bundle b = new Bundle();
				b.putString("GameID", GameID);
				b.putString("GameName", GameName_S.replace("%20", " "));

				i.putExtras(b);
				startActivity(i);

			} else {
				Toast.makeText(getApplicationContext(), "Retry", 1000).show();
			}
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();

			}

		}

	}

	public void test() {

		try {
			JSONObject jobj = new JSONObject(JsonClass_OBJ.getAPI());

			if (jobj.has("Result")) {

				Status = jobj.getString("Result");

			}

			if (jobj.has("GameId")) {

				GameID = jobj.getString("GameId");

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public void alertbox() {

		AlertDialog.Builder altDialog = new AlertDialog.Builder(this);
		altDialog
				.setMessage("The distance you have chosen may affect the playability of the game"); // here
																									// add
																									// your
																									// message
		altDialog.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						new AcceptRejectasynctask().execute();
					}
				});

		altDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		altDialog.show();

	}

}