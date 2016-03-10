package com.huntgame.Main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.huntgame.InviteFriends.SendMessagesActivity;
import com.huntgame.fb.Facebook_Activity;

public class InviteFriends_Page extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.invitefriends);
	}

	public void invite_contacts(View v) {
		startActivity(new Intent(InviteFriends_Page.this,
				SendMessagesActivity.class));
		finish();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		super.onBackPressed();
	}

	public void fb_click(View v) {

		startActivity(new Intent(InviteFriends_Page.this,
				Facebook_Activity.class));
		finish();
	}

}
