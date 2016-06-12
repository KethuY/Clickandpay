package com.soffice.clickandpay.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.soffice.clickandpay.Fragments.PageFragment;
import com.soffice.clickandpay.Fragments.SavedCardsTabFragment;


/**
 * Created by sys2025 on 13/9/15.
 */
public class SavedCardsFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 3;
        private String tabTitles[] = new String[] { "Passes", "Membership", "Saved Cards" };

        public SavedCardsFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 2:
                    return SavedCardsTabFragment.newInstance(position + 1);
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
