package com.thereza.dangerinearthquake;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MapActivity extends Activity {

	WebView web1;
	private ProgressDialog progressBar;
	private static final String TAG = "Main";
	private Menu mymenu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3983C0")));
		bar.setTitle(Html.fromHtml("<b>LOCATION ON MAP</b>"));
		
		web1=(WebView) findViewById(R.id.map);
		web1.getSettings().setJavaScriptEnabled(true);
		web1.getSettings().setBuiltInZoomControls(true);
		
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        progressBar = ProgressDialog.show(MapActivity.this, "", "Loading...Please Wait");

        web1.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "Finished loading URL: " + url);
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e(TAG, "Error: " + description);
                Toast.makeText(MapActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(description);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alertDialog.show();
            }
        });
		web1.loadUrl("http://dangerinearthquake.batikrom.com/index.php");
	}
	
	
	//when back button press perform this
			@Override
			public void onBackPressed() {
			 
			    if (web1.canGoBack()) {
			        web1.goBack();
			    } else {
			    	super.onBackPressed();
		      
			    }
	 }
			
			
			
			@Override
			public boolean onCreateOptionsMenu(Menu menu) {
				// Inflate the menu; this adds items to the action bar if it is present.
				getMenuInflater().inflate(R.menu.map, menu);
				mymenu = menu;
				return true;
			}
			
			@Override
		    public boolean onOptionsItemSelected(MenuItem item) {
		        switch(item.getItemId()) {
		        case R.id.action_refresh:
		            Intent refresh = new Intent(this,MapActivity.class);
		        	startActivity(refresh);//Start the same Activity
		        	finish(); //finish Activity.
		        }
		        
		        
		        return super.onOptionsItemSelected(item);
		    }
			
		    
			
		     
		    @SuppressLint("NewApi")
			public void resetUpdating()
		    {
		        // Get our refresh item from the menu
		        MenuItem m = mymenu.findItem(R.id.action_refresh);
		        if(m.getActionView()!=null)
		        {
		            // Remove the animation.
		            m.getActionView().clearAnimation();
		            m.setActionView(null);
		        }
		        
		        
		        
		    }
			
}
