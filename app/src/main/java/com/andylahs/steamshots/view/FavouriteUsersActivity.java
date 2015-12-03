package com.andylahs.steamshots.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.andylahs.steamshots.R;
import com.andylahs.steamshots.adapter.FavouriteUsersAdapter;
import com.andylahs.steamshots.database.DatabaseManager;
import com.andylahs.steamshots.preferences.AppPreferences;

import java.util.ArrayList;

/*
* This activity shows saved usernames from the database, and allows to delete or update them.
* */

public class FavouriteUsersActivity extends AppCompatActivity {

  private static final String LOG_TAG = FavouriteUsersActivity.class.getSimpleName();
  private DatabaseManager databaseManager;
  FavouriteUsersAdapter adapter;
  ArrayList<String> arrayList;
  String newTag = "";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_favourites);

    databaseManager = new DatabaseManager(this);
    databaseManager.open();
    arrayList = databaseManager.fetchFavouriteUsers();
    for (int i = 0; i < arrayList.size(); i ++) {
      Log.d(LOG_TAG, "Fetched favourite --> " + arrayList.get(i));
    }
    databaseManager.close();

    adapter = new FavouriteUsersAdapter(FavouriteUsersActivity.this, arrayList);
    ListView favouriteList = (ListView)findViewById(R.id.favourite_list);
    favouriteList.setAdapter(adapter);

    favouriteList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long
          id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FavouriteUsersActivity.this);
        builder.setMessage("Delete or Update");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            final EditText editText = new EditText(FavouriteUsersActivity.this);
            AlertDialog.Builder builder = new AlertDialog.Builder(FavouriteUsersActivity.this);
            builder.setView(editText);
            builder.setMessage("Enter new tag");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                newTag = editText.getText().toString();
                databaseManager.open();
                Log.d(LOG_TAG, "Old tag: " + arrayList.get(position) + " --> New tag: " + newTag);
                databaseManager.update(arrayList.get(position), newTag);
                arrayList = databaseManager.fetchFavouriteUsers();
                databaseManager.close();
                adapter.notifyDataSetChanged();
                startActivity(getIntent());
                finish();
              }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
              }
            });
            builder.show();
          }
        });

        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            databaseManager.open();
            boolean returnVal = databaseManager.deleteFavouriteUser(arrayList.get(position));
            Log.w("bool","" + returnVal);
            arrayList.remove(position);
            adapter.notifyDataSetChanged();
            databaseManager.close();
          }
        });
        builder.show();
        return false;
      }
    });

    favouriteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AppPreferences.setProfilePreference(FavouriteUsersActivity.this, arrayList.get(position));
        finish();
      }
    });

    Log.d(LOG_TAG, "onCreate Done...");
  }



}
