package com.andylahs.steamshots.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionManager {

  static String stream = null;

  public ConnectionManager(){}

  public String getHttpStream(URL url) {
    try {
      HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

      // Check the connection status
      if(urlConnection.getResponseCode() == 200)
      {
        // if response code = 200 ok
        InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

        // Read the BufferedInputStream
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
          stringBuilder.append(line);
        }
        stream = stringBuilder.toString();
        // End reading...............

        // Disconnect the HttpURLConnection
        urlConnection.disconnect();
      }
    } catch(IOException e) {
      e.printStackTrace();
      // Return the data from specified url
    }
    return stream;
  }
}