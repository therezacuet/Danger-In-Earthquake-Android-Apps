package com.thereza.dangerinearthquake;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class HomeActivity extends Activity {

     Button map_view;
	 String[] menu;
     DrawerLayout dLayout;
     ListView dList;
     ArrayAdapter<String> adapter;
     private ActionBarDrawerToggle mDrawerToggle;
     ActionBar bar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		//map_view=(Button) findViewById(R.id.view_map);
		
		bar = getActionBar();
		
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3983C0")));
		bar.setTitle(Html.fromHtml("<b>Easy Safety</b>"));
		
		Integer[] image={R.drawable.map,R.drawable.track,R.drawable.help,R.drawable.user,R.drawable.idea};
        menu = new String[]{"View Who is in Danger","Track your Friend","Quick Help","User Manual","Idea"};
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        
        dList = (ListView) findViewById(R.id.left_drawer);
        dLayout.openDrawer(dList);
        //adapter = new ArrayAdapter<String>(this,R.layout.list_item,R.id.itemName,menu);
        CustomListAdapter adapter=new CustomListAdapter(this, menu, image);

        dList.setAdapter(adapter);
        dList.setSelector(android.R.color.holo_blue_dark);
        
        

        dList.setOnItemClickListener(new OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {

                dLayout.closeDrawers();
                Bundle args = new Bundle();
                args.putString("Menu", menu[position]);
                Fragment detail = new DetailFragment();
                detail.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, detail).commit();

            }

        });
        mDrawerToggle = new ActionBarDrawerToggle(this,dLayout,R.drawable.ic_drawer,R.string.drawer_open,R.string.drawer_close ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
               
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                
            }
        };
        
        

        // Set the drawer toggle as the DrawerListener
        dLayout.setDrawerListener(mDrawerToggle);

        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
     
        
		
	}
	
	@Override
	public void onBackPressed() {
	    // TODO Auto-generated method stub

	    if(dLayout.isDrawerOpen(Gravity.LEFT)){
	    	dLayout.closeDrawer(Gravity.LEFT);
	    }else{
	        super.onBackPressed();
	    }
	}
	
	 @Override
	    public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	        mDrawerToggle.onConfigurationChanged(newConfig);
	    }
	    @Override
	    public boolean onOptionsItemSelected(final MenuItem item) {
	        if (mDrawerToggle.onOptionsItemSelected(item)) {
	            return true;
	        }
	        return super.onOptionsItemSelected(item);
	    }
	    @Override
	    protected void onPostCreate(Bundle savedInstanceState) {
	        super.onPostCreate(savedInstanceState);
	        mDrawerToggle.syncState();
	    }
	

}
