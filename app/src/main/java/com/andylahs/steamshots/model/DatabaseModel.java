package com.andylahs.steamshots.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseModel extends SQLiteOpenHelper {

  // Table Name
  public static final String TABLE_NAME = "SCREENSHOT";

  // Table columns
  public static final String THUMBNAIL_LINK = "thumbnail_link";
  public static final String PAGE_LINK = "page_link";
  public static final String HQ_LINK = "hq_link";
  public static final String DESCRIPTION = "description";

  // Database Information
  static final String DB_NAME = "STEAMSHOTS.DB";

  // database version
  static final int DB_VERSION = 1;

  // Creating table query
  private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + THUMBNAIL_LINK
      + " TEXT, " + PAGE_LINK + " TEXT, " + HQ_LINK + " TEXT, " + DESCRIPTION + " TEXT);";

  public DatabaseModel(Context context) {
    super(context, DB_NAME, null, DB_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    onCreate(db);
  }
}