package com.thetatechno.fluid.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.thetatechno.fluid.R;
import com.thetatechno.fluid.utils.CheckForNetwork;

public class NoInternetConnectionActivity extends BaseActivity {
Button retryBtn;
ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet_connection);
        retryBtn = findViewById(R.id.retryConnectBtn);
        mProgressBar = findViewById(R.id.internet_progress_bar);

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);

                if(CheckForNetwork.isConnectionOn(NoInternetConnectionActivity.this))
                {
                    mProgressBar.setVisibility(View.GONE);
                    finish();
                }
                else{
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
