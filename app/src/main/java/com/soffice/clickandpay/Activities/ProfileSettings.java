package com.soffice.clickandpay.Activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.NetWork.JsonRequester;
import com.soffice.clickandpay.NetWork.TaskListner;
import com.soffice.clickandpay.NetWork.Urls;
import com.soffice.clickandpay.Pojo.GetProfileResonseModel;
import com.soffice.clickandpay.Pojo.ProfilePojo;
import com.soffice.clickandpay.Pojo.UpdateProfileResponseModel;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.Utilty.AnimUtil;
import com.soffice.clickandpay.Utilty.Display;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.soffice.clickandpay.Utilty.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class ProfileSettings extends AppCompatActivity implements TaskListner {

    ClickandPay clickpay;
    SessionManager session;
    JsonRequester requester;
    Urls urls;
    String className;
    ImageView settings_IV, userName_edit, emailId_edit, phoneNo_edit, dob_edit, ic_gender, password_edit,
            passcode_edit, facebook_switch_edit, twitter_switch_edit, back_IV;
    TextView profileGender;
    AppCompatButton save_BTN, LogoutOpt;
    EditText profileName, profileEmail, profilePhn, profileDob;
    private RelativeLayout maleLayout, femaleLayout;
    private ImageView maleImage, femaleImage;
    private int gender;
    private SpotsDialog spotsDialog;
    private AnimUtil utils;

    Gson g = new Gson();
    private final String UPDATE_REQUEST_TAG = "update_profile_request";
    private final String GET_REQUEST_TAG = "get_profile_request";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        utils = new AnimUtil();

        clickpay = (ClickandPay) getApplicationContext();
        session = clickpay.getSession();
        className = getLocalClassName();
        requester = new JsonRequester(this);
        urls = clickpay.getUrls();
        LogoutOpt = (AppCompatButton) findViewById(R.id.logout);
        Utils.setButtonTint(LogoutOpt, ContextCompat.getColorStateList(this, R.color.button_tint));
        LogoutOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.ClearAll();
                Intent intent = new Intent(ProfileSettings.this, InitialActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        settings_IV = (ImageView) findViewById(R.id.settings_IV);
        userName_edit = (ImageView) findViewById(R.id.userName_edit);
        phoneNo_edit = (ImageView) findViewById(R.id.phoneNo_edit);
        dob_edit = (ImageView) findViewById(R.id.dob_edit);
        //gender_edit = (ImageView) findViewById(R.id.profileGender);
        password_edit = (ImageView) findViewById(R.id.password_edit);
        passcode_edit = (ImageView) findViewById(R.id.passcode_edit);
        save_BTN = (AppCompatButton) findViewById(R.id.save_BTNx);
        maleLayout = (RelativeLayout) findViewById(R.id.male_layout);
        femaleLayout = (RelativeLayout) findViewById(R.id.female_layout);

        maleImage = (ImageView) findViewById(R.id.male_image);
        femaleImage = (ImageView) findViewById(R.id.female_image);
        Utils.setButtonTint(save_BTN, ContextCompat.getColorStateList(this, R.color.button_tint));
//        facebook_switch_edit = (ImageView) findViewById(R.id.facebook_switch_edit);
//        twitter_switch_edit = (ImageView) findViewById(R.id.twitter_switch_edit);

        profileName = (EditText) findViewById(R.id.profileName);
        profileEmail = (EditText) findViewById(R.id.profileEmail);
        profilePhn = (EditText) findViewById(R.id.profilePhn);
        profileDob = (EditText) findViewById(R.id.profileDob);
        ic_gender = (ImageView) findViewById(R.id.profileGenderIcon);
        profileGender = (TextView) findViewById(R.id.profileGender);
        profileGender.setVisibility(View.GONE);
        ic_gender.setVisibility(View.GONE);
        profileEmail.setEnabled(true);
        profileDob.setText("");

        maleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maleImage.setImageResource(R.mipmap.male_active_state);
                femaleImage.setImageResource(R.mipmap.female_normal_state);
                gender = 1;

            }
        });
        femaleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                femaleImage.setImageResource(R.mipmap.female_active_state);
                maleImage.setImageResource(R.mipmap.male_normal_state);
                gender = 2;

            }
        });
        profileGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profileGender.getText().toString().equalsIgnoreCase("male")) {
                    profileGender.setText("Female");
                    ic_gender.setImageResource(R.mipmap.gender_female);
                } else {
                    profileGender.setText("Male");
                    ic_gender.setImageResource(R.mipmap.gender_male);
                }
            }
        });
        back_IV = (ImageView) findViewById(R.id.back_IV);
        profileName.clearFocus();
        profileName.setCursorVisible(false);

        getProfileData();

        profileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileName.setCursorVisible(true);
            }
        });

        save_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(profileDob.getText()) || profileDob.getText().toString().trim().equals("00-00-0000")) {
                    Display.DisplayToast(getApplicationContext(), "Please enter a valid Date of Birth ");
                    return;
                }
                String dob = "";
                spotsDialog = utils.showProgressDialog(ProfileSettings.this, "Please wait...");

                try {
                    SimpleDateFormat current = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat required = new SimpleDateFormat("yyyy-MM-dd");
                    Date actual = current.parse(profileDob.getText().toString());
                    dob = required.format(actual);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Map<String, String> params = new HashMap<String, String>();
                params.put("authkey", session.getAuthKey());
                if (!TextUtils.isEmpty(profileName.getText().toString().trim())) {
                    params.put("name", profileName.getText().toString().trim());
                }
                if (!TextUtils.isEmpty(dob)) {
                    params.put("dob", dob);
                }
                if (gender != 0) {
                    params.put("gender", "" + gender);
                }
                if (!TextUtils.isEmpty(profileEmail.getText().toString().trim())) {
                    params.put("email", profileEmail.getText().toString().trim());
                }

                requester.StringRequesterFormValues(urls.updateProfile, Request.Method.POST, className, urls.updateProfile_methodName, params, UPDATE_REQUEST_TAG);
            }
        });

        profileDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePhoneKeypad();
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getSupportFragmentManager(), "DatePicker");
            }
        });

        ProfilePojo model1 = g.fromJson(session.getProfile(), ProfilePojo.class);
        Log.d("Profile", session.getProfile());
        if (model1 != null) {
            profileName.setText(model1.userName);
            if (TextUtils.isEmpty(model1.emailId)) {
                profileEmail.setEnabled(true);
                profileEmail.setText("");

            } else {
                profileEmail.setText(model1.emailId);
                profileEmail.setEnabled(false);

            }
            profilePhn.setText(model1.userMobile);
            if (!TextUtils.isEmpty(model1.userGender)) {
                gender = Integer.valueOf(model1.userGender);
            } else {
                gender = 0;

            }
            if (gender == 1) {
                maleLayout.performClick();
                maleLayout.setEnabled(false);
                femaleLayout.setEnabled(false);
            } else if (gender == 2) {
                femaleLayout.performClick();
                maleLayout.setEnabled(false);
                femaleLayout.setEnabled(false);
            } else {
                maleLayout.setEnabled(true);
                femaleLayout.setEnabled(true);
            }
            SimpleDateFormat actualformat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat requiredformat = new SimpleDateFormat("dd-MM-yyyy");
            try {
                if (!TextUtils.isEmpty(model1.dateOfBirth) && !model1.dateOfBirth.equals("0000-00-00")) {
                    Date Actual = actualformat.parse(model1.dateOfBirth);
                    String Required = requiredformat.format(Actual);
                    profileDob.setText(Required);
                    profileDob.setEnabled(false);
//                } else if (model1.dateOfBirth.equals("0000-00-00")) {
//                    Date Actual = actualformat.parse(model1.dateOfBirth);
//                    String Required = requiredformat.format(Actual);
//                    profileDob.setEnabled(true);
//                    profileDob.setText(Required);
                } else {

//                    Date Actual = actualformat.parse("0000-00-00");
//                    String Required = requiredformat.format(Actual);
                    profileDob.setEnabled(true);
                    profileDob.setText("");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        settings_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileSettings.this, ExtraSettingsActivity.class);
                startActivity(i);
            }
        });

        passcode_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileSettings.this, ChangePassWordOrPassCode.class);
                i.putExtra("fromActivity", "changePasscode");
                startActivity(i);
            }
        });

        password_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileSettings.this, ChangePassWordOrPassCode.class);
                i.putExtra("fromActivity", "changePassword");
                startActivity(i);
            }
        });

        back_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.showPassCode = false;
                finish();
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

    public void getProfileData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("authkey", session.getAuthKey());
        requester.StringRequesterFormValues(urls.getProfile, Request.Method.POST, className, urls.getProfile_methodName, params, GET_REQUEST_TAG);
    }

    public void removePhoneKeypad() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.showPassCode = false;
        finish();
    }

    @Override
    public void onTaskfinished(String response, int cd, String _className, String _methodName) {
        Display.DisplayLogI("ADITYA", "" + response);
        utils.dismissProgressDialog(spotsDialog);
        try {
            if (cd == 00) {
                Display.DisplayToast(this, response);
            } else if (cd == 05) {
                if (_className.equalsIgnoreCase(className) && _methodName.equalsIgnoreCase(urls.updateProfile_methodName)) {
                    Gson g = new Gson();
                    UpdateProfileResponseModel model = g.fromJson(response, UpdateProfileResponseModel.class);
                    Display.DisplayLogI("ADITYA code", model.code);
                    if (model.code.equalsIgnoreCase("200")) {
                        session.setUserName(profileName.getText().toString());
                        ClickandPay.getInstance().ShouldUpdateUsername = true;
                        Display.DisplayToast(ProfileSettings.this, "" + model.message);
                    } else {
                        Display.DisplayToast(ProfileSettings.this, "" + model.message);
                    }
                }
                if (_className.equalsIgnoreCase(className) && _methodName.equalsIgnoreCase(urls.getProfile_methodName)) {
                    Gson g = new Gson();
                    GetProfileResonseModel model = g.fromJson(response, GetProfileResonseModel.class);
                    Display.DisplayLogI("ADITYA code", model.code);
                    if (model.code.equalsIgnoreCase("200")) {
                        Display.DisplayLogI("ADITYA", "GET PROFILE : " + g.toJson(model.profile.get(0)));
                        session.setProfile(g.toJson(model.profile.get(0)));
                        session.setUserName(model.profile.get(0).userName);

                        ProfilePojo model1 = g.fromJson(session.getProfile(), ProfilePojo.class);

                        if (model1 != null) {
                            profileName.setText(model1.userName);
                            if (TextUtils.isEmpty(model1.emailId)) {
                                profileEmail.setEnabled(true);
                                profileEmail.setText("");

                            } else {
                                profileEmail.setText(model1.emailId);
                                profileEmail.setEnabled(false);

                            }
                            profilePhn.setText(model1.userMobile);
                            if (!TextUtils.isEmpty(model1.userGender)) {
                                gender = Integer.valueOf(model1.userGender);
                            } else {
                                gender = 0;

                            }
                            if (gender == 1) {
                                maleLayout.performClick();
                                maleLayout.setEnabled(false);
                                femaleLayout.setEnabled(false);
                            } else if (gender == 2) {
                                femaleLayout.performClick();
                                maleLayout.setEnabled(false);
                                femaleLayout.setEnabled(false);
                            } else {
                                maleLayout.setEnabled(true);
                                femaleLayout.setEnabled(true);
                            }
                            SimpleDateFormat actualformat = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat requiredformat = new SimpleDateFormat("dd-MM-yyyy");
                            try {
                                if (!TextUtils.isEmpty(model1.dateOfBirth) && !model1.dateOfBirth.equals("0000-00-00")) {
                                    Date Actual = actualformat.parse(model1.dateOfBirth);
                                    String Required = requiredformat.format(Actual);
                                    profileDob.setText(Required);
                                    profileDob.setEnabled(false);
//                                } else if (model1.dateOfBirth.equals("0000-00-00")) {
//                                    Date Actual = actualformat.parse(model1.dateOfBirth);
//                                    String Required = requiredformat.format(Actual);
//                                    profileDob.setEnabled(true);
//                                    profileDob.setText(Required);
                                } else {

                                    //   Date Actual = actualformat.parse("0000-00-00");
                                    // String Required = requiredformat.format(Actual);
                                    profileDob.setText("");
                                    profileDob.setEnabled(true);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Display.DisplayToast(ProfileSettings.this, model.message);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
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
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);
        }
    }

    public void populateSetDate(int year, int month, int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate = null;
        String days = "00";
        String months = "00";
        try {
            if (day < 10) {
                days = "0" + day;
            } else {
                days = "" + day;
            }
            if (month < 10) {
                months = "0" + month;
            } else {
                months = "" + month;
            }
            strDate = sdf.parse(days + "/" + months + "/" + year);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Display.DisplayLogI("ADITYA", "ERRE " + new Date().after(strDate));
        if (new Date().after(strDate)) {
            profileDob.setText(days + "-" + months + "-" + year);
        } else {
            Display.DisplayLogI("ADITYA", "ERRE");
            Display.DisplayToast(getApplicationContext(), "Date of Birth can not be a future date");
//            profileDob.setText("");
        }
    }

}
