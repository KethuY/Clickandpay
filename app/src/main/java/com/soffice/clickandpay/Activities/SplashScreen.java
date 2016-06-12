package com.soffice.clickandpay.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.Utilty.SessionManager;

/**
 * Created by Aditya Sharma Malladi on 10/2/16.
 */
public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    ClickandPay clickPay;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        clickPay = (ClickandPay) getApplicationContext();
        session = clickPay.getSession();
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if(session.getIsLoggedIn()){
                    if(session.getIsVerfiedUser()) {
                        Intent i = new Intent(SplashScreen.this, PassCodeActivity.class);
                        i.putExtra("fromActivity", "Splash");
                        startActivity(i);
                    }
                    else
                    {
                        Intent i = new Intent(SplashScreen.this, VerificationActivity.class);
                        i.putExtra("fromActivity", "Splash");
                        startActivity(i);
                    }
                }else {
                    if(!session.getDisplayedWalkthrough())
                    {
                        Intent i = new Intent(SplashScreen.this, WalkthroughActivity.class);
                        startActivity(i);
                    }
                    else {
                        Intent i = new Intent(SplashScreen.this, InitialActivity.class);
                        i.putExtra("fromActivity", "Splash");
                        startActivity(i);
                    }
                }
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}