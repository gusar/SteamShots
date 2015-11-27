package com.andylahs.steamshots.controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

  public interface OnItemClickListener {
    void onItemClick(View view, int position);
  }

  private OnItemClickListener listener;
  private GestureDetector gestureDetector;

  public RecyclerItemClickListener(Context context,
                            final RecyclerView recyclerView,
                            final OnItemClickListener onItemClickListener) {
    this.listener = onItemClickListener;
    gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

      public void onTap(MotionEvent motionEvent) {
        View view = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (view != null && listener != null) {
          listener.onItemClick(view, recyclerView.getChildPosition(view));
        }
      }
    });
  }


  public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
    View view = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
    if (view != null && listener != null && gestureDetector.onTouchEvent(motionEvent)) {
      listener.onItemClick(view, recyclerView.getChildPosition(view));
    }
    return false;
  }


  @Override
  public void onTouchEvent(RecyclerView rv, MotionEvent e) {
  }

  @Override
  public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
  }
}
