package com.soffice.clickandpay.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.soffice.clickandpay.Adapters.SlidingMenuAdapter;
import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.Fragments.NearMeListFragment;
import com.soffice.clickandpay.Fragments.NearMeMapFragment;
import com.soffice.clickandpay.NetWork.JsonRequester;
import com.soffice.clickandpay.NetWork.TaskListner;
import com.soffice.clickandpay.NetWork.Urls;
import com.soffice.clickandpay.Pojo.NearByStoresDTO;
import com.soffice.clickandpay.Pojo.SlideMenu;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.Utilty.Display;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.soffice.clickandpay.Utilty.Utils;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Surya on 28-04-2016.
 */
public class NearMeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,NearMeMapFragment.LocationObserver,View.OnClickListener
{

    public CoordinatorLayout coordinatorlayout;
    DrawerLayout drawerLayout;
    ListView NavigationListView;
    SlidingMenuAdapter navadapter;
    ActionBarDrawerToggle mDrawerToggle;
    Toolbar toolbar;
    ImageView IC_Home,IC_Location,IC_Offers;
    SessionManager session;
    boolean isMap=false;
    FragmentManager fragmentManager;
    FloatingActionButton fab;
    LatLng latLng;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session= ClickandPay.getInstance().getSession();
        fragmentManager=getSupportFragmentManager();
        setContentView(R.layout.activity_nearme);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        fab= (FloatingActionButton) findViewById(R.id.nearMe_fab);
        fab.setEnabled(false);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        IC_Home= (ImageView) findViewById(R.id.ic_home);
        IC_Home.setOnClickListener(this);
        IC_Offers= (ImageView) findViewById(R.id.ic_offers);
        IC_Offers.setOnClickListener(this);
        IC_Location= (ImageView) findViewById(R.id.ic_location);
        IC_Location.setImageResource(R.mipmap.ic_action_home_screen_map_hover);
        coordinatorlayout= (CoordinatorLayout) findViewById(R.id.mycoordinator);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        NavigationListView = (ListView) findViewById(R.id.sliding_menu_lv);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.setHomeAsUpIndicator(R.mipmap.ic_action_home_screen_menu);
        drawerLayout.addDrawerListener(mDrawerToggle);
        BuildNavigationview();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayFragment();
            }
        });
        DisplayFragment();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent launchIntent;
        Display.DisplayLogD("pos",String.valueOf(position));

        switch (position)
        {
            case 0: launchIntent=new Intent(this, ProfileSettings.class);
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(launchIntent);
                finish();
                break;
            case 1: launchIntent=new Intent(this, MainActivity.class);
                launchIntent.putExtra("currentPager",0);
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(launchIntent);
                finish();
                break;
            case 2: launchIntent=new Intent(this, AddMoneyActivity.class);
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(launchIntent);
                finish();
                break;
            case 3: launchIntent=new Intent(this, MainActivity.class);
                launchIntent.putExtra("currentPager",1);
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(launchIntent);
                finish();
                break;

            case 4: launchIntent=new Intent(this, MainActivity.class);
                launchIntent.putExtra("currentPager",2);
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(launchIntent);
                finish();
                break;

            case 5: launchIntent=new Intent(this,TransactionsActivity.class);
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(launchIntent);
                finish();
                break;
            case 7: launchIntent=new Intent(this,OffersActivity.class);
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(launchIntent);
                finish();
                break;
            case 9: launchIntent=new Intent(this,SupportSideActivity.class);
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(launchIntent);
                finish();
                break;
            case 10:launchIntent=new Intent(this,AboutUsActivity.class);
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(launchIntent);
                finish();
                break;

        }
        drawerLayout.closeDrawers();
    }

    /*NAVIGATION DRAWER*/
    private void BuildNavigationview() {

        navadapter= new SlidingMenuAdapter(session,this);
        SlideMenu headerItem=new SlideMenu();
        headerItem.setTitle("Header");
        navadapter.addHeaderLayoutSection(headerItem);
        SlideMenu secItem=new SlideMenu();
        headerItem.setTitle("section");
        for(int i=0;i< Utils.nav_set_one_titles.length;i++)
        {

            SlideMenu item=new SlideMenu();
            item.setTitle(Utils.nav_set_one_titles[i]);
            item.setIcon(Utils.nav_set_one_icons[i]);
            navadapter.addItem(item);

        }
        navadapter.addSectionHeaderItem(secItem);
        for(int i=0;i< Utils.nav_set_two_titles.length;i++)
        {
            if(!Utils.nav_set_two_titles[i].equalsIgnoreCase("Near Me")) {
                SlideMenu item = new SlideMenu();
                item.setTitle(Utils.nav_set_two_titles[i]);
                item.setIcon(Utils.nav_set_two_icons[i]);
                navadapter.addItem(item);
            }
        }
        navadapter.addSectionHeaderItem(secItem);
        for(int i=0;i< Utils.nav_set_three_titles.length;i++)
        {
            SlideMenu item=new SlideMenu();
            item.setTitle(Utils.nav_set_three_titles[i]);
            item.setIcon(0);
            navadapter.addItem(item);
        }
        NavigationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        NavigationListView.setAdapter(navadapter);
        navadapter.notifyDataSetChanged();
        NavigationListView.setOnItemClickListener(this);
    }

    private void DisplayFragment()
    {
        FragmentTransaction ft=fragmentManager.beginTransaction();
        if(isMap)
        {
            isMap=false;
            Fragment mapfrag=fragmentManager.findFragmentByTag("nearme_map");
            if(mapfrag!=null)
            {
                ft.hide(mapfrag);
            }
            Fragment listfrag=fragmentManager.findFragmentByTag("nearme_list");
            if(listfrag==null)
            {
                ft.add(R.id.fragment_container, NearMeListFragment.newInstance(latLng.latitude,latLng.longitude),"nearme_list");
            }
            else
            {
                ft.show(listfrag);
            }
            fab.setImageResource(R.drawable.ic_map_white_24dp);
        }
        else
        {
            isMap=true;
            Fragment mapfrag=fragmentManager.findFragmentByTag("nearme_list");
            if(mapfrag!=null)
            {
                ft.hide(mapfrag);
            }
            Fragment mapFrag=fragmentManager.findFragmentByTag("nearme_map");
            if(mapFrag==null)
            {
                ft.add(R.id.fragment_container, NearMeMapFragment.newInstance(),"nearme_map");
            }
            else
            {
                ft.show(mapFrag);
            }
            fab.setImageResource(R.drawable.ic_list_white_24dp);
        }
        ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left);
        ft.commit();
    }

    public void DisplaySnackbarMsg(String msg)
    {
        final Snackbar snackbar;
        snackbar=Snackbar.make(coordinatorlayout,msg,Snackbar.LENGTH_LONG);
        snackbar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(ClickandPay.getInstance().ShouldDisplayPasscode) {
            Intent i = new Intent(NearMeActivity.this, PassCodeActivity.class);
            i.putExtra("fromActivity", "Login");
            startActivity(i);
            finish();
        }
    }

    @Override
    public void OnReceiverLocation(Double Lat, Double Lng) {
        Fragment fragment=fragmentManager.findFragmentByTag("nearme_list");
        FragmentTransaction ft=fragmentManager.beginTransaction();
        if(fragment!=null)
        {
            ft.remove(fragment);
            ft.commit();
        }
        latLng=new LatLng(Lat,Lng);
        fab.setEnabled(true);
    }

    @Override
    public void OnRequestLocation() {
        fab.setEnabled(false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ic_home : Intent intent=new Intent(this,MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                    startActivity(intent); finish(); break;

            case R.id.ic_offers: Intent mintent=new Intent(this,OffersActivity.class);
                                            mintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                            finish();
                                            startActivity(mintent);
                                            break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.support.v7.appcompat.R.id.home) {
            return mDrawerToggle.onOptionsItemSelected(item);
        }
        else if(item.getItemId()==R.id.notifications)
        {
            Intent intent=new Intent(this,NotificationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        return true;
    }

}
