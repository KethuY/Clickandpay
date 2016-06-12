package com.soffice.clickandpay.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.soffice.clickandpay.BuildConfig;
import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.NetWork.JsonRequester;
import com.soffice.clickandpay.NetWork.TaskListner;
import com.soffice.clickandpay.NetWork.Urls;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.soffice.clickandpay.Utilty.Utils;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by suryaashok.p on 23-04-2016.
 */
public class MyRegistrationIntentService extends IntentService implements TaskListner
{

    public static final String GCMINTENTACTION="gcmintentaction";
    JsonRequester DataRequest;
    SessionManager sessionManager;
    public MyRegistrationIntentService()
    {
        super("MyRegistrationIntentService");
    }

    public MyRegistrationIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sessionManager= ClickandPay.getInstance().getSession();
        try {
            InstanceID instanceID=InstanceID.getInstance(this);
            String token=instanceID.getToken(getResources().getString(R.string.SenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE,null);
            sessionManager.setGcmToken(token);
            RegisterInServer(token);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void RegisterInServer(String token)
    {
        Intent intent=new Intent(GCMINTENTACTION);
        try
        {
            if(Utils.CheckInternet(this)) {
                DataRequest=new JsonRequester(this);
                Map<String, String> params = new HashMap<>();
                params.put("mid", ClickandPay.getInstance().getDeviceId(getApplicationContext()));
                params.put("push_id", token);
                DataRequest.StringRequesterFormValues(Urls.push_store_url, Request.Method.POST,this.getClass().getCanonicalName(),Urls.push_store_method,params,"GCM_STORE");

            }
            else
            {

            }
        }catch (Exception e)
        {
            e.printStackTrace();
            intent.putExtra("status",500);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            return;
        }
    }

    @Override
    public void onTaskfinished(String response, int cd, String _className, String _methodName) {
        if(cd==5)
        {
            if(_className.equalsIgnoreCase(this.getClass().getCanonicalName())&&_methodName.equalsIgnoreCase(Urls.push_store_method))
            {
                sessionManager.setIsGcmRegistered(true);
            }
        }
    }
}
