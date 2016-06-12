package com.soffice.clickandpay.Utilty;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.soffice.clickandpay.ClickandPay;

/**
 * Created by Surya on 04-05-2016.
 */
public class MyActivityLifeCycle implements Application.ActivityLifecycleCallbacks {
    String CurrentActivity;
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        CurrentActivity=activity.getLocalClassName();
        ClickandPay.getInstance().IsActive=true;
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {
        if(activity.getLocalClassName().equalsIgnoreCase(CurrentActivity))
        {
            ClickandPay.getInstance().IsActive=false;
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
