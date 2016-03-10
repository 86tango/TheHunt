package com.huntgame.bounty;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huntgame.Main.R;
import com.project.LazyLoading.ImageLoader;

public class Bounties_Main_Page_Adapter extends BaseAdapter {
	private Context context;
	public ImageLoader imageLoader;
	private static LayoutInflater inflater = null;
	private ArrayList<HashMap<String, String>> data;

	public Bounties_Main_Page_Adapter(Context c, ArrayList<HashMap<String, String>> d) {
		this.data = d;
		this.context = c;
		imageLoader = new ImageLoader(c);
		inflater = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View gridView;

		if (convertView == null) {
			gridView = new View(context);
			gridView = inflater.inflate(R.layout.bounties_main_page_adapter,
					null);
		} else {
			gridView = (View) convertView;
		}

		TextView title = (TextView) gridView.findViewById(R.id.title);
		TextView userName = (TextView) gridView.findViewById(R.id.userName);
		ImageView imageView = (ImageView) gridView
				.findViewById(R.id.grid_item_image);
		String urlBanner = data.get(position).get("image");
		title.setText(data.get(position).get("title"));
		userName.setText(data.get(position).get("userName"));
		imageLoader.DisplayImage(urlBanner, imageView);

		return gridView;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}