package com.fleetmanagementb;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fleetmanagementb.helpers.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    TextView tvLat,tvLong;
    String VEHICLE_ID="PESBus1234";
    private volatile String Latitude1,Longitude1;
    public GPSTracker gps;
   // TextView tvLat,tvLong;
    public Runnable runnable;Handler handler; getLocation gl;Boolean gettingLoc;
    InputStream is=null;String result=null;String line=null;int code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps1);
        tvLat=(TextView)findViewById(R.id.tvLat1);
        tvLong=(TextView)findViewById(R.id.tvLong1);
        Button startService=(Button)findViewById(R.id.bStartService);

        gettingLoc=false;

       /* android.app.ActionBar actionBar =  getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#c53e2b"));
		actionBar.setBackgroundDrawable(colorDrawable);*/

        gps = new GPSTracker(this);
        handler = new Handler();


        startService.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                gettingLoc=true;
                gl= new getLocation();
                gl.execute();
              //  startActivity(new Intent(getApplicationContext(),MapsActivity.class));
            }

        });


        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(12.9329739, 77.5345093)).title("PES University"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                12.9329739, 77.5345093), 15));
        mMap.addCircle(new CircleOptions()
                .center(new LatLng(12.9329739, 77.5345093)).radius(150)
                .fillColor(Color.parseColor("#B2A9F6")));
    }

    public void getGPS()
    {
        double latitude = gps.getLatitude();
        Latitude1= String.valueOf(latitude);
        double longitude = gps.getLongitude();
        Longitude1= String.valueOf(longitude);

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                insertLoc al=new insertLoc();
                al.execute();
                //insert(Latitude1,Longitude1);

            }
        }).start();




        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                System.out.println("Updating UI");
                tvLat.append("Latitude   "+Latitude1+"\n");
                tvLong.append("Longitude   "+Longitude1+"\n");
            }
        });

    }

    class getLocation extends AsyncTask<String,String,String> {





        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            Toast.makeText(getApplicationContext(), "Service has been started.", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub

					/*if(gps.canGetLocation()){
						getGPS();
					}else{
						gps.showSettingsAlert();
						return;
					}
					*/

                    handler.postDelayed(runnable, 5000);
                    runnable = new Runnable() {
                        @Override
                        public void run() {
						      /* do what you need to do */
                            getGPS();


						      /* and here comes the "trick" */
                            if(gettingLoc){System.out.println("itz called");
                                handler.postDelayed(this, 3000);}
                        }
                    };

                }
            });

            return null;
        }

    }

    public void insert(String latt,String longt)
    {
        try{
            System.out.println("In insert");
            HttpClient client = new DefaultHttpClient();
            String url="http://104.199.153.214/api1/api/vehicle/"+VEHICLE_ID+"/"+latt+"/"+longt;
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);

            // Get the response
            BufferedReader rd = new BufferedReader
                    (new InputStreamReader(response.getEntity().getContent()));

            String line = "";
            while ((line = rd.readLine()) != null) {
                System.out.println("Response From HttpGet "+line);
            }

        }
        catch(Exception e)
        {
            Log.e("HTTPGet", "Updating to database failed!!");
        }
    }





    class insertLoc extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            insert(Latitude1,Longitude1);
            return null;
        }
    }
/*
 class insertSOS extends AsyncTask<String, String, String>{

	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		insertsos(Latitude1,Longitude1);
		return null;
	}
 }*/


    public void onFeedbackClick(View v)
    {
        // TODO Auto-generated method stub
        Intent intent=new Intent(MapsActivity.this,FeedBackForm.class);
        intent.setData(Uri.parse("https://docs.google.com/forms/d/1LcyV3S3iZ7neKt9fmH3sVD_B1Z0twalEUxubiv14Ey8/viewform"));
        startActivity(intent);
    }


}
