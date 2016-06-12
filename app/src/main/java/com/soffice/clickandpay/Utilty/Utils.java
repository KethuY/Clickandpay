package com.soffice.clickandpay.Utilty;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scottyab.aescrypt.AESCrypt;
import com.soffice.clickandpay.R;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Surya on 16-04-2016.
 */
public class Utils
{

    public static final String[] nav_set_one_titles={"Pay at Store","Add money","Send money","Request money","Transaction history"};
    public static final int[] nav_set_one_icons={R.drawable.nav_pay_at_home,R.drawable.nav_add_money,R.drawable.nav_send_money,R.drawable.nav_receive_money,R.drawable.nav_transaction_history};
    public static final String[] nav_set_two_titles={"Near Me","Offers"};
    public static final int[] nav_set_two_icons={R.drawable.nav_nearme,R.drawable.nav_offers,R.mipmap.passes};
    public static final String[] nav_set_three_titles={"Support","About us"};
    public static final int[] nav_set_three_icons={R.mipmap.ic_7_add_referral,R.mipmap.support,R.mipmap.support};
    private static String EncryptKey="2016050921261826";
    public static boolean CheckInternet(Context context)
    {
        if(!isAirplaneModeOn(context)) {
            // Get Connectivity Manager class object from Systems Service
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            // Get Network Info from connectivity Manager
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            // if no network is available networkInfo will be null
            // otherwise check if we are connected
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
            return false;

        }
        else
        {
            return false;
        }
    }
    private static boolean isAirplaneModeOn(Context context) {

        return Settings.System.getInt(context.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) != 0;
    }

    public static void GenerateSnackbar(final Context context, View view, String msg)
    {
        final Snackbar snackbar;
        snackbar=Snackbar.make(view,msg,Snackbar.LENGTH_LONG);
        if(msg.equalsIgnoreCase("Sent OTP Successfully"))
        {
            View mview = snackbar.getView();
            mview.setBackgroundColor(ContextCompat.getColor(context,R.color.green_color));
            TextView tv = (TextView) mview.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
        }
        snackbar.show();
    }

    public static void setButtonTint(Button button, ColorStateList tint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && button instanceof AppCompatButton) {
            ((AppCompatButton) button).setSupportBackgroundTintList(tint);
            ((AppCompatButton) button).setSupportBackgroundTintMode(PorterDuff.Mode.SRC_IN);
        } else {
            ViewCompat.setBackgroundTintList(button, tint);
        }
    }



    public static void ApplyPbarColor(Context context,ProgressBar Pbar)
    {
        if(Build.VERSION.SDK_INT<21)
        {
            Pbar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }
    }

    public static double CalculateDistance(Double lat1,Double lng1,Double lat2,Double lng2)
    {
        double theta=lng1-lng2;
        double dist=Math.sin(deg2rad(lat1))*Math.sin(deg2rad(lat2))+Math.cos(deg2rad(lat1))*Math.cos(deg2rad(lat2))*Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        dist=(double) Math.round(dist*100.0)/100.0;
        return dist;
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts decimal degrees to radians						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts radians to decimal degrees						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public static String EncryptData(String data) {
        /*try {
            byte[] keybytes = EncryptKey.getBytes();
            byte[] mData = data.getBytes();
            SecretKeySpec  myKey=new SecretKeySpec(keybytes,"AES");
            Cipher mChiper=Cipher.getInstance("AES/CBC/NoPadding");
            mChiper.init(Cipher.ENCRYPT_MODE,myKey);
            return mChiper.doFinal(mData);
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }*/

     /*   try {
            String encryptedMsg = AESCrypt.encrypt(data, EncryptKey);
            return encryptedMsg;
        } catch (GeneralSecurityException e) {
            //handle error
        }*/
        try
        {
            data=data + EncryptKey;
            data=data.trim();
            data = Base64.encodeToString(data.getBytes("UTF-8"), Base64.DEFAULT);
            return data;
        }
        catch (UnsupportedEncodingException paramString)
        {
            paramString.printStackTrace();
        }
        return null;
    }
}
