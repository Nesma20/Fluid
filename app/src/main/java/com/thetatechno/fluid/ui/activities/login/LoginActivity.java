package com.thetatechno.fluid.ui.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.thetatechno.fluid.R;
import com.thetatechno.fluid.ui.activities.main.MainActivity;
import com.thetatechno.fluid.ui.listeners.OnDataChangedCallBackListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.Person;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = "LoginActivity";
    private SignInButton loginWithGoogleAccountBtn;

    private static final int RC_SIGN_IN = 1;
    GoogleSignInClient mGoogleSignInClient;
    UserViewModel userViewModel = new UserViewModel();
    String token = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginWithGoogleAccountBtn = findViewById(R.id.login_btn);
        setTitle(getString(R.string.login));


// Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
//        Scope myScope = new Scope("https://www.googleapis.com/auth/user.birthday.read");
//        Scope myScope2 = new Scope(Scopes.PLUS_ME);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestScopes(myScope, myScope2)
                .requestIdToken(getString(R.string.request_id_token))
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
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



//    public void getAccountDetails(final GoogleSignInAccount account) throws IOException {
//        Thread myThread = new Thread() {
//            @Override
//            public void run() {
//                HttpTransport httpTransport = new NetHttpTransport();
//                JacksonFactory jsonFactory = new JacksonFactory();
//                String clientId = getString(R.string.client_id);
//                String clientSecret = getString(R.string.client_secret);
//
//                GoogleCredential credential = new GoogleCredential.Builder()
//                        .setTransport(httpTransport)
//                        .setJsonFactory(jsonFactory)
//                        .setClientSecrets(clientId, clientSecret)
//                        .build();
//
//                PeopleService peopleService =
//                        new PeopleService.Builder(httpTransport, jsonFactory, credential).setApplicationName("Fluid").
//                                build();
//                try {
//                    Log.i(TAG, "account token : " + account.getIdToken());
//                    Person profile = peopleService.people().get("people/me")
//                            .setPersonFields("birthdays,names,genders")
//                            .execute();
//                    Log.i(TAG, profile.getEmailAddresses().get(0).getValue());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        myThread.start();
//
//
//    }



    private void redirectToMain() {


        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
           final GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "getInstanceId failed", task.getException());
                                return;
                            }
                            // Get new Instance ID token
                            token = task.getResult().getToken();
                            // Log
                            Log.i(TAG, "device token : " + token);
                            Log.i(TAG, "account : " + account.getDisplayName() + " : email " + account.getEmail() + "account id " + account.getId() + "account image url " + account.getPhotoUrl().getPath());

                                   userViewModel.createUser(account.getEmail(), account.getGivenName(), account.getFamilyName(),
                                           account.getPhotoUrl().getPath(), account.getDisplayName(), token,
                                           new OnDataChangedCallBackListener<Boolean>() {
                                               @Override
                                               public void onResponse(Boolean dataChanged) {
//                                                   if (dataChanged.booleanValue()) {
                                                       redirectToMain();

                                               }
                                           });

                               }
                           });






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
