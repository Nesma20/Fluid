package com.thetatechno.fluid.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.thetatechno.fluid.R;
import com.thetatechno.fluid.ui.activities.login.LoginActivity;
import com.thetatechno.fluid.ui.activities.main.MainActivity;
import com.thetatechno.fluid.utils.App;
import com.thetatechno.fluid.utils.CheckForNetwork;
import com.thetatechno.fluid.utils.Constants;
import com.thetatechno.fluid.utils.PreferenceController;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.Locale;

public class SplashActivity extends BaseActivity {
    GoogleSignInClient mGoogleSignInClient;

   private static final String TAG ="SplashActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.i(TAG,"language name from local default: "+ Locale.getDefault().getLanguage());

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
                .requestIdToken(getString(R.string.request_id_token))
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
                Log.i(TAG,"email"+PreferenceController.getInstance(App.getContext()).get(PreferenceController.PREF_EMAIL));
                Log.i(TAG,"base url"+PreferenceController.getInstance(App.getContext()).get(Constants.BASE_URL));
                Log.i(TAG,"username"+PreferenceController.getInstance(App.getContext()).get(PreferenceController.PREF_USER_NAME));
                if ( PreferenceController.getInstance(App.getContext()).get(PreferenceController.PREF_USER_NAME).isEmpty()  && PreferenceController.getInstance(App.getContext()).get(PreferenceController.PREF_EMAIL).isEmpty()  )
                    return false;
                else {
                    return true;
                }
            }

        }, 500);
    }
}