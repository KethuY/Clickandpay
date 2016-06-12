package com.soffice.clickandpay.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.NetWork.JsonRequester;
import com.soffice.clickandpay.NetWork.TaskListner;
import com.soffice.clickandpay.NetWork.Urls;
import com.soffice.clickandpay.Pojo.ChangePasswordOrCodeResponseModel;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.Utilty.Constants;
import com.soffice.clickandpay.Utilty.Display;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.soffice.clickandpay.Utilty.Utils;

import java.util.HashMap;
import java.util.Map;

public class ChangePassWordOrPassCode extends AppCompatActivity implements TaskListner {

    TextView Label_Title_Activity, save_title;
    EditText current_word, new_word, conform_new_word;
    ImageView back_IV;
    int function = 0;
    ClickandPay clickpay;
    SessionManager session;
    JsonRequester requester;
    Urls urls;
    String className;
    private final String CHANGE_PASSWORD_REQUEST="change_password_request_tag";
    private final String CHANGE_PASSCODE_REQUEST="change_passcode_request_tag";
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass_word_or_pass_code);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        coordinatorLayout= (CoordinatorLayout) findViewById(R.id.mycoordinator);
        clickpay = (ClickandPay) getApplicationContext();
        session = clickpay.getSession();
        className = getLocalClassName();
        requester = new JsonRequester(this);
        urls = clickpay.getUrls();

        Label_Title_Activity = (TextView) findViewById(R.id.Label_Title_Activity);
        save_title = (TextView) findViewById(R.id.save_title);

        current_word = (EditText) findViewById(R.id.current_word);
        new_word = (EditText) findViewById(R.id.new_word);
        conform_new_word = (EditText) findViewById(R.id.conform_new_word);/*
        current_word.setCursorVisible(false);
        new_word.setCursorVisible(false);
        conform_new_word.setCursorVisible(false);

        current_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_word.setCursorVisible(true);
            }
        });

        new_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_word.setCursorVisible(true);
            }
        });

        conform_new_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conform_new_word.setCursorVisible(true);
            }
        });*/

        back_IV = (ImageView) findViewById(R.id.back_IV);

        Display.DisplayLogI("ADITYA", " from " + getIntent().getStringExtra("fromActivity"));

        if (getIntent().hasExtra("fromActivity") && getIntent().getStringExtra("fromActivity").equalsIgnoreCase("changePassword")) {
            function = 1;
            Label_Title_Activity.setText("Change Password");
            current_word.setHint("Current Password");
            new_word.setHint("New Password");
            conform_new_word.setHint("Confirm New Password");

        } else if (getIntent().hasExtra("fromActivity") && getIntent().getStringExtra("fromActivity").equalsIgnoreCase("changePasscode")) {
            function = 2;
            Label_Title_Activity.setText("Change Passcode");

            current_word.setHint("Current Passcode");
            new_word.setHint("New Passcode");
            conform_new_word.setHint("Confirm New Passcode");

            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(4);

            InputFilter[] fArray1 = new InputFilter[1];
            fArray1[0] = new InputFilter.LengthFilter(4);

            InputFilter[] fArray2 = new InputFilter[1];
            fArray2[0] = new InputFilter.LengthFilter(4);

            current_word.setFilters(fArray);
            new_word.setFilters(fArray1);
            conform_new_word.setFilters(fArray2);

            current_word.setInputType(InputType.TYPE_CLASS_NUMBER);
            new_word.setInputType(InputType.TYPE_CLASS_NUMBER);
            conform_new_word.setInputType(InputType.TYPE_CLASS_NUMBER);

            current_word.setTransformationMethod(PasswordTransformationMethod.getInstance());
            new_word.setTransformationMethod(PasswordTransformationMethod.getInstance());
            conform_new_word.setTransformationMethod(PasswordTransformationMethod.getInstance());


        }

        current_word.setHintTextColor(Color.parseColor("#E6CCCCCC"));
        new_word.setHintTextColor(Color.parseColor("#E6CCCCCC"));
        conform_new_word.setHintTextColor(Color.parseColor("#E6CCCCCC"));

        back_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePhoneKeypad();
                if (function == 1) {
                    if (current_word.getText().toString().length() > 0) {
                        if (new_word.getText().toString().length() > 0) {
                            if (conform_new_word.getText().toString().length() > 0) {
                                if (new_word.getText().length() >= 8 || conform_new_word.getText().length() >= 8 || current_word.getText().length() >= 8) {
                                    if (new_word.getText().toString().equals(conform_new_word.getText().toString())) {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("authkey", session.getAuthKey());
                                        params.put("oldpass", Utils.EncryptData(current_word.getText().toString()));
                                        params.put("pass", Utils.EncryptData(new_word.getText().toString()));
                                        params.put("repass", Utils.EncryptData(conform_new_word.getText().toString()));
                                        requester.StringRequesterFormValues(urls.changePassword, Request.Method.POST, className, urls.changePassword_methodName, params, CHANGE_PASSWORD_REQUEST);
                                    } else {
                                        Display.DisplaySnackbar(ChangePassWordOrPassCode.this, coordinatorLayout, "New password doesn't match with confirm password.");
                                    }
                                } else {
                                    Display.DisplaySnackbar(ChangePassWordOrPassCode.this, coordinatorLayout, "Password length should be more than 8 characters.");
                                }
                            } else {
                                Display.DisplaySnackbar(ChangePassWordOrPassCode.this, coordinatorLayout, "Confirm New Password");
                            }
                        } else {
                            Display.DisplaySnackbar(ChangePassWordOrPassCode.this, coordinatorLayout, "Enter New Password");
                        }
                    } else {
                        Display.DisplaySnackbar(ChangePassWordOrPassCode.this, coordinatorLayout, "Enter Current password");
                    }

                } else if (function == 2) {
                    if (current_word.getText().toString().length() > 0) {
                        if (new_word.getText().toString().length() > 0) {
                            if (conform_new_word.getText().toString().length() > 0) {
                                if (new_word.getText().length() == 4 || conform_new_word.getText().length() == 4 || current_word.getText().length() == 4) {
                                    if (new_word.getText().toString().equals(conform_new_word.getText().toString())) {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("authkey", session.getAuthKey());
                                        params.put("oldpass", Utils.EncryptData(current_word.getText().toString()));
                                        params.put("pass", Utils.EncryptData(new_word.getText().toString()));
                                        params.put("repass", Utils.EncryptData(conform_new_word.getText().toString()));
                                        requester.StringRequesterFormValues(urls.changePasscode, Request.Method.POST, className, urls.changePasscode_methodName, params, CHANGE_PASSCODE_REQUEST);
                                    } else {
                                        Display.DisplaySnackbar(ChangePassWordOrPassCode.this, coordinatorLayout, "New passcode doesn't match with confirm passcode.");
                                    }
                                } else {
                                    Display.DisplaySnackbar(ChangePassWordOrPassCode.this, coordinatorLayout, "Passcode length should be equal 4 characters.");
                                }
                            } else {
                                Display.DisplaySnackbar(ChangePassWordOrPassCode.this, coordinatorLayout, "Confirm New Passcode");
                            }
                        } else {
                            Display.DisplaySnackbar(ChangePassWordOrPassCode.this, coordinatorLayout, "Enter New Passcode");
                        }
                    } else {
                        Display.DisplaySnackbar(ChangePassWordOrPassCode.this, coordinatorLayout, "Enter current passcode");
                    }
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
    }

    @Override
    public void onTaskfinished(String response, int cd, String _className, String _methodName) {
        Display.DisplayLogI("ADITYA", "" + response);
        try {
            if (cd == 00) {
                Display.DisplayToast(this, response);
            } else if (cd == 05) {
                if (_className.equalsIgnoreCase(className) && (_methodName.equalsIgnoreCase(urls.changePasscode_methodName) || _methodName.equalsIgnoreCase(urls.changePassword_methodName))) {
                    Gson g = new Gson();
                    ChangePasswordOrCodeResponseModel model = g.fromJson(response, ChangePasswordOrCodeResponseModel.class);
                    Display.DisplayLogI("ADITYA code", model.code);
                    if (model.code.equalsIgnoreCase("200")) {
                        Display.DisplayToast(ChangePassWordOrPassCode.this, "" + model.message);
                        finish();
                    } else {
                        Display.DisplayToast(ChangePassWordOrPassCode.this, model.message);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void removePhoneKeypad() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
                    .getWindowToken(), 0);
        }
    }
}
