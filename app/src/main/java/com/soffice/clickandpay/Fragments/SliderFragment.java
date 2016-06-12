package com.soffice.clickandpay.Fragments;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.soffice.clickandpay.Activities.OffersDetailsActivity;
import com.soffice.clickandpay.BuildConfig;
import com.soffice.clickandpay.Pojo.Offeritem;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.UI.RobotoLightTextView;
import com.soffice.clickandpay.Utilty.Display;
import com.squareup.picasso.Picasso;

/**
 * Created by Surya on 26-04-2016.
 */
public class SliderFragment extends Fragment
{
    Offeritem Obj;
    RobotoLightTextView OfferDescription,OfferActualPrice,OfferDiscountPrice,OfferDistance,OfferLocation;
    ImageView MerchantLogo,MerchantBigPic;
    RelativeLayout OfferParent;
    public static SliderFragment newInstance(Offeritem item)
    {
        Bundle bundle=new Bundle();
        bundle.putSerializable("obj",item);
        SliderFragment instance=new SliderFragment();
        instance.setArguments(bundle);
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View itemView=inflater.inflate(R.layout.offers_childview,container,false);
        OfferDescription= (RobotoLightTextView) itemView.findViewById(R.id.offersdescpt);
        OfferActualPrice= (RobotoLightTextView) itemView.findViewById(R.id.offer_actualprice);
        OfferDiscountPrice= (RobotoLightTextView) itemView.findViewById(R.id.offer_discountprice);
        OfferDistance= (RobotoLightTextView) itemView.findViewById(R.id.offer_distance);
        OfferLocation= (RobotoLightTextView) itemView.findViewById(R.id.offer_location);
        MerchantLogo= (ImageView) itemView.findViewById(R.id.mrchntLogo);
        MerchantBigPic= (ImageView) itemView.findViewById(R.id.merchant_big_pic);
        OfferParent= (RelativeLayout) itemView.findViewById(R.id.offer_parent);
        Obj= (Offeritem) getArguments().getSerializable("obj");
        if(Obj!=null) {
            if (Obj.getOfferDescription() != null) {
                OfferDescription.setText(Obj.getOfferDescription());
            }
            if(Obj.getPlace()!=null)
            {
                OfferLocation.setText(Obj.getPlace());
            }
            if (Obj.getDiscountedPrice() != null) {
                OfferDiscountPrice.setText(Obj.getDiscountedPrice());
            }
            if (Obj.getActualprice() != null) {
                OfferActualPrice.setText(Obj.getActualprice());
                OfferActualPrice.setPaintFlags(OfferActualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            if (Obj.getMerchantLogo() != null&&!Obj.getMerchantLogo().equalsIgnoreCase("")) {
                Picasso.with(getActivity()).load(Obj.getMerchantLogo()).placeholder(R.mipmap.c_n_p_logo).into(MerchantLogo);
            } else {
                Picasso.with(getActivity()).load(R.mipmap.c_n_p_logo).into(MerchantLogo);
            }
            if (Obj.getMerchantLatitude() != null && Obj.getMerchantLongitude() != null) {

            }
            if (Obj.getOfferImage() != null) {
                Picasso.with(getActivity()).load(Obj.getOfferImage()).fit().placeholder(R.mipmap.c_n_p_logo).into(MerchantBigPic);
            } else {
                Picasso.with(getActivity()).load(R.mipmap.c_n_p_logo).into(MerchantBigPic);
            }
            OfferParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), OffersDetailsActivity.class);
                    if (BuildConfig.DEBUG) {
                        Display.DisplayLogD("ID", Obj.getOfferID());
                    }
                    intent.putExtra("id", Obj.getOfferID());
                    intent.putExtra("title", Obj.getMerchantFirstName());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);
                }
            });
        }
        return itemView;
    }
}
