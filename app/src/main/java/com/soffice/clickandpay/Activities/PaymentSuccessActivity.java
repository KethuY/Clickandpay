package com.soffice.clickandpay.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.Fragments.HomeFragment;
import com.soffice.clickandpay.Listeners.PageChangedToClearListener;
import com.soffice.clickandpay.NetWork.JsonRequester;
import com.soffice.clickandpay.NetWork.TaskListner;
import com.soffice.clickandpay.NetWork.Urls;
import com.soffice.clickandpay.Pojo.FeedbackResponseModel;
import com.soffice.clickandpay.Pojo.MerchantPojo;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.Utilty.Constants;
import com.soffice.clickandpay.Utilty.Display;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class PaymentSuccessActivity extends AppCompatActivity implements TaskListner {

    TextView codeEntered, payment_amount_label, submit_experience_tv, submit_feedback_tv, done_button,
            cancel_experience_tv, cancel_feedback_tv, storeName, address1;
    ImageView stoteLogo, back_IV;
    LinearLayout smilei1_layout, smilei2_layout, smilei3_layout, smilei4_layout;
    RelativeLayout experienceLayout, feedLayout, sayDoneLayout;
    ClickandPay clickpay;
    SessionManager session;
    JsonRequester requester;
    Urls urls;
    String className;
    EditText share_experience_EditText, share_feedback_EditText;
    MerchantPojo merchat;
    PageChangedToClearListener listener;
    String ss = "<font color=#101010>";
    String ss2 = " <font color=#bf0e14>";
    String ss4 = "</font>";
    TextView date, timeNo, billNo;
    private final String REQUEST_TAG="paysuccess_tag";
    private final String REQUEST_FEEDBACK_TAG="feedback_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        clickpay = (ClickandPay) getApplicationContext();
        session = clickpay.getSession();
        className = getLocalClassName();
        requester = new JsonRequester(this);
        urls = clickpay.getUrls();

        merchat = Constants.marchant;
        listener = HomeFragment.home;

        codeEntered = (TextView) findViewById(R.id.codeEntered);
        payment_amount_label = (TextView) findViewById(R.id.payment_amount_label);
        smilei1_layout = (LinearLayout) findViewById(R.id.smilei1_layout);
        smilei2_layout = (LinearLayout) findViewById(R.id.smilei2_layout);
        smilei3_layout = (LinearLayout) findViewById(R.id.smilei3_layout);
        smilei4_layout = (LinearLayout) findViewById(R.id.smilei4_layout);

        experienceLayout = (RelativeLayout) findViewById(R.id.experienceLayout);
        feedLayout = (RelativeLayout) findViewById(R.id.feedLayout);
        sayDoneLayout = (RelativeLayout) findViewById(R.id.sayDoneLayout);
        back_IV = (ImageView) findViewById(R.id.back_IV);

        share_experience_EditText = (EditText) findViewById(R.id.share_experience_EditText);
        share_feedback_EditText = (EditText) findViewById(R.id.share_feedback_EditText);
        submit_experience_tv = (TextView) findViewById(R.id.submit_experience_tv);
        submit_feedback_tv = (TextView) findViewById(R.id.submit_feedback_tv);
        done_button = (TextView) findViewById(R.id.done_button);
        cancel_experience_tv = (TextView) findViewById(R.id.cancel_experience_tv);
        cancel_feedback_tv = (TextView) findViewById(R.id.cancel_feedback_tv);

        storeName = (TextView) findViewById(R.id.storeName);
        address1 = (TextView) findViewById(R.id.address1);
        stoteLogo = (ImageView) findViewById(R.id.stoteLogo);

        date = (TextView) findViewById(R.id.date);
        timeNo  = (TextView) findViewById(R.id.timeNo);
        billNo  = (TextView) findViewById(R.id.billNo);
        payment_amount_label  = (TextView) findViewById(R.id.payment_amount_label);

        if(getIntent() != null && getIntent().getExtras() != null) {
            if(getIntent().hasExtra("transac_date") && getIntent().getStringExtra("transac_date") != null && getIntent().getStringExtra("transac_date").length() > 0){
                date.setText(getIntent().getStringExtra("transac_date")+"/");
            }
            if(getIntent().hasExtra("transac_time") && getIntent().getStringExtra("transac_time") != null && getIntent().getStringExtra("transac_time").length() > 0){
                timeNo.setText(getIntent().getStringExtra("transac_time"));
            }
            if(getIntent().hasExtra("transac_id") && getIntent().getStringExtra("transac_id") != null && getIntent().getStringExtra("transac_id").length() > 0){
                billNo.setText(getIntent().getStringExtra("transac_id"));
            }
            if(getIntent().hasExtra("transac_amount") && getIntent().getStringExtra("transac_amount") != null && getIntent().getStringExtra("transac_amount").length() > 0){
                payment_amount_label.setText(Html.fromHtml(ss2+getIntent().getStringExtra("transac_amount")+ss4));
            }
        }

        storeName.setText(merchat.merchantStore);
        if(merchat.merchantLogo != null && merchat.merchantLogo.length() > 0) {
            Picasso.with(this).load(merchat.merchantLogo).into(stoteLogo);
        }
        address1.setText(merchat.merchantAddress);

        experienceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        feedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        codeEntered.setText(Html.fromHtml(ss + "Code : " + ss4 + ss2 + getIntent().getStringExtra("vendorCode") + ss4));
        payment_amount_label.setText(Html.fromHtml(ss2+getIntent().getStringExtra("transac_amount")+ss4));

                smilei1_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //feedLayout.setVisibility(View.VISIBLE);
                        sayDoneLayout.setVisibility(View.VISIBLE);

                    }
                });

        smilei2_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //feedLayout.setVisibility(View.VISIBLE);
                sayDoneLayout.setVisibility(View.VISIBLE);

            }
        });

        smilei3_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //experienceLayout.setVisibility(View.VISIBLE);
                sayDoneLayout.setVisibility(View.VISIBLE);
            }
        });

        smilei4_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //experienceLayout.setVisibility(View.VISIBLE);
                sayDoneLayout.setVisibility(View.VISIBLE);
            }
        });

        submit_experience_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (share_experience_EditText.getText().toString().length() > 1) {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("authkey", session.getAuthKey());
                    params.put("tid", clickpay.getDeviceId(getApplicationContext()));
                    params.put("rating", "5");
                    params.put("comments", share_experience_EditText.getText().toString());
                    requester.StringRequesterFormValues(urls.feedback, Request.Method.POST, className, urls.feedback_methodName, params,REQUEST_TAG);
                }else{
                    Display.DisplayToast(PaymentSuccessActivity.this, "Please write your experience to improve better to best.. :)");
                }
            }
        });

        submit_feedback_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (share_feedback_EditText.getText().toString().length() > 1) {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("authkey", session.getAuthKey());
                    params.put("tid", clickpay.getDeviceId(getApplicationContext()));
                    params.put("rating", "1");
                    params.put("comments", share_feedback_EditText.getText().toString());
                    requester.StringRequesterFormValues(urls.feedback, Request.Method.POST, className, urls.feedback_methodName, params,REQUEST_FEEDBACK_TAG);
                }else{
                    Display.DisplayToast(PaymentSuccessActivity.this, "Please write your experience to improve better to best.. :)");
                }
            }
        });

        cancel_experience_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.showPassCode = false;
                listener.clearAll();
                finish();
            }
        });

        cancel_feedback_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.showPassCode = false;
                listener.clearAll();
                finish();
            }
        });

        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.showPassCode = false;
                listener.clearAll();
                finish();
            }
        });

        back_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.showPassCode = false;
                listener.clearAll();
                finish();
            }
        });



        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.showPassCode = false;
        listener.clearAll();
        finish();
    }

    @Override
    public void onTaskfinished(String response, int cd, String _className, String _methodName) {
        Display.DisplayLogI("ADITYA", "" + response);
        try {
            if (cd == 00) {
                Display.DisplayToast(this, response);
            } else if (cd == 05) {
                if (_className.equalsIgnoreCase(className) && _methodName.equalsIgnoreCase(urls.feedback_methodName)) {
                    Gson g = new Gson();
                    FeedbackResponseModel model = g.fromJson(response, FeedbackResponseModel.class);
                    Display.DisplayLogI("ADITYA code", model.code);
                    if (model.code.equalsIgnoreCase("200")) {
                        experienceLayout.setVisibility(View.GONE);
                        feedLayout.setVisibility(View.GONE);
                        sayDoneLayout.setVisibility(View.VISIBLE);
                    } else {
                        Display.DisplayToast(PaymentSuccessActivity.this, "Error : " + response);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}