package com.andylahs.steamshots.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.andylahs.steamshots.R;

public class ProfileWebView extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile_web_view);

    String profile = getIntent().getExtras().getString("profile");

    WebView webView = (WebView) findViewById(R.id.web_view);
    webView.loadUrl("http://steamcommunity.com/id/"+ profile);
    webView.setWebViewClient(new WebViewClient());
  }
}
