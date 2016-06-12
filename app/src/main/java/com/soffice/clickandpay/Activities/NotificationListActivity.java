package com.soffice.clickandpay.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.soffice.clickandpay.R;

/**
 * Created by Surya on 30-04-2016.
 */
public class NotificationListActivity extends AppCompatActivity
{
    RecyclerView recyclerView;
    ProgressBar Pbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Pbar= (ProgressBar) findViewById(R.id.pbar);
        recyclerView= (RecyclerView) findViewById(R.id.recyclerview);

    }
}
