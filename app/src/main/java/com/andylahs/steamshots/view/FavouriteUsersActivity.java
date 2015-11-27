package com.andylahs.steamshots.view;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.andylahs.steamshots.R;
import com.andylahs.steamshots.adapter.FavouriteUsersAdapter;
import com.andylahs.steamshots.database.DatabaseManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FavouriteUsersActivity extends ListActivity {

  private static final String LOG_TAG = FavouriteUsersActivity.class.getSimpleName();
  private DatabaseManager databaseManager;
  ArrayList<String> selectedForDeletion;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_favourites);

    FavouriteUsersAdapter favouriteUsersAdapter;
    favouriteUsersAdapter = new FavouriteUsersAdapter(getFavouriteUsersHashMap());
    setListAdapter(favouriteUsersAdapter);
  }


  private HashMap<String, Boolean> getFavouriteUsersHashMap() {
    HashMap<String, Boolean> favouriteUsers = new HashMap<>();
    databaseManager = new DatabaseManager(this);
    databaseManager.open();
    Cursor cursor =   databaseManager.fetchFavouriteUsers();
    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      favouriteUsers.put(cursor.getString(0), false);
    }
    return favouriteUsers;
  }


  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    Map.Entry<String, Boolean> selectedUser = (Map.Entry<String, Boolean>) l.getItemAtPosition(position);
    if (selectedUser.getValue());
  }
}
