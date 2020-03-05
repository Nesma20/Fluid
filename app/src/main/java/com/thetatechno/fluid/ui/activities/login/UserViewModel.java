package com.thetatechno.fluid.ui.activities.login;

import com.thetatechno.fluid.model.pojo.ReturnedStatus;
import com.thetatechno.fluid.model.pojo.User;
import com.thetatechno.fluid.model.services.repositories.UserRepository;
import com.thetatechno.fluid.ui.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluid.ui.listeners.UserHandler;
import com.thetatechno.fluid.utils.App;
import com.thetatechno.fluid.utils.PreferenceController;

public class UserViewModel {
    boolean isUserCreated = false;
    UserRepository userRepository = new UserRepository();

    public void createUser(final String email, String firstName, String familyName, final String imageProfile, final String displayName, final String token, final OnDataChangedCallBackListener onDataChangedCallBackListener) {
        final User user = new User(email, firstName, familyName, "F",token);

        userRepository.createNewUser(user, new UserHandler() {
            @Override
            public void onUserAddedHandler(ReturnedStatus returnedStatus) {
                isUserCreated = checkOnReturnedStatus(returnedStatus);

               if (isUserCreated) {
                    saveDataInSharedPreference(email, displayName, imageProfile,  3385);

             }
                onDataChangedCallBackListener.onResponse(isUserCreated);
            }
        });
    }

    public void getIpAndPortToCreateRetrofitInstance(final OnDataChangedCallBackListener<Boolean> onDataChangedCallBackListener){
//        FirebaseDatabaseService.getPortAndIpAddress(new OnDataChangedCallBackListener<Boolean>() {
//            @Override
//            public void onResponse(Boolean dataChanged) {
//                onDataChangedCallBackListener.onResponse(dataChanged);
//            }
//        });
    }
    private void saveDataInSharedPreference(String email, String displayName, String imageProfile, int id) {
        PreferenceController.getInstance(App.getContext()).persist(PreferenceController.PREF_EMAIL, email);
        PreferenceController.getInstance(App.getContext()).persist(PreferenceController.PREF_USER_NAME, displayName);
        PreferenceController.getInstance(App.getContext()).persist(PreferenceController.PREF_IMAGE_PROFILE_URL, imageProfile);
        PreferenceController.getInstance(App.getContext()).persist(PreferenceController.PREF_USER_ID, String.valueOf(id));
    }

    public boolean checkOnReturnedStatus(ReturnedStatus status) {
        if (status !=null && status.getReturnStatus().intValue() > 0) {
            return true;
        } else {
            return false;
        }

    }


}
