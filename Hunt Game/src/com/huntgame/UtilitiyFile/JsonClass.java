package com.huntgame.UtilitiyFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;

public class JsonClass {
	URL Api_Link;

	String jsondetails_S;

	public void setAPI(String link) {
		try {

			System.out.println("one");
			Api_Link = new URL(link);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			System.out.println("two");
			GetApiDetails(Api_Link.openStream());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	public String getAPI() {

		System.out.println("");

		return jsondetails_S;
	}

	protected void GetApiDetails(InputStream json) throws IOException,
			JSONException {
		// TODO Auto-generated method stub

		BufferedReader reader = new BufferedReader(new InputStreamReader(json));

		StringBuilder sb = new StringBuilder();
		String line = reader.readLine();
		while (line != null) {
			sb.append(line);
			line = reader.readLine();
		}

		jsondetails_S = sb.toString();
	}

}
