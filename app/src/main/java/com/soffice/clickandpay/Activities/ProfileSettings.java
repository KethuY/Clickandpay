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
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.soffice.clickandpay.UI.RobotoBoldTextView;
import com.soffice.clickandpay.Utilty.Display;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.soffice.clickandpay.Utilty.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProfileSettings extends AppCompatActivity implements TaskListner {

    ClickandPay clickpay;
    SessionManager session;
    JsonRequester requester;
    Urls urls;
    String className;
    ImageView settings_IV, userName_edit, emailId_edit, phoneNo_edit, dob_edit, ic_gender, password_edit,
            passcode_edit, facebook_switch_edit, twitter_switch_edit, back_IV;
    TextView profileEmail, profilePhn, profileDob, profileGender;
    AppCompatButton save_BTN,LogoutOpt;
    EditText profileName;
    Gson g = new Gson();
    private final String UPDATE_REQUEST_TAG="update_profile_request";
    private final String GET_REQUEST_TAG="get_profile_request";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        clickpay = (ClickandPay) getApplicationContext();
        session = clickpay.getSession();
        className = getLocalClassName();
        requester = new JsonRequester(this);
        urls = clickpay.getUrls();
        LogoutOpt= (AppCompatButton) findViewById(R.id.logout);
        Utils.setButtonTint(LogoutOpt, ContextCompat.getColorStateList(this,R.color.button_tint));
        LogoutOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.ClearAll();
                Intent intent=new Intent(ProfileSettings.this,InitialActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        settings_IV= (ImageView) findViewById(R.id.settings_IV);
        userName_edit = (ImageView) findViewById(R.id.userName_edit);
        phoneNo_edit = (ImageView) findViewById(R.id.phoneNo_edit);
        dob_edit = (ImageView) findViewById(R.id.dob_edit);
        //gender_edit = (ImageView) findViewById(R.id.profileGender);
        password_edit = (ImageView) findViewById(R.id.password_edit);
        passcode_edit = (ImageView) findViewById(R.id.passcode_edit);
        save_BTN = (AppCompatButton) findViewById(R.id.save_BTN);
        Utils.setButtonTint(save_BTN,ContextCompat.getColorStateList(this,R.color.button_tint));
//        facebook_switch_edit = (ImageView) findViewById(R.id.facebook_switch_edit);
//        twitter_switch_edit = (ImageView) findViewById(R.id.twitter_switch_edit);

        profileName = (EditText) findViewById(R.id.profileName);
        profileEmail = (TextView) findViewById(R.id.profileEmail);
        profilePhn = (TextView) findViewById(R.id.profilePhn);
        profileDob = (TextView) findViewById(R.id.profileDob);
        ic_gender= (ImageView) findViewById(R.id.profileGenderIcon);
        profileGender = (TextView) findViewById(R.id.profileGender);
        profileGender.setVisibility(View.GONE);
        ic_gender.setVisibility(View.GONE);
        profileGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(profileGender.getText().toString().equalsIgnoreCase("male"))
                {
                    profileGender.setText("Female");
                    ic_gender.setImageResource(R.mipmap.gender_female);
                }
                else
                {
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
                if(profileName.getText().toString().length() > 0){
                    if(profileDob.getText().toString().length() > 0){
                        String dob = null;
                        try
                        {
                            SimpleDateFormat current=new SimpleDateFormat("dd-MM-yyyy");
                            SimpleDateFormat required=new SimpleDateFormat("yyyy-MM-dd");
                            Date actual=current.parse(profileDob.getText().toString());
                            dob=required.format(actual);
                        }catch (ParseException e)
                        {
                            e.printStackTrace();
                        }
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("authkey", session.getAuthKey());
                        params.put("name", profileName.getText().toString());
                        params.put("dob", dob);
                        requester.StringRequesterFormValues(urls.updateProfile, Request.Method.POST, className, urls.updateProfile_methodName, params,UPDATE_REQUEST_TAG);
                    }else {
                        Display.DisplayToast(ProfileSettings.this, "Please enter valid Date of Birth");
                    }
                }else {
                    Display.DisplayToast(ProfileSettings.this, "Please enter valid Profile Name");
                }
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
        Log.d("Profile",session.getProfile());
        if(model1 != null) {
            profileName.setText(model1.userName);
            profileEmail.setText(model1.emailId);
            profilePhn.setText(model1.userMobile);
            SimpleDateFormat actualformat=new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat requiredformat=new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date Actual=actualformat.parse(model1.dateOfBirth);
                String Required=requiredformat.format(Actual);
                profileDob.setText(Required);
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

    public void getProfileData(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("authkey", session.getAuthKey());
        requester.StringRequesterFormValues(urls.getProfile, Request.Method.POST, className, urls.getProfile_methodName, params,GET_REQUEST_TAG);
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
                        ClickandPay.getInstance().ShouldUpdateUsername=true;
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

                        if(model1 != null) {
                            profileName.setText(model1.userName);
                            profileEmail.setText(model1.emailId);
                            profilePhn.setText(model1.userMobile);
                            SimpleDateFormat actualformat=new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat requiredformat=new SimpleDateFormat("dd-MM-yyyy");
                            try {
                                Date Actual=actualformat.parse(model1.dateOfBirth);
                                String Required=requiredformat.format(Actual);
                                profileDob.setText(Required);
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
        try {
            strDate = sdf.parse(day + "/" + month + "/" + year);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Display.DisplayLogI("ADITYA", "ERRE " + new Date().after(strDate));
        if (new Date().after(strDate)) {
            profileDob.setText(day + "/" + month + "/" + year);
        } else {
            Display.DisplayLogI("ADITYA", "ERRE");
            Display.DisplayToast(getApplicationContext(), "Date of Birth can not be a future date");
//            profileDob.setText("");
        }
    }

}
