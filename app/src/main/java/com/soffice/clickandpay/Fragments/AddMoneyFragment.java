package com.soffice.clickandpay.Fragments;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soffice.clickandpay.Activities.AddMoneyActivity;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.Utilty.Display;
import com.soffice.clickandpay.Utilty.Utils;


/**
 * Created by sys2025 on 13/9/15.
 */
public class AddMoneyFragment extends Fragment {
    public static final String CODE_ENTERED = "CODE_ENTERED";

    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    private int mPage;
    LinearLayout scan_QR;
    TextView Pay_addMoney;
    EditText vendorCode1, vendorCode2, vendorCode3, vendorCode4;
    String vc1, vc2, vc3, vc4;


    public static AddMoneyFragment newInstance() {
        Bundle args = new Bundle();
        AddMoneyFragment fragment = new AddMoneyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display.DisplayLogI("ADITYA", "ADD MONEY FRAG ON CREATE");
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addmoney_fragment, container, false);
        scan_QR = (LinearLayout) view.findViewById(R.id.scan_QR);

        vendorCode1 = (EditText) view.findViewById(R.id.vendorCode1);
        vendorCode2 = (EditText) view.findViewById(R.id.vendorCode2);
        vendorCode3 = (EditText) view.findViewById(R.id.vendorCode3);
        vendorCode4 = (EditText) view.findViewById(R.id.vendorCode4);
        Pay_addMoney = (TextView) view.findViewById(R.id.Pay_addMoney);

        vendorCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count == 1){
                    vendorCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        vendorCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count == 1){
                    vendorCode3.requestFocus();
                }else if(count == 0){
                    vendorCode1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        vendorCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count == 1){
                    vendorCode4.requestFocus();
                }else if(count == 0){
                    vendorCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        vendorCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count == 1){
                    Pay_addMoney.requestFocus();
                }else if(count == 0){
                    vendorCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Pay_addMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vc1 = vendorCode1.getText().toString();
                vc2 = vendorCode1.getText().toString();
                vc3 = vendorCode1.getText().toString();
                vc4 = vendorCode1.getText().toString();

                if (vc1.length() > 0 && vc2.length() > 0 && vc3.length() > 0 && vc4.length() > 0) {
                    Fragment myfragment;
                    myfragment = new ProceedToAddMoneyFragment();
                    Bundle args = new Bundle();
                    args.putString(CODE_ENTERED, vc1 + vc2 + vc3 + vc4);
                    myfragment.setArguments(args);

                    /*FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                    fragmentTransaction.replace(R.id.subFragLayout, myfragment);
                    fragmentTransaction.commit();*/
                } else {
                    Display.DisplayToast(getActivity(), "Enter a proper Vendor Code..!!");
                    vendorCode1.requestFocus();
                }
            }
        });

        vendorCode1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.requestFocus();
            }
        });


        scan_QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(ACTION_SCAN);
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    startActivityForResult(intent, 0);
                } catch (ActivityNotFoundException anfe) {

                    Display.DisplayLogI("ADITYA", "ADD MONEY FRAG SCANER NOT FOUND");
                }
            }
        });

        return view;
    }


}
