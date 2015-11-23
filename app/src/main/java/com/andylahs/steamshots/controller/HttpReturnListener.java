package com.andylahs.steamshots.controller;


import com.andylahs.steamshots.model.Screenshot;

import java.util.ArrayList;

public interface HttpReturnListener {

  void onHttpReturn(ArrayList<Screenshot> objectList);

}
