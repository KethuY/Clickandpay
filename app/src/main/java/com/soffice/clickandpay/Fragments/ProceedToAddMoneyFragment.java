package com.soffice.clickandpay.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soffice.clickandpay.R;
import com.soffice.clickandpay.Utilty.Display;


/**
 * Created by sys2025 on 13/9/15.
 */
public class ProceedToAddMoneyFragment extends Fragment {
    public static final String CODE_ENTERED = "CODE_ENTERED";


    private int mPage;
    TextView storeName, codeEntered, address1, address2;
    String vc1, vc2, vc3, vc4;

    public static ProceedToAddMoneyFragment newInstance() {
        Bundle args = new Bundle();
        ProceedToAddMoneyFragment fragment = new ProceedToAddMoneyFragment();
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display.DisplayLogI("ADITYA", "ADD MONEY FRAG ON CREATE");
        Bundle b = getArguments();
        vc1 = b.getString(CODE_ENTERED);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.proceedtoaddmoney_fragment, container, false);

        String ss = "<font color=#000000>";
        String ss1 = "Code : ";
        String ss2 = "</font> <font color=#bf0e14>";
        String ss3 = "Ameerpet road, Hyderabad - 500034";
        String ss4 = "</font>";
        String ss5 = "GS 24, Carat Building,";
        String ss6 = "BIGBAZAR Super Market";
        String ss7 = "Address : ";


        storeName = (TextView) view.findViewById(R.id.storeName);
        codeEntered = (TextView) view.findViewById(R.id.codeEntered);
        address1 = (TextView) view.findViewById(R.id.address1);
        address2 = (TextView) view.findViewById(R.id.address2);

        codeEntered.setText(Html.fromHtml(ss+ss1+ss2+vc1+ss4));
        address1.setText(Html.fromHtml(ss+ss7+ss2+ss5+ss4));


        return view;
    }
}
