package com.andylahs.steamshots.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.andylahs.steamshots.model.DatabaseModel;


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

  public void close() {
    try {
      databaseModel.close();
    } catch (SQLException e) {
      Log.e("DBManager: ", "", e);
    }
  }

  public void insert(String pageLink, String thumbnailLink, String hqLink, String description) {
    ContentValues contentValue = new ContentValues();
    contentValue.put(DatabaseModel.PAGE_LINK, pageLink);
    contentValue.put(DatabaseModel.THUMBNAIL_LINK, thumbnailLink);
    contentValue.put(DatabaseModel.HQ_LINK, hqLink);
    contentValue.put(DatabaseModel.DESCRIPTION, description);

    try {
      database.insert(DatabaseModel.TABLE_NAME, null, contentValue);
    } catch (SQLException e) {
      Log.e("DBManager: ", "", e);
    }
  }

//  public int update(int userid, int id, String title, boolean completed) {
//    ContentValues contentValue = new ContentValues();
////    contentValue.put(DatabaseModel.USER_ID, userid);
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

//  public Cursor fetch(Boolean b) {
//    String[] columns = new String[] { DatabaseModel._ID, DatabaseModel.SUBJECT, DatabaseModel.DESC };
//    Cursor cursor = null;
//    try {
//      if (b) {
//        cursor = database.query(DatabaseModel.TABLE_NAME, columns, "description='done'", null, null, null, null);
//      } else {
//        cursor = database.query(DatabaseModel.TABLE_NAME, columns, null, null, null, null, null);
//      }
//      if (cursor != null) {
//        cursor.moveToFirst();
//      }
//    } catch (SQLException e) {
//      Log.e("DBManager: ", "", e);
//    }
//    return cursor;
//  }

//  public void delete(long _id) {
//    try {
//      database.delete(DatabaseModel.TABLE_NAME, DatabaseModel._ID + "=" + _id, null);
//    } catch (SQLException e) {
//      Log.e("DBManager: ", "", e);
//    }
//  }

}
