package com.SEProject.fleetmanagement;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.webkit.WebView;

public class FeedBackForm extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_back_form);
		 WebView wv=(WebView)findViewById(R.id.webView1);
         wv.getSettings().setJavaScriptEnabled(true);
         wv.loadUrl(this.getIntent().getDataString());
	}

/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.feed_back_form, menu);
		return true;
	}*/

}
