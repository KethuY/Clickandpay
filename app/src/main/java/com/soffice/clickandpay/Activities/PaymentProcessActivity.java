package com.soffice.clickandpay.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.soffice.clickandpay.BuildConfig;
import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.NetWork.JsonRequester;
import com.soffice.clickandpay.NetWork.TaskListner;
import com.soffice.clickandpay.NetWork.Urls;
import com.soffice.clickandpay.Pojo.Addmoney;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.Utilty.AvenuesParams;
import com.soffice.clickandpay.Utilty.Constants;
import com.soffice.clickandpay.Utilty.Display;
import com.soffice.clickandpay.Utilty.RSAUtility;
import com.soffice.clickandpay.Utilty.ServiceUtility;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.soffice.clickandpay.Utilty.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Surya on 17-04-2016.
 */
public class PaymentProcessActivity extends AppCompatActivity implements View.OnClickListener
{
     ProgressBar Pbar;
     WebView PaymentView;
     Bundle bundleExtras;
     String encVal;
     String Encrypt;
     Button RetryBtn;
     int ErrorCode;
     JsonRequester DataRequester;
    String className;
    SessionManager sessionManager;
    ImageView BackBtn;
     @Override
     protected void onCreate(@Nullable Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_paymentprocess);
          className=getLocalClassName();
         BackBtn= (ImageView) findViewById(R.id.back_IV);
         BackBtn.setOnClickListener(this);
         sessionManager=ClickandPay.getInstance().getSession();
          bundleExtras=getIntent().getExtras();
          Pbar= (ProgressBar) findViewById(R.id.pbar);
          Utils.ApplyPbarColor(this,Pbar);
          PaymentView= (WebView) findViewById(R.id.webview);
          RetryBtn= (Button) findViewById(R.id.retrybtn);
         if(getSupportActionBar()!=null)
         {
             getSupportActionBar().setTitle("Secure Payment");
         }
          GetKey();
     }

    private void GetKey()
     {

         StringRequest MyRequest=new StringRequest(Request.Method.POST, Urls.getrsakey_url, new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {
                 Encrypt=response.trim();
                 if(!ServiceUtility.chkNull(Encrypt).equals("")
                         && ServiceUtility.chkNull(Encrypt).toString().indexOf("ERROR")==-1){
                     String vEncVal=AvenuesParams.CURRENCY+Constants.PARAMETER_EQUALS+bundleExtras.getString(AvenuesParams.CURRENCY)
                                                +Constants.PARAMETER_SEP+AvenuesParams.AMOUNT+Constants.PARAMETER_EQUALS+bundleExtras.getString(AvenuesParams.AMOUNT);
                     encVal = RSAUtility.encrypt(vEncVal, Encrypt);
                 }
                 DisplayPayment();
             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 Encrypt=null;

             }
         })
         {
             @Override
             public String getBodyContentType() {
                 return "application/x-www-form-urlencoded";
             }

             @Override
             protected Map<String, String> getParams() throws AuthFailureError {
                 Map<String,String> params=new HashMap<>();
                 params.put(AvenuesParams.ACCESS_CODE,bundleExtras.getString(AvenuesParams.ACCESS_CODE));
                 params.put(AvenuesParams.ORDER_ID,bundleExtras.getString(AvenuesParams.ORDER_ID));
                 return params;
             }
         };

         MyRequest.setRetryPolicy(new DefaultRetryPolicy(6000,2,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         ClickandPay.getInstance().addToRequestQueue(MyRequest);
     }

    private void DisplayPayment()
    {
        class MyJavaScriptInterface
        {
            @JavascriptInterface
            public void processHTML(String html)
            {
                ClickandPay.getInstance().IsAddMoneyStatus=true;
                int firstIndex=html.indexOf("{");
                int lastIndex=html.lastIndexOf("}");
                String Data=html.substring(firstIndex,lastIndex+1);
                ClickandPay.getInstance().IsAddMoneyStatus=true;
                ClickandPay.getInstance().PaymentStatus=ParseData(Data);
                ClickandPay.getInstance().AddedAmount=bundleExtras.getString(AvenuesParams.AMOUNT);
                Intent intent=new Intent(PaymentProcessActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                PaymentProcessActivity.this.finish();

            }
        }
        try {
            PaymentView.getSettings().setJavaScriptEnabled(true);
            PaymentView.clearCache(true);
            PaymentView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            PaymentView.getSettings().setAppCacheEnabled(false);
            PaymentView.clearFormData();
            PaymentView.clearHistory();
            PaymentView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
            PaymentView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(PaymentView, url);
                    if (url.indexOf("/CcavResponseHandler") != -1) {
                        view.setVisibility(View.GONE);
                        Pbar.setVisibility(View.VISIBLE);

                        PaymentView.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                    }
                    Pbar.setVisibility(View.GONE);
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
                }
            });

			/* An instance of this class will be registered as a JavaScript interface */
            StringBuffer params = new StringBuffer();
            params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_ID, bundleExtras.getString(AvenuesParams.MERCHANT_ID)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.ORDER_ID, bundleExtras.getString(AvenuesParams.ORDER_ID)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.REDIRECT_URL, bundleExtras.getString(AvenuesParams.REDIRECT_URL)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.CANCEL_URL, bundleExtras.getString(AvenuesParams.CANCEL_URL)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.ENC_VAL, URLEncoder.encode(encVal,"UTF-8")));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM4,sessionManager.getCustomerIdentifier()));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM3,sessionManager.getAuthKey()));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_COUNTRY,"India"));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.ACCESS_CODE, bundleExtras.getString(AvenuesParams.ACCESS_CODE)));
            //params.append(ServiceUtility.addToPostParams(AvenuesParams.PAYMENT_OPTION,bundleExtras.getString(AvenuesParams.PAYMENT_OPTION)));
            String vPostParams = params.substring(0, params.length() - 1);
            PaymentView.postUrl(Urls.trans_url,vPostParams.getBytes("UTF-8"));

        }catch (Exception e)
        {
            Display.DisplayToast(PaymentProcessActivity.this, "An error occured. Please try again.");
            e.printStackTrace();
            PaymentProcessActivity.this.finish();
        }
    }

    private boolean ParseData(String html)
    {
        try
        {
            JSONObject jobj=new JSONObject(html);
            String Statuscode=jobj.getString("CODE");
            if(Statuscode.equalsIgnoreCase("200"))
            {
                String status=jobj.getString("status");
                if(status.equalsIgnoreCase("success")) {
                    sessionManager.setWalletBal(jobj.getString("WALLET"));
                    return true;
                }
                return false;
            }
            else{
                return false;
            }
        }catch (JSONException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        DisplayCloseDialog();
    }

    @Override
    public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.retrybtn: if(Utils.CheckInternet(PaymentProcessActivity.this))
                                        {
                                            RetryBtn.setVisibility(View.GONE);
                                            Pbar.setVisibility(View.VISIBLE);
                                            if(ErrorCode==1)
                                            {
                                            }
                                            else
                                            {
                                                GetKey();
                                            }
                                        }
                                        break;

                case R.id.back_IV:DisplayCloseDialog();

            }
    }

    private void DisplayCloseDialog()
    {
        AlertDialog cancelDialog = null;
        AlertDialog.Builder alertbuilder=new AlertDialog.Builder(this);
        alertbuilder.setTitle("Cancel Payment Process");
        alertbuilder.setMessage("Are  you sure you want to cancel the payment process?");
        alertbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PaymentProcessActivity.super.onBackPressed();
            }
        });
        alertbuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        cancelDialog=alertbuilder.create();
        cancelDialog.show();
    }

    /*private String ParseData(String Data)
    {
        try
        {
            JSONObject jobj=new JSONObject(Data);

        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }*/



}
