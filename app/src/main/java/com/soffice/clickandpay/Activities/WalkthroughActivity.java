package com.soffice.clickandpay.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.Fragments.WalkThroughFragment;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.soffice.clickandpay.Utilty.Utils;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * Created by Surya on 04-05-2016.
 */
public class WalkthroughActivity extends AppCompatActivity
{
    AppCompatButton ContBtn;
    ViewPager viewPager;
    CirclePageIndicator indicator;
    SessionManager session;
    int[] Images={R.drawable.walkthrough_screen_1_android,R.drawable.walkthrough_screen_2_android,R.drawable.walkthrough_screen_3_android,R.drawable.walkthrough_screen_4_android};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session= ClickandPay.getInstance().getSession();
        setContentView(R.layout.activity_walkthrough);
        viewPager= (ViewPager) findViewById(R.id.viewpager);
        ContBtn= (AppCompatButton) findViewById(R.id.contBtn);
        indicator= (CirclePageIndicator) findViewById(R.id.pager_indicator);
        Utils.setButtonTint(ContBtn, ContextCompat.getColorStateList(this,R.color.button_tint));
        MyPagerAdapter adapter=new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
        ContBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setDisplayedWalkthrough(true);
                Intent i = new Intent(WalkthroughActivity.this, InitialActivity.class);
                i.putExtra("fromActivity", "Splash");
                startActivity(i);
                WalkthroughActivity.this.finish();
            }
        });

    }

    private class MyPagerAdapter extends FragmentPagerAdapter
    {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return WalkThroughFragment.newInstance(Images[position]);
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
