package com.andylahs.steamshots.view;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
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
import com.andylahs.steamshots.adapter.ScreenshotRecyclerViewAdapter;
import com.andylahs.steamshots.async.ScreenshotListManager;
import com.andylahs.steamshots.model.Screenshot;
import com.andylahs.steamshots.preferences.AppPreferences;

import io.realm.Realm;
import io.realm.RealmList;

/*
* Main activity
* Serves as a gallery of Steam community screenshots searchable by username or steam id
* The queries use HttpScrListAsync to reach my virtual private server.
* The server is written in Node.js and it's job is to scrape full web pages, parse for useful data,
* and then return only required data to conserve users' data consumption.
*
* */

public class UserScreenshotsActivity extends BaseActivity implements
    SearchView.OnQueryTextListener {

  private static final String LOG_TAG = UserScreenshotsActivity.class.getSimpleName();
  private RecyclerView recyclerView;
  private ScreenshotRecyclerViewAdapter recyclerViewAdapter;
  private SwipeRefreshLayout swipeRefreshLayout;
  private SearchView searchView;
  private int screenHeight;
  private Realm realm;
  private ScreenshotListManager screenshotListManager;
  private RealmList<Screenshot> scrList;

  @Override

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    realm = Realm.getInstance(this);
    screenshotListManager = ScreenshotListManager.getInstance(realm);

    screenHeight = getDisplayMetrics();
    String savedProfile = AppPreferences.getProfilePreference(this);
    Log.d(LOG_TAG, "Saved Profile: " + savedProfile);
    setTitle(savedProfile);

    swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_swipe_refresh_layout);
    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    recyclerView.addItemDecoration(new MarginDecoration(this));
    recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

    processSearch(screenHeight, savedProfile);
    loadRefreshLayout();

    /*
    * The fab links to the user's full profile through a WebView activity.
    * */
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(UserScreenshotsActivity.this, ProfileWebViewActivity.class);
        intent.putExtra("profile", AppPreferences.getProfilePreference(getApplicationContext()));
        startActivity(intent);
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


  private int getDisplayMetrics() {
    DisplayMetrics displayMetrics = new DisplayMetrics();
    WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    wm.getDefaultDisplay().getMetrics(displayMetrics);
    return displayMetrics.heightPixels;
  }


  /*
  * Initialise pull down gesture to refresh the list
  * */
  private void loadRefreshLayout() {
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
  }


  private void setTitle(String savedProfile) {
    try {
      getSupportActionBar().setTitle(savedProfile);
    } catch (NullPointerException e) {
      Log.e(LOG_TAG, "Can't Set Title", e);
    }
  }


  /*
  * Use AsyncTask to start a new search
  * */
  private boolean processSearch(int screenHeight, String profile) {
    recyclerViewAdapter = new ScreenshotRecyclerViewAdapter(
        UserScreenshotsActivity.this,
        new RealmList<Screenshot>(),
        screenHeight
    );
    recyclerView.setAdapter(recyclerViewAdapter);
    scrList = screenshotListManager.execute(profile);
    recyclerViewAdapter.loadScreenshots(scrList);

    return true;
  }





  @Override
  public boolean onQueryTextChange(String newText) {
    return true;
  }

  /*
  * Listener for search query submission
  * */
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
  protected void onResume() {
    super.onResume();
    String savedProfile = AppPreferences.getProfilePreference(this);
    processSearch(screenHeight, savedProfile);
    setTitle(savedProfile);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.search, menu);

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
    } else if (id == R.id.doFavourite) {
      //Todo: realm transaction for adding to favourites
      CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
      Snackbar.make(coordinatorLayout, "User added to favourites", Snackbar.LENGTH_LONG).show();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }


  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    if (id == R.id.nav_favourites) {
      Intent intent = new Intent(UserScreenshotsActivity.this, FavouriteUsersActivity.class);
      startActivity(intent);
    } else if (id == R.id.nav_gallery) {
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }
}
