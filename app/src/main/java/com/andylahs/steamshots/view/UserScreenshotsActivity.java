package com.andylahs.steamshots.view;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.andylahs.steamshots.R;
import com.andylahs.steamshots.controller.HttpReturnListener;
import com.andylahs.steamshots.controller.ScrListAsyncTask;
import com.andylahs.steamshots.model.Screenshot;
import com.andylahs.steamshots.preferences.AppPreferences;

import java.util.ArrayList;

public class UserScreenshotsActivity extends BaseActivity implements
    HttpReturnListener,
    SearchView.OnQueryTextListener {

  private static final String LOG_TAG = UserScreenshotsActivity.class.getSimpleName();
  private RecyclerView recyclerView;
  private ScreenshotRecyclerViewAdapter recyclerViewAdapter;
  private SwipeRefreshLayout swipeRefreshLayout;
  private int screenHeight;
  private SearchView searchView;

  @Override

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    DisplayMetrics displayMetrics = new DisplayMetrics();
    WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    wm.getDefaultDisplay().getMetrics(displayMetrics);
    screenHeight = displayMetrics.heightPixels;


    swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_swipe_refresh_layout);
    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    recyclerView.addItemDecoration(new MarginDecoration(this));
    recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    String savedProfile = AppPreferences.getProfilePreference(this);
    Log.d(LOG_TAG, "Saved Profile: " + savedProfile);
    setTitle(savedProfile);
    processSearch(screenHeight, savedProfile);
    swipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.green, R.color.blue_grey);
    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            String refreshProfile = AppPreferences.getProfilePreference(UserScreenshotsActivity.this);
            Log.d(LOG_TAG, "Refresh Profile: " + refreshProfile);
            processSearch(screenHeight, refreshProfile);
            swipeRefreshLayout.setRefreshing((false));
          }
        }, 2500);
      }
    });


    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        searchView.setIconified(false);
//        Snackbar.make(view, "haha", Snackbar.LENGTH_LONG).setAction("Action", null).show();
      }
    });

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.setDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
  }


  private void setTitle(String savedProfile) {
    try {
      getSupportActionBar().setTitle(savedProfile);
    } catch (NullPointerException e) {
      Log.e(LOG_TAG, "Can't Set Title", e);
    }
  }


  private boolean processSearch(int screenHeight, String profile) {
    recyclerViewAdapter = new ScreenshotRecyclerViewAdapter(
        UserScreenshotsActivity.this,
        new ArrayList<Screenshot>(),
        screenHeight
    );
    recyclerView.setAdapter(recyclerViewAdapter);
    ScrListAsyncTask scrListAsyncTask = new ScrListAsyncTask();
    scrListAsyncTask.setOnHttpReturnListener(this);
    scrListAsyncTask.execute(profile);

    return true;
  }


  @Override
  public void onHttpReturn(ArrayList<Screenshot> screenshotArrayList) {
    if (screenshotArrayList.size() < 1) {
      Log.d(LOG_TAG, "NO DATA RETURNED");
    } else {
      Log.d(LOG_TAG, Integer.toString(screenshotArrayList.size()));
    }
    recyclerViewAdapter.loadScreenshots(screenshotArrayList);
  }

  @Override
  public boolean onQueryTextChange(String newText) {
    return true;
  }

  @Override
  public boolean onQueryTextSubmit(String query) {
    AppPreferences.setProfilePreference(this, query);
    Log.d(LOG_TAG, "onQueryTextSubmit: " + query);
    if(getCurrentFocus()!=null) {
      InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
    searchView.setIconified(true);
    setTitle(query);
    return processSearch(screenHeight, query);
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);

    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    searchView = (SearchView) menu.findItem(R.id.search).getActionView();
    SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
    searchView.setSearchableInfo(searchableInfo);
    searchView.setOnQueryTextListener(this);

    if (AppPreferences.getProfilePreference(this).equals("null")) {
      searchView.setIconified(false);
    }

    return true;
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.search) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

}
