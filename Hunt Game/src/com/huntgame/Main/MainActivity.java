package com.huntgame.Main;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	Spinner buy, purchase;
	Button submit;
	ArrayList<String> first, second = new ArrayList<String>();
	MyAdapter myadapter1, myadapter2;
	ArrayList<String> buy_arr = new ArrayList<String>() {
		{

			add("-Buy Intel-");
			add("Bounty #1  $ .99");
			add("Bounty #2  $ .99");
			add("Pre-purchase 2  $1.75");
			add("Pre-purchase 3  $2.50");
			add("Pre-purchase 4  $3.25");
			add("Pre-purchase 5  $4.00");
		}
	};
	ArrayList<String> purchase_arr = new ArrayList<String>() {
		{

			add("-Purchase Bail-");
			add("Game #1  $ .99");
			add("Game #2  $.99");
			add("Pre-purchase 2  $1.75");
			add("Pre-purchase 3  $2.50");
			add("Pre-purchase 4  $3.25");
			add("Pre-purchase 5  $4.00");
		}
	};

	public ArrayAdapter<String> adapter1;
	public ArrayAdapter<String> adapter2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.storepage);

		purchase = (Spinner) findViewById(R.id.purchase_spinner);
		buy = (Spinner) findViewById(R.id.buy_spinner);

		submit = (Button) findViewById(R.id.submit);

		// adapter1 = new ArrayAdapter<String>(this, R.layout.spinneradapter);
		// adapter2=new ArrayAdapter<String>(this, R.layout.spinneradapter);

		myadapter1 = new MyAdapter(MainActivity.this,
				R.layout.spinneradapterblack, buy_arr);
		myadapter2 = new MyAdapter(MainActivity.this,
				R.layout.spinneradapterblack, purchase_arr);
		buy.setAdapter(myadapter1);
		purchase.setAdapter(myadapter2);

		buy.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		purchase.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				System.out.println("2");
			}
		});

		submit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Submit",
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
			System.out.println("3");
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			// System.out.println("4");
			return getCustomView(position, convertView, parent);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			System.out.println("5");

			return getCustomView(position, convertView, parent);
		}

		public View getCustomView(int position, View convertView,
				ViewGroup parent) {
			// System.out.println("6");
			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.spinneradapterblack, parent, false);
			TextView label = (TextView) row.findViewById(R.id.languageName);
			label.setText(Adapter_Arr.get(position));

			LinearLayout layout = (LinearLayout) row.findViewById(R.id.lay1);
			// if (position % 2 == 0) {
			// layout.setBackgroundColor(Color.GRAY);
			// } else {
			// layout.setBackgroundColor(Color.WHITE);
			// }

			return row;
		}

	}

}
