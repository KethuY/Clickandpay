package com.soffice.clickandpay.Fragments;

import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.soffice.clickandpay.Adapters.OffersAdapter;
import com.soffice.clickandpay.BuildConfig;
import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.NetWork.JsonRequester;
import com.soffice.clickandpay.NetWork.TaskListner;
import com.soffice.clickandpay.NetWork.Urls;
import com.soffice.clickandpay.Pojo.Offeritem;
import com.soffice.clickandpay.Pojo.OffersDTO;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.Utilty.Display;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.soffice.clickandpay.Utilty.Utils;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Surya on 24-04-2016.
 */
public class Offers extends Fragment implements TaskListner
{
    ViewPager viewPager;
    CirclePageIndicator pageIndicator;
    RecyclerView recyclerView;
    ProgressBar Pbar;
    SessionManager sessionManager;
    JsonRequester DataRequester;
    String className;
    List<Offeritem> OffersData;
    List<Offeritem> SliderData;
    private final String REQUEST_TAG="offers_request";
    Handler handler;
    int SliderPos=0;
    public static Offers newInstance(String Category)
    {
        Offers instance=new Offers();
        Bundle bundle=new Bundle();
        bundle.putString("catid",Category);
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager=ClickandPay.getInstance().getSession();
        DataRequester=new JsonRequester(this);
        className=this.getClass().getCanonicalName();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_offers,container,false);
        viewPager= (ViewPager) view.findViewById(R.id.offers_pager);
        pageIndicator= (CirclePageIndicator) view.findViewById(R.id.offers_pager_indicators);
        recyclerView= (RecyclerView) view.findViewById(R.id.offers_list);
        Pbar= (ProgressBar) view.findViewById(R.id.pbar);
        if(Build.VERSION.SDK_INT<21)
        {
            Pbar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        sessionManager= ClickandPay.getInstance().getSession();
        if(savedInstanceState==null)
        {
            ServerCall();
        }
        else
        {
            if(savedInstanceState.containsKey("sliderlist_size"))
            {
                SliderData=new ArrayList<>();
                for(int i=0;i<savedInstanceState.getInt("sliderlist_size");i++)
                {
                    SliderData.add((Offeritem) savedInstanceState.getSerializable("sliderobj"));
                }
                SliderAdapter slideradapter=new SliderAdapter(getChildFragmentManager());
                viewPager.setAdapter(slideradapter);
                pageIndicator.setViewPager(viewPager);
                InitializeSlider();

            }
            if(savedInstanceState.containsKey("list_size"))
            {
                OffersData=new ArrayList<>();
                for(int i=0;i<savedInstanceState.getInt("list_size");i++)
                {
                    OffersData.add((Offeritem) savedInstanceState.getSerializable("obj"+i));
                }
                OffersAdapter adapter=new OffersAdapter(getActivity(),OffersData);
                recyclerView.setAdapter(adapter);
            }
            else
            {
                ServerCall();
            }
        }
        return view;
    }

    private void ServerCall()
    {
        //Fetch and append data to the ArrayList
        if(Utils.CheckInternet(getActivity()))
        {
            Pbar.setVisibility(View.VISIBLE);
            Map<String,String> Params=new HashMap<>();
            Params.put("authkey",sessionManager.getAuthKey());
            if(BuildConfig.DEBUG){Log.d("categoryid",getArguments().getString("catid"));}
            Params.put("catid",getArguments().getString("catid"));
            DataRequester.StringRequesterFormValues(Urls.getoffers_url, Request.Method.POST,className,Urls.offers_method,Params,REQUEST_TAG);
        }
        else
        {

        }
    }

    @Override
    public void onTaskfinished(String response, int cd, String _className, String _methodName) {
        Pbar.setVisibility(View.GONE);
        if(cd==0)
        {
            Display.DisplayToast(getActivity(),"Oops.. Something went wrong.. Please try again.");
        }
        else if(cd==5)
        {
            Log.d("JsonData",response);
            Gson g=new Gson();
            OffersDTO Data= g.fromJson(response, OffersDTO.class);
            if(Data.getStatus().equalsIgnoreCase("200"))
            {
                SliderData=Data.getBanneroffers();
                OffersData=Data.getOffers();
                AppendData();
            }
        }
    }

    private void AppendData()
    {
        createSlider();
        OffersAdapter adapter=new OffersAdapter(getActivity(),OffersData);
        recyclerView.setAdapter(adapter);
    }

    private void createSlider()
    {
        SliderAdapter slideradapter=new SliderAdapter(getChildFragmentManager());
        viewPager.setAdapter(slideradapter);
        pageIndicator.setViewPager(viewPager);
        InitializeSlider();
    }

    private class SliderAdapter extends FragmentPagerAdapter
    {

        public SliderAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return SliderData.size();
        }

        @Override
        public Fragment getItem(int position) {
            return SliderFragment.newInstance(SliderData.get(position));
        }


    }

    private void InitializeSlider()
    {
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(SliderPos,true);
                if(SliderPos==SliderData.size()-1)
                {
                    SliderPos=0;
                }
                else
                {
                    SliderPos++;
                }
                handler.postDelayed(this,4000);
            }
        },4000);
        SliderPos++;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(OffersData!=null)
        {
            if(OffersData.size()>0)
            {
                for(int i=0;i<OffersData.size();i++)
                {
                    outState.putSerializable("offerobj"+i,OffersData.get(i));
                }
                outState.putInt("offerlist_size",OffersData.size());
            }
        }
        if(SliderData!=null)
        {
            if(SliderData.size()>0)
            {
                for(int i=0;i<SliderData.size();i++)
                {
                    outState.putSerializable("sliderobj"+i,SliderData.get(i));
                }
                outState.putInt("sliderlist_size",SliderData.size());
            }
        }
        super.onSaveInstanceState(outState);
    }
}
