package com.soffice.clickandpay.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.soffice.clickandpay.Activities.TransactionsActivity;
import com.soffice.clickandpay.Listeners.TransactionItemClickListener;
import com.soffice.clickandpay.Pojo.TransactionItem;
import com.soffice.clickandpay.Pojo.TransactionPojo;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.Utilty.Display;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sys2033 on 24/2/16.
 */
public class TranscationListAdapter extends RecyclerView.Adapter<TranscationListAdapter.FeedListRowHolder> {

    private List<TransactionPojo> feedItemList;
    protected TransactionItemClickListener listener;
    private TransactionsActivity mContext;
    int posi;
    String ss = "<font color=#101010>";
    String ss2 = " <font color=#bf0e14>";
    String ss4 = "</font>";

    public TranscationListAdapter(TransactionsActivity context, List<TransactionPojo> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
        listener = (TransactionItemClickListener) mContext;
    }

    @Override
    public FeedListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transaction_item, null);

        FeedListRowHolder mh = new FeedListRowHolder(v, i);

        return mh;
    }

    @Override
    public void onBindViewHolder(FeedListRowHolder feedListRowHolder, int i) {
        TransactionPojo feedItem = feedItemList.get(i);
        posi = i;

        if(feedItemList.get(i).transactionStatus.equalsIgnoreCase("success")) {
            feedListRowHolder.transac_status_logo.setImageResource(R.drawable.ic_success);
            feedListRowHolder.colorStyle_TV.setBackgroundColor(Color.parseColor("#FF8cc341"));
            feedListRowHolder.amount_rupees.setTextColor(Color.parseColor("#FF8cc341"));
        }
        else if(feedItemList.get(i).transactionStatus.equalsIgnoreCase("failure")||feedItemList.get(i).transactionStatus.equalsIgnoreCase("aborted"))
        {
            feedListRowHolder.transac_status_logo.setImageResource(R.drawable.ic_failed);
            feedListRowHolder.colorStyle_TV.setBackgroundColor(Color.parseColor("#FFec575c"));
            feedListRowHolder.amount_rupees.setTextColor(Color.parseColor("#FFec575c"));

        }
        else
        {
            feedListRowHolder.transac_status_logo.setImageResource(R.drawable.ic_in_process);
            feedListRowHolder.colorStyle_TV.setBackgroundColor(Color.parseColor("#FFf0b331"));
            feedListRowHolder.amount_rupees.setTextColor(Color.parseColor("#FFf0b331"));

        }

        if(feedItem.TransactionDescription.equalsIgnoreCase("1")) {
            feedListRowHolder.storeName.setText(feedItem.merchantstore);
            feedListRowHolder.codeEntered.setText("Code : "+ Html.fromHtml(ss2+feedItem.receiver+ss4));
            Picasso.with(mContext).load(feedItem.merchantLogo)
                    .into(feedListRowHolder.thumbnail);
        }
        else if(feedItem.TransactionDescription.equalsIgnoreCase("2"))
        {
            feedListRowHolder.storeName.setText("Added Money");
            feedListRowHolder.codeEntered.setText("From : "+ Html.fromHtml(ss2+"You"+ss4));
            feedListRowHolder.thumbnail.setImageResource(R.drawable.ic_add_money);
        }
        else if(feedItem.TransactionDescription.equalsIgnoreCase("3"))
        {
            feedListRowHolder.storeName.setText("Sent  Money");
            feedListRowHolder.codeEntered.setText("To : "+ Html.fromHtml(ss2+feedItem.receiver+ss4));
            feedListRowHolder.thumbnail.setImageResource(R.drawable.ic_send_money);
        }
        else
        {
            feedListRowHolder.storeName.setText("Received Money");
            feedListRowHolder.codeEntered.setText("From : "+ Html.fromHtml(ss2+feedItem.Sender+ss4));
            feedListRowHolder.thumbnail.setImageResource(R.drawable.ic_receive_money);

        }
        feedListRowHolder.date_tv.setText(feedItem.transactionDate);
        feedListRowHolder.time_tv.setText(feedItem.transactionTime);
        feedListRowHolder.address2.setText(feedItem.merchantAddress);
        feedListRowHolder.transactionID.setText(feedItem.AppTransactionID);
        feedListRowHolder.amount_rupees.setText(feedItem.amount);
        feedListRowHolder.transac_status_text.setText(feedItem.transactionStatus);
        feedListRowHolder.posit= i;
        if(i==feedItemList.size()-1)
        {
            mContext.ServerCall();
        }
    }

    @Override
    public int getItemCount() {
        return  feedItemList.size();
    }

    class FeedListRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected ImageView thumbnail, transac_status_logo;
        protected TextView date_tv, time_tv, storeName, address2, transactionID, amount_rupees, transac_status_text, codeEntered;
        View colorStyle_TV;
        int posit;

        public FeedListRowHolder(View view, int pos) {
            super(view);
            this.thumbnail = (ImageView) view.findViewById(R.id.vendorlogo);
            this.transac_status_logo = (ImageView) view.findViewById(R.id.transac_status_logo);
            this.date_tv = (TextView) view.findViewById(R.id.date_tv);
            this.time_tv = (TextView) view.findViewById(R.id.time_tv);
            this.storeName = (TextView) view.findViewById(R.id.storeName);
            this.address2 = (TextView) view.findViewById(R.id.address2);
            this.transactionID = (TextView) view.findViewById(R.id.transactionID);
            this.amount_rupees = (TextView) view.findViewById(R.id.amount_rupees);
            this.transac_status_text = (TextView) view.findViewById(R.id.transac_status_text);
            this.colorStyle_TV = view.findViewById(R.id.colorStyle_TV);
            this.codeEntered = (TextView) view.findViewById(R.id.codeEntered);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Display.DisplayLogI("ADITYA", "On Posi Adapter" + posit);
            listener.onTransactionClick(posit);
        }
    }
}