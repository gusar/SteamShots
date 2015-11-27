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

import com.andylahs.steamshots.model.Screenshot;

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

  public void insert(String username, ArrayList<Screenshot> screenshots) {
    String q = "SELECT DISTINCT " + databaseModel.USERNAME + " FROM " + databaseModel.TABLE_NAME;
    if (database.rawQuery(q, null) != null) {
      Log.d(LOG_TAG, "Username: " + username + " Quantity: " + screenshots.size());
      for (int i = 0; i < screenshots.size(); i++) {
        Screenshot screenshot = screenshots.get(i);
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
          database.insert(DatabaseModel.TABLE_NAME, null, contentValue);
        } catch (SQLException e) {
          Log.e("DBManager: ", "", e);
        }
      }
    } else {
      Toast.makeText(context, "User already in favourites", Toast.LENGTH_LONG).show();
    }
  }

  public boolean deleteFavouriteUser(String username) {
    try {
      return database.delete(DatabaseModel.TABLE_NAME, DatabaseModel.USERNAME + "='" + username + "'", null) > 0;
    } catch (SQLiteException e) {
      Log.e(LOG_TAG, "deleteFavouriteUser error: ", e);
      return false;
    }
  }

  public ArrayList<String> fetchFavouriteUsers() {
    ArrayList<String> usernames = new ArrayList<>();
    Cursor cursor;
    try {
      String q = "SELECT DISTINCT " + databaseModel.USERNAME + " FROM " + databaseModel.TABLE_NAME;
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

  public void update(String oldName, String newName) {
    ContentValues contentValue = new ContentValues();
    contentValue.put(DatabaseModel.USERNAME, newName);
    try {
      database.update(DatabaseModel.TABLE_NAME, contentValue, DatabaseModel.USERNAME + " = " + oldName, null);
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
//        cursor = database.query(DatabaseModel.TABLE_NAME, columns, "username='" + username +"'", null, null, null, null);
//      if (cursor != null) {
//        cursor.moveToFirst();
//      }
//    } catch (SQLException e) {
//      Log.e("DBManager: ", "", e);
//    }
//    return cursor;
//  }
}
