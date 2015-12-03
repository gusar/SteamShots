package com.andylahs.steamshots.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseModel extends SQLiteOpenHelper {

  // Table Name
  public static final String SCREENSHOT_TABLE = "SCREENSHOT";
  public static final String STEAMUSER_TABLE = "STEAM_USER";


  // Table columns
  public static final String USERTAG = "usertag";
  public static final String USERNAME = "username";
  public static final String IMAGE_ID = "image_id";
  public static final String THUMBNAIL_LINK = "thumbnail_link";
  public static final String HQ_LINK = "hq_link";
  public static final String DESCRIPTION = "description";

  // Database Information
  static final String DB_NAME = "STEAM_SHOTS.DB";

  // database version
  static final int DB_VERSION = 2;

  // Creating table query
  private static final String CREATE_TABLE2 = "CREATE TABLE " + STEAMUSER_TABLE + "(" +
      USERNAME + " TEXT, " + USERTAG + " TEXT);";

  private static final String CREATE_TABLE = "CREATE TABLE " + SCREENSHOT_TABLE + "(" +
      USERNAME + " TEXT, " + IMAGE_ID + " TEXT, " + THUMBNAIL_LINK +
      " TEXT, " + HQ_LINK + " TEXT, " + DESCRIPTION + " TEXT);";

  public DatabaseModel(Context context) {
    super(context, DB_NAME, null, DB_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_TABLE);
    db.execSQL(CREATE_TABLE2);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + SCREENSHOT_TABLE + STEAMUSER_TABLE);
    onCreate(db);
  }
}