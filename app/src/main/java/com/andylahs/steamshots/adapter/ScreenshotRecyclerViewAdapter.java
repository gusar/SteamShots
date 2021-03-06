package com.andylahs.steamshots.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andylahs.steamshots.R;
import com.andylahs.steamshots.model.Screenshot;
import com.andylahs.steamshots.view.DetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ScreenshotRecyclerViewAdapter extends RecyclerView.Adapter<ScreenshotRecyclerViewAdapter.ViewHolder> {

  private ArrayList<Screenshot> screenshotList;
  private Context context;
  private final String LOG_TAG = ScreenshotRecyclerViewAdapter.class.getSimpleName();
  private int screenHeight;

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    protected ImageView thumbnail;
    protected TextView caption;
    Screenshot screenshot;

    public ViewHolder(View view) {
      super(view);
      this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
      this.thumbnail.getLayoutParams().height = screenHeight / 5;
      this.thumbnail.requestLayout();
      this.caption = (TextView) view.findViewById(R.id.caption);
      view.setOnClickListener(this);
    }

    public void setDetails(Screenshot screenshot) {
      this.screenshot = screenshot;
      Picasso.with(context)
          .load(screenshot.getThumbnailLink())
          .error(R.drawable.placeholder)
          .placeholder(R.drawable.placeholder_s)
          .into(this.thumbnail);
      String description = screenshot.getDescription();
      if (!(description.equals("null"))) {
        caption.setText(description);
        caption.setBackgroundColor(Color.parseColor("#70000000"));
      } else {
        caption.setBackgroundColor(Color.parseColor("#00000000"));
      }
      Log.d(LOG_TAG, "Binding: " + screenshot.getId());
    }

    @Override
    public void onClick(View v) {
      String id = screenshot.getId();
      Intent intent = new Intent(context, DetailsActivity.class);
      intent.putExtra("SCREENSHOT_TRANSFER", id);
      Log.d(LOG_TAG, "Sending ID to Details Activity: " + id);
      context.startActivity(intent);
    }
  }

  public ScreenshotRecyclerViewAdapter(Context context, ArrayList<Screenshot> screenshotArrayList, int screenHeight) {
    this.context = context;
    this.screenshotList = screenshotArrayList;
    this.screenHeight = screenHeight;
  }

  @Override
  public ScreenshotRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, null);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.setDetails(screenshotList.get(position));
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
