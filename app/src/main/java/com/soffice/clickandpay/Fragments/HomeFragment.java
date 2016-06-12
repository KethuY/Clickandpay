package com.soffice.clickandpay.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import com.soffice.clickandpay.Activities.FullScannerActivity;
import com.soffice.clickandpay.Activities.MainActivity;
import com.soffice.clickandpay.Activities.PaymentDetailsActivity;
import com.soffice.clickandpay.Activities.PaymentSuccessActivity;
import com.soffice.clickandpay.Activities.ZxingQRCodeScanner;
import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.Listeners.PageChangedToClearListener;
import com.soffice.clickandpay.NetWork.JsonRequester;
import com.soffice.clickandpay.NetWork.TaskListner;
import com.soffice.clickandpay.NetWork.Urls;
import com.soffice.clickandpay.Pojo.MerchantPojo;
import com.soffice.clickandpay.Pojo.MerchentCheckResponseModel;
import com.soffice.clickandpay.Pojo.PaymentResponseModel;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.Utilty.CameraPreview;
import com.soffice.clickandpay.Utilty.Constants;
import com.soffice.clickandpay.Utilty.Display;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.soffice.clickandpay.Utilty.SoftKeyboard;
import com.soffice.clickandpay.Utilty.SoftKeyboardStateHelper;
import com.soffice.clickandpay.Utilty.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by sys2025 on 13/9/15.
 */
public class HomeFragment extends Fragment implements PageChangedToClearListener, SoftKeyboardStateHelper.SoftKeyboardStateListener, TaskListner {
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String CODE_ENTERED = "CODE_ENTERED";
    public static String fromActivity = null;
    public static String code_in_QR = null;
    public static HomeFragment home;
    private final String REQUEST_TAG="payment_request_tag";
    private final String MERCHANT_REQUEST="merchant_request_tag";
    private int mPage;
    LinearLayout scan_QR, addMoneyLayout, proceedAddMoneyLayout,cashBackLayout;
    TextView Pay_addMoney, or;
    public  static TextView walletBal_Home;
    EditText addMoney_EditText, vendorcode_input;
    String vc1, amountEntered, className, vendorCode;
    TextView storeName, codeEntered, address1, cashBack_offer;
    TableRow addMoney_tableRow;
    ImageView closeProceedAddMoney, marchntLogo;
    String ss = "<font color=#000000>";
    String ss1 = "Code : ";
    String ss2 = "</font> <font color=#bf0e14>";
    String ss3 = "Ameerpet road, Hyderabad - 500034";
    String ss4 = "</font>";
    String ss5 = "GS 24, Carat Building,";
    String ss6 = "BIGBAZAR Super Market";
    String ss7 = "Address : ";
    ClickandPay clickpay;
    SessionManager session;
    SoftKeyboard softKeyboard;
    SoftKeyboardStateHelper softKeyboardStateHelper;
    JsonRequester requester;
    Urls urls;
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private Class<?> mClss;
    private final int QR_SCAN_REQUEST_CODE=154;
    MainActivity ParentContext;
    ProgressDialog progressDialog;

    public static HomeFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        clickpay = (ClickandPay) getActivity().getApplicationContext();
        session = clickpay.getSession();
        home = this;

        scan_QR = (LinearLayout) view.findViewById(R.id.scan_QR);

        vendorcode_input = (EditText) view.findViewById(R.id.vendorcode_input);
        Pay_addMoney = (TextView) view.findViewById(R.id.Pay_addMoney);
        walletBal_Home = (TextView) view.findViewById(R.id.walletBal_Home);
        addMoney_EditText = (EditText) view.findViewById(R.id.addMoney_EditText);
        or = (TextView) view.findViewById(R.id.or);
        storeName = (TextView) view.findViewById(R.id.storeName);
        codeEntered = (TextView) view.findViewById(R.id.codeEntered);
        address1 = (TextView) view.findViewById(R.id.address1);
        addMoneyLayout = (LinearLayout) view.findViewById(R.id.addMoney);
        proceedAddMoneyLayout = (LinearLayout) view.findViewById(R.id.proceedAddMoney);
        closeProceedAddMoney = (ImageView) view.findViewById(R.id.closeProceedAddMoney);
        marchntLogo = (ImageView) view.findViewById(R.id.marchntLogo);
        cashBack_offer = (TextView) view.findViewById(R.id.cashBack_offer);
        cashBackLayout = (LinearLayout) view.findViewById(R.id.cashBackLayout);
        addMoney_tableRow = (TableRow) view.findViewById(R.id.addMoney_tableRow);

        softKeyboardStateHelper = new SoftKeyboardStateHelper(view.findViewById(R.id.rootView));
        softKeyboardStateHelper.addSoftKeyboardStateListener(this);

        requester = new JsonRequester(this);
        urls = clickpay.getUrls();
        className = getActivity().getLocalClassName();

        vendorcode_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (vendorcode_input.getText().toString().length() == 4) {
                    vc1 = vendorcode_input.getText().toString();
                    amountEntered = addMoney_EditText.getText().toString();
                    if (vc1.length() > 0) {

                        vendorCode = vc1;
                        checkMerchant(vc1);
                        Display.DisplayLogI("ADITYA", "ENTERED CODE : " + vc1);

                    } else {
                        ParentContext.DisplaySnackMessage("Enter a proper Vendor Code..!!");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        addMoney_tableRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddMoneyActivity.class);
                i.putExtra("fromActivity", "HomeFrag");
                startActivity(i);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        clearAll();
                    }
                }, 1000);
            }
        });


        scan_QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  //startActivity(new Intent(getActivity(), FullScannerActivity.class));
                *//*IntentIntegrator intentIntegrator=new IntentIntegrator(getActivity());
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                intentIntegrator.setOrientationLocked(Intent.);
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.setPrompt("Scan a barcode");
                intentIntegrator.initiateScan();*//*
                IntentIntegrator ScanIntent=new IntentIntegrator(getActivity())
                {
                    @Override
                    protected void startActivityForResult(Intent intent, int code) {
                        HomeFragment.this.startActivityForResult(intent,312);
                    }
                };
                ScanIntent.setBeepEnabled(false).setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES).setCaptureActivity(FullScannerActivity.class).initiateScan();
            }*/
                /*Intent intent=new Intent(getActivity(), QRCodeScannerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent,312);*/
                launchQRCodeActivity(ZxingQRCodeScanner.class);
            }
        });



        closeProceedAddMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearDetails();
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
                    if (addMoney_EditText.getText().toString().length() > 0 && Double.parseDouble(addMoney_EditText.getText().toString()) > Double.parseDouble(session.getWalletBal())) {
                        Pay_addMoney.setText("Proceed to Pay");
                    } else {
                        Pay_addMoney.setText("Pay");
                    }
                    Display.DisplayLogI("ADITYA", " AMOUNTTT " + s.toString());
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

                    if(addMoney_EditText.getText().toString().length() > 0 && Double.parseDouble(addMoney_EditText.getText().toString()) > 0) {
                        if (vendorCode != null && vendorCode.length() > 0) {
                            cashBack_offer.setText("*10% Cashback Rs. " + ((Double.parseDouble(addMoney_EditText.getText().toString()) * 10) / 100) + " will be added to your wallet \n after successful transaction.");
                            cashBackLayout.setVisibility(View.GONE);
                        }
                    }else{
                        cashBackLayout.setVisibility(View.GONE);
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

        Pay_addMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Pay_addMoney.getText().toString().equalsIgnoreCase("Pay")) {
                    amountEntered = addMoney_EditText.getText().toString();
                    if(vc1==null)
                    {
                        vc1=vendorcode_input.getText().toString();
                        vendorCode=vc1;
                    }
                    if (vc1.length() > 0) {
                        vendorCode = vc1;
                        if(amountEntered.length() > 0) {
                            if(Double.parseDouble(amountEntered)>=1.00) {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("authkey", session.getAuthKey());
                                params.put("vendercode", vendorCode);
                                params.put("version", Constants.App_Version);
                                params.put("mid", clickpay.getDeviceId(getActivity().getApplicationContext()));
                                params.put("amount", addMoney_EditText.getText().toString());
                                Pay_addMoney.setEnabled(false);
                                requester.StringRequesterFormValues(urls.payment, Request.Method.POST, className, urls.payment_methodName, params, REQUEST_TAG);
                                DisplayDialog();
                            }
                            else
                            {
                                ParentContext.DisplaySnackMessage("Payable amount should be more than  \u20B9 1");
                            }
                        }else{
                            ParentContext.DisplaySnackMessage("Enter Amount.");
                        }
                    } else {
                        if(vendorCode != null && vendorCode.length() > 0){
                            if(Double.parseDouble(amountEntered)>=1.00) {
                                Display.DisplayLogI("ADITYA", "VVCODE " + vendorCode);
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("authkey", session.getAuthKey());
                                params.put("vendercode", vendorCode);
                                params.put("version", Constants.App_Version);
                                params.put("mid", clickpay.getDeviceId(getActivity().getApplicationContext()));
                                params.put("amount", addMoney_EditText.getText().toString());
                                Pay_addMoney.setEnabled(false);
                                requester.StringRequesterFormValues(urls.payment, Request.Method.POST, className, urls.payment_methodName, params, REQUEST_TAG);
                            }
                            else
                            {
                                ParentContext.DisplaySnackMessage("Payable amount should be more than  \u20B91");
                            }

                        }else{
                            ParentContext.DisplaySnackMessage("Enter a proper Vendor Code.");
                        }
                    }

                }else if(Pay_addMoney.getText().toString().equalsIgnoreCase("Proceed to Pay")){
                    amountEntered = addMoney_EditText.getText().toString();
                    if(vc1==null)
                    {
                        vc1=vendorcode_input.getText().toString();
                        vendorCode=vc1;
                    }
                    if (vc1.length() > 0) {
//                        proceedToAddMoney(model.merchat);
                        if (addMoney_EditText.getText().toString().length() > 0 ) {
                            if(Double.parseDouble(addMoney_EditText.getText().toString()) >=Constants.MIN_SEND_REQ_MONEY) {
                                if ((Double.parseDouble(addMoney_EditText.getText().toString()) - Double.parseDouble(session.getWalletBal())) > Double.parseDouble(session.getMonthTrans())) {
                                    Intent i = new Intent(getActivity(), PaymentDetailsActivity.class);
                                    i.putExtra("fromActivity", "HomeFrag");
                                    i.putExtra("amountSpend", String.valueOf(Double.parseDouble(addMoney_EditText.getText().toString()) - Double.parseDouble(session.getWalletBal())));
                                    i.putExtra("vendorCode", vendorCode);
                                    startActivity(i);
                                } else {
                                    final Dialog AddMoenyDialog = new Dialog(getActivity(), R.style.DialogStyle);
                                    AddMoenyDialog.setContentView(R.layout.dialog_addmoney);
                                    AppCompatButton AddmoneyBtn = (AppCompatButton) AddMoenyDialog.findViewById(R.id.dialog_add_money_btn);
                                    Utils.setButtonTint(AddmoneyBtn, ContextCompat.getColorStateList(getActivity(), R.color.button_tint));
                                    AddmoneyBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getActivity(), AddMoneyActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.putExtra("amount", String.valueOf(Double.parseDouble(addMoney_EditText.getText().toString()) - Double.parseDouble(session.getWalletBal())));
                                            AddMoenyDialog.dismiss();
                                            startActivity(intent);
                                            ClearDetails();

                                        }
                                    });
                                    AddMoenyDialog.show();
                                }
                            }else
                            {
                                ParentContext.DisplaySnackMessage("Payable amount should be more than  \u20B9 "+Constants.MIN_SEND_REQ_MONEY);
                            }
                        } else {
                            ParentContext.DisplaySnackMessage("Enter amount");
                        }
                    } else {
                        ParentContext.DisplaySnackMessage("Enter a proper Vendor Code");
                        Pay_addMoney.setEnabled(true);
                        vendorcode_input.requestFocus();
                    }
                }
            }
        });

        addMoney_EditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMoney_EditText.setCursorVisible(true);
            }
        });

        return view;
    }

    private void ClearDetails() {
        addMoneyLayout.setVisibility(View.VISIBLE);
        or.setVisibility(View.VISIBLE);
        proceedAddMoneyLayout.setVisibility(View.GONE);
        closeProceedAddMoney.setVisibility(View.GONE);

        fromActivity = null;
        code_in_QR = null;
        vendorcode_input.setText("");
//                addMoney_EditText.setText("");
//                addMoney_EditText.setHint("0.00");
        cashBackLayout.setVisibility(View.GONE);

        Pay_addMoney.setText("Pay");
        vendorcode_input.clearFocus();
        vc1=null;
        vendorCode=null;
        addMoney_EditText.clearFocus();
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
        {
            if(walletBal_Home!=null)
            {
                if(session!=null) {
                    walletBal_Home.setText(session.getWalletBal());
                }
            }
        }
    }

    public void checkMerchant(String code){
        Map<String, String> params = new HashMap<String, String>();
        params.put("poscode", code);
        requester.StringRequesterFormValues(urls.merchant, Request.Method.POST, className, urls.merchant_methodName, params,MERCHANT_REQUEST);
    }

    public void proceedToAddMoney(MerchantPojo merchat){
        addMoneyLayout.setVisibility(View.GONE);
        or.setVisibility(View.GONE);
        storeName.setText(merchat.merchantStore);
        if(merchat.merchantLogo != null && merchat.merchantLogo.length() > 0) {
            Picasso.with(getActivity()).load(merchat.merchantLogo).into(marchntLogo);
        }
        address1.setText(merchat.merchantAddress);
        proceedAddMoneyLayout.setVisibility(View.VISIBLE);
        closeProceedAddMoney.setVisibility(View.VISIBLE);
        fromActivity = null;
        addMoney_EditText.clearFocus();
        Pay_addMoney.requestFocus();
//        addMoney_EditText.setCursorVisible(false);
        removePhoneKeypad();
        if(addMoney_EditText.getText().toString().length() > 0 && Double.parseDouble(addMoney_EditText.getText().toString()) > 0) {
            cashBack_offer.setText("*10% Cashback Rs. " + ((Double.parseDouble(addMoney_EditText.getText().toString()) * 10) / 100) + "will be added to your wallet \\n after successful transaction.");
            cashBackLayout.setVisibility(View.GONE);
        }else{
            cashBackLayout.setVisibility(View.GONE);
        }


//        addMoney_EditText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addMoney_EditText.setCursorVisible(true);
//            }
//        });
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Display.DisplayLogI("ADITYA", "onViewStateRestored");
        addMoney_EditText.setCursorVisible(false);
        addMoney_EditText.clearFocus();
        Pay_addMoney.requestFocus();
        clearAll();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      /*  IntentResult intentResult= IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if(data!=null)
        {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Set<String> keys = bundle.keySet();
                Iterator<String> it = keys.iterator();
                Log.e("QRCODE","Dumping Intent start");
                while (it.hasNext()) {
                    String key = it.next();
                    Log.e("QRCODE","[" + key + "=" + bundle.get(key)+"]");
                }
                Log.e("QRCODE" +
                        "","Dumping Intent end");
            }
            vendorcode_input.setText(data.getStringExtra("SCAN_RESULT"));
        }*/
        if(resultCode== Activity.RESULT_OK)
        {
            //vendorcode_input.setText(data.getStringExtra("scan_result"));
            checkMerchant(data.getStringExtra("scan_result"));
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Pay_addMoney.setEnabled(true);
        if(fromActivity != null && fromActivity.equalsIgnoreCase("QRScan")) {
            if(code_in_QR != null && code_in_QR.length() == 4) {
                codeEntered.setText(Html.fromHtml(ss + ss1 + ss2 + vendorCode + ss4));
                Display.DisplayLogI("ADITYA", "code_in_QR :: " + code_in_QR);

                vendorCode = code_in_QR;
                checkMerchant(vendorCode);
//                proceedToAddMoney();
            }
        }

        removePhoneKeypad();
        vendorcode_input.clearFocus();
        addMoney_EditText.clearFocus();
        addMoney_EditText.setText("");
        walletBal_Home.setText(session.getWalletBal());
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
        try {
            if (cd == 00) {
                ParentContext.DisplaySnackMessage(response);
            } else if (cd == 05) {
                if (_className.equalsIgnoreCase(className) && _methodName.equalsIgnoreCase(urls.merchant_methodName)) {
                    Gson g = new Gson();
                    MerchentCheckResponseModel model = g.fromJson(response, MerchentCheckResponseModel.class);
                    Display.DisplayLogI("ADITYA code", model.code);
                    if (model.code.equalsIgnoreCase("200")) {
                        codeEntered.setText(Html.fromHtml(ss + ss1 + ss2 + model.merchat.pos_code + ss4));
                        Display.DisplayLogI("ADITYA", "pos_code " + model.merchat.pos_code);
                        proceedToAddMoney(model.merchat);
                        Constants.marchant = model.merchat;
                        vc1=model.merchat.pos_code;
                        vendorCode=vc1;
                        Pay_addMoney.requestFocus();
                    } else {
                        ParentContext.DisplaySnackMessage(model.message);
                        removePhoneKeypad();
                        clearAll();
                    }
                }

                if (_className.equalsIgnoreCase(className) && _methodName.equalsIgnoreCase(urls.payment_methodName)) {
                    Pay_addMoney.setEnabled(true);
                    Gson g = new Gson();
                    PaymentResponseModel model = g.fromJson(response, PaymentResponseModel.class);
                    Display.DisplayLogI("ADITYA code", model.code);
                    if (model.code.equalsIgnoreCase("200")) {
                        session.setWalletBal(model.wallet_bal);
                        Intent i = new Intent(getActivity(), PaymentSuccessActivity.class);
                        i.putExtra("fromActivity", "PaymentDetails");
                        i.putExtra("vendorCode", vendorCode);
                        i.putExtra("transac_id", model.transctionHistory.transactionId);
                        i.putExtra("transac_date", model.transctionHistory.date);
                        i.putExtra("transac_time", model.transctionHistory.time);
                        i.putExtra("transac_amount", model.transctionHistory.transactionAmount);
                        i.putExtra("amount", addMoney_EditText.getText().toString());
                        startActivity(i);
                    } else {
                        ParentContext.DisplaySnackMessage(model.message);
                    }
                }
            }
            DismissDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            addMoney_EditText.clearFocus();
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            addMoney_EditText.clearFocus();
        }
    }

    @Override
    public void clearAll() {
        closeProceedAddMoney.performClick();
        vendorcode_input.setText("");
        addMoney_EditText.setText("");

        vendorcode_input.clearFocus();
        addMoney_EditText.clearFocus();
    }

    public void launchQRCodeActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(getActivity(), clss);
            startActivityForResult(intent,QR_SCAN_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0&&permissions[0].equalsIgnoreCase(Manifest.permission.CAMERA) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mClss != null) {
                        Intent intent = new Intent(getActivity(), mClss);
                        startActivityForResult(intent,QR_SCAN_REQUEST_CODE);
                    }
                } else {
                    ParentContext.DisplaySnackMessage("Please grant camera permission to scan the QR Code");
                }
                return;
        }
    }
}
