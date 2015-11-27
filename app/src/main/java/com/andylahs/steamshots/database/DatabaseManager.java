package com.andylahs.steamshots.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class DatabaseManager {
  private DatabaseModel databaseModel;

  private Context context;


  private SQLiteDatabase database;

  public DatabaseManager(Context context) {
    this.context = context;
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

  public void insert(String username, String imageId, String thumbnailLink, String description) {
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

  public boolean deleteFavouriteUser(String username) {
    return database.delete(DatabaseModel.TABLE_NAME, DatabaseModel.USERNAME+"="+username, null) > 0;
  }

  public Cursor fetchFavouriteUsers() {
    String[] columns = new String[] { DatabaseModel.USERNAME };
    Cursor cursor = null;
    try {
      cursor = database.query(DatabaseModel.TABLE_NAME, columns, null, null, null, null, null);
      if (cursor != null) {
        cursor.moveToFirst();
      }
    } catch (SQLException e) {
      Log.e("DBManager: ", "", e);
    }
    return cursor;
  }

  public Cursor fetchAll(String username) {
    String[] columns = new String[] {
        DatabaseModel.USERNAME,
        DatabaseModel.IMAGE_ID,
        DatabaseModel.THUMBNAIL_LINK,
        DatabaseModel.HQ_LINK,
        DatabaseModel.DESCRIPTION
    };
    Cursor cursor = null;
    try {
        cursor = database.query(DatabaseModel.TABLE_NAME, columns, "username='" + username +"'", null, null, null, null);
      if (cursor != null) {
        cursor.moveToFirst();
      }
    } catch (SQLException e) {
      Log.e("DBManager: ", "", e);
    }
    return cursor;
  }

//  public int update(int userid, int id, String title, boolean completed) {
//    ContentValues contentValue = new ContentValues();
//    contentValue.put(DatabaseModel.USER_ID, userid);
//    contentValue.put(DatabaseModel.ID, id);
//    contentValue.put(DatabaseModel.TITLE, title);
//    int completedInt = completed ? 1 : 0;
//    contentValue.put(DatabaseModel.COMPLETED, completedInt);
//    int i = 0;
//    try {
//      i = database.update(DatabaseModel.TABLE_NAME, contentValue, DatabaseModel.USER_ID + " = " + userid, null);
//    } catch (SQLException e) {
//      Log.e("DBManager: ", "", e);
//    }
//    return i;
//  }
}
