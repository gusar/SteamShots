package com.andylahs.steamshots.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.andylahs.steamshots.R;

import java.util.ArrayList;
import java.util.Map;

public class FavouriteUsersAdapter extends BaseAdapter {

  private static final String LOG_TAG = FavouriteUsersAdapter.class.getSimpleName();
  private final ArrayList<Map.Entry<String, Boolean>> favouriteUsers;

  private static class ViewHolder {
    private TextView username;
    private CheckBox checkBox;
  }

  public FavouriteUsersAdapter(Map<String, Boolean> map) {
    favouriteUsers = new ArrayList<>();
    favouriteUsers.addAll(map.entrySet());
  }

  @Override
  public int getCount() {
    return favouriteUsers.size();
  }

  @Override
  public Map.Entry<String, Boolean> getItem(int position) {
    return (Map.Entry) favouriteUsers.get(position);
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder;
    if (convertView == null) {
      convertView = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.user_row, parent, false);

      viewHolder = new ViewHolder();
      viewHolder.username = (TextView) convertView.findViewById(R.id.row_username);
      viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.row_delete);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    Map.Entry<String, Boolean> row = getItem(position);
    if (row != null) {
      viewHolder.username.setText(row.getKey());
      viewHolder.checkBox.setChecked(row.getValue());
    }

    return convertView;
  }

}
