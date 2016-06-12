package com.soffice.clickandpay.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.NetWork.JsonRequester;
import com.soffice.clickandpay.NetWork.TaskListner;
import com.soffice.clickandpay.NetWork.Urls;
import com.soffice.clickandpay.Pojo.RegistrationResponseModel;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.Utilty.Constants;
import com.soffice.clickandpay.Utilty.Display;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.soffice.clickandpay.Utilty.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements TaskListner {

    EditText name_EditText, email_EditText, password_EditText, mobile_EditText, refferal_EditText;
    AppCompatButton getStartedLayout;
    TextView signIn_TV;
    static TextView dob_EditText;
    ImageView back_IV;
    String name, email, passwd, mobile, dob, refferal;
    JsonRequester requester;
    String className,methodName,Dob;
    Urls urls;
    SessionManager session;
    ClickandPay clickpay;
    boolean ShouldProceed = false;
    private final String REQUEST_TAG="signup_tag";
    CoordinatorLayout coordinatorLayout;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        coordinatorLayout= (CoordinatorLayout) findViewById(R.id.mycoordinator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        className = getLocalClassName();
        requester = new JsonRequester(this);
        clickpay = (ClickandPay) getApplicationContext();
        session = clickpay.getSession();
        urls = clickpay.getUrls();
        name_EditText = (EditText) findViewById(R.id.name_EditText);
        email_EditText = (EditText) findViewById(R.id.email_EditText);
        password_EditText = (EditText) findViewById(R.id.password_EditText);
        mobile_EditText = (EditText) findViewById(R.id.mobile_EditText);
        dob_EditText = (TextView) findViewById(R.id.dob_EditText);
        refferal_EditText = (EditText) findViewById(R.id.refferal_EditText);
        getStartedLayout = (AppCompatButton) findViewById(R.id.getStartedLayout);
        Utils.setButtonTint(getStartedLayout,ContextCompat.getColorStateList(RegisterActivity.this,R.color.button_tint_white));
        signIn_TV = (TextView) findViewById(R.id.signIn_TV);
        back_IV = (ImageView) findViewById(R.id.back_IV);

        getStartedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = name_EditText.getText().toString();
                email = email_EditText.getText().toString();
                passwd = password_EditText.getText().toString();
                mobile = mobile_EditText.getText().toString();
                refferal = refferal_EditText.getText().toString();
                if (name != null && name.length() > 2) {
                    if (email != null && email.length() > 2) {
                        if (email != null && isEmailValid(email)) {
                            if (passwd != null && passwd.length() > 0) {
                                if(passwd.length() > 7) {
                                    if (mobile != null && mobile.length() == 10) {
                                        if (mobile.charAt(0) == '7' || mobile.charAt(0) == '8' || mobile.charAt(0) == '9') {
                                            if (dob != null && dob.length() > 2) {
                                                if(Build.VERSION.SDK_INT>=23) {
                                                    CheckPermissions();
                                                }
                                                else
                                                {
                                                    ServerCall();
                                                }
                                            } else {
                                                Display.DisplaySnackbar(RegisterActivity.this,coordinatorLayout, "Please enter your Date of Birth");
                                            }
                                        } else {
                                            Display.DisplaySnackbar(RegisterActivity.this,coordinatorLayout, "Please enter valid mobile number starts with 7 / 8 / 9");
                                        }
                                    } else {
                                        Display.DisplaySnackbar(RegisterActivity.this,coordinatorLayout, "Please enter your Mobile Number");
                                    }
                                } else {
                                    Display.DisplaySnackbar(RegisterActivity.this,coordinatorLayout, "Password must be atleast 8 characters");
                                }
                            } else {
                                Display.DisplaySnackbar(RegisterActivity.this,coordinatorLayout, "Please enter your Password");
                            }
                        } else {
                            Display.DisplaySnackbar(RegisterActivity.this,coordinatorLayout, "Please enter valid Email");
                        }
                    } else {
                        Display.DisplaySnackbar(RegisterActivity.this,coordinatorLayout, "Please enter your Email");
                    }
                } else {
                    Display.DisplaySnackbar(RegisterActivity.this,coordinatorLayout, "Please enter your Full Name");
                }
            }
        });

        signIn_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                i.putExtra("from", "regiter");
                startActivity(i);
            }
        });

        back_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dob_EditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePhoneKeypad();
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getSupportFragmentManager(), "DatePicker");
            }
        });

        back_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getStringExtra("fromActivity") != null && getIntent().getStringExtra("fromActivity").equalsIgnoreCase("Initial")) {
                    Intent i = new Intent(RegisterActivity.this, InitialActivity.class);
                    i.putExtra("fromActivity", "Register");
                    startActivity(i);
                    finish();
                } else {
                    finish();
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


    private void ServerCall() {
        if(Utils.CheckInternet(this)) {
            getStartedLayout.setEnabled(false);
            getStartedLayout.setText("Please wait..");
            getStartedLayout.setTextColor(ContextCompat.getColor(this, R.color.white));
            Map<String, String> params = new HashMap<String, String>();
            params.put("version", Constants.App_Version);
            params.put("userName", name);
            params.put("DOB", dob);
            params.put("emailId", email);
            params.put("mobile", mobile);
            params.put("password", Utils.EncryptData(passwd).toString());
            params.put("devicetype", "1");
            params.put("refCode", refferal);
            params.put("mid", clickpay.getDeviceId(getApplicationContext()));
            methodName = urls.userSignUp_methodName;
            requester.StringRequesterFormValues(urls.userSignUp, Request.Method.POST, className, methodName, params, REQUEST_TAG);
        }
        else
        {
            Display.DisplaySnackbar(this,coordinatorLayout,"Please check your internet connection and try again");
        }
    }

    /**Need to check for Android Version 6.0 and Above (RUN TIME PERMISSION CHECK) **/

    private void CheckPermissions()
    {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.READ_SMS") != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.RECEIVE_SMS") != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(this, "android.permission.READ_PHONE_STATE") != PackageManager.PERMISSION_GRANTED))
        {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_SMS")) || (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.RECEIVE_SMS")) || (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_PHONE_STATE")))
            {
                Snackbar localSnackbar = Snackbar.make(this.coordinatorLayout, "App requires an additional permission to complete the registration process",Snackbar.LENGTH_LONG);
                localSnackbar.setAction("Grant", new View.OnClickListener()
                {
                    public void onClick(View paramAnonymousView)
                    {
                        ActivityCompat.requestPermissions(RegisterActivity.this, new String[] { "android.permission.READ_SMS", "android.permission.RECEIVE_SMS", "android.permission.READ_PHONE_STATE" }, 535);
                    }
                });
                localSnackbar.setActionTextColor(Color.WHITE);
                localSnackbar.show();
                return;
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_SMS", "android.permission.RECEIVE_SMS", "android.permission.READ_PHONE_STATE"}, 535);
            }
        }
        else {
            ServerCall();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==535){
            if(permissions.length==3) {
                if (permissions[0].equals(Manifest.permission.READ_SMS) && grantResults[0] == PackageManager.PERMISSION_GRANTED&&(permissions[1].equals("android.permission.RECEIVE_SMS")) && (grantResults[1] == PackageManager.PERMISSION_GRANTED) && (permissions[2].equals("android.permission.READ_PHONE_STATE")) && (grantResults[2] == PackageManager.PERMISSION_GRANTED))
                {
                    ServerCall();
                } else {
                    Display.DisplaySnackbar(this, coordinatorLayout, "Unable to complete Sign Up process.");
                }
            }
            else
            {
                Display.DisplaySnackbar(this, coordinatorLayout, "Unable to complete Sign Up process.");
            }
        }
    }

    public void removePhoneKeypad() {

        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
                    .getWindowToken(), 0);
        }
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getIntent().getStringExtra("fromActivity") != null && getIntent().getStringExtra("fromActivity").equalsIgnoreCase("Initial")) {
            Intent i = new Intent(RegisterActivity.this, InitialActivity.class);
            i.putExtra("fromActivity", "Register");
            startActivity(i);
            finish();
        } else {
            finish();
        }
    }

    @SuppressLint("ValidFragment")
    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            if(datePickerDialog==null) {
                datePickerDialog=new DatePickerDialog(getActivity(), this, yy, mm, dd);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            }
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            datePickerDialog.getDatePicker().updateDate(yy,mm,dd);
            populateSetDate(yy, mm + 1, dd);
        }
    }

    public void populateSetDate(int year, int month, int day) {
        dob=year+"-"+month+"-"+day;
        dob_EditText.setText(day + "/" + month + "/" +year);

    }

    @Override
    public void onTaskfinished(String response, int cd, String _className, String _methodName) {
        Display.DisplayLogI("ADITYA", "" + response);
        try {
            if (cd == 00) {
                Display.DisplaySnackbar(this,coordinatorLayout,"Failed to complete the registration process. Please try again");
                getStartedLayout.setEnabled(true);
                getStartedLayout.setText("Get Started");
                getStartedLayout.setText(ContextCompat.getColor(this,R.color.colorPrimary));
            } else if (cd == 05) {
                if (_className.equalsIgnoreCase(className) && _methodName.equalsIgnoreCase(urls.userSignUp_methodName)) {
                    Gson g = new Gson();
                    RegistrationResponseModel model = g.fromJson(response, RegistrationResponseModel.class);
                    Display.DisplayLogI("ADITYA code", model.code);
                    if (model.code.equalsIgnoreCase("200")) {
                        session.setAuthKey(model.key);
                        session.setEmailId(email);
                        session.setMobileNum(mobile);
                        Intent i = new Intent(RegisterActivity.this, VerificationActivity.class);
                        i.putExtra("VERIFY_CODE", model.OTP);
                        i.putExtra("fromActivity", "Register");
                        startActivity(i);
                        finish();
                    } else {
                        Display.DisplaySnackbar(RegisterActivity.this,coordinatorLayout, model.message);
                        getStartedLayout.setEnabled(true);
                        getStartedLayout.setText("Get Started");
                        getStartedLayout.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
