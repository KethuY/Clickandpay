package com.soffice.clickandpay.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.soffice.clickandpay.R;


public class ExtraSettingsActivity extends AppCompatActivity {

    RelativeLayout suggest_a_store_row, helpUsToImProve_row,rateus_row;
    ImageView back_IV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        suggest_a_store_row = (RelativeLayout) findViewById(R.id.suggest_a_store_row);
        helpUsToImProve_row = (RelativeLayout) findViewById(R.id.helpUsToImProve_row);
        rateus_row= (RelativeLayout) findViewById(R.id.rateus_row);
        back_IV = (ImageView) findViewById(R.id.back_IV);


        suggest_a_store_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ExtraSettingsActivity.this, SuggestAStireActivity.class);
                startActivity(i);
            }
        });

        helpUsToImProve_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ExtraSettingsActivity.this, HelpUsToImproveActivity.class);
                startActivity(i);
            }
        });

        rateus_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        back_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

}
