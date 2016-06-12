package com.soffice.clickandpay.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.soffice.clickandpay.Adapters.OffersAdapter;
import com.soffice.clickandpay.Adapters.SlidingMenuAdapter;
import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.Fragments.Offers;
import com.soffice.clickandpay.NetWork.JsonRequester;
import com.soffice.clickandpay.NetWork.TaskListner;
import com.soffice.clickandpay.NetWork.Urls;
import com.soffice.clickandpay.Pojo.Offeritem;
import com.soffice.clickandpay.Pojo.OffersDTO;
import com.soffice.clickandpay.Pojo.SlideMenu;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.UI.RobotoRegularTextView;
import com.soffice.clickandpay.Utilty.Display;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.soffice.clickandpay.Utilty.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Surya on 11-04-2016.
 */
public class OffersActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener/* implements TaskListner,AdapterView.OnItemClickListener,View.OnClickListener*/
{
    ViewPager OffersPager;
    TabLayout PagerTabs;
    String className;
    SessionManager sessionManager;
    ProgressBar Pbar;
    String[] Titles={"Most Popular","Super Market","Restuarants"};
    String[] categories={"most_popular","super_market","restuarants"};
    Toolbar toolbar;
    ImageView IC_Home,IC_Location,IC_Offers;
    DrawerLayout drawerLayout;
    ListView NavigationListView;
    SlidingMenuAdapter navadapter;
    ActionBarDrawerToggle mDrawerToggle;
    CoordinatorLayout coordinatorLayout;
    RobotoRegularTextView viewMerchants;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        sessionManager=ClickandPay.getInstance().getSession();
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!=null)
        {
            setSupportActionBar(toolbar);
        }
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        IC_Home= (ImageView) findViewById(R.id.ic_home);
        IC_Home.setOnClickListener(this);
        IC_Offers= (ImageView) findViewById(R.id.ic_offers);
        IC_Location= (ImageView) findViewById(R.id.ic_location);
        IC_Location.setOnClickListener(this);
        IC_Offers.setImageResource(R.mipmap.ic_action_home_screen_offers_hover);
        viewMerchants= (RobotoRegularTextView) findViewById(R.id.offers_view_merchants);
        viewMerchants.setOnClickListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        NavigationListView = (ListView) findViewById(R.id.sliding_menu_lv);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(mDrawerToggle);
        BuildNavigationview();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ic_home: Intent intent=new Intent(this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                finish();
                startActivity(intent);
                break;
            case R.id.ic_location: Intent mintent=new Intent(this, NearMeActivity.class);
                mintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(mintent);
                break;
            case R.id.offers_view_merchants:Intent merchantsintent=new Intent(this, NearMeActivity.class);
                merchantsintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(merchantsintent);
                break;
        }
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
            case 7: launchIntent=new Intent(this,NearMeActivity.class);
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

    /**NAVIGATION DRAWER**/
    private void BuildNavigationview() {

        navadapter= new SlidingMenuAdapter(sessionManager,this);
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
            if(!Utils.nav_set_two_titles[i].equalsIgnoreCase("Offers")) {
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
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
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

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        sessionManager=ClickandPay.getInstance().getSession();
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        if(toolbar!=null)
        {
            setSupportActionBar(toolbar);
        }
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        IC_Home= (ImageView) findViewById(R.id.ic_home);
        IC_Home.setOnClickListener(this);
        IC_Offers= (ImageView) findViewById(R.id.ic_offers);
        IC_Location= (ImageView) findViewById(R.id.ic_location);
        IC_Location.setOnClickListener(this);
        IC_Offers.setImageResource(R.mipmap.ic_action_home_screen_offers_hover);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        NavigationListView = (ListView) findViewById(R.id.sliding_menu_lv);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.setHomeAsUpIndicator(R.mipmap.ic_action_home_screen_menu);
        drawerLayout.addDrawerListener(mDrawerToggle);
        BuildNavigationview();
        coordinatorLayout= (CoordinatorLayout) findViewById(R.id.mycoordinator);
        if(getSupportActionBar()!=null){getSupportActionBar().setTitle("Offers");}
        OffersPager= (ViewPager) findViewById(R.id.offer_main_pager);
        PagerTabs= (TabLayout) findViewById(R.id.offer_main_pager_tabs);
        MyPagerAdapter adapter=new MyPagerAdapter(getSupportFragmentManager());
        OffersPager.setAdapter(adapter);
        PagerTabs.setupWithViewPager(OffersPager);

}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.support.v7.appcompat.R.id.home) {
            return mDrawerToggle.onOptionsItemSelected(item);
        }
        return true;
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
            case 1: launchIntent=new Intent(this, MainActivity.class);
                        launchIntent.putExtra("currentPager",0);
                        launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
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
                        launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(launchIntent);
                        finish();
                        break;

            case 4: launchIntent=new Intent(this, MainActivity.class);
                        launchIntent.putExtra("currentPager",2);
                        launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(launchIntent);
                        finish();
                        break;

            case 5: launchIntent=new Intent(this,TransactionsActivity.class);
                        launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(launchIntent);
                        finish();
                        break;
            case 7: launchIntent=new Intent(this,NearMeActivity.class);
                        launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(launchIntent);
                        finish();
                        break;

            case 8: launchIntent=new Intent(this,PassesGiftsActivity.class);
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

    *//*NAVIGATION DRAWER*//*
    private void BuildNavigationview() {

        navadapter= new SlidingMenuAdapter(sessionManager,this);
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
            if(!Utils.nav_set_two_titles[i].equalsIgnoreCase("Offers")) {
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

    @Override
    public void onTaskfinished(String response, int cd, String _className, String _methodName) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ic_home: Intent intent=new Intent(this,MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                            finish();
                                            startActivity(intent);
                                            break;
            case R.id.ic_location: Intent mintent=new Intent(this, NearMeActivity.class);
                                                mintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                finish();
                                                startActivity(mintent);
                                                break;
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter
{
    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return com.soffice.clickandpay.Fragments.Offers.newInstance(String.valueOf(position));
    }

    @Override
    public int getCount() {
        return categories.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }
}

    @Override
    protected void onRestart() {
        super.onRestart();
        if(ClickandPay.getInstance().ShouldDisplayPasscode) {
            Intent i = new Intent(OffersActivity.this, PassCodeActivity.class);
            i.putExtra("fromActivity", "Login");
            startActivity(i);
            finish();
        }
    }*/
}
