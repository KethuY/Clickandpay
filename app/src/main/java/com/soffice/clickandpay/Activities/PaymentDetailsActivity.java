package com.soffice.clickandpay.Activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.soffice.clickandpay.Adapters.CardsFragmentPagerAdapter;
import com.soffice.clickandpay.BuildConfig;
import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.NetWork.JsonRequester;
import com.soffice.clickandpay.NetWork.TaskListner;
import com.soffice.clickandpay.NetWork.Urls;
import com.soffice.clickandpay.Pojo.Cards;
import com.soffice.clickandpay.Pojo.NetBanks;
import com.soffice.clickandpay.Pojo.PaymentResponseModel;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.UI.PagerSlidingTabStrip;
import com.soffice.clickandpay.Utilty.AvenuesParams;
import com.soffice.clickandpay.Utilty.Constants;
import com.soffice.clickandpay.Utilty.Display;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.soffice.clickandpay.Utilty.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PaymentDetailsActivity extends AppCompatActivity implements TaskListner {

    String amountSpoending, payingAmount,className, vendorCode;
    TextView addMoney_TextView, cuttingMoney_TextView, fromWallet_Checkbox,pay_rupees,StoreNameTv,StoreCodeTv,StoreAddress1Tv,StoreAddress2Tv;
    ImageView back_IV,StoreLogo;
    AppCompatCheckBox Checkbox;
    ClickandPay clickpay;
    SessionManager session;
    JsonRequester requester;
    Urls urls;
    ArrayList<NetBanks> CCList;
    ArrayList<NetBanks> DCList;
    ArrayList<NetBanks> BanksList;
    LinearLayout ReceiptStrip;
    private final String REQUEST_TAG="payment_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        clickpay = (ClickandPay) getApplicationContext();
        ReceiptStrip= (LinearLayout) findViewById(R.id.receipt_strip);
        if(Build.VERSION.SDK_INT<21) {
            ViewCompat.setElevation(ReceiptStrip, 5.0f);
        }
        else
        {
            ReceiptStrip.setElevation(3.0f);
        }
        session = clickpay.getSession();
        requester = new JsonRequester(this);
        urls = clickpay.getUrls();
        className = getLocalClassName();
        vendorCode = getIntent().getStringExtra("vendorCode");
        amountSpoending = getIntent().getStringExtra("amountSpend");
        payingAmount=getIntent().getStringExtra("amountSpend");
        SplitAmount();
        StoreNameTv= (TextView) findViewById(R.id.storeName);
        StoreCodeTv= (TextView) findViewById(R.id.codeEntered);
        StoreAddress1Tv= (TextView) findViewById(R.id.address1);
        StoreAddress2Tv= (TextView) findViewById(R.id.address2);
        StoreLogo= (ImageView) findViewById(R.id.storeLogo);
        StoreNameTv.setText(Constants.marchant.merchantStore);
        String code="Vendor Code :  "+Constants.marchant.merchantid;
        StoreCodeTv.setText(code);
        StoreAddress1Tv.setText(Constants.marchant.merchantAddress);
        if(Constants.marchant.merchantLogo!=null&&!Constants.marchant.merchantLogo.equalsIgnoreCase(""))
        {
            Picasso.with(this).load(Constants.marchant.merchantLogo).placeholder(R.mipmap.ic_logo).into(StoreLogo);
        }
        addMoney_TextView = (TextView) findViewById(R.id.addMoney_TextView);
        cuttingMoney_TextView = (TextView) findViewById(R.id.cuttingMoney_TextView);
        back_IV = (ImageView) findViewById(R.id.back_IV);
        fromWallet_Checkbox = (TextView) findViewById(R.id.fromWallet_Checkbox);
        Checkbox = (AppCompatCheckBox) findViewById(R.id.checkBox);
        Checkbox.setChecked(true);
        pay_rupees = (TextView) findViewById(R.id.pay_rupees);
        DoCalculation(true);
        back_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DoCalculation(isChecked);
            }
        });

        pay_rupees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Double.parseDouble(payingAmount)==0) {
                    proceedToPay(payingAmount, vendorCode);
                }
                else
                {
                    Intent intent=new Intent(PaymentDetailsActivity.this,PaymentProcessActivity.class);
                    intent.putExtra(AvenuesParams.ORDER_ID,"12312312385");
                    intent.putExtra(AvenuesParams.ACCESS_CODE,getResources().getString(R.string.access_code));
                    intent.putExtra(AvenuesParams.MERCHANT_ID,getResources().getString(R.string.merchant_id));
                    intent.putExtra(AvenuesParams.REDIRECT_URL,Urls.redirect_url);
                    intent.putExtra(AvenuesParams.CANCEL_URL,Urls.redirect_url);
                    intent.putExtra(AvenuesParams.CURRENCY,"INR");
                    intent.putExtra(AvenuesParams.AMOUNT,payingAmount);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }
        });

        back_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.showPassCode = false;
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
        addMoney_TextView.setText(amountSpoending);
        cuttingMoney_TextView.setText(session.getWalletBal());

    }

    private void DisplayPager()
    {

    }

    private void DoCalculation(boolean isChecked)
    {
        if(isChecked)
        {

                if(Double.parseDouble(session.getWalletBal()) > 0){
                    payingAmount = String.valueOf(Double.parseDouble(amountSpoending));
                    addMoney_TextView.setText(payingAmount);
                    //SplitAmount();
                    pay_rupees.setText("Pay \u20B9 "+payingAmount);
                }else{
                    payingAmount = String.valueOf(Double.parseDouble(session.getWalletBal()) + Double.parseDouble(amountSpoending));
                    addMoney_TextView.setText(payingAmount);
                    //SplitAmount();
                    pay_rupees.setText("Pay \u20B9 "+payingAmount);
                }
        }
        else
        {
            payingAmount = String.valueOf(Double.parseDouble(amountSpoending) + Double.parseDouble(session.getWalletBal()));
            addMoney_TextView.setText(payingAmount);
            //SplitAmount();
            pay_rupees.setText("Pay \u20B9 "+payingAmount);


        }
    }

    private void SplitAmount()
    {
        if (payingAmount.contains(".")) {
            payingAmount.split(".");
            Display.DisplayLogI("ADITYA", " SPLITTT " + payingAmount.toString().split("\\.").length);
            String[] str = payingAmount.split("\\.");
            if (str.length > 1) {
                Display.DisplayLogI("ADITYA", " SPLITTT PARTS " + str[0]+ "  "+str[1]);
                if (str[1].toString().length() > 2) {
                    int lastdigit=Integer.valueOf(str[1].substring(2,3));
                    int paisa=Integer.parseInt(str[1].substring(1,2));
                    if(lastdigit>=5)
                    {
                        paisa=paisa+1;
                    }
                    pay_rupees.setText("Pay \u20B9 "+str[0] + "." +  str[1].substring(0,1)+paisa);
                    Display.DisplayLogI("ADITYA", "str[1].substring(0, 2) " + str[1].substring(0, 2));
                }
            }
        }
        else
        {
            pay_rupees.setText(payingAmount);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.showPassCode = false;
        finish();
    }

    private class ServerCall extends AsyncTask<Void,Void,String>
    {

        @Override
        protected String doInBackground(Void... params) {
            try{
                URL url=new URL(Urls.getPayment_Opts);
                HttpURLConnection urlConnection= (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                String Content = AvenuesParams.ACCESS_CODE+"="+"AVWV64DD72AH97VWHA"
                        +"&"+AvenuesParams.CURRENCY+Constants.PARAMETER_EQUALS+"INR"+Constants.PARAMETER_SEP
                        +AvenuesParams.AMOUNT+Constants.PARAMETER_EQUALS+"10.00"+Constants.PARAMETER_SEP
                        +AvenuesParams.COMMAND+Constants.PARAMETER_EQUALS+"getJsonDataVault"+Constants.PARAMETER_SEP
                        +AvenuesParams.CUSTOMER_IDENTIFIER+Constants.PARAMETER_EQUALS+"92446";
                urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("Content-Length", String.valueOf(Content.getBytes().length));
                urlConnection.setRequestProperty("User-Agent","Mozilla/5.0 (Linux; U; Android-4.0.3; en-us; Galaxy Nexus Build/IML74K) AppleWebKit/535.7 (KHTML, like Gecko) CrMo/16.0.912.75 Mobile Safari/535.7");
                OutputStream output = urlConnection.getOutputStream();
                output.write(Content.getBytes());
                output.close();
                BufferedReader reader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder builder=new StringBuilder();
                String line;
                while((line=reader.readLine())!=null)
                {
                    builder.append(line);
                }

                return builder.toString();

            }catch (Exception e)
            {
                e.printStackTrace();
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(BuildConfig.DEBUG){
                Log.d("Response",s);}
            if(!s.equalsIgnoreCase("error"))
            {
                ParseOptionsData(s);
                DisplayPager();
            }
            else
            {
                Toast.makeText(PaymentDetailsActivity.this,"Failed to get data",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void ParseOptionsData(String Data)
    {
        try
        {
            JSONObject jobj=new JSONObject(Data);
            JSONArray Opts=jobj.getJSONArray("payOptions");
            for(int i=0;i<Opts.length();i++)
            {
                JSONObject childObj=Opts.getJSONObject(i);
                String CardsList=childObj.getString("cardsList");
                JSONArray childarray=childObj.getJSONArray(CardsList);
                for(int j=0;j<childarray.length();j++)
                {
                    JSONObject CardObj=childarray.getJSONObject(j);
                    NetBanks modelclass=new NetBanks("","","","","","","");
                    if(CardObj.has("cardName"))
                    {
                        modelclass.setCardName(CardObj.getString("cardName"));
                    }
                    else
                    {
                        modelclass.setCardName("");
                    }
                    if(CardObj.has("cardType"))
                    {
                        modelclass.setCardType(CardObj.getString("cardType"));
                    }
                    else
                    {
                        modelclass.setCardType("");
                    }
                    if(CardObj.has("payOptType"))
                    {
                        modelclass.setPayOptType(CardObj.getString("payOptType"));
                    }
                    else
                    {
                        modelclass.setPayOptType("");
                    }
                    if(CardObj.has("payOptDesc"))
                    {
                        modelclass.setPayOptDesc(CardObj.getString("payOptDesc"));
                    }
                    else
                    {
                        modelclass.setPayOptDesc("");
                    }
                    if(CardObj.has("dataAcceptedAt"))
                    {
                        modelclass.setDataAcceptedAt(CardObj.getString("dataAcceptedAt"));
                    }
                    else
                    {
                        modelclass.setDataAcceptedAt("");
                    }
                    if(CardObj.has("status"))
                    {
                        modelclass.setStatus(CardObj.getString("status"));
                    }
                    else
                    {
                        modelclass.setStatus("");
                    }

                    if(i==0)
                    {
                        CCList.add(modelclass);
                    }
                    else if(i==1)
                    {
                        DCList.add(modelclass);
                    }
                    else
                    {
                        BanksList.add(modelclass);
                    }
                }

            }
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public void proceedToPay(String amnt, String code){
        Map<String, String> params = new HashMap<String, String>();
        params.put("authkey", session.getAuthKey());
        params.put("vendercode", code);
        params.put("version", Constants.App_Version);
        params.put("mid", clickpay.getDeviceId(getApplicationContext()));
        params.put("amount", amnt);
        requester.StringRequesterFormValues(urls.payment, Request.Method.POST, className, urls.payment_methodName, params,REQUEST_TAG);
    }

    @Override
    public void onTaskfinished(String response, int cd, String _className, String _methodName) {
        Display.DisplayLogI("ADITYA", "" + response);
        try {
            if (cd == 00) {
                Display.DisplayToast(this, response);
            } else if (cd == 05) {
                if (_className.equalsIgnoreCase(className) && _methodName.equalsIgnoreCase(urls.payment_methodName)) {
                    Gson g = new Gson();
                    PaymentResponseModel model = g.fromJson(response, PaymentResponseModel.class);
                    Display.DisplayLogI("ADITYA code", model.code);
                    if (model.code.equalsIgnoreCase("200")) {
                        session.setWalletBal(model.wallet_bal);
                        Intent i = new Intent(PaymentDetailsActivity.this, PaymentSuccessActivity.class);
                        i.putExtra("fromActivity", "PaymentDetails");
                        i.putExtra("vendorCode", vendorCode);
                        i.putExtra("amount", payingAmount);
                        startActivity(i);
                        finish();
                    } else {
                        Display.DisplayToast(this, model.message);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
