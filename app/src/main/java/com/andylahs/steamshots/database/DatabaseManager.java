package com.andylahs.steamshots.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.andylahs.steamshots.model.ScreenshotModel;

import java.util.ArrayList;


public class DatabaseManager {

  private static final String LOG_TAG = DatabaseManager.class.getSimpleName();
  private DatabaseModel databaseModel;
  private Context context;
  private SQLiteDatabase database;

  public DatabaseManager(Context context) {
    this.context = context;
    Log.d(LOG_TAG, "manager created");
  }

  public DatabaseManager open() {
    try {
      databaseModel = new DatabaseModel(context);
      database = databaseModel.getWritableDatabase();
    } catch (SQLiteCantOpenDatabaseException e) {
      Log.e("DBManager: ", "can't open database!!!", e);
    }
    return this;
  }

  public boolean close() {
    try {
      databaseModel.close();
      return true;
    } catch (SQLException e) {
      Log.e("DBManager: ", "", e);
      return false;
    }
  }

  public void insert(String username, ArrayList<ScreenshotModel> screenshots) {
    String q = "SELECT DISTINCT " + DatabaseModel.USERNAME + " FROM " + DatabaseModel
        .SCREENSHOT_TABLE;
    if (database.rawQuery(q, null) != null) {
      Log.d(LOG_TAG, "Username: " + username + " Quantity: " + screenshots.size());
      for (int i = 0; i < screenshots.size(); i++) {
        ScreenshotModel screenshot = screenshots.get(i);
        String imageId = screenshot.getId();
        String thumbnailLink = screenshot.getThumbnailLink();
        String description = screenshot.getDescription();
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseModel.USERNAME, username);
        contentValue.put(DatabaseModel.IMAGE_ID, imageId);
        contentValue.put(DatabaseModel.THUMBNAIL_LINK, thumbnailLink);
        contentValue.put(DatabaseModel.HQ_LINK, "");
        contentValue.put(DatabaseModel.DESCRIPTION, description);
        try {
          database.insert(DatabaseModel.SCREENSHOT_TABLE, null, contentValue);
        } catch (SQLException e) {
          Log.e("DBManager: ", "", e);
        }
      }

      ContentValues contentValues = new ContentValues();
      contentValues.put(DatabaseModel.USERNAME, username);
      contentValues.put(DatabaseModel.USERTAG, username);
      try {
        database.insert(DatabaseModel.STEAMUSER_TABLE, null, contentValues);
      } catch (SQLException e) {
        Log.e("DBManager: ", "", e);
      }
    } else {
      Toast.makeText(context, "User already in favourites", Toast.LENGTH_LONG).show();
    }
  }

  private String findUserId(String profileTag) {
    String profileName = "";
    return profileName;
  }

  public boolean deleteFavouriteUser(String profileTag) {
    try {
      String q = ("SELECT DISTINCT " + DatabaseModel.USERNAME + " FROM "
          + DatabaseModel.STEAMUSER_TABLE + " WHERE " + DatabaseModel.USERTAG + " = '" + profileTag
          + "';");
      Cursor cursor = database.rawQuery(q, null);
      cursor.moveToFirst();
      String s = cursor.getString(0);
      Log.d(LOG_TAG, "Deleting username: " + s);
      boolean b = database.delete(DatabaseModel.STEAMUSER_TABLE, DatabaseModel.USERNAME + " =? ",
          new String[]{s}) > 0;
      if (!b) Log.d(LOG_TAG, "COUDNT DELETE FROM STEAMUSER_TABLE: " + s);
      return database.delete(DatabaseModel.SCREENSHOT_TABLE, DatabaseModel.USERNAME + " =? ", new
          String[]{s}) > 0;
    } catch (SQLiteException e) {
      Log.e(LOG_TAG, "deleteFavouriteUser error: ", e);
      return false;
    }
  }

  public ArrayList<String> fetchFavouriteUsers() {
    ArrayList<String> usernames = new ArrayList<>();
    Cursor cursor;
    try {
      String q = "SELECT " + DatabaseModel.USERTAG + " FROM " + DatabaseModel.STEAMUSER_TABLE;
      cursor = database.rawQuery(q, null);
      Log.d(LOG_TAG, "fetchFavouritedUser raw query --> " + q);
      if (cursor.moveToFirst()) {
        do {
          usernames.add(cursor.getString(0));
        } while (cursor.moveToNext());
        cursor.close();
      }
    } catch (SQLException e) {
      Log.e("DBManager: ", "", e);
    }
    return usernames;
  }

  public Cursor fetchProfileLinks() {
    return null;
  }

  public String fetchUserByTag(String tag) {
    String username = "";
    try {
      String q = ("SELECT DISTINCT " + DatabaseModel.USERNAME + " FROM "
          + DatabaseModel.STEAMUSER_TABLE + " WHERE " + DatabaseModel.USERTAG + " = '" +tag+ "';");
      Cursor cursor = database.rawQuery(q, null);
      cursor.moveToFirst();
      username = cursor.getString(0);
    } catch (SQLException e) {
      Log.e("DBManager: ", "", e);
    }
    return username;
  }

  public void update(String oldTag, String newTag) {
    ContentValues contentValue = new ContentValues();
    contentValue.put(DatabaseModel.USERTAG, newTag);
    try {
      Log.d(LOG_TAG, "Old tag: " + oldTag+ " --> New tag: " + newTag);
      database.update(DatabaseModel.STEAMUSER_TABLE, contentValue, DatabaseModel.USERTAG +"=?",
          new String[]{oldTag});
    } catch (SQLException e) {
      Log.e("DBManager: ", "", e);
    }
  }

//  public Cursor fetchAll(String username) {
//    String[] columns = new String[] {
//        DatabaseModel.USERNAME,
//        DatabaseModel.IMAGE_ID,
//        DatabaseModel.THUMBNAIL_LINK,
//        DatabaseModel.HQ_LINK,
//        DatabaseModel.DESCRIPTION
//    };
//    Cursor cursor = null;
//    try {
//        cursor = database.query(DatabaseModel.SCREENSHOT_TABLE, columns, "username='" + username +"'", null, null, null, null);
//      if (cursor != null) {
//        cursor.moveToFirst();
//      }
//    } catch (SQLException e) {
//      Log.e("DBManager: ", "", e);
//    }
//    return cursor;
//  }
}
