package com.soffice.clickandpay.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.soffice.clickandpay.Listeners.TransactionItemClickListener;
import com.soffice.clickandpay.Pojo.SavedCardsItem;
import com.soffice.clickandpay.Pojo.TransactionPojo;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.Utilty.Display;

import java.util.List;

/**
 * Created by Malladi-Bhai on 3/11/2016.
 */
public class SavedCardsListAdapter extends RecyclerView.Adapter<FeedListRowHolder1> {

    private List<SavedCardsItem> feedItemList;


    private Context mContext;

    public SavedCardsListAdapter(Context context, List<SavedCardsItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position) {

        // add here your booleans or switch() to set viewType at your needed
        // I.E if (position == 0) viewType = 1; etc. etc.
        return position%2;
    }

    @Override
    public FeedListRowHolder1 onCreateViewHolder(ViewGroup viewGroup, int i) {
        FeedListRowHolder1 mh;
        if(i==0) {
            View v;
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item, null);
            mh = new FeedListRowHolder1(v, i, mContext);
        }
        else {
            View v;
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item_blue, null);
            mh = new FeedListRowHolder1(v, i, mContext);
        }

        return mh;
    }

    @Override
    public void onBindViewHolder(FeedListRowHolder1 feedListRowHolder, int i) {
        SavedCardsItem feedItem = feedItemList.get(i);
        feedListRowHolder.bank_name.setText(feedItem.getBank_name() + " -");
        feedListRowHolder.card_type.setText(feedItem.getCard_type());
        feedListRowHolder.num_part1.setText(feedItem.getNum_part1());
        feedListRowHolder.num_part4.setText(feedItem.getNum_part4());
        feedListRowHolder.customer_name.setText(feedItem.getCustomer_name());


    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }


}

class FeedListRowHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener {

    protected TextView bank_name, card_type, num_part1, num_part4, customer_name;
    protected int posi;


    public FeedListRowHolder1(View view, int posi, Context cxt) {
        super(view);
        this.bank_name = (TextView) view.findViewById(R.id.bank_name);
        this.card_type = (TextView) view.findViewById(R.id.card_type);
        this.num_part1 = (TextView) view.findViewById(R.id.num_part1);
        this.num_part4 = (TextView) view.findViewById(R.id.num_part4);
        this.customer_name = (TextView) view.findViewById(R.id.customer_name);
        this.posi = posi;

    }

    @Override
    public void onClick(View v) {
        Display.DisplayLogI("ADITYA", "On Posi Adapter" + posi);

    }
}


