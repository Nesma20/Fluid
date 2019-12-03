package com.example.fluid.ui.activities;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fluid.R;
import com.example.fluid.utils.PreferenceController;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleBrowserClientRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.Person;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = "LoginActivity";
    private SignInButton loginWithGoogleAccountBtn;
    private static final int RC_SIGN_IN = 1;
    AccountManager accountManager;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
// Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("574842241815-stc69a4n3pctnmp0dvukd7dpv73o6r4a.apps.googleusercontent.com")
                .build();
// Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        loginWithGoogleAccountBtn = findViewById(R.id.loginWithGoogleAccountBtn);
        loginWithGoogleAccountBtn.setSize(SignInButton.SIZE_STANDARD);
        loginWithGoogleAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();


            }

        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            Toast.makeText(this, "account : " + account.getDisplayName() + " : email " + account.getEmail() + "account id " + account.getId(), Toast.LENGTH_SHORT).show();
            try {
                settt(account);
            } catch (IOException e) {
                e.printStackTrace();
            }
            redirectToMain();
        }
    }

    public void settt(final GoogleSignInAccount account) throws IOException {
        Thread myThread = new Thread() {
            @Override
            public void run() {


                HttpTransport httpTransport = new NetHttpTransport();
                JacksonFactory jsonFactory = new JacksonFactory();
                String clientId = "574842241815-20po4mvnj6ndc5cecig7sh3a0vvd5euc.apps.googleusercontent.com";
                String clientSecret = "Lkq4-hBzrBw1EQ6kYW-eA-2A";

                GoogleCredential credential = new GoogleCredential.Builder()
                        .setTransport(httpTransport)
                        .setJsonFactory(jsonFactory)
                        .setClientSecrets(clientId, clientSecret)
                        .build();

                PeopleService peopleService =
                        new PeopleService.Builder(httpTransport, jsonFactory, credential).setApplicationName("Fluid").
                                build();
                try {

                    Log.i(TAG,"account token"+account.getIdToken());
                    Person profile = peopleService.people().get("people/" + account.getId())
                            .setPersonFields("names,emailAddresses")
                            .execute();

                    Log.i(TAG, profile.getEmailAddresses().get(0).getValue());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();


    }


    private void redirectToMain() {

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            PreferenceController.getInstance(this).persist(PreferenceController.PREF_EMAIL, AccountManager.KEY_ACCOUNT_NAME);
            Toast.makeText(this, "account : " + account.getDisplayName() + " : email " + account.getEmail() + "account id " + account.getId(), Toast.LENGTH_SHORT).show();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            e.printStackTrace();
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, "signInResult:failed code=" + e.getStatusCode() + " message" + e.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: entered");
        Log.i(TAG, "onActivityResult: request code " + requestCode + " result code " + resultCode);
        if (requestCode == RC_SIGN_IN) {
            Log.i(TAG, "onActivityResult: ");
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            handleSignInResult(task);

        }

    }
}
