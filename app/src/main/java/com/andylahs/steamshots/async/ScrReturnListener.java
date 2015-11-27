package com.andylahs.steamshots.async;


import com.andylahs.steamshots.model.Screenshot;

import java.util.ArrayList;

/*
* Used to share data between activity and ScrListAsyncTask
* */
public interface ScrReturnListener {

  void onHttpReturn(ArrayList<Screenshot> objectList);

}
