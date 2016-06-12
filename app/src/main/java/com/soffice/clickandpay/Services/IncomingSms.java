package com.soffice.clickandpay.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.soffice.clickandpay.Activities.ForgetPassWordOrPassCodeActivity;
import com.soffice.clickandpay.Activities.VerificationActivity;


/**
 * Created by suryaashok.p on 23-04-2016.
 */
public class IncomingSms extends BroadcastReceiver
{
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    if(senderNum.contains("CLKPAY")) {

                        String pos=message.replaceAll("\\D+","");
                        String substring=pos.substring(0,6);
                        if(VerificationActivity.otp_EditText!=null)
                        {
                            VerificationActivity.otp_EditText.setText(substring);
                            if(VerificationActivity.countdowndialog!=null)
                            {
                                VerificationActivity.countdowndialog.dismiss();
                            }
                        }
                        else if(ForgetPassWordOrPassCodeActivity.OTPEt!=null)
                        {
                            ForgetPassWordOrPassCodeActivity.OTPEt.setText(substring);
                        }
                    }
                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }
}
