package com.vinnymac.bittool;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class JSONContact extends AsyncTask<String, Void, String> {
	@Override
	protected String doInBackground(String... params) {
		String URL = params[0];
		String page = "Bad Response";

		try {
			// Sets up the connections from the url, and page is the json string
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(URL);
			ResponseHandler<String> resHandler = new BasicResponseHandler();
			page = httpClient.execute(httpGet, resHandler);
			Log.d("result", page);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return page;
	}

	protected String convert(String... params) {
		String data = params[0];
		String key = params[1];
		String json = "No Market Data";

		// gets whatever you want from the json data!
		JSONObject jObject;
		try {
			jObject = new JSONObject(data);
			json = jObject.getString(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Log.d("key", json);

		return json;
	}

}
