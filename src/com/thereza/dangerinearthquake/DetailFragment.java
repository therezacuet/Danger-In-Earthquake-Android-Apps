package com.thereza.dangerinearthquake;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
 
@SuppressLint("NewApi")
public class DetailFragment extends Fragment {
    TextView text;
    Button track;
    EditText get_num;
    ListView list;
    ArrayList<String> data;
    ArrayAdapter<String> adapter;
    WebView web1,webview;
    private ProgressDialog progressBar;
	private static final String TAG = "Main";
	private Menu mymenu;
	ActionBar bar;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle args) {
        String menu = getArguments().getString("Menu");
        bar = getActivity().getActionBar();
        final AlertDialog alertDialog = new AlertDialog.Builder(this.getActivity()).create();
        
        if(menu.equals("View Who is in Danger")){
        	View view = inflater.inflate(R.layout.activity_map, container, false);
        	bar.setTitle("View Who is in Danger");
        	web1 = (WebView) view.findViewById(R.id.map);
    		web1.getSettings().setJavaScriptEnabled(true);
    		web1.getSettings().setBuiltInZoomControls(true);
    		
    		progressBar = ProgressDialog.show(this.getActivity(), "", "Loading...Please Wait");

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
                    //Toast.makeText(MapActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
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
        	 
        	 return view;
        }
        else if(menu.equals("Track your Friend")){
        	View view = inflater.inflate(R.layout.tracking_activity, container, false);
        	bar.setTitle("Track your Friend");
        	track=(Button) view.findViewById(R.id.track);
        	get_num=(EditText) view.findViewById(R.id.num);
        	track.setOnClickListener(new OnClickListener() {			 
    			@Override
    			public void onClick(View v) { 
    			  String phoneNo =get_num.getText().toString();
    			  
    			  if(phoneNo.trim().length()==0){
    				  alertDialog.setTitle("              Attention!!");
    				  alertDialog.setMessage("      Please give your friends phone                 number and\n              Track again!!");
    				  alertDialog.setCancelable(false);
    				  alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
						
			        	   @Override
			        	   public void onClick(DialogInterface dialog, int which) {
			        		   dialog.cancel();
							
			        	   }
			           });
    				  alertDialog.show();	  
    			  }
    			  else{
    			     String msg = "Are you safe?";
    			  try {
    				  
    				SmsManager smsManager = SmsManager.getDefault();
    				smsManager.sendTextMessage(phoneNo, null, msg, null, null);				
    				Toast.makeText(getActivity(), "Message Sent",
    							Toast.LENGTH_LONG).show();
    			  } catch (Exception ex) {
    				Toast.makeText(getActivity(),
    					ex.getMessage().toString(),
    					Toast.LENGTH_LONG).show();
    				ex.printStackTrace();
    			  }
    			 }
    			}
    			
    		});
        	
        	 return view;
        }
       
        else if(menu.equals("Quick Help")){
        	View view = inflater.inflate(R.layout.help_activity, container, false);
        	bar.setTitle("Quick Help");
        	 return view;
        }
        
        else if(menu.equals("User Manual")){
        	View view = inflater.inflate(R.layout.user_manual, container, false);
        	bar.setTitle("User Manual");
        	
        	webview = (WebView) view.findViewById(R.id.webview);
        	webview.getSettings().setJavaScriptEnabled(true);
        
    		
    		progressBar = ProgressDialog.show(this.getActivity(), "", "Loading...Please Wait");

    		webview.setWebViewClient(new WebViewClient() {
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
                    //Toast.makeText(MapActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
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
    		
    		webview.loadUrl("file:///android_asset/index.html");
        	 return view;
        }
        
        else if(menu.equals("Idea")){
        	View view = inflater.inflate(R.layout.idea, container, false);
        	bar.setTitle("Idea");
        	 return view;
        }
		return container;
       
    }
    
    
 
}