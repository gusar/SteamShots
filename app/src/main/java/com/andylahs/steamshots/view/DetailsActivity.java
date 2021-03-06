package com.andylahs.steamshots.view;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.andylahs.steamshots.R;
import com.andylahs.steamshots.async.ScrDetailsAsyncTask;
import com.andylahs.steamshots.async.StringReturnListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class DetailsActivity extends Activity implements StringReturnListener {
  /**
   * Whether or not the system UI should be auto-hidden after
   * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
   */
  private static final boolean AUTO_HIDE = true;

  /**
   * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
   * user interaction before hiding the system UI.
   */
  private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

  /**
   * Some older devices needs a small delay between UI widget updates
   * and a change of the status and navigation bar.
   */
  private static final int UI_ANIMATION_DELAY = 300;
  private static final String LOG_TAG = DetailsActivity.class.getSimpleName();

  private ImageView mContentView;
//  private View mControlsView;
  private boolean mVisible;
  private ArrayList<String> largeImageDetails;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_details);
    largeImageDetails = new ArrayList<>();

    mVisible = true;
    mContentView = (ImageView) findViewById(R.id.fullscreen_content);

    Intent intent = getIntent();
    String id = intent.getStringExtra("SCREENSHOT_TRANSFER");
    Log.d(LOG_TAG, "Received ID by DetailsActivity: " + id);

    ScrDetailsAsyncTask scrDetailsAsyncTask = new ScrDetailsAsyncTask();
    scrDetailsAsyncTask.setOnHttpReturnListener(this);
    scrDetailsAsyncTask.execute(id);
  }

  @Override
  public void onHttpReturn(ArrayList<String> stringArrayList) {
    if (stringArrayList.size() < 1) {
      Log.d(LOG_TAG, "NO DATA RETURNED");
    } else {
      largeImageDetails = stringArrayList;
      Log.d(LOG_TAG, "Full Resolution: " + largeImageDetails.get(0) + " --> " + largeImageDetails.get(1));

      Picasso.with(this).load(largeImageDetails.get(1))
          .error(R.drawable.placeholder)
          .placeholder(R.drawable.placeholder)
          .into(mContentView);
      // Set up the user interaction to manually show or hide the system UI.
      mContentView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Log.d(LOG_TAG, "CONTENT CLICKED");
          toggle();
        }
      });

      // Upon interacting with UI controls, delay any scheduled hide()
      // operations to prevent the jarring behavior of controls going away
      // while interacting with the UI.
//      findViewById(R.id.fullscreen_caption).setOnTouchListener(mDelayHideTouchListener);
    }
  }

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);

    // Trigger the initial hide() shortly after the activity has been
    // created, to briefly hint to the user that UI controls
    // are available.
    delayedHide(100);
  }

  /**
   * Touch listener to use for in-layout UI controls to delay hiding the
   * system UI. This is to prevent the jarring behavior of controls going away
   * while interacting with activity UI.
   */
  private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
      if (AUTO_HIDE) {
        delayedHide(AUTO_HIDE_DELAY_MILLIS);
      }
      return false;
    }
  };

  private void toggle() {
    if (mVisible) {
      hide();
    } else {
      show();
    }
  }

  private void hide() {
    // Hide UI first
    ActionBar actionBar = getActionBar();
    if (actionBar != null) {
      actionBar.hide();
    }
//    mControlsView.setVisibility(View.GONE);
    mVisible = false;

    // Schedule a runnable to remove the status and navigation bar after a delay
    mHideHandler.removeCallbacks(mShowPart2Runnable);
    mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
  }

  private final Runnable mHidePart2Runnable = new Runnable() {
    @SuppressLint("InlinedApi")
    @Override
    public void run() {
      // Delayed removal of status and navigation bar

      // Note that some of these constants are new as of API 16 (Jelly Bean)
      // and API 19 (KitKat). It is safe to use them, as they are inlined
      // at compile-time and do nothing on earlier devices.
      mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
          | View.SYSTEM_UI_FLAG_FULLSCREEN
          | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
          | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
          | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
          | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
  };

//  @SuppressLint("InlinedApi")
//  private void show() {
//    // Show the system bar
//    mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
//    mVisible = true;
//
//    // Schedule a runnable to display UI elements after a delay
//    mHideHandler.removeCallbacks(mHidePart2Runnable);
//    mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
//  }

  private final Runnable mShowPart2Runnable = new Runnable() {
    @Override
    public void run() {
      // Delayed display of UI elements
      ActionBar actionBar = getActionBar();
      if (actionBar != null) {
        actionBar.show();
      }
//      mControlsView.setVisibility(View.VISIBLE);
    }
  };

  private final Handler mHideHandler = new Handler();
  @SuppressLint("InlinedApi")
  private void show() {
    // Show the system bar
    mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
    mVisible = true;

    // Schedule a runnable to display UI elements after a delay
    mHideHandler.removeCallbacks(mHidePart2Runnable);
    mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
  }

  private final Runnable mHideRunnable = new Runnable() {
    @Override
    public void run() {
      hide();
    }
  };

  /**
   * Schedules a call to hide() in [delay] milliseconds, canceling any
   * previously scheduled calls.
   */
  private void delayedHide(int delayMillis) {
    mHideHandler.removeCallbacks(mHideRunnable);
    mHideHandler.postDelayed(mHideRunnable, delayMillis);
  }
}
