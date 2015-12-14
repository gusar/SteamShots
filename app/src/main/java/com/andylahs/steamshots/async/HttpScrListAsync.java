package com.andylahs.steamshots.async;


import android.util.Log;

import com.andylahs.steamshots.model.Screenshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmList;

public class HttpScrListAsync extends HttpAsyncTask {

  private static final String LOG_TAG = HttpScrListAsync.class.getSimpleName();

  public ScrReturnListener scrReturnListener;
  private Realm realm;

  public String setLinkPrefix() {
    return "http://188.226.204.13/screens?user=";
  }

  public void setOnHttpReturnListener (ScrReturnListener listener) {
    this.scrReturnListener = listener;
  }


  @Override
  protected void onPostExecute(String stream) {
    RealmList<Screenshot> screenshotList = new RealmList<>();
    try {
      JSONArray jsonArray = new JSONArray(stream);
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
    scrReturnListener.onHttpReturn(screenshotList);
  }

}
