package com.soffice.clickandpay.Services;

import android.app.IntentService;
import android.content.Intent;

import com.soffice.clickandpay.Utilty.Constants;
/**
 * Created by suryaashok.p on 23-04-2016.
 */
public class NotificationService extends IntentService {

    public NotificationService() {
        super(Constants.SOFFICESERVICE);
    }
    public NotificationService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
            GenerateNotification(intent);
    }

    private void GenerateNotification(Intent intent)
    {

    }

}
