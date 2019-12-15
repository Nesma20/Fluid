package com.example.fluid.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.fluid.R;
import com.example.fluid.utils.App;
import com.example.fluid.utils.CheckForNetwork;
import com.example.fluid.utils.PreferenceController;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class SplashActivity extends BaseActivity {
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


    }

    public void redirectToLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void redirectToNoInternetConnection() {
        Intent intent = new Intent(SplashActivity.this, NoInternetConnectionActivity.class);
        startActivity(intent);
        finish();
    }

    public void redirectToMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("574842241815-r9t9g16s08jflvunfu9rjdd99uscvfir.apps.googleusercontent.com")
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                if (CheckForNetwork.isConnectionOn(SplashActivity.this)) {
                    if (isUserLoggedIn()) {
                        redirectToMain();
                    } else {
                        redirectToLogin();
                    }
                } else {
                    redirectToNoInternetConnection();
                }


            }

            private boolean isUserLoggedIn() {
                if (PreferenceController.getInstance(App.getContext()).get(PreferenceController.PREF_EMAIL).isEmpty() )
                    return false;
                else {
                    return true;
                }
            }

        }, 500);
    }
}