package com.soffice.clickandpay.Fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soffice.clickandpay.Adapters.SavedCardsListAdapter;
import com.soffice.clickandpay.Pojo.SavedCardsItem;
import com.soffice.clickandpay.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sys2025 on 13/9/15.
 */
public class SavedCardsTabFragment extends Fragment {
    private List<SavedCardsItem> feedItemList = new ArrayList<SavedCardsItem>();
    private RecyclerView mRecyclerView;
    private SavedCardsListAdapter adapter;

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;

    public static SavedCardsTabFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        SavedCardsTabFragment fragment = new SavedCardsTabFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved_cards_tab_fragment, container, false);

        for (int i = 0; i < 10; i++) {
            SavedCardsItem bean = new SavedCardsItem();
            bean.setBank_name("ICICI Bank " + i);
            bean.setCard_type("Credit Card");
            bean.setNum_part1("4242");
            bean.setNum_part4("4242");
            bean.setCustomer_name("Kartheek");
            feedItemList.add(bean);
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.saved_cards_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(40));
        adapter = new SavedCardsListAdapter(getContext(), feedItemList);
        mRecyclerView.setAdapter(adapter);

        return view;
    }

    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int mVerticalSpaceHeight;

        public VerticalSpaceItemDecoration(int mVerticalSpaceHeight) {
            this.mVerticalSpaceHeight = mVerticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = mVerticalSpaceHeight;
        }
    }
}
