package com.soffice.clickandpay.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.soffice.clickandpay.R;

/**
 * Created by Surya on 04-05-2016.
 */
public class WalkThroughFragment extends Fragment
{
    public static WalkThroughFragment newInstance(int ImageID)
    {
        WalkThroughFragment instance=new WalkThroughFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("img_id",ImageID);
        instance.setArguments(bundle);
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_walkthrough,container,false);
        ImageView iv= (ImageView) view.findViewById(R.id.walkthrough_iv);
        if(Build.VERSION.SDK_INT>16) {
            iv.setBackground(ContextCompat.getDrawable(getActivity(), getArguments().getInt("img_id")));
        }
        else
        {
            iv.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), getArguments().getInt("img_id")));
        }
        return view;
    }
}
