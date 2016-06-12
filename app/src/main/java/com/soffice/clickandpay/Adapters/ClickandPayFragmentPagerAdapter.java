package com.soffice.clickandpay.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.soffice.clickandpay.Fragments.HomeFragment;
import com.soffice.clickandpay.Fragments.PageFragment;
import com.soffice.clickandpay.Fragments.RequestMoneyFragment;
import com.soffice.clickandpay.Fragments.SendMoneyFragment;


/**
 * Created by sys2025 on 13/9/15.
 */
public class ClickandPayFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 3;
        private String tabTitles[] = new String[] { "PAY AT STORE", "SEND MONEY", "REQUEST MONEY" };

        public ClickandPayFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return HomeFragment.newInstance(position + 1);
                case 1:
                    return SendMoneyFragment.newInstance(position + 1);
                case 2:
                    return RequestMoneyFragment.newInstance(position + 1);
                default:
                    return PageFragment.newInstance(position + 1);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }

}
