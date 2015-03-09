package com.SEProject.fleetmanagement;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.SEProject.helpers.GPSTracker;

public class MainActivity extends Activity {
	
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
        tvLat=(TextView)findViewById(R.id.tvLat);
        tvLong=(TextView)findViewById(R.id.tvLong);
        gettingLoc=false;
        
        android.app.ActionBar actionBar =  getActionBar();
		ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#c53e2b"));
		actionBar.setBackgroundDrawable(colorDrawable);
		
		gps = new GPSTracker(this);
		handler = new Handler();
		
		
		startService.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				gettingLoc=true;
			  gl= new getLocation();
			  gl.execute();
			}
				
		});
		
		stopService.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//handler.removeCallbacks(runnable);
				//gl.cancel(true);
				gettingLoc=false;
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
		
		insertLoc al=new insertLoc();
		al.execute();
		//insert(Latitude1,Longitude1);
        
		
		
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

    	
    	
    	
		/*@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			gettingLoc=false;
		}*/

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
    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
   	nameValuePairs.add(new BasicNameValuePair("Latitude",latt));
   	nameValuePairs.add(new BasicNameValuePair("Longitude",longt));
    	
    	try
    	{
		HttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new HttpPost("http://mathdemat.comuf.com/insert1.php");
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        HttpResponse response = httpclient.execute(httppost); 
	        HttpEntity entity = response.getEntity();
	        is = entity.getContent();
	        Log.e("pass 1", "connection success ");
	}
        catch(Exception e)
	{
        	Log.e("Fail 1", e.toString());
	    	//Toast.makeText(getApplicationContext(), "Invalid IP Address",
			//Toast.LENGTH_LONG).show();
	}     
        
        try
        {
            BufferedReader reader = new BufferedReader
			(new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
	    {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
	    Log.e("pass 2", "connection success ");
	}
        catch(Exception e)
	{
            Log.e("Fail 2", e.toString());
	}     
       
	try
	{
            JSONObject json_data = new JSONObject(result);
            code=(json_data.getInt("code"));
			
            if(code==1)
            {
            	//Toast.makeText(getBaseContext(), "Inserted Successfully",
            			//System.out.println("Inserted Sucessfully");
            }
            else
            {
			 /*Toast.makeText(getBaseContext(), "Sorry, Try Again",
			 Toast.LENGTH_LONG).show();*/
            	//System.out.println("Sorry Try again");
            }
	}
	catch(Exception e)
	{
            Log.e("Fail 3", e.toString());
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

  
}
