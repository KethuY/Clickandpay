package com.soffice.clickandpay.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.soffice.clickandpay.Activities.OffersDetailsActivity;
import com.soffice.clickandpay.BuildConfig;
import com.soffice.clickandpay.Pojo.Offeritem;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.UI.RobotoLightTextView;
import com.soffice.clickandpay.UI.RobotoRegularTextView;
import com.soffice.clickandpay.Utilty.Display;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Surya on 11-04-2016.
 */
public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.OffersViewHolder>
{

    List<Offeritem> offers;
    Context context;
    public OffersAdapter(Context context,List<Offeritem> offers) {
        this.offers = offers;
        this.context=context;
    }
    @Override
    public OffersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.offers_childview,parent,false);
        return new OffersViewHolder(view);
    }
    @Override
    public void onBindViewHolder(OffersViewHolder holder, final int position) {
        if (offers.get(position).getPlace()!=null)
        {
            holder.OfferLocation.setText(offers.get(position).getPlace());
        }

        if(offers.get(position).getOfferDescription()!=null)
        {
            holder.OfferDescription.setText(offers.get(position).getOfferDescription());
        }
        if(offers.get(position).getDiscountedPrice()!=null)
        {
            holder.OfferDiscountPrice.setText(offers.get(position).getDiscountedPrice());
        }
        if(offers.get(position).getActualprice()!=null)
        {
            holder.OfferActualPrice.setText(offers.get(position).getActualprice());
            holder.OfferActualPrice.setPaintFlags(holder.OfferActualPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }
        if(offers.get(position).getMerchantLogo()!=null&&!offers.get(position).getMerchantLogo().equalsIgnoreCase(""))
        {
            Picasso.with(context).load(offers.get(position).getMerchantLogo()).placeholder(R.mipmap.c_n_p_logo).into(holder.MerchantLogo);
        }
        else
        {
            Picasso.with(context).load(R.mipmap.c_n_p_logo).into(holder.MerchantLogo);
        }
        if(offers.get(position).getMerchantLatitude()!=null&&offers.get(position).getMerchantLongitude()!=null)
        {

        }
        if(offers.get(position).getOfferImage()!=null)
        {
            Picasso.with(context).load(offers.get(position).getOfferImage()).fit().placeholder(R.mipmap.c_n_p_logo).into(holder.MerchantBigPic);
        }
        else
        {
            Picasso.with(context).load(R.mipmap.c_n_p_logo).into(holder.MerchantBigPic);
        }
        holder.OfferParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, OffersDetailsActivity.class);
                if(BuildConfig.DEBUG){
                    Display.DisplayLogD("ID",offers.get(position).getOfferID());}
                intent.putExtra("obj",offers.get(position));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    public class OffersViewHolder extends RecyclerView.ViewHolder
    {
        RobotoLightTextView OfferDescription,OfferActualPrice,OfferDiscountPrice,OfferDistance,OfferLocation;
        ImageView MerchantLogo,MerchantBigPic;
        RelativeLayout OfferParent;
        public OffersViewHolder(View itemView) {
            super(itemView);
            OfferDescription= (RobotoLightTextView) itemView.findViewById(R.id.offersdescpt);
            OfferActualPrice= (RobotoLightTextView) itemView.findViewById(R.id.offer_actualprice);
            OfferDiscountPrice= (RobotoLightTextView) itemView.findViewById(R.id.offer_discountprice);
            OfferDistance= (RobotoLightTextView) itemView.findViewById(R.id.offer_distance);
            OfferLocation= (RobotoLightTextView) itemView.findViewById(R.id.offer_location);
            MerchantLogo= (ImageView) itemView.findViewById(R.id.mrchntLogo);
            MerchantBigPic= (ImageView) itemView.findViewById(R.id.merchant_big_pic);
            OfferParent= (RelativeLayout) itemView.findViewById(R.id.offer_parent);
        }
    }
}
