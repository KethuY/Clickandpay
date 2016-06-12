package com.soffice.clickandpay.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.NetWork.JsonRequester;
import com.soffice.clickandpay.NetWork.TaskListner;
import com.soffice.clickandpay.NetWork.Urls;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.UI.RobotoLightTextView;
import com.soffice.clickandpay.Utilty.Display;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.soffice.clickandpay.Utilty.Utils;

import java.util.HashMap;

public class SuggestAStireActivity extends AppCompatActivity implements TaskListner {

    EditText storeName_ET, locality_ET, city_ET, additional_info_ET;
    ClickandPay clickpay;
    SessionManager session;
    JsonRequester requester;
    Urls urls;
    String className;
    ImageView back_IV;
    RobotoLightTextView save_title;
    JsonRequester DataRequester;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_astire);
        coordinatorLayout= (CoordinatorLayout) findViewById(R.id.mycoordinator);
        DataRequester=new JsonRequester(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        clickpay = (ClickandPay) getApplicationContext();
        session = clickpay.getSession();
        className = getLocalClassName();
        requester = new JsonRequester(this);
        urls = clickpay.getUrls();

        storeName_ET = (EditText) findViewById(R.id.storeName_ET);
        locality_ET = (EditText) findViewById(R.id.locality_ET);
        city_ET = (EditText) findViewById(R.id.city_ET);
        additional_info_ET = (EditText) findViewById(R.id.additional_info_ET);
        back_IV = (ImageView) findViewById(R.id.back_IV);
        save_title = (RobotoLightTextView) findViewById(R.id.save_title);
        back_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        storeName_ET.setHint("Store Name");
        locality_ET.setHint("Locality");
        city_ET.setHint("City");

        save_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (storeName_ET.getText().toString().length() > 0) {
                    if (locality_ET.getText().toString().length() > 0) {
                        if (city_ET.getText().toString().length() > 0) {
                            ServerCall();
                        }else{
                            Display.DisplaySnackbar(SuggestAStireActivity.this,coordinatorLayout, "Enter City");
                        }
                    }else{
                        Display.DisplaySnackbar(SuggestAStireActivity.this,coordinatorLayout, "Enter Locality");
                    }
                }else{
                    Display.DisplaySnackbar(SuggestAStireActivity.this,coordinatorLayout, "Enter Store Name");
                }
            }
        });
    }

    private void ServerCall() {
        if (Utils.CheckInternet(this))
        {
            this.save_title.setEnabled(false);
            this.save_title.setTextColor(ContextCompat.getColor(this,R.color.white_disabled));
            HashMap<String,String> localHashMap = new HashMap<>();
            localHashMap.put("authkey", this.session.getAuthKey());
            localHashMap.put("storename", this.storeName_ET.getText().toString());
            localHashMap.put("locality", this.locality_ET.getText().toString());
            localHashMap.put("city", this.city_ET.getText().toString());
            localHashMap.put("Description", this.additional_info_ET.getText().toString());
            this.DataRequester.StringRequesterFormValues(Urls.suggest_a_store_url, 1, getLocalClassName(),Urls.suggest_a_store_method, localHashMap, Urls.suggest_a_store_method);
        }
        else {
            Utils.GenerateSnackbar(this,coordinatorLayout, "Please check your internet connection and try again.");
        }
    }

    @Override
    public void onTaskfinished(String response, int cd, String _className, String _methodName) {
        this.save_title.setEnabled(true);
        this.save_title.setTextColor(Color.WHITE);
        if (cd == 0) {
            Display.DisplaySnackbar(this, this.coordinatorLayout, "Oops.. Something went wrong. Please try again.");
        }
        else if (cd == 5)
        {
            Display.DisplayLogI("ADITYA", "" + response);
            Gson gson=new Gson();
            StoreModel modelClass =gson.fromJson(response, StoreModel.class);
            if (modelClass.Code.equalsIgnoreCase("200"))
            {
                storeName_ET.setText("");
                locality_ET.setText("");
                city_ET.setText("");
                Display.DisplaySnackbar(this, this.coordinatorLayout, modelClass.Message);
            }
            else if (modelClass.Code.equalsIgnoreCase("205"))
            {
                ClickandPay.getInstance().RedirectToLogin();
                Display.DisplaySnackbar(this, this.coordinatorLayout, modelClass.Message);
            }
            else {
                Display.DisplaySnackbar(this, this.coordinatorLayout, modelClass.Message);
            }
        }
    }

    private class StoreModel
    {
        @SerializedName("CODE")
        String Code;
        @SerializedName("MESSAGE")
        String Message;

        private StoreModel() {}
    }

}
