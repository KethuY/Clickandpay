package com.soffice.clickandpay.Activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.NetWork.JsonRequester;
import com.soffice.clickandpay.NetWork.TaskListner;
import com.soffice.clickandpay.NetWork.Urls;
import com.soffice.clickandpay.Pojo.ResetModelDTO;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.UI.RobotoBoldTextView;
import com.soffice.clickandpay.UI.RobotoLightTextView;
import com.soffice.clickandpay.UI.RobotoRegularEditText;
import com.soffice.clickandpay.Utilty.AnimUtil;
import com.soffice.clickandpay.Utilty.Display;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.soffice.clickandpay.Utilty.Utils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Surya on 01-05-2016.
 */
public class ForgetPassWordOrPassCodeActivity extends AppCompatActivity implements TaskListner {
    SessionManager session;
    RobotoBoldTextView TItleTv_one, TItleTv_two;
    public static RobotoRegularEditText OTPEt;
    RobotoRegularEditText NewPassEt, ConfPassEt, MobilenoEt, DobEt;
    CardView ResetLayoutOne, ResetLayoutTwo;
    RobotoLightTextView JustRemembered;
    int which;
    JsonRequester jsonRequester;
    AppCompatButton ResetBtn, OTPBtn, Cancelbtn;
    CoordinatorLayout coordinator;
    String ClassName, REQUEST_TAG = "ForgotPassRequest";
    String TAG = "ForgetPassActivity";
    private String UserKey;
    String dob;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        which = getIntent().getIntExtra("which", 1);
        BuildUI();
        session = ClickandPay.getInstance().getSession();
        Log.d("dob", session.getDOB());
        jsonRequester = new JsonRequester(this);
        ClassName = getLocalClassName();
    }

    public void BuildUI() {
        coordinator = (CoordinatorLayout) findViewById(R.id.mycoordinator);
        ResetLayoutOne = (CardView) findViewById(R.id.resetlayout_one);
        ResetLayoutTwo = (CardView) findViewById(R.id.resetlayout_two);
        AnimUtil.SlideOutRight(ResetLayoutTwo, 0);
        TItleTv_one = (RobotoBoldTextView) findViewById(R.id.Label_Title_Dialog_one);
        TItleTv_two = (RobotoBoldTextView) findViewById(R.id.Label_Title_Dialog_two);
        OTPEt = (RobotoRegularEditText) findViewById(R.id.reset_otp);
        NewPassEt = (RobotoRegularEditText) findViewById(R.id.reset_newpass);
        ConfPassEt = (RobotoRegularEditText) findViewById(R.id.reset_conf_pass);
        MobilenoEt = (RobotoRegularEditText) findViewById(R.id.reset_mobileno);
        DobEt = (RobotoRegularEditText) findViewById(R.id.reset_dob);
        DobEt.setKeyListener(null);
        ResetBtn = (AppCompatButton) findViewById(R.id.resetBtn);
        OTPBtn = (AppCompatButton) findViewById(R.id.otpbtn);
        Cancelbtn = (AppCompatButton) findViewById(R.id.cancelBtn);
        JustRemembered = (RobotoLightTextView) findViewById(R.id.justremeberedtv);
        Utils.setButtonTint(ResetBtn, ContextCompat.getColorStateList(this, R.color.button_tint));
        Utils.setButtonTint(OTPBtn, ContextCompat.getColorStateList(this, R.color.button_tint));
        Utils.setButtonTint(Cancelbtn, ContextCompat.getColorStateList(this, R.color.button_tint));

        if (which == 1) {
            TItleTv_one.setText("Forgot Password");
            TItleTv_two.setText("Reset Password");
            NewPassEt.setHint(getString(R.string.newpassword));
            ConfPassEt.setHint(getString(R.string.conf_password));
            NewPassEt.setInputType(InputType.TYPE_CLASS_TEXT);
            ConfPassEt.setInputType(InputType.TYPE_CLASS_TEXT);
            ConfPassEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
            NewPassEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
            JustRemembered.setText("Just remembered? Login");
        } else {
            TItleTv_one.setText("Forgot Passcode");
            TItleTv_two.setText("Reset Passcode");
            NewPassEt.setHint(getString(R.string.newpasscode));
            NewPassEt.setInputType(InputType.TYPE_CLASS_NUMBER);
            ConfPassEt.setHint(getString(R.string.conf_password));
            ConfPassEt.setInputType(InputType.TYPE_CLASS_NUMBER);
            ConfPassEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
            NewPassEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ConfPassEt.setHint(getString(R.string.confpasscode));
            JustRemembered.setText("Just remembered passcode?");
        }
        DobEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePhoneKeypad();
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getSupportFragmentManager(), "DatePicker");
            }
        });
        ResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NewPassEt.getText().toString().equalsIgnoreCase("") && !ConfPassEt.getText().toString().equalsIgnoreCase("")) {

                    if (which == 1 ? (NewPassEt.getText().toString().length() > 7 && ConfPassEt.getText().length() > 7) : (NewPassEt.getText().toString().length() == 4 && ConfPassEt.getText().length() == 4)) {
                        if (NewPassEt.getText().toString().equalsIgnoreCase(ConfPassEt.getText().toString())) {
                            if (!OTPEt.getText().toString().equalsIgnoreCase("")) {
                                ResetPass();
                            } else {
                                Utils.GenerateSnackbar(ForgetPassWordOrPassCodeActivity.this, coordinator, "Please enter OTP code");
                            }
                        } else {
                            Utils.GenerateSnackbar(ForgetPassWordOrPassCodeActivity.this, coordinator, which == 1 ? "New password and re-enter new passsword are not matched" : "New passcode and re-enter new passscode are not matched");
                        }
                    } else {
                        if (which == 1 ? NewPassEt.getText().toString().length() < 8 : NewPassEt.getText().toString().length() < 4 || NewPassEt.getText().toString().length() > 4) {
                            Utils.GenerateSnackbar(ForgetPassWordOrPassCodeActivity.this, coordinator, which == 1 ? "Password must be at least 8 characters" : "Passcode should be a 4 digit number");
                        } else {
                            Utils.GenerateSnackbar(ForgetPassWordOrPassCodeActivity.this, coordinator, which == 1 ? "New password and re-enter new passsword are not matched" : "New passcode and re-enter new passscode are not matched");
                        }
                    }

                } else {

                    if (NewPassEt.getText().toString().equalsIgnoreCase("")) {
                        Utils.GenerateSnackbar(ForgetPassWordOrPassCodeActivity.this, coordinator, which == 1 ? "Please enter new password to reset" : "Please enter new passcode to reset");
                    } else if (ConfPassEt.getText().toString().equalsIgnoreCase("")) {
                        Utils.GenerateSnackbar(ForgetPassWordOrPassCodeActivity.this, coordinator, which == 1 ? "Please re-enter new password to reset" : "Please re-enter new passcode to reset");
                    } else {
                        Utils.GenerateSnackbar(ForgetPassWordOrPassCodeActivity.this, coordinator, which == 1 ? "Please enter new password to reset" : "Please enter new passcode to reset");
                    }
                }
            }
        });
        OTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MobilenoEt.getText().toString().equalsIgnoreCase("")) {
                    if (isNumeric(MobilenoEt.getText().toString())) {
                        if (MobilenoEt.getText().toString().length() == 10) {
                            if (MobilenoEt.getText().toString().charAt(0) == '7' || MobilenoEt.getText().toString().charAt(0) == '8' || MobilenoEt.getText().toString().charAt(0) == '9') {
                                //if (DobEt.getText().length() > 2) {
                                OTPRequest();
//                                } else {
//                                    Utils.GenerateSnackbar(ForgetPassWordOrPassCodeActivity.this, coordinator, "Please enter your date of birth");
//                                }
                            } else {
                                Utils.GenerateSnackbar(ForgetPassWordOrPassCodeActivity.this, coordinator, "Please enter valid mobile number starts with 7, 8, 9");
                            }
                        } else {
                            Utils.GenerateSnackbar(ForgetPassWordOrPassCodeActivity.this, coordinator, "Please Enter valid Mobile Number");
                            return;
                        }
                    } else {
                        if (isEmailValid(MobilenoEt.getText().toString())) {
                            // if (DobEt.getText().length() > 2) {
                            OTPRequest();
//                            } else {
//                                Utils.GenerateSnackbar(ForgetPassWordOrPassCodeActivity.this, coordinator, "Please enter your date of birth");
//                            }
                        } else {
                            Utils.GenerateSnackbar(ForgetPassWordOrPassCodeActivity.this, coordinator, "Please Enter valid Email");
                            return;
                        }
                    }
                } else {
                    Utils.GenerateSnackbar(ForgetPassWordOrPassCodeActivity.this, coordinator, "Please Enter valid Mobile number / Email ID");
                }
            }
        });
        JustRemembered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgetPassWordOrPassCodeActivity.this.finish();
            }
        });

        Cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgetPassWordOrPassCodeActivity.this.finish();
            }
        });
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
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

    public void OTPRequest() {
        if (Utils.CheckInternet(this)) {
            OTPBtn.setEnabled(false);
            OTPBtn.setText("Please wait...");
            MobilenoEt.setEnabled(false);
            DobEt.setEnabled(false);
            Map<String, String> params = new HashMap<>();
            params.put("authkey", which == 1 ? "" : session.getAuthKey());
            params.put("mobiorpass", MobilenoEt.getText().toString());
//         params.put("dob",dob);
            jsonRequester.StringRequesterFormValues(which == 1 ? Urls.forgot_password_url : Urls.forgotPasscode, Request.Method.POST, ClassName, which == 1 ? Urls.forgot_password_method : Urls.forgotPasscode_methodName,
                    params, REQUEST_TAG);

        } else {
            Utils.GenerateSnackbar(this, coordinator, "Please check your internet connection and try again.");
        }
    }

    public void ResetPass() {
        if (Utils.CheckInternet(this)) {
            ResetBtn.setText("Please wait..");
            ResetBtn.setEnabled(false);
            NewPassEt.setEnabled(false);
            OTPEt.setEnabled(false);
            ConfPassEt.setEnabled(false);
            Map<String, String> params = new HashMap<>();
            params.put("authkey", which == 1 ? UserKey : session.getAuthKey());
            params.put("otp", OTPEt.getText().toString());
            params.put("pass", Utils.EncryptData(NewPassEt.getText().toString()));
            params.put("repass", Utils.EncryptData(ConfPassEt.getText().toString()));
            jsonRequester.StringRequesterFormValues(which == 1 ? Urls.reset_password_url : Urls.reset_passcode_url, Request.Method.POST, ClassName, which == 1 ? Urls.reset_password_method : Urls.reset_passcode_method, params, REQUEST_TAG);
        } else {
            Utils.GenerateSnackbar(this, coordinator, "Please check your internet connection and try again.");
        }
    }

    public void removePhoneKeypad() {

        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
                    .getWindowToken(), 0);
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
            if (datePickerDialog == null) {
                datePickerDialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);
                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
            }
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            datePickerDialog.getDatePicker().updateDate(yy, mm, dd);
            populateSetDate(yy, mm + 1, dd);
        }
    }

    public void populateSetDate(int year, int month, int day) {
        dob = year + "-" + month + "-" + day;
        DobEt.setText(day + "/" + month + "/" + year);

    }

    @Override
    public void onTaskfinished(String response, int cd, String _className, String _methodName) {
        Display.DisplayLogD("response", response);
        if (cd == 0) {
            Utils.GenerateSnackbar(this, coordinator, "Oops.. Something went wrong.Please try again.");
            if (_className.equalsIgnoreCase(ClassName) && (_methodName.equalsIgnoreCase(Urls.reset_password_method) || _methodName.equalsIgnoreCase(Urls.reset_passcode_method))) {
                NewPassEt.setEnabled(true);
                ResetBtn.setEnabled(true);
                ConfPassEt.setEnabled(true);
                ResetBtn.setEnabled(true);
                OTPEt.setEnabled(true);
                ResetBtn.setText(which == 1 ? "Reset Password" : "Reset Passcode");
            } else if (_className.equalsIgnoreCase(ClassName) && (_methodName.equalsIgnoreCase(Urls.forgot_password_method) || _methodName.equalsIgnoreCase(Urls.forgot_passcode_method))) {
                OTPBtn.setEnabled(true);
                MobilenoEt.setEnabled(true);
                DobEt.setEnabled(true);
                OTPEt.setEnabled(true);
                OTPBtn.setText("Send Confirmation Code");
            }
        } else if (cd == 5) {

            Gson g = new Gson();
            ResetModelDTO modelDTO = g.fromJson(response, ResetModelDTO.class);
            if (modelDTO.Code.equalsIgnoreCase("200")) {
                if (_className.equalsIgnoreCase(ClassName) && _methodName.equalsIgnoreCase(Urls.forgot_password_method)) {
                    AnimUtil.SlideOutLeft(ResetLayoutOne, 500);
                    AnimUtil.SlideOutLeft(JustRemembered, 500);
                    AnimUtil.SlideInRight(ResetLayoutTwo, 500, 700);
                    ResetBtn.setText("Reset Password");
                    UserKey = modelDTO.AuthKey;
                    Utils.GenerateSnackbar(this, coordinator, "Sent OTP Successfully");
                } else if (_className.equalsIgnoreCase(ClassName) && _methodName.equalsIgnoreCase(Urls.forgotPasscode_methodName)) {
                    AnimUtil.SlideOutLeft(ResetLayoutOne, 500);
                    AnimUtil.SlideOutLeft(JustRemembered, 500);
                    AnimUtil.SlideInRight(ResetLayoutTwo, 500, 700);
                    ResetBtn.setText("Reset Passcode");
                    Utils.GenerateSnackbar(this, coordinator, "Sent OTP Successfully");

                } else if (_className.equalsIgnoreCase(ClassName) && _methodName.equalsIgnoreCase(Urls.reset_passcode_method)) {
                    Utils.GenerateSnackbar(this, coordinator, "Your account pass code has been successfully updated");
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(ForgetPassWordOrPassCodeActivity.this, PassCodeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }, 3000);
                } else if (_className.equalsIgnoreCase(ClassName) && _methodName.equalsIgnoreCase(Urls.reset_password_method)) {
                    Utils.GenerateSnackbar(this, coordinator, "Your account password has been successfully updated");
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(ForgetPassWordOrPassCodeActivity.this, MobileRegistrationActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }, 3000);
                }

            } else {
                Utils.GenerateSnackbar(ForgetPassWordOrPassCodeActivity.this, coordinator, modelDTO.Message);
                if (_methodName.equalsIgnoreCase(Urls.forgot_passcode_method) || _methodName.equalsIgnoreCase(Urls.forgot_password_method)) {
                    OTPBtn.setEnabled(true);
                    MobilenoEt.setEnabled(true);
                    DobEt.setEnabled(true);
                    OTPEt.setEnabled(true);
                    OTPBtn.setText("Send Confirmation Code");
                } else {
                    NewPassEt.setEnabled(true);
                    ResetBtn.setEnabled(true);
                    ConfPassEt.setEnabled(true);
                    ResetBtn.setEnabled(true);
                    OTPEt.setEnabled(true);
                    ResetBtn.setText(which == 1 ? "Reset Password" : "Reset Passcode");
                }
            }

        }
    }


}
