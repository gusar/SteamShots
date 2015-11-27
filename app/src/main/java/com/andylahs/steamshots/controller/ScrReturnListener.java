package com.andylahs.steamshots.controller;


import com.andylahs.steamshots.model.Screenshot;

import java.util.ArrayList;

public interface ScrReturnListener {

  void onHttpReturn(ArrayList<Screenshot> objectList);

}
