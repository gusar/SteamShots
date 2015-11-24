package com.andylahs.steamshots.controller;


import android.os.AsyncTask;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

public class HttpAsyncTask extends AsyncTask<String, Long, String> {

  private static final String LOG_TAG = HttpAsyncTask.class.getSimpleName();

  HttpReturnListener httpReturnListener;

  public void setOnHttpReturnListener (HttpReturnListener listener) {
    this.httpReturnListener = listener;
  }

  @Override
  protected String doInBackground(String... params) {
    ConnectionManager connectionManager = new ConnectionManager();
      try {
        URL url;
//        url = new URL(params[0]);
//        url = new URL("http://andylahs.com:2080/screens?user=truegusar");
        url = new URL("http://andylahs.com:2080/screens?user=ovmise");
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
