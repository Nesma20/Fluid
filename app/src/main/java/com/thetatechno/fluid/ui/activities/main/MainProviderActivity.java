package com.thetatechno.fluid.ui.activities.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.thetatechno.fluid.R;
import com.thetatechno.fluid.ui.home.HomeProviderFragment;

public class MainProviderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_provider);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.providerFrame, new HomeProviderFragment())
                .commit();
    }
}
