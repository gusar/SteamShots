package com.andylahs.steamshots.model;

import java.io.Serializable;

public class Screenshot implements Serializable {

  private static final long serialVersionUID = 1L;

  String id;
  String username;
  String thumbnailLink;
  String hqLink;
  String description;

  public Screenshot() {
  }

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

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
