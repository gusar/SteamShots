package com.andylahs.steamshots.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Screenshot extends RealmObject{

  @PrimaryKey
  String id;
  String thumbnailLink;
  String hqLink;
  String description;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getThumbnailLink() {
    return thumbnailLink;
  }

  public void setThumbnailLink(String thumbnailLink) {
    this.thumbnailLink = thumbnailLink;
  }

  public String getHqLink() {
    return hqLink;
  }

  public void setHqLink(String hqLink) {
    this.hqLink = hqLink;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
