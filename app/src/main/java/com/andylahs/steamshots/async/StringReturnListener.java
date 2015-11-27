package com.andylahs.steamshots.async;


import java.util.ArrayList;

/*
* Used to share data between activity and ScrDetailsAsyncTask
* */
public interface StringReturnListener {

  void onHttpReturn(ArrayList<String> stringList);
}
