package com.andylahs.steamshots.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.andylahs.steamshots.R;

import java.util.List;

public class FavouriteUsersAdapter extends ArrayAdapter<String> {

  public FavouriteUsersAdapter(Context context, List<String> users) {
    super(context, R.layout.user_row, users);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = LayoutInflater.from(getContext());

    if(convertView == null) {
      convertView = inflater.inflate(R.layout.user_row, parent, false);
    }

    TextView user_id = (TextView)convertView.findViewById(R.id.row_username);

    String singleUser = getItem(position);

    user_id.setText(singleUser);

    return convertView;
  }
}
