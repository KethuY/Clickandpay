package com.soffice.clickandpay.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.NetWork.JsonRequester;
import com.soffice.clickandpay.NetWork.TaskListner;
import com.soffice.clickandpay.NetWork.Urls;
import com.soffice.clickandpay.Pojo.RegistrationResponseModel;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.Utilty.AnimUtil;
import com.soffice.clickandpay.Utilty.Constants;
import com.soffice.clickandpay.Utilty.Display;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.soffice.clickandpay.Utilty.Utils;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

/**
 * Created by admin on 6/14/2016.
 */
public class MobileRegistrationActivity extends AppCompatActivity implements TaskListner {

    TextView key1, key2, key3, key4, key5, key6, key7, key8, key9, key0, tv1;

    TextView btn[] = new TextView[12];
    ImageView backspace, done, back_IV;

    EditText userInput, signupMobileNumberET, appended;
    private CoordinatorLayout coordinatorLayout;
    private JsonRequester requester;
    private String className, methodName, Dob;
    private Urls urls;
    private SessionManager session;
    private ClickandPay clickpay;
    private final String REQUEST_TAG = "signup_tag";
    private AnimUtil utils;
    private SpotsDialog spotsDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_mobile);
        utils = new AnimUtil();
        userInput = (EditText) findViewById(R.id.signupMobileNumberET);
        appended = (EditText) findViewById(R.id.appended);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.mycoordinator);
        className = getLocalClassName();
        requester = new JsonRequester(this);
        clickpay = (ClickandPay) getApplicationContext();
        session = clickpay.getSession();
        urls = clickpay.getUrls();
        userInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideDefaultKeyboard();

                return true;
            }
        });
        appended.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideDefaultKeyboard();

                return true;
            }
        });

//        CustomKeyboard mCustomKeyboard1 = new CustomKeyboard(this,
//                R.id.keyboardview1, R.layout.custom_keypad);
//        mCustomKeyboard1.registerEditText(R.id.signupMobileNumberET);

        key1 = (TextView) findViewById(R.id.key1);
        key2 = (TextView) findViewById(R.id.key2);
        key3 = (TextView) findViewById(R.id.key3);
        key4 = (TextView) findViewById(R.id.key4);
        key5 = (TextView) findViewById(R.id.key5);
        key6 = (TextView) findViewById(R.id.key6);
        key7 = (TextView) findViewById(R.id.key7);
        key8 = (TextView) findViewById(R.id.key8);
        key9 = (TextView) findViewById(R.id.key9);
        key0 = (TextView) findViewById(R.id.key0);
        back_IV = (ImageView) findViewById(R.id.back_IV);

        btn[0] = (TextView) findViewById(R.id.key1);
        btn[1] = (TextView) findViewById(R.id.key2);
        btn[2] = (TextView) findViewById(R.id.key3);
        btn[3] = (TextView) findViewById(R.id.key3);
        btn[4] = (TextView) findViewById(R.id.key4);
        btn[5] = (TextView) findViewById(R.id.key5);
        btn[6] = (TextView) findViewById(R.id.key6);
        btn[7] = (TextView) findViewById(R.id.key7);
        btn[8] = (TextView) findViewById(R.id.key8);
        btn[9] = (TextView) findViewById(R.id.key9);
        btn[10] = (TextView) findViewById(R.id.key0);

        backspace = (ImageView) findViewById(R.id.backSpace);
        done = (ImageView) findViewById(R.id.done);

        for (int i = 0; i < 11; i++) {
            btn[i].setOnClickListener(clickListener);
        }

        backspace.setOnClickListener(clickListener);
        done.setOnClickListener(clickListener);

    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(MobileRegistrationActivity.this, InitialActivity.class);
        i.putExtra("fromActivity", "Mobile");
        startActivity(i);
        super.onBackPressed();
    }

    private void hideDefaultKeyboard() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.key1:
                    addtoarray("1");
                    break;
                case R.id.key2:
                    addtoarray("2");
                    break;
                case R.id.key3:
                    addtoarray("3");
                    break;
                case R.id.key4:
                    addtoarray("4");
                    break;
                case R.id.key5:
                    addtoarray("5");
                    break;
                case R.id.key6:
                    addtoarray("6");
                    break;
                case R.id.key7:
                    addtoarray("7");
                    break;
                case R.id.key8:
                    addtoarray("8");
                    break;
                case R.id.key9:
                    addtoarray("9");
                    break;
                case R.id.key0:
                    addtoarray("0");
                    break;
                case R.id.done:
                    validationsOnMobileNumber(userInput.getText().toString().trim());
                    break;
                case R.id.backSpace:
                    //get the length of input
                    int slength = userInput.length();
                    if (slength > 0) {
                        //get the last character of the input
                        String selection = userInput.getText().toString().substring(0,slength - 1);
                        Log.e("Selection", selection);

                       // String result = userInput.getText().toString().replace(selection, "");
                        //Log.e("Result", result);

                        userInput.setText(selection);
                        userInput.setSelection(userInput.getText().length());
                        if (userInput.getText().toString().length() == 10) {
                            done.setImageResource(R.drawable.yes_icon_hover);
                        } else {
                            done.setImageResource(R.drawable.yes_icon_normal);

                        }


                    }
                    break;


            }

        }
    };

    public void addtoarray(String numbers) {
        //register TextBox
        userInput.append(numbers);
        userInput.requestFocus();
        userInput.setFocusable(true);
        if (userInput.getText().toString().length() == 10) {
            done.setImageResource(R.drawable.yes_icon_hover);
        } else {
            done.setImageResource(R.drawable.yes_icon_normal);

        }

    }

    private void validationsOnMobileNumber(String mobile) {
        if (mobile != null && mobile.length() == 10) {

            if (mobile.charAt(0) == '7' || mobile.charAt(0) == '8' || mobile.charAt(0) == '9') {
                if (Build.VERSION.SDK_INT >= 23) {
                    CheckPermissions();
                } else {
                    ServerCall();
                }

            } else {
                Display.DisplaySnackbar(MobileRegistrationActivity.this, coordinatorLayout, "Please enter valid mobile number starts with 7 / 8 / 9");
            }
        } else {
            Display.DisplaySnackbar(MobileRegistrationActivity.this, coordinatorLayout, "Please enter your Mobile Number");
        }

    }


    /**
     * Need to check for Android Version 6.0 and Above (RUN TIME PERMISSION CHECK)
     **/

    private void CheckPermissions() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.READ_SMS") != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.RECEIVE_SMS") != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(this, "android.permission.READ_PHONE_STATE") != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_SMS")) || (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.RECEIVE_SMS")) || (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_PHONE_STATE"))) {
                Snackbar localSnackbar = Snackbar.make(this.coordinatorLayout, "App requires an additional permission to complete the registration process", Snackbar.LENGTH_LONG);
                localSnackbar.setAction("Grant", new View.OnClickListener() {
                    public void onClick(View paramAnonymousView) {
                        ActivityCompat.requestPermissions(MobileRegistrationActivity.this, new String[]{"android.permission.READ_SMS", "android.permission.RECEIVE_SMS", "android.permission.READ_PHONE_STATE"}, 535);
                    }
                });
                localSnackbar.setActionTextColor(Color.WHITE);
                localSnackbar.show();
                return;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_SMS", "android.permission.RECEIVE_SMS", "android.permission.READ_PHONE_STATE"}, 535);
            }
        } else {
            ServerCall();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 535) {
            if (permissions.length == 3) {
                if (permissions[0].equals(Manifest.permission.READ_SMS) && grantResults[0] == PackageManager.PERMISSION_GRANTED && (permissions[1].equals("android.permission.RECEIVE_SMS")) && (grantResults[1] == PackageManager.PERMISSION_GRANTED) && (permissions[2].equals("android.permission.READ_PHONE_STATE")) && (grantResults[2] == PackageManager.PERMISSION_GRANTED)) {
                    ServerCall();
                } else {
                    Display.DisplaySnackbar(this, coordinatorLayout, "Unable to complete Sign Up process.");
                }
            } else {
                Display.DisplaySnackbar(this, coordinatorLayout, "Unable to complete Sign Up process.");
            }
        }
    }


    private void ServerCall() {
        if (Utils.CheckInternet(this)) {
            utils.showProgressDialog(MobileRegistrationActivity.this, "Please wait");
            Map<String, String> params = new HashMap<String, String>();
            params.put("version", Constants.App_Version);
            params.put("mobile", userInput.getText().toString().trim());
            params.put("devicetype", "1");
            params.put("mid", clickpay.getDeviceId(getApplicationContext()));
            methodName = urls.userRegister_methodName;
            requester.StringRequesterFormValues(urls.userRegister, Request.Method.POST, className, methodName, params, REQUEST_TAG);
        } else {
            Display.DisplaySnackbar(this, coordinatorLayout, "Please check your internet connection and try again");
        }
    }


    @Override
    public void onTaskfinished(String response, int cd, String _className, String _methodName) {
        Display.DisplayLogI("ADITYA", "" + response);
        try {
            utils.dismissProgressDialog(spotsDialog);
            if (cd == 00) {
                Display.DisplaySnackbar(this, coordinatorLayout, "Failed to complete the registration process. Please try again");
            } else if (cd == 05) {
                if (_className.equalsIgnoreCase(className) && _methodName.equalsIgnoreCase(urls.userRegister_methodName)) {
                    Gson g = new Gson();
                    RegistrationResponseModel model = g.fromJson(response, RegistrationResponseModel.class);
                    Display.DisplayLogI("ADITYA code", model.code);
                    if (model.code.equalsIgnoreCase("200")) {
                        session.setAuthKey(model.key);
                        session.setMobileNum(userInput.getText().toString());
                        Intent i = new Intent(MobileRegistrationActivity.this, VerificationActivity.class);
                        i.putExtra("VERIFY_CODE", model.OTP);
                        i.putExtra("fromActivity", "Mobile");
                        startActivity(i);
                        finish();
                    } else {
                        Display.DisplaySnackbar(MobileRegistrationActivity.this, coordinatorLayout, model.message);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
