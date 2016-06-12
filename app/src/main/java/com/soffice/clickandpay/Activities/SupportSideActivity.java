package com.soffice.clickandpay.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.soffice.clickandpay.Adapters.TranscationListAdapter;
import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.NetWork.JsonRequester;
import com.soffice.clickandpay.NetWork.TaskListner;
import com.soffice.clickandpay.NetWork.Urls;
import com.soffice.clickandpay.Pojo.LastTransactionResponseModel;
import com.soffice.clickandpay.Pojo.TransactionPojo;
import com.soffice.clickandpay.Pojo.TransactionResponseModel;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.UI.RobotoLightTextView;
import com.soffice.clickandpay.UI.RobotoRegularTextView;
import com.soffice.clickandpay.Utilty.Display;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.soffice.clickandpay.Utilty.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Surya on 08-05-2016.
 */
public class SupportSideActivity extends AppCompatActivity implements TaskListner,View.OnClickListener
{
    JsonRequester DataRequest;
    SessionManager sessionManager;
    CoordinatorLayout coordinatorLayout;
    CardView LastTransDetails;
    ScrollView scrollView;
    ProgressBar Pbar;
    String ss = "<font color=#101010>";
    String ss2 = " <font color=#bf0e14>";
    String ss4 = "</font>";
    View ColorStyleTv;
    TransactionPojo Obj;
    RobotoRegularTextView TitleTv,TransacDateTv,TransacTimeTv,TransacStorename,PickTransaction;
    ImageView VendorLogo,TransacStatusLogo,Backbtn;
    RobotoLightTextView PaymentIssue,Clicknpayissue,AccountIssue,OffersdealsIssue,SecurityPayIssue,OthersIssue,Transaccode,TransacAddress,TransacID,TransacAmount,TransacStatusText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_sidemenu);
        sessionManager=ClickandPay.getInstance().getSession();
        DataRequest=new JsonRequester(this);
        BuildUI();
        ServerCall();
    }

    private void BuildUI()
    {
        coordinatorLayout= (CoordinatorLayout) findViewById(R.id.mycoordinator);
        scrollView= (ScrollView) findViewById(R.id.support_side_scroll);
        Pbar= (ProgressBar) findViewById(R.id.pbar);
        TitleTv= (RobotoRegularTextView) findViewById(R.id.support_side_title);
        LastTransDetails= (CardView) findViewById(R.id.support_side_last_trans_details);
        PickTransaction= (RobotoRegularTextView) findViewById(R.id.support_side_pick_transaction);
        Backbtn= (ImageView) findViewById(R.id.back_IV);
        Backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SupportSideActivity.this.finish();
            }
        });
        PickTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SupportSideActivity.this,TransactionsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        PaymentIssue= (RobotoLightTextView) findViewById(R.id.support_side_payment_issue);
        PaymentIssue.setOnClickListener(this);
        Clicknpayissue= (RobotoLightTextView) findViewById(R.id.support_side_clicknpayissue);
        Clicknpayissue.setOnClickListener(this);
        AccountIssue= (RobotoLightTextView) findViewById(R.id.support_side_account_issue);
        AccountIssue.setOnClickListener(this);
        OffersdealsIssue= (RobotoLightTextView) findViewById(R.id.support_side_offerndealsissues);
        OffersdealsIssue.setOnClickListener(this);
        SecurityPayIssue= (RobotoLightTextView) findViewById(R.id.support_side_securityprivacy);
        SecurityPayIssue.setOnClickListener(this);
        OthersIssue= (RobotoLightTextView) findViewById(R.id.support_side_others);
        OthersIssue.setOnClickListener(this);
        VendorLogo= (ImageView) findViewById(R.id.vendorlogo);
        TransacStatusLogo= (ImageView) findViewById(R.id.transac_status_logo);
        TransacDateTv= (RobotoRegularTextView) findViewById(R.id.date_tv);
        TransacTimeTv= (RobotoRegularTextView) findViewById(R.id.time_tv);
        TransacStorename= (RobotoRegularTextView) findViewById(R.id.storeName);
        Transaccode= (RobotoLightTextView) findViewById(R.id.codeEntered);
        TransacAddress= (RobotoLightTextView) findViewById(R.id.address2);
        TransacID= (RobotoLightTextView) findViewById(R.id.transactionID);
        TransacAmount= (RobotoLightTextView) findViewById(R.id.amount_rupees);
        TransacStatusText= (RobotoLightTextView) findViewById(R.id.transac_status_text);
        ColorStyleTv=findViewById(R.id.colorStyle_TV);
        LastTransDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SupportSideActivity.this, SupportActivity.class);
                i.putExtra("transacpojo",Obj);
                startActivity(i);
                finish();
            }
        });
    }

    private void ServerCall()
    {
        if(Utils.CheckInternet(this))
        {
            Pbar.setVisibility(View.VISIBLE);
            Map<String,String> params=new HashMap<>();
            params.put("authkey",sessionManager.getAuthKey());
            DataRequest.StringRequesterFormValues(Urls.Last_transaction_url, Request.Method.POST,getLocalClassName(),Urls.Last_transaction_method,params,"LAST_TRANS_REQUEST");
        }
        else{
            Utils.GenerateSnackbar(this,coordinatorLayout,"Please check your internet connection and try again.");
        }
    }

    @Override
    public void onTaskfinished(String response, int cd, String _className, String _methodName) {
        Display.DisplayLogD("Response",response);
        if(cd==0)
        {
            Utils.GenerateSnackbar(this,coordinatorLayout,"Something went wrong. Please try again");
        }
        else if(cd==5) {
            try {
                if(ParseCheck(response)) {
                Gson g = new Gson();
                LastTransactionResponseModel model = g.fromJson(response, LastTransactionResponseModel.class);
                Display.DisplayLogI("ADITYA code", model.code);
                if (model.code.equalsIgnoreCase("200")) {

                        Obj = model.transactionPojos.get(0);
                        if (Obj.transactionStatus.equalsIgnoreCase("success")) {
                            TransacStatusLogo.setImageResource(R.drawable.ic_success);
                            ColorStyleTv.setBackgroundColor(Color.parseColor("#FF8cc341"));
                            TransacAmount.setTextColor(Color.parseColor("#FF8cc341"));
                        } else if (Obj.transactionStatus.equalsIgnoreCase("failure") || Obj.transactionStatus.equalsIgnoreCase("aborted")) {
                            TransacStatusLogo.setImageResource(R.drawable.ic_failed);
                            ColorStyleTv.setBackgroundColor(Color.parseColor("#FFec575c"));
                            TransacAmount.setTextColor(Color.parseColor("#FFec575c"));

                        } else {
                            TransacStatusLogo.setImageResource(R.drawable.ic_in_process);
                            ColorStyleTv.setBackgroundColor(Color.parseColor("#FFf0b331"));
                            TransacAmount.setTextColor(Color.parseColor("#FFf0b331"));

                        }
                        if (Obj.TransactionDescription.equalsIgnoreCase("1")) {
                            TransacStorename.setText(Obj.merchantstore);
                            Transaccode.setText("Code : " + Html.fromHtml(ss2 + Obj.receiver + ss4));
                            Picasso.with(this).load(Obj.merchantLogo)
                                    .into(VendorLogo);
                        } else if (Obj.TransactionDescription.equalsIgnoreCase("2")) {
                            TransacStorename.setText("Added Money");
                            Transaccode.setText("From : " + Html.fromHtml(ss2 + "You" + ss4));
                            VendorLogo.setImageResource(R.drawable.ic_add_money);
                        } else if (Obj.TransactionDescription.equalsIgnoreCase("3")) {
                            TransacStorename.setText("Sent  Money");
                            Transaccode.setText("To : " + Html.fromHtml(ss2 + Obj.receiver + ss4));
                            VendorLogo.setImageResource(R.drawable.ic_send_money);
                        } else {
                            TransacStorename.setText("Received Money");
                            Transaccode.setText("From : " + Html.fromHtml(ss2 + Obj.Sender + ss4));
                            VendorLogo.setImageResource(R.drawable.ic_receive_money);

                        }
                        TransacDateTv.setText(Obj.transactionDate);
                        TransacTimeTv.setText(Obj.transactionTime);
                        TransacAddress.setText(Obj.merchantAddress);
                        TransacID.setText(Obj.AppTransactionID);
                        TransacAmount.setText(Obj.amount);
                        TransacStatusText.setText(Obj.transactionStatus);
                        if (Obj.transactionStatus.equalsIgnoreCase("success")) {
                            ColorStyleTv.setBackgroundColor(Color.parseColor("#FF8cc341"));
                        } else if (Obj.transactionStatus.equalsIgnoreCase("aborted") || Obj.transactionStatus.equalsIgnoreCase("failed")) {
                            ColorStyleTv.setBackgroundColor(Color.parseColor("#FFec575c"));
                        } else {
                            ColorStyleTv.setBackgroundColor(Color.parseColor("#FFf0b331"));
                        }
                    }

                    TitleTv.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.VISIBLE);
                }
                else
                {
                    TitleTv.setVisibility(View.GONE);
                    LastTransDetails.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Utils.GenerateSnackbar(this,coordinatorLayout,"Oops.. Something went wrong.");
        }

        Pbar.setVisibility(View.GONE);
    }

    private boolean ParseCheck(String data)
    {
        try
        {
            JSONObject obj = new JSONObject(data);
            if (obj.has("TRANSACTIONS")) {
                if(!obj.getString("TRANSACTIONS").equalsIgnoreCase(""))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(SupportSideActivity.this,TellUsAboutItActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        switch (v.getId()){
            case R.id.support_side_payment_issue:intent.putExtra("opt","8"); break;
            case R.id.support_side_clicknpayissue:intent.putExtra("opt","9"); break;
            case R.id.support_side_account_issue:intent.putExtra("opt","10"); break;
            case R.id.support_side_offerndealsissues:intent.putExtra("opt","11"); break;
            case R.id.support_side_securityprivacy:intent.putExtra("opt","12"); break;
            case R.id.support_side_others:intent.putExtra("opt","13"); break;
        }
        intent.putExtra("cat",((TextView)v).getText().toString());
        startActivity(intent);
    }
}
