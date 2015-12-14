package com.andylahs.steamshots.async;


import android.support.annotation.Nullable;
import android.util.Log;

import com.andylahs.steamshots.model.Profile;
import com.andylahs.steamshots.model.Screenshot;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ScreenshotListManager implements ScrReturnListener {

  private static final String LOG_TAG = ScreenshotListManager.class.getSimpleName();
  private static ScreenshotListManager instance = null;
  private Realm realm;
  private RealmList<Screenshot> screenshotList;

  private ScreenshotListManager(Realm realm) {
    this.realm = realm;
  }

  public static ScreenshotListManager getInstance(Realm realm) {
    if (instance == null) {
      instance = new ScreenshotListManager(realm);
    }
    return instance;
  }


  public RealmList<Screenshot> execute(String profileId) {
    Profile profile = profileExists(profileId);
    if (profile != null) {
      return profile.getScreenshots();
    }
    HttpScrListAsync httpScrListAsync = new HttpScrListAsync();
    httpScrListAsync.setOnHttpReturnListener(this);
    httpScrListAsync.execute(profileId);
    return screenshotList;
  }


  @Nullable
  private Profile profileExists(String profile) {
    RealmResults<Profile> result = realm.where(Profile.class)
        .equalTo("id", profile)
        .findAll();
    if (result.size() > 0) {
      return result.get(0);
    }
    return null;
  }


  @Override
  public void onHttpReturn(RealmList<Screenshot> objectList) {
    screenshotList = new RealmList<>();
    if (objectList.size() < 1) {
      Log.d(LOG_TAG, "NO DATA RETURNED");
    } else {
      Log.d(LOG_TAG, Integer.toString(objectList.size()));
      screenshotList = objectList;
    }
  }
}
