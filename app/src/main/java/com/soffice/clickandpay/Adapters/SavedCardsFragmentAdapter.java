package com.soffice.clickandpay.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.soffice.clickandpay.Fragments.SavedCardsFragment;


/**
 * Created by sys2025 on 13/9/15.
 */
public class SavedCardsFragmentAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 3;
        private String tabTitles[] = new String[] { "Crd1", "Crd2", "Crd3" };

        public SavedCardsFragmentAdapter(FragmentManager fm) {
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
                    return SavedCardsFragment.newInstance(position + 1);
                default:
                    return SavedCardsFragment.newInstance(position + 1);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }

}
