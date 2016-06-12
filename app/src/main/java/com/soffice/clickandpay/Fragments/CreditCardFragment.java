package com.soffice.clickandpay.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.soffice.clickandpay.Pojo.NetBanks;
import com.soffice.clickandpay.R;

import java.util.ArrayList;

/**
 * Created by Surya on 12-04-2016.
 */
public class CreditCardFragment extends Fragment
{
    ArrayList<NetBanks> List;

    public static CreditCardFragment newInstance(ArrayList<NetBanks> List)
     {
         CreditCardFragment instance=new CreditCardFragment();
         Bundle bundle=new Bundle();
         bundle.putParcelableArrayList("list",List);
         return instance;
     }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.credit_card_details_fragment,container,false);
        return view;
    }
}
