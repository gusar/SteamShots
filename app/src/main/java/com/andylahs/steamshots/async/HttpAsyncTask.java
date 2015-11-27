package com.andylahs.steamshots.async;


import android.os.AsyncTask;
import android.util.Log;

import com.andylahs.steamshots.connection.ConnectionManager;

import java.net.MalformedURLException;
import java.net.URL;

public class HttpAsyncTask extends AsyncTask<String, Long, String> {

  private static final String LOG_TAG = HttpAsyncTask.class.getSimpleName();

  public String setLinkPrefix() {
    return "";
  }



  @Override
  protected String doInBackground(String... params) {
    ConnectionManager connectionManager = new ConnectionManager();
      try {
        String urlString = setLinkPrefix() + params[0];
        Log.d(LOG_TAG, urlString);
        URL url = new URL(urlString);
        return connectionManager.getHttpStream(url);
      } catch (MalformedURLException e) {
        Log.e(LOG_TAG, "URL CANNOT BE CREATED FROM STRING: " + params[0], e);
        return "Bad Url!";
      }
  }

  @Override
  protected void onPostExecute(String stream) {
  }
}
