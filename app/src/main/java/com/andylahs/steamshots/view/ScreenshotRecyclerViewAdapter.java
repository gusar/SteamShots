package com.andylahs.steamshots.view;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andylahs.steamshots.R;
import com.andylahs.steamshots.model.Screenshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ScreenshotRecyclerViewAdapter extends RecyclerView.Adapter<ScreenshotRecyclerViewAdapter.ViewHolder> {

  private ArrayList<Screenshot> screenshotList;
  private Context context;
  private final String LOG_TAG = ScreenshotRecyclerViewAdapter.class.getSimpleName();

  public class ViewHolder extends RecyclerView.ViewHolder {
    protected ImageView thumbnail;
    protected TextView caption;

    public ViewHolder(View view) {
      super(view);
      this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
      this.caption = (TextView) view.findViewById(R.id.caption);
    }
  }

  @Override
  public ScreenshotRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    Screenshot screenshot = screenshotList.get(position);
    Log.d(LOG_TAG, "Processing: " + screenshot.getPageLink() + " --> " + Integer.toString(position));

    Picasso.with(context)
        .load(screenshot.getThumbnailLink())
        .error(R.drawable.placeholder)
        .placeholder(R.drawable.placeholder_s)
        .into(holder.thumbnail);
    holder.caption.setText(screenshot.getDescription());
  }

  public void loadScreenshots(ArrayList<Screenshot> newScreenshotList) {
    screenshotList = newScreenshotList;
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    return (null != screenshotList ? screenshotList.size() : 0);
  }

  public Screenshot getScreenshot(int position) {
    return (null != screenshotList ? screenshotList.get(position) : null);
  }
}
