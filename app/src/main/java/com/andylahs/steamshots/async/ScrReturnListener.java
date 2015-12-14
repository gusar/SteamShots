package com.andylahs.steamshots.async;


import com.andylahs.steamshots.model.Screenshot;

import io.realm.RealmList;

/*
* Used to share data between activity and HttpScrListAsync
* */
public interface ScrReturnListener {

  void onHttpReturn(RealmList<Screenshot> objectList);

}
