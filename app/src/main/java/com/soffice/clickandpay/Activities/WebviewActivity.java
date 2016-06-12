package com.soffice.clickandpay.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.soffice.clickandpay.R;
import com.soffice.clickandpay.Utilty.AvenuesParams;
import com.soffice.clickandpay.Utilty.Constants;
import com.soffice.clickandpay.Utilty.RSAUtility;
import com.soffice.clickandpay.Utilty.ServiceUtility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Surya on 14-04-2016.
 */
public class WebviewActivity extends AppCompatActivity
{
    WebView webview;
    Intent mainIntent;
    String Encrypteddata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webview= (WebView) findViewById(R.id.webview);
        try {
            TransactionCheck();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void TransactionCheck() throws UnsupportedEncodingException {
        //Encrypting Card  Details

        StringBuffer data=new StringBuffer("");
        data.append(ServiceUtility.addToPostParams(AvenuesParams.CVV, "123"));
        data.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT,"10.00"));
        data.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY,"INR"));
        data.append(ServiceUtility.addToPostParams(AvenuesParams.CARD_NAME,"Test Name"));
        data.append(ServiceUtility.addToPostParams(AvenuesParams.CARD_NUMBER,"123456789123"));
        data.append(ServiceUtility.addToPostParams(AvenuesParams.CUSTOMER_IDENTIFIER,"stg"));
        data.append(ServiceUtility.addToPostParams(AvenuesParams.EXPIRY_YEAR, "2019"));
        data.append(ServiceUtility.addToPostParams(AvenuesParams.EXPIRY_MONTH, "05"));
        Encrypteddata=RSAUtility.encrypt(data.substring(0,data.length()-1), "06DFA989BB31723CFD729C59230FBF98");
        class MyJavaScriptInterface
        {
            @JavascriptInterface
            public void processHTML(String html)
            {
                // process the html as needed by the app
                String status = null;
                if(html.indexOf("Failure")!=-1){
                    status = "Transaction Declined!";
                }else if(html.indexOf("Success")!=-1){
                    status = "Transaction Successful!";
                }else if(html.indexOf("Aborted")!=-1){
                    status = "Transaction Cancelled!";
                }else{
                    status = "Status Not Known!";
                }
                //Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),StatusActivity.class);
                intent.putExtra("transStatus", status);
                startActivity(intent);
            }
        }

        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(webview, url);
                if (url.indexOf("/ccavResponseHandler.jsp") != -1) {
                    webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });

        StringBuffer params = new StringBuffer();
        params.append(ServiceUtility.addToPostParams(AvenuesParams.ACCESS_CODE,"AVWV64DD72AH97VWHA"));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_ID,"92446"));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.ORDER_ID,"1"));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.REDIRECT_URL,""));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.CANCEL_URL,""));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.LANGUAGE,"EN"));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_NAME,"test"));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_ADDRESS,"address"));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_CITY,"hyderabad"));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_STATE,"telangana"));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_ZIP,"500075"));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_COUNTRY,"India"));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_TEL,"9999999999"));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_EMAIL,"anonymous@gmail.com"));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_NAME,"test"));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_ADDRESS,"address"));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_CITY,"hyderabad"));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_STATE,"telangana"));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_ZIP,"500075"));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_COUNTRY,"India"));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_TEL,"9999999999"));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM1,"additional Info."));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM2,"additional Info."));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM3,"additional Info."));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM4,"additional Info."));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.PAYMENT_OPTION,"OPTCRDC"));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.CARD_TYPE,"CRDC"));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.CARD_NAME,"Amex"));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.DATA_ACCEPTED_AT,"CCAvenue"));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.ISSUING_BANK,""));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.ENC_VAL, URLEncoder.encode(Encrypteddata, "UTF-8")));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.EMI_PLAN_ID,"0"));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.EMI_TENURE_ID,"0"));
//        if(mainIntent.getStringExtra(AvenuesParams.SAVE_CARD)!=null)
//            params.append(ServiceUtility.addToPostParams(AvenuesParams.SAVE_CARD,mainIntent.getStringExtra(AvenuesParams.SAVE_CARD)));

        String vPostParams = params.substring(0,params.length()-1);
        try {
            webview.postUrl(Constants.TRANS_URL, vPostParams.getBytes("UTF-8"));
        } catch (Exception e) {
            Toast.makeText(this,"Exception occured while opening webview.",Toast.LENGTH_SHORT).show();
        }

    }


}
