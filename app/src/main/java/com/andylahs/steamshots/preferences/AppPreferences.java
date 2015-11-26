package com.andylahs.steamshots.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {

  private static final String FILE = "appPreferences";

  public static void setProfilePreference(Context context, String profile) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString("profile", profile);
    editor.apply();
  }

  public static String getProfilePreference(Context context) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
    return sharedPreferences.getString("profile", "null");
  }

  public static void removeAllPreferences(Context context) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
    sharedPreferences.edit().remove("profile").apply();
  }

}
