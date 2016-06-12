package com.soffice.clickandpay.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.soffice.clickandpay.Adapters.SavedCardsFragmentPagerAdapter;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.UI.PagerSlidingTabStrip;

import com.soffice.clickandpay.R;

public class SavedCardsActivity extends AppCompatActivity {

    ViewPager viewPager;
    PagerSlidingTabStrip tabsStrip;
    SavedCardsFragmentPagerAdapter cardsAdp;
    ImageView back_IV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_cards);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        back_IV = (ImageView) findViewById(R.id.back_IV);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.viewpager_cards);
        cardsAdp = new SavedCardsFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(cardsAdp);

        // Give the PagerSlidingTabStrip the ViewPager
        tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);

        back_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.showPassCode = false;
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.showPassCode = false;
        finish();
    }
}
