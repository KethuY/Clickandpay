package com.soffice.clickandpay.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.soffice.clickandpay.BuildConfig;
import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.NetWork.JsonRequester;
import com.soffice.clickandpay.NetWork.TaskListner;
import com.soffice.clickandpay.NetWork.Urls;
import com.soffice.clickandpay.Pojo.VerificationResponseModel;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.Utilty.AnimUtil;
import com.soffice.clickandpay.Utilty.Display;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.soffice.clickandpay.Utilty.Utils;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class VerificationActivity extends AppCompatActivity implements TaskListner {

    //CardView getStartedLayout;
    public static EditText otp_EditText;
    public static Dialog countdowndialog;
    TextView resend_TV, why_verify_tv, phn_TV;
    String otp_code, className;
    ImageView back_IV, ok_IV;
    ClickandPay clickpay;
    SessionManager session;
    JsonRequester requester;
    Urls urls;
    ProgressBar Pbar;
    CoordinatorLayout coordinator;
    private final String REQUEST_TAG = "verification_request";
    Handler handler = new Handler();
    Runnable myRunnable;
    int countdown;
    int ResendCounter = 0;
    AppCompatButton CallmeBtn;
    TextView key1, key2, key3, key4, key5, key6, key7, key8, key9, key0, tv1;

    TextView btn[] = new TextView[12];
    ImageView backspace, done;
    private SpotsDialog spotsDialog;
    private AnimUtil utils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        coordinator = (CoordinatorLayout) findViewById(R.id.mycoordinator);
        Pbar = (ProgressBar) findViewById(R.id.pbar);
        utils = new AnimUtil();
        Pbar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        /*CallmeBtn= (AppCompatButton) findViewById(R.id.callme);
        Utils.setButtonTint(CallmeBtn,ContextCompat.getColorStateList(this,R.color.button_tint_white));
        CallmeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:7396891206"));
                startActivity(intent);
            }
        });*/
        clickpay = (ClickandPay) getApplicationContext();
        session = clickpay.getSession();
        className = getLocalClassName();
        requester = new JsonRequester(this);
        urls = clickpay.getUrls();

        //getStartedLayout = (CardView) findViewById(R.id.getStartedLayout);
        otp_EditText = (EditText) findViewById(R.id.otp_EditText);
        resend_TV = (TextView) findViewById(R.id.resend_TV);
        resend_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((VerificationActivity.this.resend_TV.isEnabled()) && (VerificationActivity.this.ResendCounter <= 2)) {
                    ResendOTP();
                }
            }
        });
        why_verify_tv = (TextView) findViewById(R.id.why_verify_tv);
        back_IV = (ImageView) findViewById(R.id.back_IV);
        ok_IV = (ImageView) findViewById(R.id.ok_IV);
        phn_TV = (TextView) findViewById(R.id.phn_TV);


        phn_TV.setText(session.getMobileNum());

        /*getStartedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/


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
        backspace.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int i;
                for (i = otp_EditText.length(); i >= 0; i--) {
                    backspace.performClick();
                }
                return true;
            }
        });
        done.setOnClickListener(clickListener);
        otp_EditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideDefaultKeyboard();

                return true;
            }
        });
        otp_EditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });

        otp_EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Display.DisplayLogI("ADITYA", "onTextChanged onTextChanged " + count);
                otp_code = otp_EditText.getText().toString();
                if (otp_code.length() == 6) {
                    done.setImageResource(R.drawable.yes_icon_hover);
                } else {
                    done.setImageResource(R.drawable.yes_icon_normal);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        back_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getStringExtra("fromActivity") != null && getIntent().getStringExtra("fromActivity").equalsIgnoreCase("Mobile")) {
                    Intent i = new Intent(VerificationActivity.this, MobileRegistrationActivity.class);
                    i.putExtra("fromActivity", "Verification");
                    startActivity(i);
                    finish();
                } else {
                    finish();
                }
            }
        });

        ok_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.CheckInternet(VerificationActivity.this)) {
                    spotsDialog = utils.showProgressDialog(VerificationActivity.this, "Please wait");

                    otp_code = otp_EditText.getText().toString();
                    if (otp_code.length() > 0) {
                        if (BuildConfig.DEBUG) {
                            Display.DisplayLogI("ADITYA", "AUTH KEY " + session.getAuthKey());
                        }
//                        ok_IV.setVisibility(View.GONE);
//                        Pbar.setVisibility(View.VISIBLE);
                        if (session.getAuthKey() != null && session.getAuthKey().length() > 0) {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("authkey", session.getAuthKey());
                            params.put("otp", otp_code);
                            params.put("devicetype", "1");
                            params.put("mid", clickpay.getDeviceId(getApplicationContext()));
                            requester.StringRequesterFormValues(urls.otpVerification, Request.Method.POST, className, urls.otpVerification_methodName, params, REQUEST_TAG);
                        }
                    } else {
                        Display.DisplaySnackbar(VerificationActivity.this, coordinator, "Invalid OTP Code. Please enter a valid OTP Code");
                    }
                } else {
                    Display.DisplaySnackbar(VerificationActivity.this, coordinator, "Please check your internet connection and try again.");
                }
            }
        });

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        if (!getIntent().getStringExtra("fromActivity").equalsIgnoreCase("Splash")) {
            DisplayCountDown();
        }
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
                    ok_IV.performClick();
                    break;
                case R.id.backSpace:
                    //get the length of input
                    int slength = otp_EditText.length();
                    if (slength > 0) {
                        //get the last character of the input
                        String selection = otp_EditText.getText().toString().substring(0, slength - 1);
                        Log.e("Selection", selection);

//                        String result = otp_EditText.getText().toString().replace(selection, "");
//                        Log.e("Result", result);

                        otp_EditText.setText(selection);
                        otp_EditText.setSelection(otp_EditText.getText().length());
                        if (otp_EditText.getText().toString().length() == 6) {
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
        otp_EditText.append(numbers);
        otp_EditText.requestFocus();
        otp_EditText.setFocusable(true);
        if (otp_EditText.getText().toString().length() == 6) {
            done.setImageResource(R.drawable.yes_icon_hover);
        } else {
            done.setImageResource(R.drawable.yes_icon_normal);

        }

    }

    private void ResendOTP() {
        if (Utils.CheckInternet(this)) {
            Map<String, String> params = new HashMap<>();
            params.put("authkey", session.getAuthKey());
            requester.StringRequesterFormValues(Urls.resend_OTP, Request.Method.POST, className, Urls.resend_OTP_Method, params, "RESNED_OTP");
            resend_TV.setEnabled(false);
            resend_TV.setTextColor(ContextCompat.getColor(this, R.color.lighter_gray));
            ResendCounter += 1;
        } else {
            Display.DisplaySnackbar(this, coordinator, "Please check your internet connection and try again");
        }
    }

    private void hideDefaultKeyboard() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(VerificationActivity.this, MobileRegistrationActivity.class);
        i.putExtra("fromActivity", "Login");
        startActivity(i);
        finish();
    }


    @Override
    public void onTaskfinished(String response, int cd, String _className, String _methodName) {
        Display.DisplayLogI("ADITYA", "" + response);
        try {
            utils.dismissProgressDialog(spotsDialog);

            if (cd == 00) {
                Display.DisplaySnackbar(VerificationActivity.this, coordinator, "Failed to verify. Please try again.");
                if (ok_IV.getVisibility() == View.GONE) {
                    Pbar.setVisibility(View.GONE);
                    //ok_IV.setVisibility(View.VISIBLE);
                }

            } else if (cd == 05) {
                if (_className.equalsIgnoreCase(className) && _methodName.equalsIgnoreCase(urls.otpVerification_methodName)) {
                    Gson g = new Gson();
                    VerificationResponseModel model = g.fromJson(response, VerificationResponseModel.class);
                    Display.DisplayLogI("ADITYA code", model.code);
                    if (model.code.equalsIgnoreCase("200")) {
                        session.setAuthKey(model.key);
                        session.setIsVerifiedUser(true);
                        Intent i = new Intent(VerificationActivity.this, PassCodeActivity.class);
                        i.putExtra("fromActivity", "Verification");
                        i.putExtra("user_status", model.userStatus);
                        startActivity(i);
                        finish();
                    } else {
//                        if (ok_IV.getVisibility() == View.GONE) {
//                            Pbar.setVisibility(View.GONE);
//                            ok_IV.setVisibility(View.VISIBLE);
//                        }
                        Display.DisplaySnackbar(this, coordinator, model.message);
                    }
                } else if (_className.equalsIgnoreCase(className) && _methodName.equalsIgnoreCase(Urls.resend_OTP_Method)) {
                    Gson g = new Gson();
                    VerificationResponseModel model = g.fromJson(response, VerificationResponseModel.class);
                    Display.DisplayLogI("ADITYA code", model.code);
                    if (model.code.equalsIgnoreCase("200")) {
                        Display.DisplaySnackbar(this, coordinator, model.message);
                        if (this.ResendCounter == 2) {
                            this.resend_TV.setEnabled(false);
                            this.resend_TV.setTextColor(ContextCompat.getColor(this, R.color.lighter_gray));
                        } else {
                            this.resend_TV.setEnabled(true);
                            this.resend_TV.setTextColor(Color.WHITE);
                        }

                        DisplayCountDown();
                    } else if (model.code.equalsIgnoreCase("205")) {
                        ClickandPay.getInstance().RedirectToLogin();
                        Display.DisplaySnackbar(this, this.coordinator, model.message);
                    } else {
                        Display.DisplaySnackbar(this, coordinator, model.message);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void DisplayCountDown() {
        this.countdown = 30;
        countdowndialog = new Dialog(this, R.style.ResetTheme);
        countdowndialog.setContentView(R.layout.dialog_countdown);
        countdowndialog.setCancelable(false);
        TextView cancel = (TextView) countdowndialog.findViewById(R.id.countdown_cancel_tv);
        final TextView countdown = (TextView) countdowndialog.findViewById(R.id.countdown_timer_tv);
        countdown.setText("(00:" + this.countdown + ")");
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                VerificationActivity.countdowndialog.dismiss();
                VerificationActivity.countdowndialog = null;
            }
        });
        this.myRunnable = new Runnable() {
            public void run() {
                if (VerificationActivity.this.countdown == 0) {
                    if ((VerificationActivity.countdowndialog != null) && (VerificationActivity.countdowndialog.isShowing())) {
                        VerificationActivity.countdowndialog.dismiss();
                        VerificationActivity.countdowndialog = null;
                    }
                    VerificationActivity.this.handler.removeCallbacks(VerificationActivity.this.myRunnable);
                }
                VerificationActivity.this.countdown -= 1;
                countdown.setText("(00:" + VerificationActivity.this.countdown + ")");
                VerificationActivity.this.handler.postDelayed(this, 1000L);
            }
        };
        this.handler.postDelayed(this.myRunnable, 1000L);
        countdowndialog.show();
    }
}
