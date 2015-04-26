package com.example.admin.parentapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class Emergency extends ActionBarActivity {

    private String lat,lon,n,usn;
    private TextView info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        Intent i = getIntent();
        n = i.getStringExtra("info");
        usn = i.getStringExtra("usn");
        lat = i.getStringExtra("lat");
        lon = i.getStringExtra("lon");

        info = (TextView) findViewById(R.id.textView3);
        if(n==null)
            info.setText("Subscriber with USN:"+usn+" has been involved in emergency. Please View Map for last known location");
        else
            info.setText(n);
    }

    public void showMap(View v)
    {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?q="+lat+","+lon));
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_emergency, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
