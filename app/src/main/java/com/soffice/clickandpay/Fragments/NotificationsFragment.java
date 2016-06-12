package com.soffice.clickandpay.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.soffice.clickandpay.R;

/**
 * Created by Surya on 11-05-2016.
 */
public class NotificationsFragment extends Fragment
{
   public static NotificationsFragment newInstance()
   {
       NotificationsFragment instance=new NotificationsFragment();
       return instance;
   }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_notifications,container,false);
        return view;
    }
}
