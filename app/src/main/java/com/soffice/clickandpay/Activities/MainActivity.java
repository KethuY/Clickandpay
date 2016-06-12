package com.soffice.clickandpay.Activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.soffice.clickandpay.Adapters.ClickandPayFragmentPagerAdapter;
import com.soffice.clickandpay.Adapters.SlidingMenuAdapter;
import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.Listeners.PageChangedToClearListener;
import com.soffice.clickandpay.Pojo.SlideMenu;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.Services.MyRegistrationIntentService;
import com.soffice.clickandpay.UI.CustomTablayout;
import com.soffice.clickandpay.Utilty.Display;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.soffice.clickandpay.Utilty.Utils;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,DrawerLayout.DrawerListener,View.OnClickListener
{

    ViewPager viewPager;
    ClickandPayFragmentPagerAdapter clicandPayAdp;
    CustomTablayout tabsStrip;
    Toolbar toolbar;
    ClickandPay clickPay;
    SessionManager session;
    static boolean doubleClick = false;
    RelativeLayout rel_main_menu, payAtStore, transactionHistory_row, addMoney_row, sendMoney_row, requestdMoney_row, aboutUs_row,
            saved_cards_row, nearMe_row,offers_row;
    public static RelativeLayout alertBox;
    ImageView IC_Home,IC_Location,IC_Offers;
    public static ImageView img_alert;
    TextView profileSettings_edit, userName_sidemenu;
    public static TextView alertTextView, alertResponseTextView;
    PageChangedToClearListener clearSend, clearRequest, clearHome;
    public static boolean showPassCode = true;
    ScrollView sideMenu_scroll_layout;
    public static String fromMapTo;
    DrawerLayout drawerLayout;
    ListView NavigationListView;
    SlidingMenuAdapter navadapter;
    ActionBarDrawerToggle mDrawerToggle;
    public CoordinatorLayout coordinatorLayout;
    int mSelectedPos=-1;
    boolean Clicked=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        coordinatorLayout= (CoordinatorLayout) findViewById(R.id.mycoordinator);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_action_home_screen_menu);
        }
        clickPay = (ClickandPay) getApplicationContext();
        session = clickPay.getSession();
        session.setIsLoggedIn(true);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        drawerLayout.addDrawerListener(this);
        NavigationListView = (ListView) findViewById(R.id.sliding_menu_lv);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.setHomeAsUpIndicator(R.mipmap.ic_action_home_screen_menu);
       drawerLayout.addDrawerListener(mDrawerToggle);
        BuildNavigationview();
        IC_Home= (ImageView) findViewById(R.id.ic_home);
        IC_Offers= (ImageView) findViewById(R.id.ic_offers);
        IC_Offers.setOnClickListener(this);
        IC_Location= (ImageView) findViewById(R.id.ic_location);
        IC_Location.setOnClickListener(this);
        IC_Home.setImageResource(R.mipmap.ic_action_home_screen_wallet_hover);
        alertBox = (RelativeLayout) findViewById(R.id.alertBox);
        img_alert = (ImageView) findViewById(R.id.img_alert);
        alertTextView = (TextView) findViewById(R.id.alertTextView);
        alertResponseTextView = (TextView) findViewById(R.id.alertResponseTextView);
        transactionHistory_row = (RelativeLayout) findViewById(R.id.transactionHistory_row);
        doubleClick = false;
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        clicandPayAdp = new ClickandPayFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(clicandPayAdp);
        // Give the PagerSlidingTabStrip the ViewPager
        tabsStrip = (CustomTablayout) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setupWithViewPager(viewPager);
        if(getIntent().hasExtra("currentPager"))
        {
            viewPager.setCurrentItem(getIntent().getIntExtra("currentPager",0),true);
        }

        tabsStrip.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(),true);
                Display.DisplayLogI("ADITYA", "PAGE CHANGED " + tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(fromMapTo != null){
            if(fromMapTo.equalsIgnoreCase("home")){
                viewPager.setCurrentItem(0, true);
            }else  if(fromMapTo.equalsIgnoreCase("sendMoney")){
                viewPager.setCurrentItem(1, true);
            }else  if(fromMapTo.equalsIgnoreCase("requestMoney")){
                viewPager.setCurrentItem(2, true);
            }
        }
        if(session.getIsLoggedIn()&&!session.getIsGcmRegistered())
        {
            RegisterGCM();
        }
        if(ClickandPay.getInstance().IsAddMoneyStatus)
        {
            ClickandPay.getInstance().IsAddMoneyStatus=false;
            if(ClickandPay.getInstance().PaymentStatus)
            {
                Utils.GenerateSnackbar(MainActivity.this,coordinatorLayout,"Rs."+ClickandPay.getInstance().AddedAmount+" has been successfully added into the wallet.");
                ClickandPay.getInstance().PaymentStatus=false;
                ClickandPay.getInstance().AddedAmount="";
            }
            else
            {
                Utils.GenerateSnackbar(MainActivity.this,coordinatorLayout,"Failed to add money into your wallet");
            }
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Display.DisplayLogI("ADITYA", "MAIN ACTIVITY RESTART");
        if(ClickandPay.getInstance().ShouldDisplayPasscode) {
                Intent i = new Intent(MainActivity.this, PassCodeActivity.class);
                i.putExtra("fromActivity", "Main");
                startActivity(i);
                finish();
            }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawers();
        }
        else
        {
            Display.DisplayLogI("ADITYA", "doubleClick "+doubleClick);
            if(doubleClick){
                finish();
             }else{
                doubleClick = true;
            }
            Display.DisplayToast(MainActivity.this, "Press again to exit");
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void removePhoneKeypad() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
                    .getWindowToken(), 0);
        }
    }

    private void RegisterGCM()
    {
        //InstanceID instanceID=InstanceID.getInstance(this);
        Intent Myservice=new Intent(this,MyRegistrationIntentService.class);
        startService(Myservice);

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
        else if(item.getItemId()==R.id.notifications)
        {
            Intent intent=new Intent(this,NotificationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Display.DisplayLogD("pos",String.valueOf(position));
        drawerLayout.closeDrawers();
        mSelectedPos=position;

    }


    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        if(ClickandPay.getInstance().ShouldUpdateUsername)
        {
            View view=NavigationListView.getChildAt(0);
            if(view==null)
                return;
            TextView username= (TextView) view.findViewById(R.id.navigation_usernametv);
            if(username==null)
                return;
            username.setText(session.getUserName());
            ClickandPay.getInstance().ShouldUpdateUsername=false;
        }

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
                    viewPager.setCurrentItem(0, true);
                    break;
                case 2:
                    launchIntent = new Intent(this, AddMoneyActivity.class);
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(launchIntent);
                    break;
                case 3:
                    viewPager.setCurrentItem(1, true);
                    break;

                case 4:
                    viewPager.setCurrentItem(2, true);
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

    public void DisplaySnackMessage(String msg)
    {
        Display.DisplaySnackbar(this,coordinatorLayout,msg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ic_offers: Intent intent = new Intent(this,OffersActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            break;

            case R.id.ic_location: Intent mintent =new Intent(this, NearMeActivity.class);
                                                mintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(mintent); break;

        }
    }
}
