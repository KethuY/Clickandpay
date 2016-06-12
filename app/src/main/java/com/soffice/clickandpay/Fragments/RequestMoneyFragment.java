package com.soffice.clickandpay.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContentResolverCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.soffice.clickandpay.Activities.MainActivity;
import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.Listeners.ContactsClickListener;
import com.soffice.clickandpay.Listeners.PageChangedToClearListener;
import com.soffice.clickandpay.NetWork.JsonRequester;
import com.soffice.clickandpay.NetWork.TaskListner;
import com.soffice.clickandpay.NetWork.Urls;
import com.soffice.clickandpay.Pojo.RequestMoneyResponseModel;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.Utilty.Constants;
import com.soffice.clickandpay.Utilty.Display;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.soffice.clickandpay.Utilty.SoftKeyboard;
import com.soffice.clickandpay.Utilty.SoftKeyboardStateHelper;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by sys2025 on 13/9/15.
 */
public class RequestMoneyFragment extends Fragment implements PageChangedToClearListener, ContactsClickListener, SoftKeyboardStateHelper.SoftKeyboardStateListener, TaskListner {
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String CODE_ENTERED = "CODE_ENTERED";
    public static String fromActivity = null;
    public static String code_in_QR = null;
    public static RequestMoneyFragment requestMoney;
    public static boolean canClear_request = false;
    private int mPage;
    LinearLayout utilities_layout_request,
            shopping_layout_request, home_layout_request, personal_layout_request;
    TextView Pay_addMoney, or;
    public static TextView walletBal_Home;
    EditText addMoney_EditText, sendMoney_contact_num, other_desc_editText_sendMoney;
    String className;
    TextView personal_TV_requestMoney, houserent_TV_requestMoney, shopping_TV_requestMoney,
            utilities_TV_requestMoney;
    ImageView personal_IV_requestMoney, houserent_IV_requestMoney, shopping_IV_requestMoney,
            utilities_IV_requestMoney, addContacts_IV;
    int Category=1;
    ClickandPay clickpay;
    SessionManager session;
    SoftKeyboardStateHelper softKeyboardStateHelper;
    JsonRequester requester;
    Urls urls;
    private final String REQUEST_TAG="request_tag";
    private final String MERCHANT_REQUEST_TAG="merchant_request_tag";
    private final int FETCH_CONTACT_REQUEST_CODE=369;
    MainActivity ParentContext;
    ProgressDialog progressDialog;

    public static RequestMoneyFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        RequestMoneyFragment fragment = new RequestMoneyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        ParentContext= (MainActivity) getActivity();
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.request_money_fragment, container, false);
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        clickpay = (ClickandPay) getActivity().getApplicationContext();
        session = clickpay.getSession();
        requester = new JsonRequester(this);
        urls = clickpay.getUrls();
        className = getActivity().getLocalClassName();
        requestMoney = this;

        Pay_addMoney = (TextView) view.findViewById(R.id.request_rupees);
        walletBal_Home = (TextView) view.findViewById(R.id.walletBal_Home);
        addMoney_EditText = (EditText) view.findViewById(R.id.requestMoney_EditText);
        sendMoney_contact_num = (EditText) view.findViewById(R.id.request_money_contact_num);
        other_desc_editText_sendMoney = (EditText) view.findViewById(R.id.request_other_desc_editText);
        personal_IV_requestMoney = (ImageView) view.findViewById(R.id.personal_IV_requestMoney);
        houserent_IV_requestMoney = (ImageView) view.findViewById(R.id.houserent_IV_requestMoney);
        shopping_IV_requestMoney = (ImageView) view.findViewById(R.id.shopping_IV_requestMoney);
        utilities_IV_requestMoney = (ImageView) view.findViewById(R.id.utilities_IV_requestMoney);
        personal_TV_requestMoney = (TextView) view.findViewById(R.id.personal_TV_requestMoney);
        houserent_TV_requestMoney = (TextView) view.findViewById(R.id.houserent_TV_requestMoney);
        shopping_TV_requestMoney = (TextView) view.findViewById(R.id.shopping_TV_requestMoney);
        utilities_TV_requestMoney = (TextView) view.findViewById(R.id.utilities_TV_requestMoney);
        personal_layout_request = (LinearLayout) view.findViewById(R.id.personal_layout_request);
        home_layout_request = (LinearLayout) view.findViewById(R.id.home_layout_request);
        shopping_layout_request = (LinearLayout) view.findViewById(R.id.shopping_layout_request);
        utilities_layout_request = (LinearLayout) view.findViewById(R.id.utilities_layout_request);
        addContacts_IV = (ImageView) view.findViewById(R.id.addContacts_IV_request);
        sendMoney_contact_num.setCursorVisible(false);

        sendMoney_contact_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMoney_contact_num.setCursorVisible(true);
            }
        });

        softKeyboardStateHelper = new SoftKeyboardStateHelper(view.findViewById(R.id.rootView));
        softKeyboardStateHelper.addSoftKeyboardStateListener(this);

        personal_layout_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category=1;
                personal_IV_requestMoney.setImageResource(R.mipmap.personal_active);
                personal_TV_requestMoney.setTextColor(getResources().getColor(R.color.colorPrimary));

                houserent_IV_requestMoney.setImageResource(R.mipmap.house_rent_inactive);
                houserent_TV_requestMoney.setTextColor(getResources().getColor(R.color.lightBlack));
                shopping_IV_requestMoney.setImageResource(R.mipmap.shoping_inactive);
                shopping_TV_requestMoney.setTextColor(getResources().getColor(R.color.lightBlack));
                utilities_IV_requestMoney.setImageResource(R.mipmap.utilities_inactive);
                utilities_TV_requestMoney.setTextColor(getResources().getColor(R.color.lightBlack));
            }
        });

        home_layout_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category=2;
                houserent_IV_requestMoney.setImageResource(R.mipmap.house_rent_active);
                houserent_TV_requestMoney.setTextColor(getResources().getColor(R.color.colorPrimary));

                personal_IV_requestMoney.setImageResource(R.mipmap.personal_inactive);
                personal_TV_requestMoney.setTextColor(getResources().getColor(R.color.lightBlack));
                shopping_IV_requestMoney.setImageResource(R.mipmap.shoping_inactive);
                shopping_TV_requestMoney.setTextColor(getResources().getColor(R.color.lightBlack));
                utilities_IV_requestMoney.setImageResource(R.mipmap.utilities_inactive);
                utilities_TV_requestMoney.setTextColor(getResources().getColor(R.color.lightBlack));
            }
        });

        shopping_layout_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category=3;
                shopping_IV_requestMoney.setImageResource(R.mipmap.shoping_active);
                shopping_TV_requestMoney.setTextColor(getResources().getColor(R.color.colorPrimary));

                personal_IV_requestMoney.setImageResource(R.mipmap.personal_inactive);
                personal_TV_requestMoney.setTextColor(getResources().getColor(R.color.lightBlack));
                houserent_IV_requestMoney.setImageResource(R.mipmap.house_rent_inactive);
                houserent_TV_requestMoney.setTextColor(getResources().getColor(R.color.lightBlack));
                utilities_IV_requestMoney.setImageResource(R.mipmap.utilities_inactive);
                utilities_TV_requestMoney.setTextColor(getResources().getColor(R.color.lightBlack));

            }
        });

        utilities_layout_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category=4;
                utilities_IV_requestMoney.setImageResource(R.mipmap.utilities_active);
                utilities_TV_requestMoney.setTextColor(getResources().getColor(R.color.colorPrimary));

                personal_IV_requestMoney.setImageResource(R.mipmap.personal_inactive);
                personal_TV_requestMoney.setTextColor(getResources().getColor(R.color.lightBlack));
                houserent_IV_requestMoney.setImageResource(R.mipmap.house_rent_inactive);
                houserent_TV_requestMoney.setTextColor(getResources().getColor(R.color.lightBlack));
                shopping_IV_requestMoney.setImageResource(R.mipmap.shoping_inactive);
                shopping_TV_requestMoney.setTextColor(getResources().getColor(R.color.lightBlack));
            }
        });

        addContacts_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=23) {
                    CheckLocationPermission();
                }
                else
                {
                    PickContact();
                }
            }
        });

        Pay_addMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Display.DisplayLogI("ADITYA", "Request Money");
                if (sendMoney_contact_num != null && sendMoney_contact_num.length() == 10) {
                    if (sendMoney_contact_num.getText().toString().charAt(0) == '7' || sendMoney_contact_num.getText().toString().charAt(0) == '8' || sendMoney_contact_num.getText().toString().charAt(0) == '9') {
                        if (addMoney_EditText.getText().toString().length() > 0) {
                                if(Double.parseDouble(addMoney_EditText.getText().toString())>=Constants.MIN_SEND_REQ_MONEY&&Double.parseDouble(addMoney_EditText.getText().toString())<=Constants.MAX_SEND_REQ_MONEY) {
                                    //                        if (other_desc_editText_sendMoney.getText().toString().length() > 0) {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("authkey", session.getAuthKey());
                                    params.put("mobile", sendMoney_contact_num.getText().toString());
                                    params.put("amount", addMoney_EditText.getText().toString());
                                    params.put("desc", other_desc_editText_sendMoney.getText().toString());
                                    Pay_addMoney.setEnabled(false);
                                    Pay_addMoney.setText("Please wait..");
                                    requester.StringRequesterFormValues(urls.requestMoney, Request.Method.POST, className, urls.requestMoney_methodName, params, REQUEST_TAG);
                                    //                        } else {
                                    //                            Display.DisplayToast(getActivity(), "Enter Descrition");
                                    //                        }
                                    DisplayDialog();
                                }
                                else
                                {
                                    ParentContext.DisplaySnackMessage("Currently you can only request an amount between ₹ 1 to ₹ 5000");
                                }
                        } else {
                            ParentContext.DisplaySnackMessage("Please enter amount to request");
                        }
                    } else {
                        ParentContext.DisplaySnackMessage("Please enter valid mobile number starts with 7 / 8 / 9");
                    }
                } else {
                    ParentContext.DisplaySnackMessage("Please enter mobile number to request the money.");
                }
            }
        });

        walletBal_Home.setText(session.getWalletBal());

        addMoney_EditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!addMoney_EditText.getText().toString().contains(".")) {
                        if (addMoney_EditText.getText().toString().length() == 0) {
                            addMoney_EditText.setText(addMoney_EditText.getText().toString() + "0.00");
                        } else {
                            addMoney_EditText.setText(addMoney_EditText.getText().toString() + ".00");
                        }
                    } else {
                        String[] str = addMoney_EditText.getText().toString().split("\\.");
                        if (str.length > 1) {
                            if (str[1].toString().length() == 1) {
                                addMoney_EditText.setText(addMoney_EditText.getText().toString() + "0");
                            } else if (str[1].toString().length() > 2) {
                                addMoney_EditText.setText(str[0] + "." + str[1].substring(0, 2));
                                Display.DisplayLogI("ADITYA", "str[1].substring(0, 2) " + str[1].substring(0, 2));
                            } else if (str[1].toString().length() == 0) {
                                addMoney_EditText.setText(addMoney_EditText.getText().toString() + ".00");
                            }
                        }
                    }
                }else{
                    addMoney_EditText.setText("");
                }
            }
        });

        addMoney_EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                if (addMoney_EditText.getText().toString().length() == 0) {
//                    addMoney_EditText.setText("");
//                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    Display.DisplayLogI("ADITYA", " AMOUNTTT " + s.toString());
//                    String test = String.format("%.02f", Float.parseFloat(s.toString()));
//                    addMoney_EditText.setText(test);
                    if (addMoney_EditText.getText().toString().contains(".")) {
                        s.toString().split(".");
                        Display.DisplayLogI("ADITYA", " SPLITTT " + s.toString().split("\\.").length);
                        String[] str = addMoney_EditText.getText().toString().split("\\.");
                        if (str.length > 1) {
                            Display.DisplayLogI("ADITYA", " SPLITTT PARTS " + str[0]+ "  "+str[1]);
                            if (str[1].toString().length() > 2) {
                                addMoney_EditText.setText(str[0] + "." + str[1].substring(0, 2));
                                Display.DisplayLogI("ADITYA", "str[1].substring(0, 2) " + str[1].substring(0, 2));
                                removePhoneKeypad();
                                addMoney_EditText.clearFocus();
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    addMoney_EditText.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!isVisibleToUser)
        {
            if(addMoney_EditText!=null) {
                addMoney_EditText.setText("");
                sendMoney_contact_num.setText("");
                Pay_addMoney.setEnabled(true);
                walletBal_Home.setText(session.getWalletBal());
                removePhoneKeypad();
            }
        }
        else
        {
            if(walletBal_Home!=null)
            {
                if(session!=null) {
                    walletBal_Home.setText(session.getWalletBal());
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Display.DisplayLogI("ADITYA", "REQUEST RESUME " + canClear_request);
        Pay_addMoney.setEnabled(true);
        if (canClear_request) {
            addMoney_EditText.setText("");
            sendMoney_contact_num.setText("");
        }
        walletBal_Home.setText(session.getWalletBal());
        removePhoneKeypad();
    }

    /**Need to check for Android Version 6.0 and Above (RUN TIME PERMISSION CHECK) **/
    private void CheckLocationPermission() {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_CONTACTS))
            {
                Snackbar localSnackbar = Snackbar.make(ParentContext.coordinatorLayout, "App requires an additional permission to complete the registration process", Snackbar.LENGTH_LONG);
                localSnackbar.setAction("Grant", new View.OnClickListener()
                {
                    public void onClick(View paramAnonymousView)
                    {
                        ActivityCompat.requestPermissions(RequestMoneyFragment.this.getActivity(), new String[] { "android.permission.READ_CONTACTS" }, 535);
                    }
                });
                localSnackbar.setActionTextColor(Color.WHITE);
                localSnackbar.show();
            }
            else
            {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS},535);
            }
            return;
        }
        PickContact();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==535){
            if(permissions.length==1&&permissions[0].equals(Manifest.permission.READ_CONTACTS)&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                PickContact();
            }
            else
            {
                ParentContext.DisplaySnackMessage("Unable to retrieve contacts.");
            }
        }
    }

    private void DismissDialog()
    {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
    }

    private void DisplayDialog()
    {
        if (this.progressDialog == null)
        {
            this.progressDialog = new ProgressDialog(getActivity());
            this.progressDialog.setCancelable(false);
            this.progressDialog.setMessage("Please wait..");
        }
        this.progressDialog.show();
    }

    private void PickContact()
    {
        Intent intent=new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent,FETCH_CONTACT_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==FETCH_CONTACT_REQUEST_CODE&&resultCode==getActivity().RESULT_OK)
        {
            Uri uriOfPhoneNumberRecord = data.getData();
            String idOfPhoneRecord = uriOfPhoneNumberRecord.getLastPathSegment();
            Cursor cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, ContactsContract.CommonDataKinds.Phone._ID + "=?", new String[]{idOfPhoneRecord}, null);
            if(cursor != null) {
                if(cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    String formattedPhoneNumber = cursor.getString( cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    formattedPhoneNumber=formattedPhoneNumber.replace(" ","");
                    formattedPhoneNumber=formattedPhoneNumber.replace("-","");
                    Display.DisplayLogD("Num",formattedPhoneNumber);
                    if(formattedPhoneNumber.contains("+91"))
                    {
                        formattedPhoneNumber=formattedPhoneNumber.substring(3,formattedPhoneNumber.length());
                    }
                    else if(String.valueOf(formattedPhoneNumber.charAt(0)).equalsIgnoreCase("0"))
                    {
                        formattedPhoneNumber=formattedPhoneNumber.substring(1,formattedPhoneNumber.length());
                    }
                    sendMoney_contact_num.setText(formattedPhoneNumber);
                    Log.d("TestActivity", String.format("The selected phone number is: %s", formattedPhoneNumber));
                }
                cursor.close();
            }
        }
    }


    @Override
    public void onSoftKeyboardOpened(int keyboardHeightInPx) {

    }

    @Override
    public void onSoftKeyboardClosed() {
        addMoney_EditText.clearFocus();
//        addMoney_EditText.setCursorVisible(false);
    }

    public void removePhoneKeypad() {

        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus()
                    .getWindowToken(), 0);
        }
    }

    @Override
    public void onTaskfinished(String response, int cd, String _className, String _methodName) {
        Display.DisplayLogI("ADITYA", "" + response);
        DismissDialog();
        try {
            if (cd == 00) {
                ParentContext.DisplaySnackMessage(response);
            } else if (cd == 05) {
                if (_className.equalsIgnoreCase(className) && _methodName.equalsIgnoreCase(urls.requestMoney_methodName)) {
                    Gson g = new Gson();
                    Pay_addMoney.setEnabled(true);
                    RequestMoneyResponseModel model = g.fromJson(response, RequestMoneyResponseModel.class);
                    Display.DisplayLogI("ADITYA code", model.code);
                    if (model.code.equalsIgnoreCase("200")) {
                        ParentContext.DisplaySnackMessage(model.message);
                        addMoney_EditText.setText("");
                        sendMoney_contact_num.setText("");
                    } else {
                        ParentContext.DisplaySnackMessage(model.message);
                    }
                }
            }
            Pay_addMoney.setEnabled(true);
            Pay_addMoney.setText("Request");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onContactsItemClick(String name, String number) {
        sendMoney_contact_num.setText(number);
    }

    @Override
    public void clearAll() {
        Display.DisplayLogI("ADITYA", "REQUEST CLEAR " + canClear_request);
        if (canClear_request) {
            addMoney_EditText.setText("");
            sendMoney_contact_num.setText("");
        }
        removePhoneKeypad();
    }
}
