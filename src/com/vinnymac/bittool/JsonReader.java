package com.vinnymac.bittool;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class JsonReader extends AsyncTask<String, Void, String> {

	@Override
	protected String doInBackground(String... params) {
		String url = params[0];
		String object = params[1];
		String askPrice = "No Market Data";
		try {
			// Sets up the connections from the url, and page is the json string
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			ResponseHandler<String> resHandler = new BasicResponseHandler();
			String page = httpClient.execute(httpGet, resHandler);
			Log.d("result", page);

			// gets the ask string aka the ask price!
			JSONObject jObject = new JSONObject(page);
			askPrice = jObject.getString(object);
			Log.d("ask", askPrice);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return askPrice;
	}

}