package com.soffice.clickandpay.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
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


public class HelpUsToImproveActivity extends AppCompatActivity implements TaskListner
{

    ImageView back_IV;
    RobotoLightTextView save_title;
    EditText content_word;
    SessionManager sessionManager;
    JsonRequester DataRequester;
    CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_us_to_improve);
        coordinatorLayout= (CoordinatorLayout) findViewById(R.id.mycoordinator);
        sessionManager= ClickandPay.getInstance().getSession();
        DataRequester=new JsonRequester(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        back_IV = (ImageView) findViewById(R.id.back_IV);
        save_title = (RobotoLightTextView) findViewById(R.id.save_title);
        content_word = (EditText) findViewById(R.id.content_word);

        back_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(content_word.getText().toString().length() > 0){
                    ServerCall();

                }else{
                    Display.DisplaySnackbar(HelpUsToImproveActivity.this,coordinatorLayout, "Please enter your suggestions");
                }
            }
        });
    }

    private void ServerCall()
    {
        if (Utils.CheckInternet(this))
        {
            this.save_title.setEnabled(false);
            this.save_title.setTextColor(ContextCompat.getColor(this, R.color.white_disabled));
            HashMap<String,String> localHashMap = new HashMap();
            localHashMap.put("authkey", this.sessionManager.getAuthKey());
            localHashMap.put("help", this.content_word.getText().toString());
            this.DataRequester.StringRequesterFormValues(Urls.help_us_improve_url, Request.Method.POST, getLocalClassName(),Urls.help_us_improve_method, localHashMap, Urls.help_us_improve_method);
        }
        else {
            Utils.GenerateSnackbar(this, coordinatorLayout, "Please check your internet connection and try again.");
        }
    }

    @Override
    public void onTaskfinished(String response, int cd, String _className, String _methodName) {
        save_title.setEnabled(true);
        save_title.setTextColor(Color.WHITE);
        if(cd==0)
        {
            Display.DisplaySnackbar(this,coordinatorLayout,"Oops.. Something went wrong. Please try again.");
        }
        else if(cd==5&&_className.equalsIgnoreCase(getLocalClassName())&&_methodName.equalsIgnoreCase(Urls.help_us_improve_method))
        {
            Gson gson=new Gson();
            StoreModel model=gson.fromJson(response,StoreModel.class);
            if(model.Code.equalsIgnoreCase("200"))
            {
                content_word.setText("");
                Display.DisplaySnackbar(this,coordinatorLayout,model.Message);
            }
            else if(model.Code.equalsIgnoreCase("205"))
            {
                ClickandPay.getInstance().RedirectToLogin();
                Display.DisplaySnackbar(this,coordinatorLayout,model.Message);
            }
            else
            {
                Display.DisplaySnackbar(this,coordinatorLayout,model.Message);
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
