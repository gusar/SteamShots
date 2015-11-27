package com.andylahs.steamshots.controller;


import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ScrDetailsAsyncTask extends HttpAsyncTask {

  public static final String LOG_TAG = ScrDetailsAsyncTask.class.getSimpleName();

  Context context;
  StringReturnListener stringReturnListener;

  public void setOnHttpReturnListener (StringReturnListener listener) {
    this.stringReturnListener = listener;
  }

  public String setLinkPrefix() {
    return "http://188.226.204.13/screens/large?id=";
  }

  @Override
  protected void onPostExecute(String stream) {
    DatabaseManager databaseManager = new DatabaseManager(context);
    ArrayList<String> stringArrayList;
    stringArrayList = new ArrayList<>();
    try {
      JSONArray jsonArray = new JSONArray(stream);
      JSONObject jsonObject = jsonArray.getJSONObject(0);
      stringArrayList.add(jsonObject.getString("id"));
      stringArrayList.add(jsonObject.getString("imageLink"));
    } catch (JSONException e) {
      Log.e(LOG_TAG, "JSON LARGE IMAGE PROBLEM!!!", e);
    }

    stringReturnListener.onHttpReturn(stringArrayList);
  }
}
