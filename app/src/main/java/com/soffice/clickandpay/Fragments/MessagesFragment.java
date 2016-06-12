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
public class MessagesFragment extends Fragment
{
    public static MessagesFragment newInstance()
    {
        MessagesFragment instance=new MessagesFragment();
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_messages,container,false);
        return view;
    }
}
