package com.soffice.clickandpay.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.soffice.clickandpay.R;
import com.soffice.clickandpay.UI.RobotoRegularTextView;

public class AboutUsActivity
  extends AppCompatActivity
{
  RobotoRegularTextView PrivacyTv;
  RobotoRegularTextView TomTv;
  ImageView back_IV;
  
  private void DisplayPolicy()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.clickandpay.co.in/terms.html"));
    startActivity(localIntent);
  }
  
  public void onBackPressed()
  {
    super.onBackPressed();
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_aboutus);
    this.TomTv = ((RobotoRegularTextView)findViewById(R.id.tomtv));
    this.PrivacyTv = ((RobotoRegularTextView)findViewById(R.id.privacytv));
    this.TomTv.setOnClickListener(new OnClickListener()
    {
      public void onClick(View v)
      {
        AboutUsActivity.this.DisplayPolicy();
      }
    });
    this.PrivacyTv.setOnClickListener(new OnClickListener()
    {
      public void onClick(View v)
      {
        AboutUsActivity.this.DisplayPolicy();
      }
    });
  }
}
