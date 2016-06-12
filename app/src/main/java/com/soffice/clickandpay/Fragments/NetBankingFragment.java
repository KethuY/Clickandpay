package com.soffice.clickandpay.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.SpinnerAdapter;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.soffice.clickandpay.Activities.WebviewActivity;
import com.soffice.clickandpay.Adapters.PremiumBanksAdapter;
import com.soffice.clickandpay.BuildConfig;
import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.Pojo.NetBanks;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.Utilty.AvenuesParams;
import com.soffice.clickandpay.Utilty.Constants;
import com.soffice.clickandpay.Utilty.RSAUtility;
import com.soffice.clickandpay.Utilty.ServiceUtility;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Surya on 12-04-2016.
 */
public class NetBankingFragment extends Fragment
{
    private static final String NetBankingRequest="netbanking_request";
    AppCompatButton Paybtn;
    public static ArrayList<NetBanks> List=new ArrayList<>();
    private ArrayList<NetBanks> PremiumBanks=new ArrayList<>();
    GridView PremiumBanksGrid;
    AppCompatSpinner BanksSpinner;
    public static NetBankingFragment newInstance(ArrayList<NetBanks> List)
    {
        NetBankingFragment instance=new NetBankingFragment();
        Bundle bundle=new Bundle();
        bundle.putParcelableArrayList("list",List);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //List=getArguments().getParcelableArrayList("list");
        Log.d("data0",List.get(0).getCardName());
        FilterPremiumBanks();

    }

    private void FilterPremiumBanks()
    {
        ArrayList<Integer> Matches=new ArrayList<>();
        for(int i=0;i<List.size();i++)
        {
            if(List.get(i).getCardName().equalsIgnoreCase("axis bank"))
            {
                PremiumBanks.add(List.get(i));
                Matches.add(i);
            }
            else if(List.get(i).getCardName().equalsIgnoreCase("sbi bank"))
            {
                PremiumBanks.add(List.get(i));
                Matches.add(i);
            }
            else if(List.get(i).getCardName().equalsIgnoreCase("icici bank"))
            {
                PremiumBanks.add(List.get(i));
                Matches.add(i);
            }
            else if(List.get(i).getCardName().equalsIgnoreCase("hdfc bank"))
            {
                PremiumBanks.add(List.get(i));
                Matches.add(i);
            }
            else if(List.get(i).getCardName().equalsIgnoreCase("yes bank"))
            {
                PremiumBanks.add(List.get(i));
                Matches.add(i);
            }
            else if(List.get(i).getCardName().equalsIgnoreCase("canara bank"))
            {
                PremiumBanks.add(List.get(i));
                Matches.add(i);
            }
        }

        if(BuildConfig.DEBUG){Log.d("Size",String.valueOf(PremiumBanks.size()));}

        for(int i=0;i<Matches.size();i++)
        {
            List.remove(Matches.get(i));
        }
        Matches.clear();
        Matches.trimToSize();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_netbanking,container,false);
        //GetPaymentOptions();
        BuildUI(view);
        return view;
    }

    private void BuildUI(View view)
    {
        PremiumBanksGrid= (GridView) view.findViewById(R.id.premium_banks_grid);
        PremiumBanksAdapter adapter=new PremiumBanksAdapter(getActivity(),0,PremiumBanks);
        PremiumBanksGrid.setAdapter(adapter);
        BanksSpinner= (AppCompatSpinner) view.findViewById(R.id.banks_spinner);
        ArrayAdapter spinneradapter = new ArrayAdapter(getActivity(), R.layout.spinner_view, List);
        BanksSpinner.setAdapter(spinneradapter);

    }


    /*private void GetPaymentOptions()
    {
        JsonObjectRequest myrequest=new JsonObjectRequest(Request.Method.POST, Constants.JSON_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response",response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put(AvenuesParams.ACCESS_CODE,"AVWV64DD72AH97VWHA");
                params.put(AvenuesParams.CURRENCY,"INR");
                params.put(AvenuesParams.AMOUNT,"10.00");
                params.put(AvenuesParams.CUSTOMER_IDENTIFIER,"92446");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers=new HashMap<>();
                headers.put("User-Agent","Mozilla/5.0 (Linux; U; Android-4.0.3; en-us; Galaxy Nexus Build/IML74K) AppleWebKit/535.7 (KHTML, like Gecko) CrMo/16.0.912.75 Mobile Safari/535.7");
                return headers;

            }
        };

        myrequest.setRetryPolicy(new DefaultRetryPolicy(6000,2,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ClickandPay.getInstance().addToRequestQueue(myrequest);
    }*/

    }



