package com.andylahs.steamshots.async;


import android.content.Context;
import android.util.Log;

import com.andylahs.steamshots.database.DatabaseManager;
import com.andylahs.steamshots.model.Screenshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ScrListAsyncTask extends HttpAsyncTask {

  private static final String LOG_TAG = ScrListAsyncTask.class.getSimpleName();

  Context context;
  ScrReturnListener scrReturnListener;

  public String setLinkPrefix() {
    return "http://188.226.204.13/screens?user=";
  }

  public void setOnHttpReturnListener (ScrReturnListener listener) {
    this.scrReturnListener = listener;
  }


  @Override
  protected void onPostExecute(String stream) {
    DatabaseManager databaseManager = new DatabaseManager(context);
    ArrayList<Screenshot> screenshotList;
    screenshotList = new ArrayList<>();
    try {
      JSONArray jsonArray = new JSONArray(stream);
//      databaseManager.open();
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject jsonObject = jsonArray.getJSONObject(i);
        Screenshot screenshot = new Screenshot();
        screenshot.setThumbnailLink(jsonObject.getString("thumb"));
        screenshot.setId(jsonObject.getString("id"));
        screenshot.setDescription((jsonObject.getString("desc")));
        screenshotList.add(screenshot);
      }
    } catch (JSONException e) {
      Log.e(LOG_TAG, "JSON PROBLEM!!!", e);
    }
//    databaseManager.close();
    scrReturnListener.onHttpReturn(screenshotList);
  }

}
