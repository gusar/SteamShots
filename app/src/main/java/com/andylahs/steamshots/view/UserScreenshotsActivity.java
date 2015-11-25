package com.andylahs.steamshots.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.andylahs.steamshots.R;
import com.andylahs.steamshots.controller.HttpReturnListener;
import com.andylahs.steamshots.controller.ScrListAsyncTask;
import com.andylahs.steamshots.model.Screenshot;

import java.util.ArrayList;

public class UserScreenshotsActivity extends BaseActivity implements HttpReturnListener {

  private static final String LOG_TAG = UserScreenshotsActivity.class.getSimpleName();
  private RecyclerView recyclerView;
  private ScreenshotRecyclerViewAdapter recyclerViewAdapter;
  private SwipeRefreshLayout swipeRefreshLayout;
  private int screenHeight;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    DisplayMetrics displayMetrics = new DisplayMetrics();
    WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    wm.getDefaultDisplay().getMetrics(displayMetrics);
//    int screenWidth = displayMetrics.widthPixels;
    screenHeight = displayMetrics.heightPixels;

    /*
    *
    * */

    swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_swipe_refresh_layout);
    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    recyclerView.addItemDecoration(new MarginDecoration(this));
    recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    initAdapter(screenHeight);
    swipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.green, R.color.blue_grey);
    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            initAdapter(screenHeight);
            swipeRefreshLayout.setRefreshing((false));
          }
        }, 1500);
      }
    });

    /*
    *
    * */

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "haha", Snackbar.LENGTH_LONG).setAction("Action", null).show();
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

  private void initAdapter(int screenHeight) {
    recyclerViewAdapter = new ScreenshotRecyclerViewAdapter(
        UserScreenshotsActivity.this,
        new ArrayList<Screenshot>(),
        screenHeight
    );
    recyclerView.setAdapter(recyclerViewAdapter);
    ScrListAsyncTask scrListAsyncTask = new ScrListAsyncTask();
    scrListAsyncTask.setOnHttpReturnListener(this);
    scrListAsyncTask.execute();
  }

//  @Override
//  protected void onResume() {
//    super.onResume();
//
//    ScrListAsyncTask scrListAsyncTask = new ScrListAsyncTask();
//    scrListAsyncTask.setOnHttpReturnListener(this);
//    scrListAsyncTask.execute();
//  }


  @Override
  public void onHttpReturn(ArrayList<Screenshot> screenshotArrayList) {
//    for (Screenshot object: objectList) {
//      Log.v("SCREENSHOT: ", object.getPageLink());
//    }
    recyclerViewAdapter.loadScreenshots(screenshotArrayList);
  }

}
