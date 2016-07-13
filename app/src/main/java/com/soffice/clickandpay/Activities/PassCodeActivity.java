package com.soffice.clickandpay.Activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.NetWork.JsonRequester;
import com.soffice.clickandpay.NetWork.TaskListner;
import com.soffice.clickandpay.NetWork.Urls;
import com.soffice.clickandpay.Pojo.SetPassCodeResponseModel;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.Utilty.Constants;
import com.soffice.clickandpay.Utilty.Display;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.soffice.clickandpay.Utilty.Utils;

import java.util.HashMap;
import java.util.Map;

public class PassCodeActivity extends AppCompatActivity implements TaskListner {

    static String enteredCode = "";
    static String conformEnteredCode = "";
    static String fromActivity;
    String className;
    ClickandPay clickpay;
    SessionManager session;
    JsonRequester requester;
    Urls urls;
    ImageView passCode1, passCode2, passCode3, passCode4, backSpace, back_IV, done;
    TextView key1, key2, key3, key4, key5, key6, key7, key8, key9, key0, tv1, tv3;
    View.OnClickListener clickEventOnKeys;
    View.OnClickListener clickEventOnKeysConform;
    View.OnClickListener backspaceEvent;
    View.OnClickListener conformBackspaceEvent;
    Handler handler = new Handler();
    LinearLayout forgot_passcode_layout;
    ProgressBar Pbar;
    boolean isRemoving = false;
    private final String REQUEST_TAG = "pass_request";
    CoordinatorLayout coordinatorLayout;
    int userStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_code);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.mycoordinator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Pbar = (ProgressBar) findViewById(R.id.pbar);
        Pbar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        clickpay = (ClickandPay) getApplicationContext();
        session = clickpay.getSession();
        className = getLocalClassName();
        requester = new JsonRequester(this);
        urls = clickpay.getUrls();
        fromActivity = getIntent().getStringExtra("fromActivity");
        if (getIntent().hasExtra("user_status") && getIntent().getStringExtra("user_status") != null) {
            userStatus = Integer.parseInt(getIntent().getStringExtra("user_status"));
        } else {
            userStatus = 1;
        }

        passCode1 = (ImageView) findViewById(R.id.passcode1);
        passCode2 = (ImageView) findViewById(R.id.passcode2);
        passCode3 = (ImageView) findViewById(R.id.passcode3);
        passCode4 = (ImageView) findViewById(R.id.passcode4);
        backSpace = (ImageView) findViewById(R.id.backSpace);
        done = (ImageView) findViewById(R.id.done);

        back_IV = (ImageView) findViewById(R.id.back_IV);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv3 = (TextView) findViewById(R.id.tv3);


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

        forgot_passcode_layout = (LinearLayout) findViewById(R.id.forgot_passcode_layout);

        forgot_passcode_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassCodeActivity.this, ForgetPassWordOrPassCodeActivity.class);
                intent.putExtra("which", 2);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        if (fromActivity.equalsIgnoreCase("Splash") || fromActivity.equalsIgnoreCase("Login")) {
            back_IV.setVisibility(View.GONE);
            tv1.setText("Enter Passcode");
            tv3.setVisibility(View.GONE);
            forgot_passcode_layout.setVisibility(View.VISIBLE);
        } else if (fromActivity.equalsIgnoreCase("Verification")) {
            back_IV.setVisibility(View.GONE);
            if (userStatus == 1) {
                tv1.setText("Enter Passcode");
                tv3.setVisibility(View.GONE);
                forgot_passcode_layout.setVisibility(View.VISIBLE);
            } else {
                tv1.setText("Enter New Passcode");
                back_IV.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.VISIBLE);
            }
        } else if (fromActivity.equalsIgnoreCase("Main")) {
            back_IV.setVisibility(View.GONE);
            tv1.setText("Enter Passcode");
            tv3.setVisibility(View.GONE);
            forgot_passcode_layout.setVisibility(View.VISIBLE);
        } else if (fromActivity.equalsIgnoreCase("Login_veri")) {
            back_IV.setVisibility(View.VISIBLE);
        }


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        clickEventOnKeys = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enteredCode.length() < 4) {
                    enteredCode = "" + enteredCode + v.getTag();
                    Display.DisplayLogI("ADITYA", "CLICK PASSCODE " + enteredCode);
                    if (enteredCode.length() == 1) {
                        passCode1.setImageResource(R.drawable.passcode_fill);
                    } else if (enteredCode.length() == 2) {
                        passCode2.setImageResource(R.drawable.passcode_fill);
                    } else if (enteredCode.length() == 3) {
                        passCode3.setImageResource(R.drawable.passcode_fill);
                    } else if (enteredCode.length() == 4) {
                        passCode4.setImageResource(R.drawable.passcode_fill);
                        done.setImageResource(R.drawable.yes_icon_hover);
                        if ((fromActivity.equalsIgnoreCase("Verification") && userStatus == 0) || fromActivity.equalsIgnoreCase("Login_veri")) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    prepareForConformPasscode();
                                }
                            }, 500);
                        } else if (fromActivity.equalsIgnoreCase("Splash") || fromActivity.equalsIgnoreCase("Main") || fromActivity.equalsIgnoreCase("Login") || userStatus == 1) {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("authkey", session.getAuthKey());
                            params.put("passcode", Utils.EncryptData(enteredCode));
                            params.put("mid", clickpay.getDeviceId(getApplicationContext()));
                            params.put("devicetype", "android");
                            requester.StringRequesterFormValues(urls.check_passcode, Request.Method.POST, className, urls.check_passcode_methodName, params, REQUEST_TAG);
                            disableKeys();
                            tv1.setText("Please wait..");
                        }
                        Display.DisplayLogI("ADITYA", "CLICK PASSCODE ");
                    }
                }
            }
        };

        clickEventOnKeysConform = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (conformEnteredCode.length() < 4) {
                    conformEnteredCode = "" + conformEnteredCode + v.getTag();
                    Display.DisplayLogI("ADITYA", "CLICK CONFORM PASSCODE " + conformEnteredCode);
                    if (conformEnteredCode.length() == 1) {
                        passCode1.setImageResource(R.drawable.passcode_fill);
                    } else if (conformEnteredCode.length() == 2) {
                        passCode2.setImageResource(R.drawable.passcode_fill);
                    } else if (conformEnteredCode.length() == 3) {
                        passCode3.setImageResource(R.drawable.passcode_fill);
                    } else if (conformEnteredCode.length() == 4) {
                        passCode4.setImageResource(R.drawable.passcode_fill);
                        if (enteredCode.equalsIgnoreCase(conformEnteredCode)) {
                            if (session.getAuthKey() != null && session.getAuthKey().length() > 2) {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("authkey", session.getAuthKey());
                                params.put("mid", clickpay.getDeviceId(getApplicationContext()));
                                params.put("passcode", Utils.EncryptData(enteredCode));
                               // params.put("version", Constants.App_Version);
                                requester.StringRequesterFormValues(urls.passcode, Request.Method.POST, className, urls.passcode_methodName, params, REQUEST_TAG);
                                disableKeys();
                                tv1.setText("Please wait..");

                            }
                        } else {
//                        enteredCode = "";
                            conformEnteredCode = "";
                            prepareForConformPasscode();
                            Display.DisplaySnackbar(PassCodeActivity.this, coordinatorLayout, "Pass Code does not match..!!");
                        }
                    }
                }
            }
        };

        backspaceEvent = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRemoving) {
                    isRemoving = true;
                    backSpace.setEnabled(false);
                    enteredCode = removeLastChar(enteredCode);
                    Display.DisplayLogI("ADITYA", "CLICK BACKSPACE " + enteredCode);
                    /*if (enteredCode.length() < 4) {
                        enableKeys();
                    }*/
                    if (enteredCode.length() == 1) {
                        passCode2.setImageResource(R.drawable.passcode_empty);
                        passCode3.setImageResource(R.drawable.passcode_empty);
                        passCode4.setImageResource(R.drawable.passcode_empty);
                    } else if (enteredCode.length() == 2) {
                        passCode3.setImageResource(R.drawable.passcode_empty);
                        passCode4.setImageResource(R.drawable.passcode_empty);
                    } else if (enteredCode.length() == 3) {
                        passCode4.setImageResource(R.drawable.passcode_empty);
                    } else if (enteredCode.length() == 0) {
                        passCode1.setImageResource(R.drawable.passcode_empty);
                        passCode2.setImageResource(R.drawable.passcode_empty);
                        passCode3.setImageResource(R.drawable.passcode_empty);
                        passCode4.setImageResource(R.drawable.passcode_empty);

                    }
                    done.setImageResource(R.drawable.yes_icon_normal);
                    isRemoving = false;
                    backSpace.setEnabled(true);
                }
            }
        };

        conformBackspaceEvent = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conformEnteredCode = removeLastChar(conformEnteredCode);
                if (conformEnteredCode.length() < 4) {
                    enableKeys();
                } else {
                    done.setImageResource(R.drawable.yes_icon_hover);
                }
                if (conformEnteredCode.length() == 1) {
                    passCode2.setImageResource(R.drawable.passcode_empty);
                    passCode3.setImageResource(R.drawable.passcode_empty);
                    passCode4.setImageResource(R.drawable.passcode_empty);
                } else if (conformEnteredCode.length() == 2) {
                    passCode3.setImageResource(R.drawable.passcode_empty);
                    passCode4.setImageResource(R.drawable.passcode_empty);
                } else if (conformEnteredCode.length() == 3) {
                    passCode4.setImageResource(R.drawable.passcode_empty);
                } else if (conformEnteredCode.length() == 0) {
                    passCode1.setImageResource(R.drawable.passcode_empty);
                    passCode2.setImageResource(R.drawable.passcode_empty);
                    passCode3.setImageResource(R.drawable.passcode_empty);
                    passCode4.setImageResource(R.drawable.passcode_empty);
                }
            }
        };

        backSpace.setOnClickListener(backspaceEvent);

        key1.setOnClickListener(clickEventOnKeys);
        key2.setOnClickListener(clickEventOnKeys);
        key3.setOnClickListener(clickEventOnKeys);
        key4.setOnClickListener(clickEventOnKeys);
        key5.setOnClickListener(clickEventOnKeys);
        key6.setOnClickListener(clickEventOnKeys);
        key7.setOnClickListener(clickEventOnKeys);
        key8.setOnClickListener(clickEventOnKeys);
        key9.setOnClickListener(clickEventOnKeys);
        key0.setOnClickListener(clickEventOnKeys);

        back_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv1.getText().toString().equalsIgnoreCase("Enter New Passcode")) {
                    if (getIntent().getStringExtra("fromActivity") != null && (getIntent().getStringExtra("fromActivity").equalsIgnoreCase("Verification") || fromActivity.equalsIgnoreCase("Login_veri"))) {
                        Intent i = new Intent(PassCodeActivity.this, MobileRegistrationActivity.class);
                        i.putExtra("fromActivity", "PassCode");
                        startActivity(i);
                        finish();
                    } else {
                        finish();
                    }
                    enteredCode = "";
                } else if (tv1.getText().toString().equalsIgnoreCase("Re-Enter New Passcode")) {
                    tv1.setText("Enter New Passcode");
                    tv3.setVisibility(View.VISIBLE);
                    if (fromActivity.equalsIgnoreCase("Login_veri")) {
                        back_IV.setVisibility(View.VISIBLE);
                    } else {
                        back_IV.setVisibility(View.GONE);
                    }


                    passCode1.setImageResource(R.drawable.passcode_empty);
                    passCode2.setImageResource(R.drawable.passcode_empty);
                    passCode3.setImageResource(R.drawable.passcode_empty);
                    passCode4.setImageResource(R.drawable.passcode_empty);

                    backSpace.setOnClickListener(backspaceEvent);

                    key1.setOnClickListener(clickEventOnKeys);
                    key2.setOnClickListener(clickEventOnKeys);
                    key3.setOnClickListener(clickEventOnKeys);
                    key4.setOnClickListener(clickEventOnKeys);
                    key5.setOnClickListener(clickEventOnKeys);
                    key6.setOnClickListener(clickEventOnKeys);
                    key7.setOnClickListener(clickEventOnKeys);
                    key8.setOnClickListener(clickEventOnKeys);
                    key9.setOnClickListener(clickEventOnKeys);
                    key0.setOnClickListener(clickEventOnKeys);
                    enteredCode = "";
                }
            }
        });

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    public void prepareForConformPasscode() {

        Display.DisplayLogI("ADITYA", "CLICK prepareForConformPasscode ");

        tv1.setText("Re-Enter New Passcode");
        tv3.setVisibility(View.GONE);


        passCode1.setImageResource(R.drawable.passcode_empty);
        passCode2.setImageResource(R.drawable.passcode_empty);
        passCode3.setImageResource(R.drawable.passcode_empty);
        passCode4.setImageResource(R.drawable.passcode_empty);

        key1.setOnClickListener(clickEventOnKeysConform);
        key2.setOnClickListener(clickEventOnKeysConform);
        key3.setOnClickListener(clickEventOnKeysConform);
        key4.setOnClickListener(clickEventOnKeysConform);
        key5.setOnClickListener(clickEventOnKeysConform);
        key6.setOnClickListener(clickEventOnKeysConform);
        key7.setOnClickListener(clickEventOnKeysConform);
        key8.setOnClickListener(clickEventOnKeysConform);
        key9.setOnClickListener(clickEventOnKeysConform);
        key0.setOnClickListener(clickEventOnKeysConform);

        backSpace.setOnClickListener(conformBackspaceEvent);

        back_IV.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (tv1.getText().toString().equalsIgnoreCase("Enter New Passcode")) {
            if (getIntent().getStringExtra("fromActivity") != null && getIntent().getStringExtra("fromActivity").equalsIgnoreCase("Verification")) {
                Intent i = new Intent(PassCodeActivity.this, MobileRegistrationActivity.class);
                i.putExtra("fromActivity", "PassCode");
                startActivity(i);
                finish();
            } else {
                finish();
            }
            enteredCode = "";
        } else if (tv1.getText().toString().equalsIgnoreCase("Re-Enter New Passcode")) {
            tv1.setText("Enter New Passcode");

            passCode1.setImageResource(R.drawable.passcode_empty);
            passCode2.setImageResource(R.drawable.passcode_empty);
            passCode3.setImageResource(R.drawable.passcode_empty);
            passCode4.setImageResource(R.drawable.passcode_empty);

            backSpace.setOnClickListener(backspaceEvent);

            key1.setOnClickListener(clickEventOnKeys);
            key2.setOnClickListener(clickEventOnKeys);
            key3.setOnClickListener(clickEventOnKeys);
            key4.setOnClickListener(clickEventOnKeys);
            key5.setOnClickListener(clickEventOnKeys);
            key6.setOnClickListener(clickEventOnKeys);
            key7.setOnClickListener(clickEventOnKeys);
            key8.setOnClickListener(clickEventOnKeys);
            key9.setOnClickListener(clickEventOnKeys);
            key0.setOnClickListener(clickEventOnKeys);
            enteredCode = "";

            back_IV.setVisibility(View.GONE);
        }
    }

    public void prepareForPasscode() {

        Display.DisplayLogI("ADITYA", "CLICK prepareForPasscode ");

        passCode1.setImageResource(R.drawable.passcode_empty);
        passCode2.setImageResource(R.drawable.passcode_empty);
        passCode3.setImageResource(R.drawable.passcode_empty);
        passCode4.setImageResource(R.drawable.passcode_empty);

        key1.setOnClickListener(clickEventOnKeys);
        key2.setOnClickListener(clickEventOnKeys);
        key3.setOnClickListener(clickEventOnKeys);
        key4.setOnClickListener(clickEventOnKeys);
        key5.setOnClickListener(clickEventOnKeys);
        key6.setOnClickListener(clickEventOnKeys);
        key7.setOnClickListener(clickEventOnKeys);
        key8.setOnClickListener(clickEventOnKeys);
        key9.setOnClickListener(clickEventOnKeys);
        key0.setOnClickListener(clickEventOnKeys);

        backSpace.setOnClickListener(backspaceEvent);

        enableKeys();
    }

    public void enableKeys() {
        key1.setEnabled(true);
        key2.setEnabled(true);
        key3.setEnabled(true);
        key4.setEnabled(true);
        key5.setEnabled(true);
        key6.setEnabled(true);
        key7.setEnabled(true);
        key8.setEnabled(true);
        key9.setEnabled(true);
        key0.setEnabled(true);
        backSpace.setEnabled(true);
        back_IV.setEnabled(true);
        Pbar.setVisibility(View.GONE);
        passCode1.setImageResource(R.drawable.passcode_empty);
        passCode2.setImageResource(R.drawable.passcode_empty);
        passCode3.setImageResource(R.drawable.passcode_empty);
        passCode4.setImageResource(R.drawable.passcode_empty);
        done.setImageResource(R.drawable.yes_icon_normal);
        enteredCode = "";
    }

    public void disableKeys() {
        key1.setEnabled(false);
        key2.setEnabled(false);
        key3.setEnabled(false);
        key4.setEnabled(false);
        key5.setEnabled(false);
        key6.setEnabled(false);
        key7.setEnabled(false);
        key8.setEnabled(false);
        key9.setEnabled(false);
        key0.setEnabled(false);
        backSpace.setEnabled(false);
        back_IV.setEnabled(false);

        //  Pbar.setVisibility(View.VISIBLE);
    }

    public String removeLastChar(String str) {
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    @Override
    protected void onPause() {
        super.onPause();
        enteredCode = "";
    }


    @Override
    public void onTaskfinished(String response, int cd, String _className, String _methodName) {
        Display.DisplayLogI("ADITYA", "" + response);
        try {
            if (cd == 00) {
                Display.DisplaySnackbar(this, coordinatorLayout, "Oops.. Something went wrong. Please try again");
                enableKeys();
            } else if (cd == 05) {
                if (_className.equalsIgnoreCase(className) && _methodName.equalsIgnoreCase(urls.passcode_methodName)) {
                    Gson g = new Gson();
                    SetPassCodeResponseModel model = g.fromJson(response, SetPassCodeResponseModel.class);
                    Display.DisplayLogI("ADITYA passcode_methodName", model.toString());
                    if (model.code.equalsIgnoreCase("200")) {
                        session.setWalletBal(model.wallet_amount);
                        session.setMonthTrans(model.month_trans);
                        session.setUserName(model.Username);
                        session.setCustomerIdentifier(model.customer_identifier);
                        LaunchNextActivity();
                    } else if (model.code.equalsIgnoreCase("205")) {
                        ClickandPay.getInstance().RedirectToLogin();
                        Display.DisplaySnackbar(this, coordinatorLayout, model.message);
                    } else {
                        tv1.setText("Enter Passcode");
                        Display.DisplaySnackbar(PassCodeActivity.this, coordinatorLayout, model.message);
                        enableKeys();
                    }
                }

                if (_className.equalsIgnoreCase(className) && _methodName.equalsIgnoreCase(urls.check_passcode_methodName)) {
                    Gson g = new Gson();
                    SetPassCodeResponseModel model = g.fromJson(response, SetPassCodeResponseModel.class);
                    Display.DisplayLogI("ADITYA check_passcode_methodName", model.toString());
                    if (model.code.equalsIgnoreCase("200")) {
                        session.setWalletBal(model.wallet_amount);
                        session.setMonthTrans(model.month_trans);
                        session.setUserName(model.Username);
                        session.setCustomerIdentifier(model.customer_identifier);
                        LaunchNextActivity();
                    } else if (model.code.equalsIgnoreCase("205")) {
                        ClickandPay.getInstance().RedirectToLogin();
                        Display.DisplaySnackbar(this, coordinatorLayout, model.message);
                    } else {
                        tv1.setText("Enter Passcode");
                        Display.DisplaySnackbar(PassCodeActivity.this, coordinatorLayout, model.message);
                        enableKeys();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void LaunchNextActivity() {
        Intent i = new Intent(PassCodeActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        ClickandPay.getInstance().StartTimerSession();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ClickandPay.getInstance().cancelPendingRequests(REQUEST_TAG);
    }
}
