package com.andylahs.steamshots.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.andylahs.steamshots.R;
import com.andylahs.steamshots.adapter.FavouriteUsersAdapter;
import com.andylahs.steamshots.database.DatabaseManager;

import java.util.ArrayList;

public class FavouriteUsersActivity extends AppCompatActivity {

  private static final String LOG_TAG = FavouriteUsersActivity.class.getSimpleName();
  private DatabaseManager databaseManager;
  FavouriteUsersAdapter adapter;
  ArrayList<String> arrayList;
  String newName = "";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_favourites);

    databaseManager = new DatabaseManager(this);
    databaseManager.open();
    arrayList = databaseManager.fetchFavouriteUsers();
    databaseManager.close();

    adapter = new FavouriteUsersAdapter(FavouriteUsersActivity.this, arrayList);
    ListView favouriteList = (ListView)findViewById(R.id.favourite_list);
    favouriteList.setAdapter(adapter);

    favouriteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FavouriteUsersActivity.this);
        builder.setMessage("Delete or Update");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            databaseManager.open();
            databaseManager.update(arrayList.get(position), newName);
            adapter.notifyDataSetChanged();
            databaseManager.close();
          }
        });

        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            databaseManager.open();
            databaseManager.deleteFavouriteUser(arrayList.get(position));
            arrayList.remove(position);
            adapter.notifyDataSetChanged();
            databaseManager.close();
          }
        });
        builder.show();
      }
    });

    Log.d(LOG_TAG, "onCreate Done...");
  }

}
