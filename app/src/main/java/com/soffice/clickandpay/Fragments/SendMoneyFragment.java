package com.soffice.clickandpay.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
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
import com.soffice.clickandpay.Activities.AddMoneyActivity;
import com.soffice.clickandpay.Activities.MainActivity;
import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.Listeners.ContactsClickListener;
import com.soffice.clickandpay.NetWork.JsonRequester;
import com.soffice.clickandpay.NetWork.TaskListner;
import com.soffice.clickandpay.NetWork.Urls;
import com.soffice.clickandpay.Pojo.SendMoneyResponseModel;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.UI.RobotoLightTextView;
import com.soffice.clickandpay.UI.RobotoRegularTextView;
import com.soffice.clickandpay.Utilty.Constants;
import com.soffice.clickandpay.Utilty.Display;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.soffice.clickandpay.Utilty.SoftKeyboard;
import com.soffice.clickandpay.Utilty.SoftKeyboardStateHelper;
import com.soffice.clickandpay.Utilty.Utils;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by sys2025 on 13/9/15.
 */
public class SendMoneyFragment extends Fragment implements  ContactsClickListener, SoftKeyboardStateHelper.SoftKeyboardStateListener, TaskListner {
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String CODE_ENTERED = "CODE_ENTERED";
    public static String fromActivity = null;
    public static String code_in_QR = null;
    public static SendMoneyFragment sendMoney;
    public static boolean canClear_send = false;
    private final String SEND_REQUEST_TAG="send_request_tag";
    private final String MERCHANT_REQUEST_TAG="merchant_request_tag";
    Dialog AddMoneyDialog;
    AppCompatButton AddMoneyBtn;
    AppCompatButton CancelBtn;
    private int mPage;
    LinearLayout personal_layout, home_layout,
            shopping_layout, utilities_layout;
    TextView Pay_addMoney, or, personal_TV_sendMoney, houserent_TV_sendMoney,
            shopping_TV_sendMoney, utilities_TV_sendMoney;
    public static TextView walletBal_Home;
    EditText addMoney_EditText, sendMoney_contact_num, other_desc_editText_sendMoney;
    ImageView personal_IV_sendMoney, houserent_IV_sendMoney, shopping_IV_sendMoney,
            utilities_IV_sendMoney, addContacts_IV;
    ClickandPay clickpay;
    SessionManager session;
    SoftKeyboardStateHelper softKeyboardStateHelper;
    JsonRequester requester;
    Urls urls;
    boolean IsUIBuilt=false;
    private final int FETCH_CONTACT_REQUEST_CODE=368;
    int Category=1;
    String className;
    MainActivity ParentContext;
    RobotoRegularTextView DialogTitle;
    RobotoLightTextView DialogMsg;
    ProgressDialog progressDialog;



    public static SendMoneyFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        SendMoneyFragment fragment = new SendMoneyFragment();
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
        View view = inflater.inflate(R.layout.send_money_fragment, container, false);
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        clickpay = (ClickandPay) getActivity().getApplicationContext();
        session = clickpay.getSession();
        requester = new JsonRequester(this);
        urls = clickpay.getUrls();
        className = getActivity().getLocalClassName();
        sendMoney = this;
        Pay_addMoney = (TextView) view.findViewById(R.id.Send_rupees);
        walletBal_Home = (TextView) view.findViewById(R.id.walletBal_Home);
        addMoney_EditText = (EditText) view.findViewById(R.id.sendMoney_EditText);
        sendMoney_contact_num = (EditText) view.findViewById(R.id.sendMoney_contact_num);
        other_desc_editText_sendMoney = (EditText) view.findViewById(R.id.other_desc_editText_sendMoney);
        personal_IV_sendMoney = (ImageView) view.findViewById(R.id.personal_IV_sendMoney);
        houserent_IV_sendMoney = (ImageView) view.findViewById(R.id.houserent_IV_sendMoney);
        shopping_IV_sendMoney = (ImageView) view.findViewById(R.id.shopping_IV_sendMoney);
        utilities_IV_sendMoney = (ImageView) view.findViewById(R.id.utilities_IV_sendMoney);
        personal_TV_sendMoney = (TextView) view.findViewById(R.id.personal_TV_sendMoney);
        houserent_TV_sendMoney = (TextView) view.findViewById(R.id.houserent_TV_sendMoney);
        shopping_TV_sendMoney = (TextView) view.findViewById(R.id.shopping_TV_sendMoney);
        utilities_TV_sendMoney = (TextView) view.findViewById(R.id.utilities_TV_sendMoney);
        personal_layout = (LinearLayout) view.findViewById(R.id.personal_layout);
        home_layout = (LinearLayout) view.findViewById(R.id.home_layout);
        shopping_layout = (LinearLayout) view.findViewById(R.id.shopping_layout);
        utilities_layout = (LinearLayout) view.findViewById(R.id.utilities_layout);
        addContacts_IV = (ImageView) view.findViewById(R.id.addContacts_IV);
        sendMoney_contact_num.setCursorVisible(false);
        sendMoney_contact_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMoney_contact_num.setCursorVisible(true);
            }
        });
        softKeyboardStateHelper = new SoftKeyboardStateHelper(view.findViewById(R.id.rootView));
        softKeyboardStateHelper.addSoftKeyboardStateListener(this);

        personal_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category=1;
                personal_IV_sendMoney.setImageResource(R.mipmap.personal_active);
                personal_TV_sendMoney.setTextColor(getResources().getColor(R.color.colorPrimary));

                houserent_IV_sendMoney.setImageResource(R.mipmap.house_rent_inactive);
                houserent_TV_sendMoney.setTextColor(getResources().getColor(R.color.lightBlack));
                shopping_IV_sendMoney.setImageResource(R.mipmap.shoping_inactive);
                shopping_TV_sendMoney.setTextColor(getResources().getColor(R.color.lightBlack));
                utilities_IV_sendMoney.setImageResource(R.mipmap.utilities_inactive);
                utilities_TV_sendMoney.setTextColor(getResources().getColor(R.color.lightBlack));
            }
        });

        home_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category=2;
                houserent_IV_sendMoney.setImageResource(R.mipmap.house_rent_active);
                houserent_TV_sendMoney.setTextColor(getResources().getColor(R.color.colorPrimary));

                personal_IV_sendMoney.setImageResource(R.mipmap.personal_inactive);
                personal_TV_sendMoney.setTextColor(getResources().getColor(R.color.lightBlack));
                shopping_IV_sendMoney.setImageResource(R.mipmap.shoping_inactive);
                shopping_TV_sendMoney.setTextColor(getResources().getColor(R.color.lightBlack));
                utilities_IV_sendMoney.setImageResource(R.mipmap.utilities_inactive);
                utilities_TV_sendMoney.setTextColor(getResources().getColor(R.color.lightBlack));
            }
        });

        shopping_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category=3;
                shopping_IV_sendMoney.setImageResource(R.mipmap.shoping_active);
                shopping_TV_sendMoney.setTextColor(getResources().getColor(R.color.colorPrimary));

                personal_IV_sendMoney.setImageResource(R.mipmap.personal_inactive);
                personal_TV_sendMoney.setTextColor(getResources().getColor(R.color.lightBlack));
                houserent_IV_sendMoney.setImageResource(R.mipmap.house_rent_inactive);
                houserent_TV_sendMoney.setTextColor(getResources().getColor(R.color.lightBlack));
                utilities_IV_sendMoney.setImageResource(R.mipmap.utilities_inactive);
                utilities_TV_sendMoney.setTextColor(getResources().getColor(R.color.lightBlack));

            }
        });

        utilities_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category=4;
                utilities_IV_sendMoney.setImageResource(R.mipmap.utilities_active);
                utilities_TV_sendMoney.setTextColor(getResources().getColor(R.color.colorPrimary));

                personal_IV_sendMoney.setImageResource(R.mipmap.personal_inactive);
                personal_TV_sendMoney.setTextColor(getResources().getColor(R.color.lightBlack));
                houserent_IV_sendMoney.setImageResource(R.mipmap.house_rent_inactive);
                houserent_TV_sendMoney.setTextColor(getResources().getColor(R.color.lightBlack));
                shopping_IV_sendMoney.setImageResource(R.mipmap.shoping_inactive);
                shopping_TV_sendMoney.setTextColor(getResources().getColor(R.color.lightBlack));
            }
        });

        Pay_addMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeverCall(1);
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
                            Display.DisplayLogI("ADITYA", " SPLITTT PARTS " + str[0] + "  " + str[1]);
                            if (str[1].toString().length() > 2) {
                                addMoney_EditText.setText(str[0] + "." + str[1].substring(0, 2));
                                Display.DisplayLogI("ADITYA", "str[1].substring(0, 2) " + str[1].substring(0, 2));
                                removePhoneKeypad();
                                addMoney_EditText.clearFocus();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    addMoney_EditText.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        IsUIBuilt=true;
        return view;
    }



    /**Need to check for Android Version 6.0 and Above (RUN TIME PERMISSION CHECK) **/
    private void CheckLocationPermission() {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_CONTACTS))
            {
                Snackbar localSnackbar = Snackbar.make(ParentContext.coordinatorLayout, "App requires an additional permission to read contact", Snackbar.LENGTH_LONG);
                localSnackbar.setAction("Grant", new View.OnClickListener()
                {
                    public void onClick(View paramAnonymousView)
                    {
                        ActivityCompat.requestPermissions(SendMoneyFragment.this.getActivity(), new String[] { "android.permission.READ_CONTACTS" }, 535);
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
        else {
            PickContact();
        }
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
        Display.DisplayLogI("ADITYA", "SEND RESUME " + canClear_send);

    }

    public void checkMerchant(String code) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("poscode", code);
        requester.StringRequesterFormValues(urls.merchant, Request.Method.POST, className, urls.merchant_methodName, params,MERCHANT_REQUEST_TAG);
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

    private void SeverCall(int Flag)
    {
        if(Utils.CheckInternet(getActivity())) {
            Display.DisplayLogI("ADITYA", "Send Money");
                    if (sendMoney_contact_num != null && sendMoney_contact_num.length() == 10) {
                        if (sendMoney_contact_num.getText().toString().charAt(0) == '7' || sendMoney_contact_num.getText().toString().charAt(0) == '8' || sendMoney_contact_num.getText().toString().charAt(0) == '9') {
                            if (addMoney_EditText.getText().length() > 0) {
                                if (Double.parseDouble(addMoney_EditText.getText().toString()) >= Constants.MIN_SEND_REQ_MONEY && Double.parseDouble(addMoney_EditText.getText().toString()) <= Constants.MAX_SEND_REQ_MONEY) {
                                if (Double.parseDouble(addMoney_EditText.getText().toString()) <= Double.parseDouble(session.getWalletBal())) {
                                    //                        if (other_desc_editText_sendMoney.getText().toString().length() > 0) {
                                    DisplayDialog();
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("authkey", session.getAuthKey());
                                    params.put("mobile", sendMoney_contact_num.getText().toString());
                                    params.put("amount", addMoney_EditText.getText().toString());
                                    params.put("category",String.valueOf(Category));
                                    params.put("desc", "" + other_desc_editText_sendMoney.getText().toString());
                                    Pay_addMoney.setEnabled(false);
                                    requester.StringRequesterFormValues(Flag == 1 ? urls.sendMoney : urls.BufferSendMoney, Request.Method.POST, className, urls.sendMoney_methodName, params, SEND_REQUEST_TAG);
                                    //                        } else {
                                    //                            Display.DisplayToast(getActivity(), "Enter Descrition");
                                    //                        }

                                } else {
                                    DisplayAddmoneyDialog(1);
                                }
                        } else {
                                    ParentContext.DisplaySnackMessage("Currently you can only send an amount between ₹ 1 to ₹ 5000");
                                }
                    } else {
                                ParentContext.DisplaySnackMessage("Please enter amount to send");
                            }
                } else {
                            ParentContext.DisplaySnackMessage("Please enter valid mobile number starts with 7 / 8 / 9");
                        }
            } else {
                        ParentContext.DisplaySnackMessage("Please enter mobile number to send.");
                    }
        }
        else {
            ParentContext.DisplaySnackMessage("Please check your internet connection and try again.");
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
                if (_className.equalsIgnoreCase(className) && _methodName.equalsIgnoreCase(urls.sendMoney_methodName)) {
                    Gson g = new Gson();
                    Pay_addMoney.setEnabled(true);
                    SendMoneyResponseModel model = g.fromJson(response, SendMoneyResponseModel.class);
                    Display.DisplayLogI("ADITYA code", model.code);
                    if (model.code.equalsIgnoreCase("200")) {
                        if (!model.WALLET_BAL.contains(".")) {
                            session.setWalletBal(model.WALLET_BAL + ".00");
                        }else{
                            session.setWalletBal(model.WALLET_BAL);
                        }
                        ParentContext.DisplaySnackMessage(model.message);
                        addMoney_EditText.setText("");
                        sendMoney_contact_num.setText("");
                        other_desc_editText_sendMoney.setText("");
                        walletBal_Home.setText(session.getWalletBal());
                        HomeFragment.walletBal_Home.setText(session.getWalletBal());
                        RequestMoneyFragment.walletBal_Home.setText(session.getWalletBal());
                    }
                    else if(model.code.equalsIgnoreCase("203"))
                    {
                        DisplayAddmoneyDialog(2);
                    }
                    else {
                        ParentContext.DisplaySnackMessage(model.message);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onContactsItemClick(String name, String number) {
        sendMoney_contact_num.setText(number);
    }

    private void DisplayAddmoneyDialog(int Flag)
    {
        if(AddMoneyDialog==null) {
            AddMoneyDialog=new Dialog(getActivity(),R.style.ResetTheme);
            AddMoneyDialog.setContentView(R.layout.dialog_addmoney_two);
            AddMoneyDialog.setCancelable(false);
            AddMoneyBtn= (AppCompatButton) AddMoneyDialog.findViewById(R.id.send_add_btn);
            Utils.setButtonTint(AddMoneyBtn, ContextCompat.getColorStateList(getActivity(),R.color.button_tint));
            CancelBtn= (AppCompatButton) AddMoneyDialog.findViewById(R.id.send_cancel_btn);
            Utils.setButtonTint(CancelBtn,ContextCompat.getColorStateList(getActivity(),R.color.button_tint));
            DialogTitle= (RobotoRegularTextView) AddMoneyDialog.findViewById(R.id.send_title);
            DialogMsg= (RobotoLightTextView) AddMoneyDialog.findViewById(R.id.send_msg);
        }
        CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMoneyDialog.dismiss();
            }
        });
        if(Flag==1)
        {
               AddMoneyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), AddMoneyActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("amount", String.valueOf(Double.parseDouble(addMoney_EditText.getText().toString()) - Double.parseDouble(session.getWalletBal())));
                    AddMoneyDialog.dismiss();
                    startActivity(intent);
                }
            });
            DialogTitle.setText("Insufficient Balance");
            DialogMsg.setText("Your balance is insufficient for this request. Please add money in your wallet before proceeding.");
            AddMoneyBtn.setText("Add Money");
        }
        else{
            AddMoneyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SeverCall(2);
                    AddMoneyDialog.dismiss();
                }
            });
            DialogTitle.setText("Unverified Wallet");
            DialogMsg.setText("Your friends mobile email is not registered on Click&Pay. They will receive a link on this mobile/email to register and claim this amount. Please confirm to Send Money.");
            AddMoneyBtn.setText("Send Money");
        }

        AddMoneyDialog.show();
    }


}
