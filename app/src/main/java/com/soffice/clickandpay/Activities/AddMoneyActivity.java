package com.soffice.clickandpay.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.soffice.clickandpay.Adapters.CardsFragmentPagerAdapter;
import com.soffice.clickandpay.BuildConfig;
import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.Fragments.NetBankingFragment;
import com.soffice.clickandpay.NetWork.JsonRequester;
import com.soffice.clickandpay.NetWork.TaskListner;
import com.soffice.clickandpay.NetWork.Urls;
import com.soffice.clickandpay.Pojo.Addmoney;
import com.soffice.clickandpay.Pojo.NetBanks;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.UI.PagerSlidingTabStrip;
import com.soffice.clickandpay.UI.RobotoLightTextView;
import com.soffice.clickandpay.UI.RobotoRegularEditText;
import com.soffice.clickandpay.UI.RobotoRegularTextView;
import com.soffice.clickandpay.Utilty.AvenuesParams;
import com.soffice.clickandpay.Utilty.Constants;
import com.soffice.clickandpay.Utilty.Display;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.soffice.clickandpay.Utilty.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class AddMoneyActivity extends AppCompatActivity implements TaskListner,RobotoRegularEditText.handleDismissingKeyboard,TextWatcher
{

    ClickandPay clickpay;
    SessionManager session;
    JsonRequester requester;
    Urls urls;
    String className;
    //ViewPager viewPager;
    //TabLayout tabsStrip;
    //CardsFragmentPagerAdapter cardsAdp;
    TextView pay_rupees, walletBal_Home;
    ImageView back_IV;
    FrameLayout LoadingLayout;
    ProgressBar Pbar;
    RobotoRegularEditText addMoney_EditText;
    SessionManager sessionManager;
    //ProgressBar Pbar;
    ArrayList<NetBanks> CCList=new ArrayList<>();
    ArrayList<NetBanks> DCList=new ArrayList<>();
    ArrayList<NetBanks> BanksList=new ArrayList<>();
    String amountEntered;
    boolean isValidating=false;
    private final String REQUEST_TAG="addmoney_request";
    CoordinatorLayout coordinatorLayout;
    LinearLayout PromoCodeLayout,PromoTextLayout;
    TextInputLayout PromoInputLayout;
    TextInputEditText PromoEt;
    String[] Titles={"Credit Card Payment","Debit Card Payment","Net Banking"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
        coordinatorLayout= (CoordinatorLayout) findViewById(R.id.mycoordinator);
        LoadingLayout= (FrameLayout) findViewById(R.id.loadinglayout);
        Pbar= (ProgressBar) findViewById(R.id.pbar);
        Pbar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,android.R.color.white), PorterDuff.Mode.SRC_IN);
        sessionManager=ClickandPay.getInstance().getSession();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //Pbar= (ProgressBar) findViewById(R.id.pbar);
        if(Build.VERSION.SDK_INT<21)
        {
            //Pbar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        }
        clickpay = (ClickandPay) getApplicationContext();
        session = clickpay.getSession();
        requester = new JsonRequester(this);
        urls = clickpay.getUrls();
        className = getLocalClassName();
        pay_rupees = (TextView) findViewById(R.id.pay_rupees);
        back_IV = (ImageView) findViewById(R.id.back_IV);
        walletBal_Home = (TextView) findViewById(R.id.walletBal_Home);
        addMoney_EditText = (RobotoRegularEditText) findViewById(R.id.addMoney_EditText);
        PromoCodeLayout= (LinearLayout) findViewById(R.id.selectCardLayout);
        PromoTextLayout= (LinearLayout) findViewById(R.id.promotextlayout);
        PromoInputLayout= (TextInputLayout) findViewById(R.id.promotextinputlayout);
        PromoEt= (TextInputEditText) findViewById(R.id.promotextet);
        PromoEt.setAllCaps(true);
        PromoEt.addTextChangedListener(this);
        PromoEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    PromoInputLayout.setHint("Enter promo code");
                }
                else
                {
                    if(((TextInputEditText)v).getText().length()==0)
                    {
                        PromoInputLayout.setHint("Have promo code?");
                    }
                }
            }
        });
        addMoney_EditText.setHandleDismissingKeyboard(this);
        if (session.getWalletBal().equalsIgnoreCase("0") || !session.getWalletBal().contains(".")) {
            walletBal_Home.setText(session.getWalletBal() + ".00");
        }

        walletBal_Home.setText(session.getWalletBal());

        if(getIntent() != null && getIntent().getExtras() != null){
            if(getIntent().hasExtra("fromActivity") && getIntent().getStringExtra("fromActivity") != null && getIntent().getStringExtra("fromActivity").equalsIgnoreCase("HomeFrag")){
                if(getIntent().hasExtra("amountSpend") && getIntent().getStringExtra("amountSpend") != null && getIntent().getStringExtra("amountSpend").length() > 0) {
                    addMoney_EditText.setText(getIntent().getStringExtra("amountSpend"));
                }
            }
        }

        back_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.showPassCode = false;
                finish();
            }
        });

        pay_rupees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountEntered = addMoney_EditText.getText().toString();
                if(amountEntered.length() > 0) {
                    if(Double.parseDouble(addMoney_EditText.getText().toString())<=Double.parseDouble(session.getMonthTrans())) {
                        if (Double.parseDouble(addMoney_EditText.getText().toString()) >= Constants.MIN_ADDMONEY && (Double.parseDouble(addMoney_EditText.getText().toString()) <= Constants.MAX_ADDMONEY)) {
                            Intent intent=new Intent(AddMoneyActivity.this,PaymentProcessActivity.class);
                            intent.putExtra(AvenuesParams.ORDER_ID,GetOrderID());
                            intent.putExtra(AvenuesParams.ACCESS_CODE,getResources().getString(R.string.access_code));
                            intent.putExtra(AvenuesParams.MERCHANT_ID,getResources().getString(R.string.merchant_id));
                            intent.putExtra(AvenuesParams.REDIRECT_URL,Urls.redirect_url);
                            intent.putExtra(AvenuesParams.CANCEL_URL,Urls.redirect_url);
                            intent.putExtra(AvenuesParams.CURRENCY,"INR");
                            intent.putExtra(AvenuesParams.AMOUNT,amountEntered);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        } else {
                            Display.DisplaySnackbar(AddMoneyActivity.this, coordinatorLayout, "Currently you can only add an amount between ₹ 10 to ₹ 10000 per month");
                        }
                    }
                    else
                    {
                        if(Double.parseDouble(session.getMonthTrans())==0.0){
                            Display.DisplaySnackbar(AddMoneyActivity.this, coordinatorLayout,"You have reached maximum limit");
                        }
                        Display.DisplaySnackbar(AddMoneyActivity.this, coordinatorLayout,"Currently you can only add an amount between Rs 10 to 10000 per month.");
                    }
                }

                else{
                    Display.DisplaySnackbar(AddMoneyActivity.this,coordinatorLayout, "Please enter Amount");
                }
            }
        });

        addMoney_EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (addMoney_EditText.getText().toString().contains(".")) {
                    s.toString().split(".");
                    Display.DisplayLogI("ADITYA", " SPLITTT " + s.toString().split("\\.").length);
                    String[] str = addMoney_EditText.getText().toString().split("\\.");
                    if (str.length > 1) {
                        Display.DisplayLogI("ADITYA", " SPLITTT PARTS " + str[0]+ "  "+str[1]);
                        if (str[1].toString().length() > 2) {
                            int lastdigit=Integer.valueOf(str[1].substring(2,3));
                            int paisa=Integer.parseInt(str[1].substring(1,2));
                            if(lastdigit>=5)
                            {
                                paisa=paisa+1;
                            }
                            addMoney_EditText.setText(str[0] + "." + str[1].substring(0,1)+paisa);
                            addMoney_EditText.setSelection(addMoney_EditText.getText().length());
                            Display.DisplayLogI("ADITYA", "str[1].substring(0, 2) " + str[1].substring(0, 2));
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if(getIntent().hasExtra("amount"))
        {
            addMoney_EditText.setText(getIntent().getStringExtra("amount"));
        }



       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

    }

    private void CheckAddMoneyCriteria() {
        if(Utils.CheckInternet(this))
        {
            pay_rupees.setText("Please wait..");
            LoadingLayout.setVisibility(View.VISIBLE);
            //Pbar.setVisibility(View.VISIBLE);
            pay_rupees.setEnabled(false);
            Map<String,String> params=new HashMap<>();
            params.put("authkey",sessionManager.getAuthKey());
            params.put("amount",amountEntered);
            params.put("userId",sessionManager.getCustomerIdentifier());
            requester.StringRequesterFormValues(Urls.checkAddMoney, Request.Method.POST,className,Urls.checkAddMoney_methodName,params,REQUEST_TAG);
        }
        else
        {
            Display.DisplaySnackbar(this,coordinatorLayout,"Please check your internet connection and try again.");
        }
    }

    /*private void DisplayPager()
    {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.viewpager_addMoney);
        cardsAdp = new CardsFragmentPagerAdapter(getSupportFragmentManager(),CCList,DCList,BanksList);
        viewPager.setAdapter(cardsAdp);

        // Give the PagerSlidingTabStrip the ViewPager
        tabsStrip = (TabLayout) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setTabsFromPagerAdapter(cardsAdp);
        tabsStrip.setupWithViewPager(viewPager);
    }

    private int CheckCurrency(String Amount)
    {
        float Currency=Float.valueOf(Amount);
        if(Currency<9.99)
        {
            return 2;
        }

        else if(Currency>=10000.00)
        {
            return 3;
        }
        else
        {
            return 1;
        }
    }

    private boolean IsaValidAmount(String Amount)
    {
        String RegularExp="^(?:0|[1-9]d*)(?:.(?!.*000)d+)?$";
        if(Pattern.matches(RegularExp,(Amount)))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
*/
    /** SEAMLESS CODE
    /*private class ServerCall extends AsyncTask<Void,Void,String>
    {

        @Override
        protected String doInBackground(Void... params) {
            try{
                URL url=new URL(Constants.JSON_URL);
                HttpURLConnection urlConnection= (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                String Content = AvenuesParams.ACCESS_CODE+"="+"AVWV64DD72AH97VWHA"
                        +"&"+AvenuesParams.CURRENCY+Constants.PARAMETER_EQUALS+"INR"+Constants.PARAMETER_SEP
                        +AvenuesParams.AMOUNT+Constants.PARAMETER_EQUALS+amountEntered+Constants.PARAMETER_SEP
                        +AvenuesParams.COMMAND+Constants.PARAMETER_EQUALS+"getJsonDataVault"+Constants.PARAMETER_SEP
                        +AvenuesParams.CUSTOMER_IDENTIFIER+Constants.PARAMETER_EQUALS+"92446";
                urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("Content-Length", String.valueOf(Content.getBytes().length));
                urlConnection.setRequestProperty("User-Agent","Mozilla/5.0 (Linux; U; Android-4.0.3; en-us; Galaxy Nexus Build/IML74K) AppleWebKit/535.7 (KHTML, like Gecko) CrMo/16.0.912.75 Mobile Safari/535.7");
                OutputStream output = urlConnection.getOutputStream();
                output.write(Content.getBytes());
                output.close();
                BufferedReader reader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder builder=new StringBuilder();
                String line;
                while((line=reader.readLine())!=null)
                {
                    builder.append(line);
                }

                return builder.toString();

            }catch (Exception e)
            {
                e.printStackTrace();
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(BuildConfig.DEBUG){
                Log.d("Response",s);}
            if(!s.equalsIgnoreCase("error"))
            {
                ParseOptionsData(s);
                Pbar.setVisibility(View.GONE);
                DisplayPager();
            }
            else
            {
                Toast.makeText(AddMoneyActivity.this,"Failed to get data",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void ParseOptionsData(String Data)
    {
        try
        {
            JSONObject jobj=new JSONObject(Data);
            JSONArray Opts=jobj.getJSONArray("payOptions");
            for(int i=0;i<Opts.length();i++)
            {
                JSONObject childObj=Opts.getJSONObject(i);
                String CardsList=childObj.getString("cardsList");
                JSONArray childarray=new JSONArray(CardsList);
                for(int j=0;j<childarray.length();j++)
                {
                    JSONObject CardObj=childarray.getJSONObject(j);
                    NetBanks modelclass=new NetBanks("","","","","","","");
                    if(CardObj.has("cardName"))
                    {
                        modelclass.setCardName(CardObj.getString("cardName"));
                    }
                    else
                    {
                        modelclass.setCardName("");
                    }
                    if(CardObj.has("cardType"))
                    {
                        modelclass.setCardType(CardObj.getString("cardType"));
                    }
                    else
                    {
                        modelclass.setCardType("");
                    }
                    if(CardObj.has("payOptType"))
                    {
                        modelclass.setPayOptType(CardObj.getString("payOptType"));
                    }
                    else
                    {
                        modelclass.setPayOptType("");
                    }
                    if(CardObj.has("payOptDesc"))
                    {
                        modelclass.setPayOptDesc(CardObj.getString("payOptDesc"));
                    }
                    else
                    {
                        modelclass.setPayOptDesc("");
                    }
                    if(CardObj.has("dataAcceptedAt"))
                    {
                        modelclass.setDataAcceptedAt(CardObj.getString("dataAcceptedAt"));
                    }
                    else
                    {
                        modelclass.setDataAcceptedAt("");
                    }
                    if(CardObj.has("status"))
                    {
                        modelclass.setStatus(CardObj.getString("status"));
                    }
                    else
                    {
                        modelclass.setStatus("");
                    }

                    if(i==0)
                    {
                        CCList.add(modelclass);
                    }
                    else if(i==1)
                    {
                        DCList.add(modelclass);
                    }
                    else
                    {
                        NetBankingFragment.List.add(modelclass);
                    }
                }

            }
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }*/


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

    }

   /* public void removePhoneKeypad() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }*/

    @Override
    protected void onRestart() {
        super.onRestart();
        if(ClickandPay.getInstance().ShouldDisplayPasscode) {
            Intent i = new Intent(AddMoneyActivity.this, PassCodeActivity.class);
            i.putExtra("fromActivity", "Login");
            startActivity(i);
            finish();
        }
//        }else{
////            showPassCode = true;
//        }
        /*InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput (InputMethodManager.SHOW_FORCED, InputMethodManager.SHOW_IMPLICIT);*/
    }

    @Override
    public void onTaskfinished(String response, int cd, String _className, String _methodName) {
        LoadingLayout.setVisibility(View.GONE);
        Display.DisplayLogI("ADITYA", "" + response);
        try {
            if (cd == 00) {
                Display.DisplaySnackbar(this,coordinatorLayout,"Oops.. Something went wrong. Please try again.");
                pay_rupees.setText("Add cash");
                pay_rupees.setEnabled(true);
            } else if (cd == 05) {
                if (_className.equalsIgnoreCase(className) && _methodName.equalsIgnoreCase(urls.payment_methodName)) {
               /* Gson g = new Gson();
                PaymentResponseModel model = g.fromJson(response, PaymentResponseModel.class);
                Display.DisplayLogI("ADITYA code", model.code);
                if (model.code.equalsIgnoreCase("200")) {
                    session.setWalletBal(model.wallet_bal);
                    Intent i = new Intent(PaymentDetailsActivity.this, PaymentSuccessActivity.class);
                    i.putExtra("fromActivity", "PaymentDetails");
                    i.putExtra("vendorCode", vendorCode);
                    i.putExtra("amount", payingAmount);
                    startActivity(i);
                    finish();
                } else {
                    Display.DisplayToast(this, "Error : " + response);
                }*/
                }
                if(_className.equalsIgnoreCase(className)&&_methodName.equalsIgnoreCase(urls.checkAddMoney_methodName))
                {
                        Gson g=new Gson();
                        Addmoney modelClass=g.fromJson(response,Addmoney.class);
                        if(modelClass.Code.equalsIgnoreCase("200"))
                        {
                            //Pbar.setVisibility(View.GONE);
                            pay_rupees.setText("Add Cash");
                            pay_rupees.setEnabled(true);
                            isValidating=false;
                            Intent intent=new Intent(AddMoneyActivity.this,PaymentProcessActivity.class);
                            intent.putExtra(AvenuesParams.ORDER_ID,GetOrderID());
                            intent.putExtra(AvenuesParams.ACCESS_CODE,getResources().getString(R.string.access_code));
                            intent.putExtra(AvenuesParams.MERCHANT_ID,getResources().getString(R.string.merchant_id));
                            intent.putExtra(AvenuesParams.REDIRECT_URL,Urls.redirect_url);
                            intent.putExtra(AvenuesParams.CANCEL_URL,Urls.redirect_url);
                            intent.putExtra(AvenuesParams.CURRENCY,"INR");
                            intent.putExtra(AvenuesParams.AMOUNT,amountEntered);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(intent);
                        }
                        else
                        {
                            //Pbar.setVisibility(View.GONE);
                            pay_rupees.setText(getResources().getText(R.string.add_cash));
                            isValidating=false;
                            pay_rupees.setEnabled(true);
                            Display.DisplaySnackbar(this,coordinatorLayout,modelClass.Message);
                        }
                }

                if(_methodName.equalsIgnoreCase(Urls.promo_code_validation_method)&&_className.equalsIgnoreCase(getLocalClassName()))
                {
                    HideKeyBoard(PromoEt);
                    Bundle bundle=ParsePromoData(response);
                    //noinspection ConstantConditions
                    if(bundle.getString("code").equalsIgnoreCase("200"))
                    {
                        RobotoLightTextView promomsg=new RobotoLightTextView(this);
                        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                        promomsg.setLayoutParams(params);
                        promomsg.setTextColor(Color.BLACK);
                        promomsg.setText(bundle.getString("message"));
                        if(PromoCodeLayout.getChildCount()>1)
                        {
                            PromoCodeLayout.removeViewAt(1);
                        }
                        PromoCodeLayout.addView(promomsg);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            pay_rupees.setText("Add Cash");
            pay_rupees.setEnabled(true);
            isValidating=false;
            Display.DisplaySnackbar(this,coordinatorLayout,"Oops.. Something went wrong. Please try again.");

        }
    }

    @Override
    public void dismissKeyboard()
    {


    }

    private void HideKeyBoard(EditText et)
    {
        InputMethodManager imm= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(),0);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(charSequence.length()==0)
            {
                PromoTextLayout.removeViewAt(2);
            }
        else
            {
                if(PromoTextLayout.getChildCount()==2) {
                    AppCompatButton applybtn = new AppCompatButton(AddMoneyActivity.this);
                    Utils.setButtonTint(applybtn, ContextCompat.getColorStateList(AddMoneyActivity.this, R.color.button_tint));
                    Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/Roboto-Light.ttf");
                    applybtn.setSupportAllCaps(true);
                    applybtn.setTextColor(Color.WHITE);
                    applybtn.setTypeface(typeface);
                    applybtn.setText("Apply");
                    applybtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ValidatePromoCode(PromoEt.getText().toString());
                        }
                    });
                    PromoTextLayout.addView(applybtn);
                }

            }
    }

    private void ValidatePromoCode(String promo)
    {
        if(Utils.CheckInternet(this))
        {
                HashMap<String,String> postObj=new HashMap<>();
                postObj.put("authkey",sessionManager.getAuthKey());
                postObj.put("amount",addMoney_EditText.getText().toString());
                postObj.put("couponcode",promo);
                Display.DisplayLogD("Params",postObj.toString());
                requester.StringRequesterFormValues(Urls.promo_code_validation, Request.Method.POST,getLocalClassName(),Urls.promo_code_validation_method,postObj,"promovalidation");

        }
        else
        {
            Utils.GenerateSnackbar(this,coordinatorLayout,"Please check your internet connection and try again.");
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public interface SavedInterface
    {
        public HashMap<String,String> GetDetails();
    }

    public interface DCCCInterface
    {
        public boolean ValidateDetails();
        public HashMap<String,String> GetDetails();
    }

    private String GetOrderID()
    {
        Calendar cal=Calendar.getInstance();
        String OrderID=String.valueOf(cal.getTimeInMillis())+session.getMobileNum();
        return OrderID;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ClickandPay.getInstance().cancelPendingRequests(REQUEST_TAG);
    }

    private Bundle ParsePromoData(String Data){
        Bundle bundle=new Bundle();
        try
        {
            JSONObject jobj=new JSONObject(Data);
            bundle.putString("code",jobj.getString("CODE"));
            bundle.putString("message",jobj.getString("MESSAGE"));
        }catch (JSONException e){
            e.printStackTrace();
        }
        return bundle;
    }
}
