package com.soffice.clickandpay.Services;

import android.content.Intent;
import android.os.IBinder;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by suryaashok.p on 23-04-2016.
 */
public class MyGcmIDListener extends InstanceIDListenerService
{
    public MyGcmIDListener() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        Intent Myservice=new Intent(this,MyRegistrationIntentService.class);
        startService(Myservice);
    }
}
