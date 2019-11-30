package com.example.fluid.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.fluid.R;
import com.example.fluid.utils.PreferenceController;

public class LoginActivity extends AppCompatActivity {
private final String TAG = "LoginActivity";
private Button loginWithGoogleAccountBtn;
private static final int INTENT_REQUEST_CODE= 1;
    AccountManager accountManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginWithGoogleAccountBtn = findViewById(R.id.loginWithGoogleAccountBtn);
        loginWithGoogleAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 accountManager = (AccountManager) getSystemService(Context.ACCOUNT_SERVICE);
                Intent intent = accountManager.newChooseAccountIntent(null, null, new String[]{"com.google","com.google.android.legacyimap"},
                        false, null, null, null, null);
                startActivityForResult(intent, INTENT_REQUEST_CODE);



            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG,"onActivityResult: entered");
        Log.i(TAG,"onActivityResult: request code "+requestCode+" result code "+resultCode);
        if(requestCode== INTENT_REQUEST_CODE){
            Log.i(TAG,"onActivityResult: ");
            if (resultCode == RESULT_OK) {

            //    Log.i(TAG,data.getStringExtra(String.valueOf(accountManager.getAccountsByType("com.google").length)));
                Log.i(TAG,data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));

                PreferenceController.getInstance(this).persist(PreferenceController.PREF_EMAIL,AccountManager.KEY_ACCOUNT_NAME);


                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "didn't choose account", Toast.LENGTH_LONG).show();
            }
        }

    }
}
