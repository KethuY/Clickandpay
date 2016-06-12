package com.soffice.clickandpay.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.soffice.clickandpay.Pojo.TransactionPojo;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.UI.RobotoLightTextView;
import com.soffice.clickandpay.UI.RobotoRegularTextView;
import com.squareup.picasso.Picasso;

public class SupportActivity extends AppCompatActivity implements View.OnClickListener
{

    TextView text_Billing_iisue, text_Cashback_iisue, text_Vendorcode_iisue, text_Wrong_payment_iisue, text_copy_of_Invoice_iisue,
            text_Payment_delayed_iisue, text_Other_iisue;
    RobotoRegularTextView TransacTitle,TransacDate,TransacTime;
    RobotoLightTextView TransacCode,TransacAddress,TransacID,TransacAmount,TransacStatus;
    ImageView back_IV,TransacStatusImage,TransacLogo;
    View StatusColor;
    TransactionPojo Obj;
    String ss = "<font color=#101010>";
    String ss2 = " <font color=#bf0e14>";
    String ss4 = "</font>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Obj= (TransactionPojo) getIntent().getSerializableExtra("transacpojo");
        setContentView(R.layout.activity_support);
        BuildUI();
        BindData();

    }

    private void BuildUI()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TransacStatusImage= (ImageView) findViewById(R.id.transac_status_logo);
        text_Billing_iisue = (TextView) findViewById(R.id.text_Billing_iisue);
        text_Billing_iisue.setOnClickListener(this);
        text_Cashback_iisue = (TextView) findViewById(R.id.text_Cashback_iisue);
        text_Cashback_iisue.setOnClickListener(this);
        text_Vendorcode_iisue = (TextView) findViewById(R.id.text_Vendorcode_iisue);
        text_Vendorcode_iisue.setOnClickListener(this);
        text_Wrong_payment_iisue = (TextView) findViewById(R.id.text_Wrong_payment_iisue);
        text_Wrong_payment_iisue.setOnClickListener(this);
        text_copy_of_Invoice_iisue = (TextView) findViewById(R.id.text_copy_of_Invoice_iisue);
        text_copy_of_Invoice_iisue.setOnClickListener(this);
        text_Payment_delayed_iisue = (TextView) findViewById(R.id.text_Payment_delayed_iisue);
        text_Payment_delayed_iisue.setOnClickListener(this);
        text_Other_iisue = (TextView) findViewById(R.id.text_Other_iisue);
        text_Other_iisue.setOnClickListener(this);
        back_IV = (ImageView) findViewById(R.id.back_IV);
        back_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TransacTitle= (RobotoRegularTextView) findViewById(R.id.storeName);
        TransacCode= (RobotoLightTextView) findViewById(R.id.codeEntered);
        TransacAddress= (RobotoLightTextView) findViewById(R.id.address2);
        TransacID= (RobotoLightTextView) findViewById(R.id.transactionID);
        TransacAmount= (RobotoLightTextView) findViewById(R.id.amount_rupees);
        TransacStatus= (RobotoLightTextView) findViewById(R.id.transac_status_text);
        TransacLogo= (ImageView) findViewById(R.id.vendorlogo);
        TransacDate= (RobotoRegularTextView) findViewById(R.id.date_tv);
        TransacTime= (RobotoRegularTextView) findViewById(R.id.time_tv);
        StatusColor=findViewById(R.id.colorStyle_TV);
    }

    private void BindData() {
        if(Obj.transactionStatus.equalsIgnoreCase("success")) {
            TransacStatusImage.setImageResource(R.drawable.ic_success);
            StatusColor.setBackgroundColor(Color.parseColor("#FF8cc341"));
            TransacAmount.setTextColor(Color.parseColor("#FF8cc341"));
        }
        else if(Obj.transactionStatus.equalsIgnoreCase("failure")||Obj.transactionStatus.equalsIgnoreCase("aborted"))
        {
            TransacStatusImage.setImageResource(R.drawable.ic_failed);
            StatusColor.setBackgroundColor(Color.parseColor("#FFec575c"));
            TransacAmount.setTextColor(Color.parseColor("#FFec575c"));
        }
        else
        {
            TransacStatusImage.setImageResource(R.drawable.ic_in_process);
            StatusColor.setBackgroundColor(Color.parseColor("#FFf0b331"));
            TransacAmount.setTextColor(Color.parseColor("#FFf0b331"));

        }
        if(Obj.TransactionDescription.equalsIgnoreCase("1")) {
            TransacTitle.setText(Obj.merchantstore);
            TransacCode.setText("Code : "+ Html.fromHtml(ss2+Obj.receiver+ss4));
            Picasso.with(this).load(Obj.merchantLogo).into(TransacLogo);
        }
        else if(Obj.TransactionDescription.equalsIgnoreCase("2"))
        {
            TransacTitle.setText("Added Money");
            TransacCode.setText("From : "+ Html.fromHtml(ss2+"You"+ss4));
            TransacLogo.setImageResource(R.drawable.ic_add_money);
        }
        else if(Obj.TransactionDescription.equalsIgnoreCase("3"))
        {
            TransacTitle.setText("Sent  Money");
            TransacCode.setText("To : "+ Html.fromHtml(ss2+Obj.receiver+ss4));
            TransacLogo.setImageResource(R.drawable.ic_send_money);
        }
        else
        {
            TransacTitle.setText("Received Money");
            TransacCode.setText("From : "+ Html.fromHtml(ss2+Obj.Sender+ss4));
            TransacLogo.setImageResource(R.drawable.ic_receive_money);

        }
        TransacDate.setText(Obj.transactionDate);
        TransacTime.setText(Obj.transactionTime);
        TransacAddress.setText(Obj.merchantAddress);
        TransacID.setText(Obj.AppTransactionID);
        TransacAmount.setText(Obj.amount);
        TransacStatus.setText(Obj.transactionStatus);
        if(Obj.transactionStatus.equalsIgnoreCase("success")) {
            StatusColor.setBackgroundColor(Color.parseColor("#FF8cc341"));
        }
        else if(Obj.transactionStatus.equalsIgnoreCase("aborted")||Obj.transactionStatus.equalsIgnoreCase("failure"))
        {
            StatusColor.setBackgroundColor(Color.parseColor("#FFec575c"));
        }
        else
        {
            StatusColor.setBackgroundColor(Color.parseColor("#FFf0b331"));
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(SupportActivity.this,TellUsAboutItActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("txtid",Obj.AppTransactionID);
        intent.putExtra("cat",((TextView)v).getText().toString());
        switch (v.getId())
        {
            case R.id.text_Billing_iisue:intent.putExtra("opt","1"); break;
            case R.id.text_Cashback_iisue:intent.putExtra("opt","2"); break;
            case R.id.text_Vendorcode_iisue:intent.putExtra("opt","3"); break;
            case R.id.text_Wrong_payment_iisue:intent.putExtra("opt","4"); break;
            case R.id.text_copy_of_Invoice_iisue:intent.putExtra("opt","5"); break;
            case R.id.text_Payment_delayed_iisue:intent.putExtra("opt","6"); break;
            case R.id.text_Other_iisue:intent.putExtra("opt","7"); break;
        }
        startActivity(intent);
    }
}
