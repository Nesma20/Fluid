package com.example.fluid.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.fluid.R;

public class NoInternetConnectionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet_connection);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
