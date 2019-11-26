package com.example.fluid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.fluid.ui.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
private final String TAG = "LoginActivity";
private Button getAllAccountsBtn;
private static final int INTENT_REQUEST_CODE= 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        getAllAccountsBtn = findViewById(R.id.getAllAccountsBtn);
        getAllAccountsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AccountManager.newChooseAccountIntent(null, null, new String[]{"com.google","com.google.android.legacyimap"},
                        false, null, null, null, null);
                startActivityForResult(intent, INTENT_REQUEST_CODE);


            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG,"onActivityResult: entered");
        Log.i(TAG,"onActivityResult: resquest code "+requestCode+" result code "+resultCode);
        if(requestCode== INTENT_REQUEST_CODE){
            Log.i(TAG,"onActivityResult: ");
            if (resultCode == RESULT_OK) {
                 Log.i(TAG,data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));
                Log.i(TAG,data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "didn't choose account", Toast.LENGTH_LONG).show();
            }
        }

    }
}
