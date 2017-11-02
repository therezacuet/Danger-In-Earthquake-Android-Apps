package com.thereza.dangerinearthquake;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


@SuppressLint("NewApi")
public class MainActivity extends Activity {
	
	// The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
     
	     StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    // GPSTracker class
	     GPSTracker gps;
		 Button safe,danger;
		 static boolean exit;
		 String imei;
		 String getSimNumber="";
		 double latitude,longitude;
		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        Intent intent = new Intent(MainActivity.this, ShakeService.class);
        startService(intent);

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        
        
        
        
        boolean canWriteSms;
        if(!SmsWriteOpUtil.isWriteEnabled(getApplicationContext())) {
            canWriteSms = SmsWriteOpUtil.setWriteEnabled(getApplicationContext(), true);
        }
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        imei=telephonyManager.getDeviceId();
        
        safe = (Button) findViewById(R.id.safe);
        danger= (Button) findViewById(R.id.danger);
        // Get intent object sent from the SMSBroadcastReceiver
        Intent sms_intent=getIntent();
        Bundle b=sms_intent.getExtras();
        if(b!=null){
           	// Display SMS in the TextView
        	final String num_msg=b.getString("sms_str");
        	
        	if(num_msg.equals("GP Internet")){
        		danger.setOnClickListener(new View.OnClickListener() {
                    
                    @Override
                    public void onClick(View arg0) {
                    	
                    	
                    	
                    	// create class object
                        gps = new GPSTracker(MainActivity.this);
                        // check if GPS enabled     
                        if(gps.canGetLocation()){
                            latitude = gps.getLatitude();
                            longitude = gps.getLongitude();
                            // \n is for new line
                            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();    
                        
                            
                            new AsyncTaskRunnerCurrent().execute();
                            
                        }else{
                            // can't get location
                            // GPS or Network is not enabled
                            // Ask user to enable GPS/network in settings
                            gps.showSettingsAlert();
                        }
                        
                       
                        
                         
                    }
                });
        		
        		safe.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						//String phoneNo = "01788345138";
						  String msg = "I am safe and thank you for your responsibility."; 
						  try {
							  
							SmsManager smsManager = SmsManager.getDefault();
							smsManager.sendTextMessage(num_msg, null, msg, null, null);				
							Toast.makeText(getApplicationContext(), "Message Sent",
										Toast.LENGTH_LONG).show();
						  } catch (Exception ex) {
							Toast.makeText(getApplicationContext(),
								ex.getMessage().toString(),
								Toast.LENGTH_LONG).show();
							ex.printStackTrace();
						  }
						  
						  finish();
					}
				});
        	}
        	
        	mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

                @Override
                public void onShake(int count) {
                    //Toast.makeText(getApplicationContext(), "shake",Toast.LENGTH_SHORT).show();

                	// create class object
                    gps = new GPSTracker(MainActivity.this);
                    // check if GPS enabled     
                    if(gps.canGetLocation()){
                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();
                        // \n is for new line
                        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();    
                    
                        
                        new AsyncTaskRunnerCurrent().execute();
                        
                    }else{
                        // can't get location
                        // GPS or Network is not enabled
                        // Ask user to enable GPS/network in settings
                        gps.showSettingsAlert();
                    }
                }
            });
        	
        	
        	
        }
        
    }
    
    class AsyncTaskRunnerCurrent extends AsyncTask<String, String, String>
		{
			 ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
			
			String error="";
			
			
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				
				String result = null;
      	   	InputStream is = null;
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				String imei_no=imei.toString();
				String lat =String.valueOf(latitude);
				String lon = String.valueOf(longitude);

      	    nameValuePairs.add(new BasicNameValuePair("imei_no",imei_no));
      	   	nameValuePairs.add(new BasicNameValuePair("lat",lat));
      	   	nameValuePairs.add(new BasicNameValuePair("long",lon));

      	   	StrictMode.setThreadPolicy(policy); 

				try
				{
					HttpClient httpclient = new DefaultHttpClient();
      	        HttpPost httppost = new HttpPost("http://dangerinearthquake.batikrom.com/dp.php");
      	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
      	        HttpResponse response = httpclient.execute(httppost); 
      	        HttpEntity entity = response.getEntity();
      	        is = entity.getContent();

      	        Log.e("log_tag", "connection success ");
      	        //Toast.makeText(getApplicationContext(), "pass", Toast.LENGTH_SHORT).show();
      	   }
      	
      	
      	catch(Exception e)
      	{
      	        Log.e("log_tag", "Error in http connection "+e.toString());
      	        Toast.makeText(getApplicationContext(), "Connection fail", Toast.LENGTH_SHORT).show();

      	}
      	  
				return result;
		}
			
			
			
			protected void onPostExecute(String string)
			{
				
				progressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "Thanks", Toast.LENGTH_SHORT).show();
				finish();
				
			
			}
			
			protected void onPreExecute()
			{
				 
				progressDialog.setMessage("Please Wait...");
				progressDialog.setCancelable(false);
				progressDialog.show();
			}
			
			
			
			
		}
		
		
		
		//------------------------------------------------------------------
    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }
    
    
    
    
}