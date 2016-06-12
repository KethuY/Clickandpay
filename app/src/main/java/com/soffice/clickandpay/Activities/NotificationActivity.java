package com.soffice.clickandpay.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.soffice.clickandpay.Adapters.SlidingMenuAdapter;
import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.Fragments.MessagesFragment;
import com.soffice.clickandpay.Fragments.NotificationsFragment;
import com.soffice.clickandpay.Pojo.SlideMenu;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.soffice.clickandpay.Utilty.Utils;

/**
 * Created by Surya on 11-05-2016.
 */
public class NotificationActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener,DrawerLayout.DrawerListener
{
    TabLayout PagerTabs;
    ViewPager viewPager;
    String title[]={"Notifications","Messages"};
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ListView NavigationListView;
    SlidingMenuAdapter navadapter;
    ActionBarDrawerToggle mDrawerToggle;
    SessionManager sessionManager;
    ImageView IC_Home,IC_Location,IC_Offers;
    int mSelectedPos=-1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_list);
        sessionManager= ClickandPay.getInstance().getSession();
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        if(getSupportActionBar()!=null)
        {
            setSupportActionBar(toolbar);
        }
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        drawerLayout.addDrawerListener(this);
        NavigationListView = (ListView) findViewById(R.id.sliding_menu_lv);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(mDrawerToggle);
        BuildNavigationview();
        NavigationListView.setOnItemClickListener(this);
        IC_Home= (ImageView) findViewById(R.id.ic_home);
        IC_Home.setOnClickListener(this);
        IC_Offers= (ImageView) findViewById(R.id.ic_offers);
        IC_Offers.setOnClickListener(this);
        IC_Location= (ImageView) findViewById(R.id.ic_location);
        IC_Location.setOnClickListener(this);
        PagerTabs= (TabLayout) findViewById(R.id.notifications_main_pager_tabs);
        viewPager= (ViewPager) findViewById(R.id.notifications_main_pager);
        MyPagerAdapter adapter=new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        PagerTabs.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.ic_home:Intent intent = new Intent(this,MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            break;
            case R.id.ic_offers: Intent ointent = new Intent(this,OffersActivity.class);
                ointent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(ointent);
                break;

            case R.id.ic_location: Intent mintent =new Intent(this, NearMeActivity.class);
                mintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mintent); break;

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        drawerLayout.closeDrawers();
        mSelectedPos=position;
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {
        Intent launchIntent;
        if(mSelectedPos!=-1) {
            switch (mSelectedPos) {
                case 0:
                    launchIntent = new Intent(this, ProfileSettings.class);
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(launchIntent);
                    break;
                case 1:
                    launchIntent = new Intent(this, MainActivity.class);
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    launchIntent.putExtra("currentPager",0);
                    startActivity(launchIntent);
                    break;
                case 2:
                    launchIntent = new Intent(this, AddMoneyActivity.class);
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(launchIntent);
                    break;
                case 3:
                    launchIntent = new Intent(this, MainActivity.class);
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    launchIntent.putExtra("currentPager",1);
                    startActivity(launchIntent);
                    break;

                case 4:
                    launchIntent = new Intent(this, MainActivity.class);
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    launchIntent.putExtra("currentPager",2);
                    startActivity(launchIntent);
                    break;

                case 5:
                    launchIntent = new Intent(this, TransactionsActivity.class);
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(launchIntent);
                    break;
                case 7:
                    launchIntent = new Intent(this, NearMeActivity.class);
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(launchIntent);
                    break;
                case 8:
                    launchIntent = new Intent(this, OffersActivity.class);
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(launchIntent);
                    break;
                case 9:
                    launchIntent = new Intent(this, PassesGiftsActivity.class);
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(launchIntent);
                    break;


                case 10:
                    launchIntent = new Intent(this, SupportSideActivity.class);
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(launchIntent);
                    break;
                case 11:
                    launchIntent = new Intent(this, AboutUsActivity.class);
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(launchIntent);
                    break;

            }
        }
        mSelectedPos=-1;
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    private class MyPagerAdapter extends FragmentPagerAdapter
    {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return NotificationsFragment.newInstance();
                case 1:
                    return MessagesFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }

    /*NAVIGATION DRAWER*/
    private void BuildNavigationview() {

        navadapter= new SlidingMenuAdapter(sessionManager,this);
        SlideMenu headerItem=new SlideMenu();
        headerItem.setTitle("Header");
        navadapter.addHeaderLayoutSection(headerItem);
        SlideMenu secItem=new SlideMenu();
        headerItem.setTitle("section");
        for(int i = 0; i< Utils.nav_set_one_titles.length; i++)
        {

            SlideMenu item=new SlideMenu();
            item.setTitle(Utils.nav_set_one_titles[i]);
            item.setIcon(Utils.nav_set_one_icons[i]);
            navadapter.addItem(item);

        }
        navadapter.addSectionHeaderItem(secItem);
        for(int i=0;i< Utils.nav_set_two_titles.length;i++)
        {
            SlideMenu item=new SlideMenu();
            item.setTitle(Utils.nav_set_two_titles[i]);
            item.setIcon(Utils.nav_set_two_icons[i]);
            navadapter.addItem(item);
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
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawers();
        }
        else {
            super.onBackPressed();
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.support.v7.appcompat.R.id.home) {
            return mDrawerToggle.onOptionsItemSelected(item);
        }
        return true;
    }
}
