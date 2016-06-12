package com.soffice.clickandpay.NetWork;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by ADITYA on 12/9/15.
 */
public class ConnectionDetector {
    private Context _context;

    public ConnectionDetector(Context context) {
        this._context = context;
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo currentNetworkInfo = connectivity.getActiveNetworkInfo();
        if (currentNetworkInfo != null && currentNetworkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}