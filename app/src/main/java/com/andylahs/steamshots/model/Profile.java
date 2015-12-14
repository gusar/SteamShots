package com.andylahs.steamshots.model;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Profile extends RealmObject {

  @PrimaryKey
  private String id;
  private String alias;
  private RealmList<Screenshot> screenshots;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public RealmList<Screenshot> getScreenshots() {
    return screenshots;
  }

  public void setScreenshots(RealmList<Screenshot> screenshots) {
    this.screenshots = screenshots;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }
}
