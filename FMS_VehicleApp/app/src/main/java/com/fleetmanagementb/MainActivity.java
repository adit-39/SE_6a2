package com.fleetmanagementb;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fleetmanagementb.helpers.GPSTracker;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends Activity {
	
	String VEHICLE_ID="PESBus1234";
	private volatile String Latitude1,Longitude1;
	public GPSTracker gps;
	TextView tvLat,tvLong;
	public Runnable runnable;Handler handler; getLocation gl;Boolean gettingLoc;
	InputStream is=null;String result=null;String line=null;int code;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startService=(Button)findViewById(R.id.bStartService);
        Button stopService=(Button)findViewById(R.id.bStopService);
        //tvLat=(TextView)findViewById(R.id.tvLat);
        //tvLong=(TextView)findViewById(R.id.tvLong);
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
                startActivity(new Intent(getApplicationContext(),MapsActivity.class));
			}
				
		});
		
		stopService.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//handler.removeCallbacks(runnable);
				//gl.cancel(true);
				gettingLoc=false;
				System.out.println("Real Time Tracking has been stopped Succesfully!!");
			}
		});
		
		/*startService.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				   Use the LocationManager class to obtain GPS locations 
			      LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

			      LocationListener mlocListener = new MyLocationListener();
			      mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
			   
				
				
				if(gps.canGetLocation()){  
					getGPS();
				}else{
					gps.showSettingsAlert();
					return;
				}
			}
		});
   */ 
		
    
    
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
			    tvLat.append("Latitude   "+Latitude1+"\n");
				tvLong.append("Longitude   "+Longitude1+"\n");
			}
		});
		
	}
    
    class getLocation extends AsyncTask<String,String,String>{

    	
    	
    	
		
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
		    Log.e("HTTPGet","Updating to database failed!!");
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
     Intent intent=new Intent(MainActivity.this,FeedBackForm.class);
     intent.setData(Uri.parse("https://docs.google.com/forms/d/1LcyV3S3iZ7neKt9fmH3sVD_B1Z0twalEUxubiv14Ey8/viewform"));
     startActivity(intent);  
 }

/*

 public void onSOSClick(View v) {
     Log.i("Send SMS", "");

     new insertSOS().execute();

     String phoneNo = "9611836018";
     String message = "Emergency Alert::\nhttps://www.google.com/maps/place/"+Latitude1+","+Longitude1;

     try {
        SmsManager smsManager = SmsManager.getDefault();
        //smsManager.sendTextMessage(phoneNo, null, message, null, null);
        Toast.makeText(getApplicationContext(), "SMS sent.",
        Toast.LENGTH_LONG).show();
     } catch (Exception e) {
        Toast.makeText(getApplicationContext(),
        "SMS faild, please try again.",
        Toast.LENGTH_LONG).show();
        e.printStackTrace();
     }
  }
*/


  
}
