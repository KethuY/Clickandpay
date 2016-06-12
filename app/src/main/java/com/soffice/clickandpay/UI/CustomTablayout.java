package com.soffice.clickandpay.UI;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Surya on 27-04-2016.
 */
public class CustomTablayout extends TabLayout {
    Typeface mTypeface;
    Context mContext;
    public CustomTablayout(Context context) {
        super(context);
        mContext=context;
        mTypeface=Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-Medium-.ttf");
    }

    public CustomTablayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        mTypeface=Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-Medium.ttf");
    }

    public CustomTablayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        mTypeface=Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-Medium.ttf");
    }

    @Override
    public void setupWithViewPager(@Nullable ViewPager viewPager) {
        super.setupWithViewPager(viewPager);
        if (mTypeface != null)
        {
            this.removeAllTabs();
            ViewGroup slidingTabStrip = (ViewGroup) getChildAt(0);
            PagerAdapter adapter = viewPager.getAdapter();
            for (int i = 0, count = adapter.getCount(); i < count; i++)
            {
                Tab tab = this.newTab();
                this.addTab(tab.setText(adapter.getPageTitle(i)));
                AppCompatTextView view = (AppCompatTextView) ((ViewGroup) slidingTabStrip.getChildAt(i)).getChildAt(1);
                view.setTextSize(12.0f);
                view.setGravity(Gravity.CENTER);
                view.setTypeface(mTypeface,Typeface.NORMAL);
            }
        }
    }
}
