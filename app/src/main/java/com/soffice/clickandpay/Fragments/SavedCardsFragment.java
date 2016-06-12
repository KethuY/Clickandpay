package com.soffice.clickandpay.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.soffice.clickandpay.Adapters.SavedCardDetailsFragmentAdapter;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.UI.ZoomOutPageTransformer;
import com.viewpagerindicator.CirclePageIndicator;


/**
 * Created by sys2025 on 13/9/15.
 */
public class SavedCardsFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    ViewPager viewPager;
    SavedCardDetailsFragmentAdapter adp;
    CirclePageIndicator titles;

    public static SavedCardsFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        SavedCardsFragment fragment = new SavedCardsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.saved_cards_fragment, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager_savedCardsFrag);
        titles = (CirclePageIndicator) view.findViewById(R.id.titles);
//        viewPager.setClipToPadding(false);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                adp = new SavedCardDetailsFragmentAdapter(getChildFragmentManager());
                viewPager.setAdapter(adp);
                viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
                viewPager.setOffscreenPageLimit(2);
                viewPager.setPageMargin((int) getResources().getDimension(R.dimen.viewpager_margin));
                titles.setViewPager(viewPager);
                titles.setFillColor(R.color.colorPrimary);
            }
        });


        return view;
    }

}
