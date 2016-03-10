package com.huntgame.InviteFriends;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huntgame.Main.R;

public class ContactsAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Contact> contacts;

	public ContactsAdapter(Context context, ArrayList<Contact> contacts) {
		this.context = context;
		this.contacts = contacts;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
	 
			LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 
			View gridView;
	 
			if (convertView == null) {
	 
				gridView = new View(context);
				
	 
				// get layout from mobile.xml
				gridView = inflater.inflate(R.layout.contact, null);
	 
				TextView textName = (TextView) gridView.findViewById(R.id.name);
				textName.setText( contacts.get(position).getName());
				
				TextView textMobile = (TextView) gridView.findViewById(R.id.mobile);
				textMobile.setText( contacts.get(position).getMobile());
	 
			} else {
				gridView = (View) convertView;
			}
			
			if(position %2==0) {
				gridView.setBackgroundColor(Color.parseColor("#55d3d3d3"));
	            }
	        else{
				gridView.setBackgroundColor(Color.parseColor("#55ffffff"));
	        }
			
			
		 
			return gridView;
		}

	public int getCount() {
		return contacts.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}
}
