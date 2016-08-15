package com.soffice.clickandpay;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.soffice.clickandpay.Activities.MobileRegistrationActivity;
import com.soffice.clickandpay.NetWork.Urls;
import com.soffice.clickandpay.Utilty.LruBitmapCache;
import com.soffice.clickandpay.Utilty.MyActivityLifeCycle;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.soffice.clickandpay.database.DataBaseManager;
import com.soffice.clickandpay.database.DataCenter;

import java.util.UUID;

/**
 * Created by sys2033 on 7/2/16.
 */
public class ClickandPay extends Application {

    public static final String TAG = ClickandPay.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static ClickandPay mInstance;
    private static String MID;
    private Urls urls;
    private static SessionManager session;
    DataCenter datacenter;
    public boolean IsAddMoneyStatus = false;
    public boolean PaymentStatus = false;
    public String AddedAmount = "";
    public boolean IsActive = true;
    public boolean ShouldDisplayPasscode = false;
    public boolean ShouldUpdateUsername = false;
    Handler RedirectHandler = new Handler();
    Handler sessionHandler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        urls = new Urls();
        new CheckAndCreateDB().execute();
        registerActivityLifecycleCallbacks(new MyActivityLifeCycle());
    }

    public static synchronized ClickandPay getInstance() {
        return mInstance;
    }

    public SessionManager getSession() {
        session = new SessionManager(getApplicationContext());
        return session;
    }

    public DataCenter getDatacenter() {
        if (datacenter != null) {
            return datacenter;
        } else {
            datacenter = new DataCenter(getApplicationContext());
            return datacenter;
        }
    }

    public Urls getUrls() {
        return urls;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public String getDeviceId(Context context) {
        try {
            TelephonyManager phonespecs = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            MID = phonespecs.getDeviceId();
            String macAddr = "", androidId = "";
            if (MID == null) {
                WifiManager wifiMan = (WifiManager) context
                        .getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInf = wifiMan.getConnectionInfo();
                macAddr = wifiInf.getMacAddress();
                androidId = ""
                        + android.provider.Settings.Secure.getString(
                        context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
                UUID deviceUuid = new UUID(androidId.hashCode(),
                        macAddr.hashCode());
                MID = deviceUuid.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        if (getSession() != null) {
        if (getSession().getDeviceId() != null) {
            return getSession().getDeviceId();
        } else {
            getSession().setDeviceId(MID);
            return getSession().getDeviceId();
        }
//        } else {
//
//        }
//        return MID;
    }

    public class CheckAndCreateDB extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            datacenter = new DataCenter(getApplicationContext());
            DataBaseManager.initializeInstance(datacenter);

            return null;
        }
    }

    public void StartTimerSession() {
        ShouldDisplayPasscode = false;
        sessionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (IsActive) {
                    sessionHandler.postDelayed(this, 1 * 60 * 1000);
                } else {
                    ShouldDisplayPasscode = true;
                }
            }
        }, 2 * 60 * 1000);
    }

    public void RedirectToLogin() {
        session.ClearAll();
        final Intent localIntent = new Intent(this, MobileRegistrationActivity.class);
        localIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.RedirectHandler.postDelayed(new Runnable() {
            public void run() {
                ClickandPay.this.startActivity(localIntent);
            }
        }, 4000);
    }


}