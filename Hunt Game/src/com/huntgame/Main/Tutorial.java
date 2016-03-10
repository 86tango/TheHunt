package com.huntgame.Main;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class Tutorial extends Activity {

	TextView tutorial_txt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tutorial);

		tutorial_txt = (TextView) findViewById(R.id.tutorial_txt);

		tutorial_txt.setText(Html.fromHtml(getString(R.string.tutorial)));
	}
}
