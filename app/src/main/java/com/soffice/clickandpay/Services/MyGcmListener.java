package com.soffice.clickandpay.Services;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;


/**
 * Created by suryaashok.p on 23-04-2016.
 */
public class MyGcmListener extends GcmListenerService
{

    public MyGcmListener() {
        super();
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        CreatePushNotification(data);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageSent(String msgId) {
        super.onMessageSent(msgId);
    }

    @Override
    public void onSendError(String msgId, String error) {
        super.onSendError(msgId, error);
    }

    public void CreatePushNotification(Bundle bundle)
    {
       /* if(!AppUtils.Isrunning)
        {
            Intent CallerIntent=new Intent(this,CallReceiverActivity.class);
            CallerIntent.putExtra("conference_id", bundle.getString("a_tag"));
            CallerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            CallerIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(CallerIntent);
        }*/

        /*NotificationManager mnotificationmanager=(NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent pendintent = null;
        NotificationCompat.Builder mnotificationbuilder;

        if(bundle.containsKey("a_tag")) {
            pendintent=new Intent(this,MainActivity.class);
            pendintent.putExtra("conference_id", bundle.getString("a_tag"));
            pendintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        PendingIntent pendingintent=PendingIntent.getActivity(this,0,pendintent,PendingIntent.FLAG_ONE_SHOT);

        mnotificationbuilder=new NotificationCompat.Builder(this)
                .setContentText(bundle.getString("a_title"))
                .setContentTitle("Virinchi")
                .setAutoCancel(true)
                .setContentIntent(pendingintent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL);

        //mnotificationbuilder.setColor(this.getResources().getColor(R.color.red_color));

        Bitmap icon= BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);

        mnotificationbuilder.setSmallIcon(R.mipmap.ic_launcher);

        mnotificationmanager.notify(0, mnotificationbuilder.build());*/
    }

    /*public void ActivityRunState(Context context)
    {
        ActivityManager activityManager= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> info=activityManager.get
    }*/



}
