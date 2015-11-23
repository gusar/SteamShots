package com.andylahs.steamshots.model;

public class Screenshot {

  String pageLink;
  String thumbnailLink;
  String hqLink;
  String description;

  public Screenshot() {
  }

  public String getPageLink() {
    return pageLink;
  }

  public void setPageLink(String pageLink) {
    this.pageLink = pageLink;
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
