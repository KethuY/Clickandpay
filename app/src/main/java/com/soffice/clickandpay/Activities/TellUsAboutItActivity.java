package com.soffice.clickandpay.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.soffice.clickandpay.ClickandPay;
import com.soffice.clickandpay.NetWork.JsonRequester;
import com.soffice.clickandpay.NetWork.TaskListner;
import com.soffice.clickandpay.NetWork.Urls;
import com.soffice.clickandpay.R;
import com.soffice.clickandpay.UI.RobotoLightEditText;
import com.soffice.clickandpay.UI.RobotoRegularEditText;
import com.soffice.clickandpay.UI.RobotoRegularTextView;
import com.soffice.clickandpay.Utilty.Display;
import com.soffice.clickandpay.Utilty.SessionManager;
import com.soffice.clickandpay.Utilty.Utils;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class TellUsAboutItActivity extends AppCompatActivity implements View.OnClickListener,TaskListner
{

    RobotoLightEditText EmaiEt,UploadPathEt,DescriptionEt;
    RobotoRegularTextView Callus;
    RobotoRegularTextView OthersTv;
    AppCompatButton SubmitBtn;
    private final int CAMERA_REQUEST=79;
    Bitmap TransacBitpmap;
    String Bytes;
    SessionManager sessionManager;
    ImageView BackBtn;
    CoordinatorLayout coordinatorLayout;
    JsonRequester DataRequester;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager= ClickandPay.getInstance().getSession();
        DataRequester=new JsonRequester(this);
        setContentView(R.layout.activity_tell_us_about_it);
        BuildUI();
    }

    private void BuildUI()
    {
        coordinatorLayout= (CoordinatorLayout) findViewById(R.id.mycoordinator);
        Callus= (RobotoRegularTextView) findViewById(R.id.text_call_us);
        Callus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:7396891206"));
                startActivity(intent);
            }
        });
        BackBtn= (ImageView) findViewById(R.id.back_IV);
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TellUsAboutItActivity.this.finish();
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        EmaiEt= (RobotoLightEditText) findViewById(R.id.support_emailet);
        UploadPathEt= (RobotoLightEditText) findViewById(R.id.support_uploadpath_et);
        UploadPathEt.setInputType(InputType.TYPE_NULL);
        UploadPathEt.setKeyListener(null);
        DescriptionEt= (RobotoLightEditText) findViewById(R.id.support_descrp_et);
        SubmitBtn= (AppCompatButton) findViewById(R.id.support_submit_btn);
        Utils.setButtonTint(SubmitBtn, ContextCompat.getColorStateList(this,R.color.button_tint));
        UploadPathEt.setOnClickListener(this);
        SubmitBtn.setOnClickListener(this);
        OthersTv= (RobotoRegularTextView) findViewById(R.id.otherText);
        OthersTv.setText(Html.fromHtml(getIntent().getStringExtra("cat")));
        EmaiEt.setText(sessionManager.getEmailId());
        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DescriptionEt.getText().length()>0)
                {
                    ServerCall();
                }
                else
                {
                    Display.DisplaySnackbar(TellUsAboutItActivity.this,coordinatorLayout,"Please enter description");
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.support_uploadpath_et:UploadImage();break;
            case R.id.support_submit_btn:break;
        }
    }

    private void UploadImage()
    {
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Upload Picture"),CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==CAMERA_REQUEST&&resultCode==RESULT_OK)
        {
            Uri selectedImageUri = data.getData();
            /*String[] projection = { MediaStore.MediaColumns.DATA };
            CursorLoader cursorLoader = new CursorLoader(this,selectedImageUri, projection, null, null, null);
            Cursor cursor =cursorLoader.loadInBackground();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            String selectedImagePath = cursor.getString(column_index);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(selectedImagePath, options);
            final int REQUIRED_SIZE = 200;
            int scale = 1;
            while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                    && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;
            options.inSampleSize = scale;
            options.inJustDecodeBounds = false;
            TransacBitpmap = BitmapFactory.decodeFile(selectedImagePath, options);
            Matrix matrix=new Matrix();
            matrix.postRotate(90);
            TransacBitpmap=Bitmap.createBitmap(TransacBitpmap,100,0,200,200,matrix,true);
            Bytes= ConverttoString(TransacBitpmap);*/
            UploadPathEt.setText(selectedImageUri.getPath());
        }
    }

    public void ServerCall()
    {
        if(Utils.CheckInternet(this))
        {
                HashMap<String,String> params =new HashMap<>();
                params.put("authkey", this.sessionManager.getAuthKey());
                params.put("image", "");
                if (getIntent().getExtras().containsKey("txtid"))
                {
                    params.put("txtid", getIntent().getStringExtra("txtid"));
                }
                params.put("desc", this.DescriptionEt.getText().toString());
                params.put("issue", getIntent().getStringExtra("opt"));
                Log.d("Params", params.toString());
                DataRequester.StringRequesterFormValues(Urls.support_url, 1, getLocalClassName(),Urls.support_method, params, Urls.support_method);
                SubmitBtn.setEnabled(false);
                SubmitBtn.setText("Please wait..");
        }
        else {
            Utils.GenerateSnackbar(this, this.coordinatorLayout, "Please check your internet connection and try again.");
        }
    }

    public  String ConverttoString(Bitmap bitmap) {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] b = baos.toByteArray();
            String temp = null;
            try {
                System.gc();
                temp = Base64.encodeToString(b, Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                b = baos.toByteArray();
                temp = Base64.encodeToString(b, Base64.DEFAULT);
                Display.DisplayLogD("Support","Out of memory error catched");
            }
            return temp;
        }

    @Override
    public void onTaskfinished(String response, int cd, String _className, String _methodName) {
        this.SubmitBtn.setEnabled(true);
        this.SubmitBtn.setText("Submit");
        if (cd == 0) {
            Utils.GenerateSnackbar(this, this.coordinatorLayout, "Oops...Something went wrong. Please try again.");
        }
        else if (cd == 5&&_className.equals(getLocalClassName())&&_methodName.equals("support_method"))
        {
            Display.DisplayLogD("Output", response);
            Gson gson=new Gson();
            SupportModel supportModel=gson.fromJson(response,SupportModel.class);
            if (supportModel.Code.equals("200"))
            {
                this.DescriptionEt.setText("");
                this.UploadPathEt.setText("");
                Utils.GenerateSnackbar(this, this.coordinatorLayout, supportModel.Message);
            }
            else if (supportModel.Code.equalsIgnoreCase("205")) {
                ClickandPay.getInstance().RedirectToLogin();
                Utils.GenerateSnackbar(this, this.coordinatorLayout, supportModel.Message);
            }
        }
        else
        {
            Utils.GenerateSnackbar(this, this.coordinatorLayout, "Oops...Something went wrong. Please try again.");
        }
    }

    public class SupportModel
    {
        @SerializedName("CODE")
        String Code;
        @SerializedName("MESSAGE")
        String Message;
    }
}
