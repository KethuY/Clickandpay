package com.soffice.clickandpay.Activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.NetWork.JsonRequester;
import com.soffice.clickandpay.NetWork.TaskListner;
import com.soffice.clickandpay.NetWork.Urls;
import com.soffice.clickandpay.Pojo.GetProfileResonseModel;
import com.soffice.clickandpay.Pojo.SignInResponseModel;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.UI.RobotoLightTextView;
import com.soffice.clickandpay.Utilty.Display;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.soffice.clickandpay.Utilty.Utils;
import com.soffice.clickandpay.database.DataBaseManager;
import com.soffice.clickandpay.database.DataCenter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements TaskListner {

    AppCompatButton getStartedLayout;
    RobotoLightTextView ForgotPassword;
    EditText password_EditText, userId_EditText;
    ClickandPay clickpay;
    SessionManager session;
    JsonRequester requester;
    Urls urls;
    String className;
    ImageView back_IV;
    /*DataCenter datacenter;*/
    SQLiteDatabase mYDB;
    Cursor pCur = null;
    int which;
    String OTP;
    /*InsertsmsData task;*/
    private final String REQUEST_TAG="login_request";
    private final String PROFILE_REQUEST_TAG="profile_request_tag";
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        coordinatorLayout= (CoordinatorLayout) findViewById(R.id.mycoordinator);
        getStartedLayout= (AppCompatButton) findViewById(R.id.getStartedLayout);
        Utils.setButtonTint(getStartedLayout, ContextCompat.getColorStateList(this,R.color.button_tint_white));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        clickpay = (ClickandPay) getApplicationContext();
        session = clickpay.getSession();
        className = getLocalClassName();
        requester = new JsonRequester(this);
        urls = clickpay.getUrls();
        /*datacenter = clickpay.getDatacenter();
        mYDB = DataBaseManager.getInstance().openDatabase();
        task = new InsertsmsData();
        task.execute();*/
        userId_EditText = (EditText) findViewById(R.id.userId_EditText);
        password_EditText = (EditText) findViewById(R.id.password_EditText);
        back_IV = (ImageView) findViewById(R.id.back_TV);
        ForgotPassword= (RobotoLightTextView) findViewById(R.id.forgot_password_tv);
        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,ForgetPassWordOrPassCodeActivity.class);
                intent.putExtra("which",1);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        getStartedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(userId_EditText.getText().toString().length() > 0){
                        if(isNumeric(userId_EditText.getText().toString())){
                            session.setMobileNum(userId_EditText.getText().toString());
                            if(userId_EditText.getText().toString().length() == 10){
                                if(userId_EditText.getText().toString().charAt(0) == '7' || userId_EditText.getText().toString().charAt(0) == '8' || userId_EditText.getText().toString().charAt(0) == '9'){
                                }else {
                                    Display.DisplaySnackbar(LoginActivity.this,coordinatorLayout, "Please enter valid mobile number starts with 7, 8, 9");
                                }
                            }else{
                                Display.DisplaySnackbar(LoginActivity.this,coordinatorLayout, "Please Enter valid Mobile Number");
                                return;
                            }
                        }else{
                            if(isEmailValid(userId_EditText.getText().toString())){
                                session.setEmailId(userId_EditText.getText().toString());
                            }else{
                                Display.DisplaySnackbar(LoginActivity.this,coordinatorLayout, "Please Enter valid Email");
                                return;
                            }
                        }

                    if(password_EditText.getText().toString().length() > 0){
                        if(Build.VERSION.SDK_INT>=23)
                        {
                            CheckPermissions();
                        }
                        else
                        {
                            ServerCall();
                        }

                    }else{
                        Display.DisplaySnackbar(LoginActivity.this,coordinatorLayout, "Please Enter Password");
                    }
                }else{
                    Display.DisplaySnackbar(LoginActivity.this, coordinatorLayout,"Please Enter Mobile Number / Email");
                }
            }
        });

        back_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(getIntent().getStringExtra("fromActivity") != null && getIntent().getStringExtra("fromActivity").equalsIgnoreCase("Initial")) {
                if(getIntent().getStringExtra("from") != null && getIntent().getStringExtra("from").equalsIgnoreCase("regiter")){
                    finish();
                }else{
                    Intent i = new Intent(LoginActivity.this, InitialActivity.class);
                    i.putExtra("fromActivity", "Login");
                    startActivity(i);
                    finish();
                }
//                }else{
//                    finish();
//                }
            }
        });
    }

    private void CheckPermissions()
    {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.READ_PHONE_STATE") != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_PHONE_STATE"))
            {
                Snackbar snackbar = Snackbar.make(this.coordinatorLayout, "App requires an additional permission to complete the login process", Snackbar.LENGTH_SHORT);
                snackbar.setAction("Grant", new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        ActivityCompat.requestPermissions(LoginActivity.this, new String[] { "android.permission.READ_PHONE_STATE" }, 535);
                    }
                });
                snackbar.setActionTextColor(Color.WHITE);
                snackbar.show();
                return;
            }
            ActivityCompat.requestPermissions(this, new String[] { "android.permission.READ_PHONE_STATE" }, 535);
        }
        else {
            ServerCall();
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 535)
        {
            if (permissions.length != 1) {
                Display.DisplaySnackbar(this, this.coordinatorLayout, "Unable to complete login  process.");
                return;
            }
            if ((permissions[0].equals("android.permission.READ_PHONE_STATE")) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                ServerCall();
            }
        }
        else
        {
            Display.DisplaySnackbar(this, this.coordinatorLayout, "Unable to complete login process.");
        }
    }

    private void ServerCall(){
        if(Utils.CheckInternet(LoginActivity.this)) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("userId", userId_EditText.getText().toString());
            params.put("password", Utils.EncryptData(password_EditText.getText().toString()));
            getStartedLayout.setEnabled(false);
            getStartedLayout.setTextColor(ContextCompat.getColor(LoginActivity.this, android.R.color.white));
            getStartedLayout.setText("Signing in..");
            requester.StringRequesterFormValues(urls.userSignIn, Request.Method.POST, className, urls.userSignIn_methodName, params, REQUEST_TAG);
        }
        else
        {
            Display.DisplaySnackbar(LoginActivity.this,coordinatorLayout,"Please check your internet connection and try again");
        }
    }

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if(getIntent().getStringExtra("fromActivity") != null && getIntent().getStringExtra("fromActivity").equalsIgnoreCase("Initial")) {
            Intent i = new Intent(LoginActivity.this, InitialActivity.class);
            i.putExtra("fromActivity", "Login");
            startActivity(i);
            finish();
//        }else{
//            finish();
//        }
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
    public void getProfileData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("authkey", session.getAuthKey());
        requester.StringRequesterFormValues(urls.getProfile, Request.Method.POST, className, urls.getProfile_methodName, params,PROFILE_REQUEST_TAG);

    }

    @Override
    public void onTaskfinished(String response, int cd, String _className, String _methodName) {
        Display.DisplayLogI("ADITYA", "" + response);
        try {
            if (cd == 00) {
                Display.DisplayToast(this, response);
                getStartedLayout.setEnabled(true);
                getStartedLayout.setText("Sign in");
                getStartedLayout.setTextColor(ContextCompat.getColor(LoginActivity.this,R.color.colorPrimary));
            } else if (cd == 05) {
                if (_className.equalsIgnoreCase(className) && _methodName.equalsIgnoreCase(urls.userSignIn_methodName)) {
                    Gson g = new Gson();
                    SignInResponseModel model = g.fromJson(response, SignInResponseModel.class);
                    Display.DisplayLogI("ADITYA code", model.code);
                    if (model.code.equalsIgnoreCase("200")) {
                        session.setAuthKey(model.auth_key);
                        if (model.USER_STATUS.equalsIgnoreCase("0")) {
                            session.setIsLoggedIn(false);
                            which=1;
                        } else if (model.USER_STATUS.equalsIgnoreCase("1")) {
                            session.setIsLoggedIn(true);
                            which=2;
                        } else if (model.USER_STATUS.equalsIgnoreCase("2")) {
                            which=3;
                            session.setIsLoggedIn(false);
                            OTP=model.OTP;
                        }
                        getProfileData();
                    } else {
                        getStartedLayout.setEnabled(true);
                        getStartedLayout.setText("Sign in");
                        getStartedLayout.setTextColor(ContextCompat.getColor(LoginActivity.this,R.color.colorPrimary));
                        Display.DisplaySnackbar(this, this.coordinatorLayout, model.message);

                    }
                }
                else if(_className.equalsIgnoreCase(className)&&_methodName.equalsIgnoreCase(Urls.getProfile_methodName))
                {
                    Gson g = new Gson();
                    GetProfileResonseModel model = g.fromJson(response, GetProfileResonseModel.class);
                    Display.DisplayLogI("ADITYA code", model.code);
                    if (model.code.equalsIgnoreCase("200")) {
                        Display.DisplayLogI("ADITYA", "GET PROFILE : " + g.toJson(model.profile.get(0)));
                        session.setProfile(g.toJson(model.profile.get(0)));
                        session.setUserName(model.profile.get(0).userName);
                        session.setDOB(model.profile.get(0).dateOfBirth);
                        session.setReferralCode(model.profile.get(0).userReferralCode);
                        session.setDeviceId(model.profile.get(0).deviceid);
                        session.setMobileNum(model.profile.get(0).userMobile);
                        LaunchPassCodeActivity(which);
                    }
                    if (model.code.equalsIgnoreCase("205"))
                    {
                        ClickandPay.getInstance().RedirectToLogin();
                        Display.DisplaySnackbar(this, this.coordinatorLayout, model.message);
                        return;
                    }else {
                        Display.DisplaySnackbar(LoginActivity.this,coordinatorLayout, model.message);
                        getStartedLayout.setEnabled(true);
                        getStartedLayout.setText("Sign in");
                        getStartedLayout.setTextColor(ContextCompat.getColor(LoginActivity.this,R.color.colorPrimary));
                        Display.DisplaySnackbar(this, this.coordinatorLayout, model.message);
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void LaunchPassCodeActivity(int which)
    {
        if(which==1||which==2) {
            Intent i = new Intent(LoginActivity.this, PassCodeActivity.class);
            session.setIsVerifiedUser(true);
            i.putExtra("fromActivity", which == 1 ? "Login_veri" : "Login");
            startActivity(i);
        }
        else
        {
            Intent i = new Intent(LoginActivity.this, VerificationActivity.class);
            i.putExtra("VERIFY_CODE", OTP);
            i.putExtra("fromActivity", "Login");
            startActivity(i);
        }
            finish();
    }


    /************
     * INSERT SMS AND CONTACTS INTO DATABASE
     ************/

    /*class InsertsmsData extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            ContentResolver cr = getContentResolver();
            try {
                pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID, null,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE NOCASE");
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    if (datacenter.insertContacts(pCur, mYDB)) {
                        Display.DisplayLogI("ADITYA", "Inserting Data Lower");
                    }
                } else {
                    if (datacenter.insertContacts(pCur, mYDB)) {
                        Display.DisplayLogI("ADITYA", "Inserting Data");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (pCur != null) {
                    pCur.close();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                Display.DisplayLogI("ADITYA", "SMS AND CONTACTS DUMP STARTED");
                DataBaseManager.getInstance().openDatabase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                Cursor c = datacenter.getContacts(mYDB);
                Display.DisplayLogI("ADITYA", "COUNT CONTACTS DUMP " + c.getCount());
                DataBaseManager.getInstance().closeDatabase();
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
            Display.DisplayLogI("ADITYA", "SMS AND CONTACTS DUMP DONE");
            new ContacstSyncUpdateInitializationTask().execute();
        }

    }

    class ContacstSyncUpdateInitializationTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            ContacstSyncUpdateInitialization();
            return null;
        }
    }

    public void ContacstSyncUpdateInitialization() {
        try {
            ContentResolver cr = getApplicationContext().getContentResolver();
            Cursor pCur1 = cr.query(ContactsContract.RawContacts.CONTENT_URI, new String[]{ContactsContract.RawContacts.VERSION, ContactsContract.RawContacts.CONTACT_ID, ContactsContract.RawContacts.DELETED, ContactsContract.RawContacts.STARRED}, null, null, ContactsContract.RawContacts.CONTACT_ID + " ASC");
            Long maxContactid = null;
            JSONObject sessionContactlist = new JSONObject(), contact;
            ArrayList<Long> keyslist = new ArrayList<>();
            String Contact_idlist = "";
            while (pCur1.moveToNext()) {
                Long contact_Id = pCur1.getLong(pCur1.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID));
                String mainversion = pCur1.getString(pCur1.getColumnIndex(ContactsContract.RawContacts.VERSION));
                String starred = pCur1.getString(pCur1.getColumnIndex(ContactsContract.RawContacts.STARRED));
                if (keyslist.contains(contact_Id)) {

                    String version = sessionContactlist.get("" + contact_Id).toString();
                    version = version + "," + mainversion + " - " + starred;
                    sessionContactlist.put("" + contact_Id, version);

                } else {
                    if (contact_Id != 0) {
                        maxContactid = contact_Id;
                        keyslist.add(contact_Id);
                        sessionContactlist.put("" + contact_Id, "" + mainversion + " - " + starred);
                        if (Contact_idlist.length() == 0) {
                            Contact_idlist = "" + contact_Id;
                        } else {
                            Contact_idlist = Contact_idlist + "," + contact_Id;
                        }
                    }
                }
            }

            session.setMaxContact("" + maxContactid);
            session.setStoredContactlist(Contact_idlist);
            session.setStoredVersionsList(sessionContactlist.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ClickandPay.getInstance().cancelPendingRequests(REQUEST_TAG);
        ClickandPay.getInstance().cancelPendingRequests(PROFILE_REQUEST_TAG);
    }
}
