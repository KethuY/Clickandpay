package com.soffice.clickandpay.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.soffice.clickandpay.BuildConfig;
import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.NetWork.JsonRequester;
import com.soffice.clickandpay.NetWork.TaskListner;
import com.soffice.clickandpay.NetWork.Urls;
import com.soffice.clickandpay.Pojo.OfferDetailsDTO;
import com.soffice.clickandpay.Pojo.Offeritem;
import com.soffice.clickandpay.Pojo.OffersDTO;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.UI.RobotoLightTextView;
import com.soffice.clickandpay.UI.RobotoMediumTextView;
import com.soffice.clickandpay.Utilty.Display;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.soffice.clickandpay.Utilty.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Surya on 22-04-2016.
 */
public class OffersDetailsActivity extends AppCompatActivity implements TaskListner,OnMapReadyCallback
{
    ImageView MerchantLogo,CollapsingImageView;
    MapView mapView;
    RobotoLightTextView OfferDescription,OffersOthers,OfferMerchantAddress,OfferMerchantDetails;
    LinearLayout OfferConditions,OfferActions,OfferDetailsParent;
    ProgressBar Pbar;
    JsonRequester DataRequester;
    String className;
    SessionManager sessionManager;
    Offeritem DetailsDTO;
    Toolbar mToolbar;
    private final String REQUEST_TAG="offer_details";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);
        DetailsDTO= (Offeritem) getIntent().getSerializableExtra("obj");
        className=getLocalClassName();
        sessionManager= ClickandPay.getInstance().getSession();
        BuidlUI(savedInstanceState);
        ServerCall();
    }

    public void BuidlUI(Bundle savedInstanceState)
    {
        OfferDetailsParent= (LinearLayout) findViewById(R.id.offer_details_parent);
        mToolbar= (Toolbar) findViewById(R.id.mtoolbar);
        mToolbar.setTitle(getIntent().getStringExtra("title"));
        CollapsingImageView= (ImageView) findViewById(R.id.collapsing_iv);
        MerchantLogo = (ImageView) findViewById(R.id.mrchntLogo);
        if(DetailsDTO.getOfferImage()!=null) {
            if(!DetailsDTO.getOfferImage().equalsIgnoreCase("")) {
                Picasso.with(this).load(DetailsDTO.getOfferImage()).placeholder(R.mipmap.c_n_p_logo).fit().into(CollapsingImageView);
            }
            else
            {
                CollapsingImageView.setImageResource(R.mipmap.c_n_p_logo);
            }
        }
        if(DetailsDTO.getMerchantLogo()!=null) {
            if(!DetailsDTO.getMerchantLogo().equalsIgnoreCase("")) {
                Picasso.with(this).load(DetailsDTO.getMerchantLogo()).placeholder(R.mipmap.c_n_p_logo).fit().into(MerchantLogo);
            }
            else
            {
                MerchantLogo.setImageResource(R.mipmap.c_n_p_logo);
            }
        }
        mapView= (MapView) findViewById(R.id.offer_merchantmap);
        mapView.onCreate(savedInstanceState);
        OfferDescription= (RobotoLightTextView) findViewById(R.id.offer_description);
        OffersOthers= (RobotoLightTextView) findViewById(R.id.offer_others);
        OfferMerchantAddress= (RobotoLightTextView) findViewById(R.id.offer_merchantaddress);
        OfferMerchantDetails= (RobotoLightTextView) findViewById(R.id.offer_merchant_details);
        OfferConditions= (LinearLayout) findViewById(R.id.offer_conditions_parent);
        OfferActions= (LinearLayout) findViewById(R.id.offer_actions_layout);
        Pbar= (ProgressBar) findViewById(R.id.pbar);
        if(Build.VERSION.SDK_INT<21)
        {
            Pbar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        }
    }


    private void ServerCall() {
        if(Utils.CheckInternet(this))
        {
            Pbar.setVisibility(View.VISIBLE);
            DataRequester=new JsonRequester(this);
            Map<String,String> postparams=new HashMap<>();
            postparams.put("authkey",sessionManager.getAuthKey());
            postparams.put("offerid",DetailsDTO.getOfferID());
            DataRequester.StringRequesterFormValues(Urls.getoffer_details_url, Request.Method.POST,className,Urls.getoffer_details_method,postparams,REQUEST_TAG+postparams.get("offerid"));
        }
    }

    @Override
    public void onTaskfinished(String response, int cd, String _className, String _methodName) {
        if(cd==0)
        {
            Display.DisplayToast(this,"Oops.. Something went wrong. Please try again.");
        }
        else if(cd==5) {
            if (_methodName.equalsIgnoreCase(Urls.getoffer_details_method)) {
                Log.d("Response", response);
                Gson g = new Gson();
                OfferDetailsDTO OfferDTO = g.fromJson(response, OfferDetailsDTO.class);
                if (OfferDTO.getStatus().equalsIgnoreCase("200")) {
                    DetailsDTO = OfferDTO.getOffers();
                    OfferDescription.setText(Html.fromHtml("<strong>" + DetailsDTO.getOfferDescription() + "</strong>"));
                    OfferDescription.append("\n");
                    OfferDescription.append("+" + DetailsDTO.getDescriptiontag());


                    int mChildPos = 0;
                    if (DetailsDTO.getOfferUsage() != null) {
                        View view = LayoutInflater.from(this).inflate(R.layout.listchildview_one, null);
                        ImageView iv = (ImageView) view.findViewById(R.id.listchildview_one_iv);
                        TextView tv = (TextView) view.findViewById(R.id.listchildview_one_tv);
                        View divider = new View(this);
                        divider.setLayoutParams(new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT));
                        divider.setBackgroundColor(ContextCompat.getColor(this, R.color.card_bg));
                        iv.setImageResource(R.drawable.ic_infinity);
                        tv.setText(DetailsDTO.getOfferUsage());
                        OfferConditions.addView(view, mChildPos);
                        mChildPos++;
                        OfferConditions.addView(divider, mChildPos);
                        mChildPos++;

                    }
                    if (DetailsDTO.getOfferValidty() != null) {
                        View view = LayoutInflater.from(this).inflate(R.layout.listchildview_one, null);
                        ImageView iv = (ImageView) view.findViewById(R.id.listchildview_one_iv);
                        TextView tv = (TextView) view.findViewById(R.id.listchildview_one_tv);
                        View divider = new View(this);
                        divider.setLayoutParams(new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT));
                        divider.setBackgroundColor(ContextCompat.getColor(this, R.color.card_bg));
                        iv.setImageResource(R.drawable.ic_clock);
                        tv.setText(DetailsDTO.getOfferValidty());
                        OfferConditions.addView(view, mChildPos);
                        mChildPos++;
                        OfferConditions.addView(divider, mChildPos);
                        mChildPos++;
                    }
                    if (DetailsDTO.getOfferapplicable() != null) {
                        View view = LayoutInflater.from(this).inflate(R.layout.listchildview_one, null);
                        ImageView iv = (ImageView) view.findViewById(R.id.listchildview_one_iv);
                        TextView tv = (TextView) view.findViewById(R.id.listchildview_one_tv);
                        View divider = new View(this);
                        divider.setLayoutParams(new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT));
                        divider.setBackgroundColor(ContextCompat.getColor(this, R.color.card_bg));
                        iv.setImageResource(R.drawable.ic_place_pin);
                        tv.setText(DetailsDTO.getOfferapplicable());
                        OfferConditions.addView(view, mChildPos);
                    }
                    /*if (DetailsDTO.getMoreOffers() != null) {
                        OffersOthers.setText(DetailsDTO.getMoreOffers() + " offers available for this branch.");
                    }
                    if (DetailsDTO.getMerchantAddress() != null) {
                        OfferMerchantAddress.setText(Html.fromHtml("<strong>Where to find us</strong>"));
                        OfferMerchantAddress.append("\n");
                        OfferMerchantAddress.append(DetailsDTO.getMerchantAddress());
                    }*/
                    if (DetailsDTO.getMerchantLatitude() != null && DetailsDTO.getMerchantLongitude() != null) {

                        mapView.getMapAsync(this);
                    }
                    mChildPos = 0;
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.weight = 1;
                    /*if (DetailsDTO.getMerchantMobileNumber() != null) {
                        View mview = LayoutInflater.from(this).inflate(R.layout.listchildview_two, null);
                        ImageView miv = (ImageView) mview.findViewById(R.id.listchildview_two_iv);
                        TextView mtv = (TextView) mview.findViewById(R.id.listchildview_two_tv);
                        mtv.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                        LinearLayout mdivider = new LinearLayout(this);
                        mdivider.setLayoutParams(new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT));
                        mdivider.setBackgroundColor(ContextCompat.getColor(this, R.color.card_bg));
                        miv.setImageResource(R.drawable.ic_call);
                        mtv.setText(DetailsDTO.getMerchantMobileNumber());
                        OfferActions.addView(mview);
                        mChildPos++;
                        mview.setLayoutParams(params);
                        System.out.print("Pos........." + mChildPos);
                        OfferActions.addView(mdivider);
                        mview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + DetailsDTO.getMerchantMobileNumber()));
                                startActivity(intent);
                            }
                        });
                    }*/
                    if (DetailsDTO.getMerchantLongitude() != null && DetailsDTO.getMerchantLongitude() != null) {
                        View mview = LayoutInflater.from(this).inflate(R.layout.listchildview_two, null);

                        mview.setLayoutParams(params);
                        ImageView miv = (ImageView) mview.findViewById(R.id.listchildview_two_iv);
                        TextView mtv = (TextView) mview.findViewById(R.id.listchildview_two_tv);
                        mtv.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                        LinearLayout mdivider = new LinearLayout(this);
                        mdivider.setLayoutParams(new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT));
                        mdivider.setBackgroundColor(ContextCompat.getColor(this, R.color.card_bg));
                        miv.setImageResource(R.drawable.ic_directions);
                        mtv.setText("Directions");
                        OfferActions.addView(mview);
                        OfferActions.addView(mdivider);
                        mview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + DetailsDTO.getMerchantLatitude() + "," + DetailsDTO.getMerchantLongitude()
                                        + "?z=5"));
                                intent.setPackage("com.google.android.apps.maps");
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                    /*if (DetailsDTO.getMerchantMobileNumber() != null) {
                        View mview = LayoutInflater.from(this).inflate(R.layout.listchildview_two, null);
                        mview.setLayoutParams(params);
                        ImageView miv = (ImageView) mview.findViewById(R.id.listchildview_two_iv);
                        TextView mtv = (TextView) mview.findViewById(R.id.listchildview_two_tv);
                        mtv.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                        LinearLayout mdivider = new LinearLayout(this);
                        mdivider.setLayoutParams(new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT));
                        mdivider.setBackgroundColor(ContextCompat.getColor(this, R.color.card_bg));
                        miv.setImageResource(R.drawable.ic_contact);
                        mtv.setText("Add to contact");
                        OfferActions.addView(mview);
                        mview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ArrayList<ContentValues> Data = new ArrayList<ContentValues>();
                                ContentValues row1 = new ContentValues();
                                row1.put(ContactsContract.Data.DISPLAY_NAME, DetailsDTO.getMerchantFirstName());
                                ContentValues row2 = new ContentValues();
                                row2.put(ContactsContract.Data.HAS_PHONE_NUMBER, DetailsDTO.getMerchantMobileNumber());
                                Data.add(row1);
                                Data.add(row2);
                                Intent intent = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
                                intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, Data);
                                startActivity(intent);
                            }
                        });
                    }*/
                    if (DetailsDTO.getMerchantFirstName() != null) {
                        OfferMerchantDetails.setText(Html.fromHtml("<strong>About " + DetailsDTO.getMerchantFirstName() + "</strong>"));
                        OfferMerchantDetails.append("\n");
                        OfferMerchantDetails.append("\n");
                        //OfferMerchantDetails.append(DetailsDTO.getMerchantAddress());
                    }

                    if (DetailsDTO.getMerchantLogo() != null&&!DetailsDTO.getMerchantLogo().equalsIgnoreCase(""))
                    {
                        Picasso.with(this).load(DetailsDTO.getMerchantLogo()).placeholder(R.mipmap.ic_logo).fit().into(MerchantLogo);
                    }

                    Pbar.setVisibility(View.GONE);
                    OfferDetailsParent.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
            googleMap.setMyLocationEnabled(true);
            LatLng position=new LatLng(Double.parseDouble(DetailsDTO.getMerchantLatitude()),Double.parseDouble(DetailsDTO.getMerchantLongitude()));
            MarkerOptions markerOptions=new MarkerOptions()
                                                                    .title(DetailsDTO.getMerchantFirstName())
                                                                    .position(position)
                                                                   .visible(true)
                                                                   .draggable(false)
                                                                   .flat(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13));

        googleMap.addMarker(markerOptions);
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mapView!=null)
        {
            mapView.onDestroy();
        }
        DetailsDTO=null;
        ClickandPay.getInstance().cancelPendingRequests(REQUEST_TAG);
    }
}
