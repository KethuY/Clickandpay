package com.soffice.clickandpay.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DialogTitle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.soffice.clickandpay.Listeners.TransactionItemClickListener;
import com.soffice.clickandpay.Adapters.TranscationListAdapter;
import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.NetWork.JsonRequester;
import com.soffice.clickandpay.NetWork.TaskListner;
import com.soffice.clickandpay.NetWork.Urls;
import com.soffice.clickandpay.Pojo.TransactionItem;
import com.soffice.clickandpay.Pojo.TransactionPojo;
import com.soffice.clickandpay.Pojo.TransactionResponseModel;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.UI.RobotoRegularEditText;
import com.soffice.clickandpay.Utilty.Display;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.soffice.clickandpay.Utilty.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransactionsActivity extends AppCompatActivity implements TaskListner, TransactionItemClickListener,DialogInterface.OnDismissListener
{

    private List<TransactionPojo> feedItemList1 = new ArrayList<TransactionPojo>();
    private RecyclerView mRecyclerView;
    private TranscationListAdapter adapter;
    ClickandPay clickPay;
    SessionManager session;
    JsonRequester requester;
    boolean isLoading=false;
    boolean hasData=true;
    Urls urls;
    int PageCount=0;
    ProgressBar Pbar;
    String className;
    ImageView back_IV;
    CoordinatorLayout coordinatorLayout;
    Dialog TransacDetailsDialog;
    Dialog EmailInvoiceDialog;
    RobotoRegularEditText InvoiceEmailET;
    AppCompatButton InvoiceSendBtn,InvoiceCancelBtn;
    TextView DialogLogo,DialogTransacTitle,DialogTransacCode,DialogTransacAddress,DialogTransacOrderno,DialogTransacDate,DialogTransbillno,
                    DialogTransacTime,DialogTransacAmount,DialogTransacCouponText,DialogTransacEmail,DialogTransacSupportText,DialogTransacPaymentStatus;
    LinearLayout DialogCouponLayout,DialogEmailLayout,DialogTransacSupportLayout,NoTransacLayout;
    ImageView DialogTransacLogo,DialogTransacStatusImage,DialogTransacCouponImage,DialogTransacEmailImage,DialogTransacSupportImage,DialogCloseBtn;
    private final String REQUEST_TAG="transac_request";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        coordinatorLayout= (CoordinatorLayout) findViewById(R.id.mycoordinator);
        NoTransacLayout= (LinearLayout) findViewById(R.id.no_transac_layout);
        Pbar= (ProgressBar) findViewById(R.id.pbar);
        Utils.ApplyPbarColor(this,Pbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        clickPay = (ClickandPay) getApplicationContext();
        session = clickPay.getSession();
        session.setIsLoggedIn(true);
        className = getLocalClassName();
        requester = new JsonRequester(this);
        if(Utils.CheckInternet(this)) {
            ServerCall();
            Pbar.setVisibility(View.VISIBLE);
        }
        else
        {
            Utils.GenerateSnackbar(this,coordinatorLayout,"Please check your internet connection and try again.");
        }
        back_IV = (ImageView) findViewById(R.id.back_IV);

        back_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.showPassCode = false;
                finish();
            }
        });


    }

    public void ServerCall()
    {
        if(!isLoading&&hasData) {
            isLoading=true;
            urls = clickPay.getUrls();
            if (Utils.CheckInternet(this)) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authkey", session.getAuthKey());
                params.put("page", String.valueOf(PageCount));
                requester.StringRequesterFormValues(urls.getTransactions, Request.Method.POST, className, urls.getTransactions_methodName, params, REQUEST_TAG);
            } else {
                Toast.makeText(this, "Please check your internet connection and try again.", Toast.LENGTH_SHORT).show();

            }
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
        Pbar.setVisibility(View.GONE);
        try {
            if (cd == 00) {
                Display.DisplayToast(this, response);
            } else if (cd == 05) {
                if (_className.equalsIgnoreCase(className) && _methodName.equalsIgnoreCase(urls.getTransactions_methodName)) {
                    Gson g = new Gson();
                    TransactionResponseModel model = g.fromJson(response, TransactionResponseModel.class);
                    Display.DisplayLogI("ADITYA code", model.code);
                    if (model.code.equalsIgnoreCase("200")) {
                        if (ParseCheck(response)) {
                            if (model.transactionPojos.size() > 0) {
                                if (feedItemList1.size() == 0) {
                                    feedItemList1 = model.transactionPojos;
                                         /* Initialize recyclerview */
                                    mRecyclerView = (RecyclerView) findViewById(R.id.transactions_recycler);
                                    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                                    mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(10));
                                    adapter = new TranscationListAdapter(TransactionsActivity.this, feedItemList1);
                                    mRecyclerView.setAdapter(adapter);
                                } else {
                                    for (TransactionPojo Obj : model.transactionPojos) {
                                        feedItemList1.add(Obj);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                                PageCount++;
                            } else {
                                NoTransacLayout.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {
                            if(feedItemList1!=null)
                            {
                                if(feedItemList1.size()>0)
                                {
                                    hasData=false;
                                }
                            }
                            else
                            {
                                NoTransacLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    else {
                        Display.DisplayToast(this, model.message);

                    }

                /*SignInResponseModel model = g.fromJson(response, SignInResponseModel.class);
                Display.DisplayLogI("ADITYA code", model.code);
                if (model.code.equalsIgnoreCase("200")) {

                }else{
                    Display.DisplayToast(LoginActivity.this, model.message);
                }*/
                }
                if(_className.equalsIgnoreCase(className)&&_methodName.equalsIgnoreCase(urls.getTransactions_details_methodName))
                {
                    Log.d("Details_Response",response);
                }
            }
            isLoading=false;
        }catch (Exception e){
            e.printStackTrace();
            hasData=false;
        }
    }

    private boolean ParseCheck(String data)
    {
        try
        {
            JSONObject obj = new JSONObject(data);
            if (obj.has("TRANSACTIONS")) {
                if(!obj.getString("TRANSACTIONS").equalsIgnoreCase(""))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onTransactionClick(int position) {
      DisplayTranscDetailsDialog(feedItemList1.get(position));
    }

    private void DisplayTranscDetailsDialog(final TransactionPojo Obj)
    {
        if(TransacDetailsDialog==null)
        {
            TransacDetailsDialog=new Dialog(this,R.style.TransactionTheme);
            TransacDetailsDialog.setContentView(R.layout.dialog_transaction);
            TransacDetailsDialog.setOnDismissListener(this);
            TransacDetailsDialog.setCancelable(false);
            DialogCloseBtn= (ImageView) TransacDetailsDialog.findViewById(R.id.closeButton);
            DialogCouponLayout= (LinearLayout) TransacDetailsDialog.findViewById(R.id.use_coupon_layout);
            DialogEmailLayout= (LinearLayout) TransacDetailsDialog.findViewById(R.id.email_invoice_layout);
            DialogTransacSupportLayout= (LinearLayout) TransacDetailsDialog.findViewById(R.id.support_layout);
            DialogTransacLogo= (ImageView) TransacDetailsDialog.findViewById(R.id.topLevellogo);
            DialogTransacAmount= (TextView) TransacDetailsDialog.findViewById(R.id.textAmount);
            DialogTransacTitle= (TextView) TransacDetailsDialog.findViewById(R.id.textCompanyName);
            DialogTransacCode= (TextView) TransacDetailsDialog.findViewById(R.id.textCode);
            DialogTransacAddress= (TextView) TransacDetailsDialog.findViewById(R.id.textLabelAddress);
            DialogTransacDate= (TextView) TransacDetailsDialog.findViewById(R.id.date_text);
            DialogTransbillno= (TextView) TransacDetailsDialog.findViewById(R.id.billno_text);
            DialogTransacPaymentStatus= (TextView) TransacDetailsDialog.findViewById(R.id.textPaymentStatus);
            DialogTransacStatusImage= (ImageView) TransacDetailsDialog.findViewById(R.id.imagePayment);
            DialogTransacCouponImage= (ImageView) TransacDetailsDialog.findViewById(R.id.houserent_IV_sendMoney);
            DialogTransacEmailImage= (ImageView) TransacDetailsDialog.findViewById(R.id.shopping_IV_sendMoney);
            DialogTransacSupportImage= (ImageView) TransacDetailsDialog.findViewById(R.id.utilities_IV_sendMoney);
            DialogTransacCouponText= (TextView) TransacDetailsDialog.findViewById(R.id.houserent_TV_sendMoney);
            DialogTransacSupportImage= (ImageView) TransacDetailsDialog.findViewById(R.id.utilities_IV_sendMoney);
            DialogTransacSupportText= (TextView) TransacDetailsDialog.findViewById(R.id.utilities_TV_sendMoney);
            DialogCouponLayout.setEnabled(false);
            DialogTransacCouponImage.getDrawable().mutate().setColorFilter(ContextCompat.getColor(this,R.color.icon_disable_state), PorterDuff.Mode.SRC_IN);
            DialogTransacCouponText.setTextColor(ContextCompat.getColor(this,R.color.text_disable_red));
            DialogCloseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TransacDetailsDialog.dismiss();
                }
            });
        }

        DialogTransacSupportLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransactionsActivity.this, SupportActivity.class);
                i.putExtra("transacpojo",Obj);
                startActivity(i);
            }
        });

        DialogEmailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuildEmailInvoiceDialog();
            }
        });
        switch (Integer.parseInt(Obj.TransactionDescription))
        {
            case 1: BuildPaymentView(Obj); break;
            case 2:BuildAddMoneyView(Obj);break;
            case 3:BuildSendMoneyView(Obj);break;
            case 4:BuildRecevieMoneyView(Obj);break;
        }
        TransacDetailsDialog.show();
    }

    private void BuildPaymentView(TransactionPojo Obj)
    {
        if(Obj.merchantLogo!=null)
        {
            if(!Obj.merchantLogo.equalsIgnoreCase(""))
            {
                Picasso.with(this).load(Obj.merchantLogo).into(DialogTransacLogo);
            }
        }
        DialogTransacTitle.setText(Obj.merchantstore);
        DialogTransacCode.setText("Code: ");
        DialogTransacCode.append(Html.fromHtml("<font color='#d12127'>"+Obj.receiver+"</font>"));
        DialogTransacAddress.setText(Obj.merchantAddress);
        DialogTransacDate.setText(Obj.transactionDate+" | "+Obj.transactionTime);
        DialogTransbillno.setText("Transaction ID: "+Obj.AppTransactionID);
        DialogTransacPaymentStatus.setText(Obj.transactionStatus);
        if(Obj.transactionStatus.equalsIgnoreCase("success")) {
            DialogTransacStatusImage.setImageResource(R.drawable.ic_success);
        }
        else if(Obj.transactionStatus.equalsIgnoreCase("aborted")||Obj.transactionStatus.equalsIgnoreCase("failure"))
        {
            DialogTransacStatusImage.setImageResource(R.drawable.ic_failed);
        }
        else
        {
            DialogTransacStatusImage.setImageResource(R.drawable.ic_in_process);
        }
        DialogTransacAmount.setText(Html.fromHtml("<font color='#000000'><small>&#8377;  </small></font><font>"+Obj.amount+"</font>"));



    }

    private void BuildAddMoneyView(TransactionPojo Obj)
    {
        DialogTransacLogo.setImageResource(R.drawable.ic_add_money);
        DialogTransacTitle.setText("Added Money");
        DialogTransacCode.setText(Html.fromHtml("<font color='#d12127'>From: You</font>"));
        DialogTransacDate.setText(Obj.transactionDate+" | "+Obj.transactionTime);
        DialogTransbillno.setText("Transaction ID: "+Obj.AppTransactionID);
        DialogTransacPaymentStatus.setText(Obj.transactionStatus);
        if(Obj.transactionStatus.equalsIgnoreCase("success")) {
            DialogTransacStatusImage.setImageResource(R.drawable.ic_success);
        }
        else if(Obj.transactionStatus.equalsIgnoreCase("aborted")||Obj.transactionStatus.equalsIgnoreCase("failure"))
        {
            DialogTransacStatusImage.setImageResource(R.drawable.ic_failed);
        }
        else
        {
            DialogTransacStatusImage.setImageResource(R.drawable.ic_in_process);
        }
        DialogTransacAmount.setText(Html.fromHtml("<font color='#000000'><small>&#8377;  </small></font><font>"+Obj.amount+"</font>"));
    }

    private void BuildSendMoneyView(TransactionPojo Obj)
    {
        DialogTransacLogo.setImageResource(R.drawable.ic_send_money);
        if(Obj.merchantLogo!=null)
        {
            if(!Obj.merchantLogo.equalsIgnoreCase(""))
            {
                Picasso.with(this).load(Obj.merchantLogo).into(DialogTransacLogo);
            }
        }

        DialogTransacTitle.setText("Sent Money");
        DialogTransacCode.setText("To: ");
        DialogTransacCode.append(Html.fromHtml("<font color='#d12127'>"+Obj.receiver+"</font>"));
        DialogTransacDate.setText(Obj.transactionDate+" | "+Obj.transactionTime);
        DialogTransbillno.setText("Transaction ID: "+Obj.AppTransactionID);
        DialogTransacPaymentStatus.setText(Obj.transactionStatus);
        if(Obj.transactionStatus.equalsIgnoreCase("success")) {
            DialogTransacStatusImage.setImageResource(R.drawable.ic_success);
        }
        else if(Obj.transactionStatus.equalsIgnoreCase("aborted")||Obj.transactionStatus.equalsIgnoreCase("failure"))
        {
            DialogTransacStatusImage.setImageResource(R.drawable.ic_failed);
        }
        else
        {
            DialogTransacStatusImage.setImageResource(R.drawable.ic_in_process);
        }
        DialogTransacAmount.setText(Html.fromHtml("<font color='#000000'><small>&#8377;  </small></font><font>"+Obj.amount+"</font>"));
    }

    private void BuildRecevieMoneyView(TransactionPojo Obj)
    {
        DialogTransacLogo.setImageResource(R.drawable.ic_receive_money);
        if(Obj.merchantLogo!=null)
        {
            if(!Obj.merchantLogo.equalsIgnoreCase(""))
            {
                Picasso.with(this).load(Obj.merchantLogo).into(DialogTransacLogo);
            }
        }

        DialogTransacTitle.setText("Received Money");
        DialogTransacCode.setText("From: ");
        DialogTransacCode.append(Html.fromHtml("<font color='#d12127'>"+Obj.Sender+"</font>"));
        DialogTransacDate.setText(Obj.transactionDate+" | "+Obj.transactionTime);
        DialogTransbillno.setText("Transaction ID: "+Obj.AppTransactionID);
        DialogTransacPaymentStatus.setText(Obj.transactionStatus);
        if(Obj.transactionStatus.equalsIgnoreCase("success")) {
            DialogTransacStatusImage.setImageResource(R.drawable.ic_success);
        }
        else if(Obj.transactionStatus.equalsIgnoreCase("aborted")||Obj.transactionStatus.equalsIgnoreCase("failure"))
        {
            DialogTransacStatusImage.setImageResource(R.drawable.ic_failed);
        }
        else
        {
            DialogTransacStatusImage.setImageResource(R.drawable.ic_in_process);
        }
        DialogTransacAmount.setText(Html.fromHtml("<font color='#000000'><small>&#8377;  </small></font><font>"+Obj.amount+"</font>"));
    }

    public void BuildEmailInvoiceDialog()
    {
        if(EmailInvoiceDialog==null)
        {
            EmailInvoiceDialog=new Dialog(this,R.style.ResetTheme);
            EmailInvoiceDialog.setContentView(R.layout.dialog_email_invoice);
            InvoiceEmailET= (RobotoRegularEditText) EmailInvoiceDialog.findViewById(R.id.invoice_mobileno);
            InvoiceSendBtn= (AppCompatButton) EmailInvoiceDialog.findViewById(R.id.invoice_send_btn);
            InvoiceCancelBtn= (AppCompatButton) EmailInvoiceDialog.findViewById(R.id.invoice_cancel);
            Utils.setButtonTint(InvoiceSendBtn,ContextCompat.getColorStateList(this,R.color.button_tint));
            Utils.setButtonTint(InvoiceCancelBtn,ContextCompat.getColorStateList(this,R.color.button_tint));
            EmailInvoiceDialog.setCancelable(true);
        }

        InvoiceSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InvoiceEmailET.getText().toString().length() > 0) {
                        if (isEmailValid(InvoiceEmailET.getText().toString())) {

                        } else {
                            Utils.GenerateSnackbar(TransactionsActivity.this,coordinatorLayout,"Please enter a valid Email ID");
                            return;
                        }
                    }
                }
        });

        InvoiceCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmailInvoiceDialog.dismiss();
            }
        });

        EmailInvoiceDialog.show();

    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        DialogTransacTitle.setText("");
        DialogTransacCode.setText("");
        DialogTransacAddress.setText("");
        DialogTransacDate.setText("");
        DialogTransbillno.setText("");
        DialogTransacPaymentStatus.setText("");
        DialogTransacStatusImage.setImageResource(R.drawable.ic_success);
        DialogTransacAmount.setText("");
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

    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int mVerticalSpaceHeight;

        public VerticalSpaceItemDecoration(int mVerticalSpaceHeight) {
            this.mVerticalSpaceHeight = mVerticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = mVerticalSpaceHeight;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(ClickandPay.getInstance().ShouldDisplayPasscode) {
            Intent i = new Intent(TransactionsActivity.this, PassCodeActivity.class);
            i.putExtra("fromActivity", "Login");
            startActivity(i);
            finish();
        }
    }
}
