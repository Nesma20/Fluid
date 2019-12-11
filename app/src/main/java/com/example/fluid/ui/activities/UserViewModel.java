package com.example.fluid.ui.activities;

import android.util.Log;

import com.example.fluid.model.pojo.ReturnedStatus;
import com.example.fluid.model.pojo.User;
import com.example.fluid.model.services.repositories.UserRepository;
import com.example.fluid.ui.listeners.OnDataChangedCallBackListener;
import com.example.fluid.ui.listeners.UserHandler;
import com.example.fluid.utils.App;
import com.example.fluid.utils.PreferenceController;

import java.io.IOException;

public class UserViewModel {
    boolean isUserCreated = false;
    UserRepository userRepository = new UserRepository();

    public void createUser(final String email, String firstName, String familyName, final String imageProfile, final String displayName, final OnDataChangedCallBackListener onDataChangedCallBackListener) {
        final User user = new User(email, firstName, familyName, "F");

        userRepository.createNewUser(user, new UserHandler() {
            @Override
            public void onUserAddedHandler(ReturnedStatus returnedStatus) {
                isUserCreated = checkOnReturnedStatus(returnedStatus);

                if (isUserCreated) {
                    saveDataInSharedPreference(email, displayName, imageProfile, returnedStatus.getReturnStatus().intValue());
                }
                Log.i("LoginActivity", "status " + returnedStatus.getReturnStatus().intValue());
                onDataChangedCallBackListener.onResponse(isUserCreated);
            }
        });
    }

    private void saveDataInSharedPreference(String email, String displayName, String imageProfile, int id) {
        PreferenceController.getInstance(App.getContext()).persist(PreferenceController.PREF_EMAIL, email);
        PreferenceController.getInstance(App.getContext()).persist(PreferenceController.PREF_USER_NAME, displayName);
        PreferenceController.getInstance(App.getContext()).persist(PreferenceController.PREF_IMAGE_PROFILE_URL, imageProfile);
        PreferenceController.getInstance(App.getContext()).persist(PreferenceController.PREF_USER_ID, String.valueOf(id));
    }

    public boolean checkOnReturnedStatus(ReturnedStatus status) {
        if (status.getReturnStatus().intValue() > 0) {
            return true;
        } else {
            return false;
        }

    }


}
