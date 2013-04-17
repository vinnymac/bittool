package com.vinnymac.bittool;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.AsyncTask;
import android.util.Log;

public class HttpTask extends AsyncTask {
	private static final String TAG = "HTTP_TASK";



	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub

    	HttpUriRequest request = (HttpUriRequest) params[0];
    	HttpClient client = new DefaultHttpClient();

        try {
        	// The UI Thread shouldn't be blocked long enough to do the reading in of the stream.
        	HttpResponse response =  client.execute(request);

// TODO handle bad response codes (such as 404, etc)

        	BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			StringBuilder builder = new StringBuilder();
			for (String line = null; (line = reader.readLine()) != null; ) {
			    builder.append(line).append("\n");
			}
		JSONTokener tokener = new JSONTokener(builder.toString());
	JSONObject json = new JSONObject(tokener);
	return json;

        } catch (Exception e) {
	// TODO handle different exception cases
        	Log.e(TAG,e.toString());
			e.printStackTrace();
			return null;
		}
    }

	protected void onPostExecute(JSONObject json) {
		Object result = json;
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		// Done on UI Thread
        if(result != null) {               
        	taskHandler.taskSuccessful(json);
        } else {
        	taskHandler.taskFailed();
        }
	}

public static interface HttpTaskHandler {
	        void taskSuccessful(JSONObject json);
	        void taskFailed();
	    }

	HttpTaskHandler taskHandler;

	public void setTaskHandler(HttpTaskHandler taskHandler) {
        		this.taskHandler = taskHandler;
    	}

}