package com.soffice.clickandpay.Utilty;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.soffice.clickandpay.R;

/**
 * Created by sys2025 on 13/9/15.
 */
public class Display {
    Context context;

    public Display(Context cont) {
        context = cont;
    }

    public static void DisplayToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    public static void DisplayLogI(String tag , String msg) {
        Log.i(tag, msg);
    }

    public static void DisplayLogD(String tag , String msg) {
        Log.d(tag, msg);
    }

    public static void DisplayLogE(String tag , String msg) {
        Log.e(tag, msg);
    }

    public static void DisplaySnackbar(final Context context, View view, String msg)
    {
        final Snackbar snackbar;
        snackbar=Snackbar.make(view,msg,Snackbar.LENGTH_LONG);
        if(msg.equalsIgnoreCase("Sent OTP Successfully"))
        {
            View mview = snackbar.getView();
            mview.setBackgroundColor(ContextCompat.getColor(context, R.color.green_color));
            TextView tv = (TextView) mview.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
        }
        snackbar.show();
    }
}
