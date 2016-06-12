package com.soffice.clickandpay.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.soffice.clickandpay.Fragments.CreditCardFragment;
import com.soffice.clickandpay.Fragments.NetBankingFragment;
import com.soffice.clickandpay.Fragments.PageFragment;
import com.soffice.clickandpay.Fragments.SavedCardsFragment;
import com.soffice.clickandpay.Pojo.NetBanks;

import java.util.ArrayList;


/**
 * Created by sys2025 on 13/9/15.
 */
public class CardsFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 3;
        private String tabTitles[] = new String[] { "Saved Cards", "Credit/Debit", "Net Banking" };
        ArrayList<NetBanks> CCList;
        ArrayList<NetBanks> DCList;
        ArrayList<NetBanks> BanksList;
        public CardsFragmentPagerAdapter(FragmentManager fm,ArrayList<NetBanks> CCList,ArrayList<NetBanks> DCList,ArrayList<NetBanks> BanksList) {
            super(fm);
            this.CCList=CCList;
            this.DCList=DCList;
            this.BanksList=BanksList;
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
                case 1:
                    return CreditCardFragment.newInstance(CCList);
                case 2:
                    return NetBankingFragment.newInstance(BanksList);
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
