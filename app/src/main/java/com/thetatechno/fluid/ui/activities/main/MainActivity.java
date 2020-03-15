package com.thetatechno.fluid.ui.activities.main;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.test.espresso.IdlingResource;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.thetatechno.fluid.R;
import com.thetatechno.fluid.model.pojo.CurrentLocation;
import com.thetatechno.fluid.model.pojo.LocationList;
import com.thetatechno.fluid.ui.activities.BaseActivity;
import com.thetatechno.fluid.ui.activities.NoInternetConnectionActivity;
import com.thetatechno.fluid.ui.activities.login.LoginActivity;
import com.thetatechno.fluid.ui.adapters.ViewPagerAdapter;
import com.thetatechno.fluid.ui.home.HomeFragment;
import com.thetatechno.fluid.ui.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluid.ui.listeners.UpdateEventListener;
import com.thetatechno.fluid.ui.activities.locations.LocationsActivity;
import com.thetatechno.fluid.utils.CheckForNetwork;
import com.thetatechno.fluid.utils.Constants;
import com.thetatechno.fluid.utils.PreferenceController;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener {

    private ArrayList<CurrentLocation> currentLocationList = new ArrayList<>();
    boolean isAppointmentStarted = true;
    private FloatingActionButton startOrEndFab, callFab, arrivalFab;
    private Toolbar toolbar;
    private ViewPager2 mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private TabLayout mTabLayout;
    private ProgressBar mProgressBar;
    DrawerLayout drawer;
    NavigationView navigationView;
    private List<UpdateEventListener> myCallListenerList;
    GoogleSignInClient mGoogleSignInClient;
    TextView tabTitle;
    TextView tabCount;
    View customTabView;
    // user data UI
    ImageView mImageView;
    TextView mEmailTxtView;
    TextView mUserNameTxtView;
    ConstraintLayout noLocationAvailableConstraintLayout;
    MainViewModel mainViewModel;
    Button retryGetLocation;
    Map<Integer,Fragment> fragmentsListMap = new HashMap<>();
    UpdateEventListener listener;
    HomeFragment homeFragment;
    AlertDialog alertDialog;
    private static final int DRAWABLE_RESOURCE_FOR_START_STATE = R.drawable.animation_fab_finish;
    private static final int COLOR_ID_FOR_START_STATE = R.color.colorAccent;
    private static final int DRAWABLE_RESOURCE_FOR_FINISH_STATE = R.drawable.animation_fab_start;
    private static final int COLOR_ID_FOR_FINISH_STATE = R.color.colorPrimary;
    public static final String LOCATIONS = "locations";
    public static final String TAG = "MainActivity";
    private int numOfCalls;
    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource loadDataIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        if (savedInstanceState != null) {
            myCallListenerList = savedInstanceState.getParcelableArrayList("listenerList");
                currentLocationList = savedInstanceState.getParcelableArrayList("locationList");

        }
        setContentView(R.layout.activity_main);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.request_id_token))
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        initializeViews();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        checkOnTheCurrentLanguage();
        View header = navigationView.getHeaderView(0);
        mImageView = header.findViewById(R.id.userImageView);
        mUserNameTxtView = header.findViewById(R.id.userNameTxt);
        mEmailTxtView = header.findViewById(R.id.emailTxtView);
        displayUserInfo();

        callFab.setOnClickListener(v -> {

            if (CheckForNetwork.isConnectionOn(MainActivity.this)) {

                if (isAppointmentStarted) {
                    getCurrentFragment();
                    listener.callPatient();

                } else {
                    showAlertWithMessage(getResources().getString(R.string.error_call_when_there_is_customer_checked_in));
                }
            } else {
                redirectToNoInternetConnection();
            }
        });
        arrivalFab.setOnClickListener(v -> {


            if (CheckForNetwork.isConnectionOn(MainActivity.this)) {
                getCurrentFragment();
                listener.confirmArrived();

            } else {
                redirectToNoInternetConnection();
            }

        });
        startOrEndFab.setOnClickListener(v -> {
            if (CheckForNetwork.isConnectionOn(MainActivity.this)) {
                if (numOfCalls > 0) {
                    getCurrentFragment();
                    if (isAppointmentStarted) {
                        getCurrentFragment();
                        listener.checkInPatient();

                    } else {
                        listener.checkOutPatient();


                    }
                } else {
                    // TODO: alert with there is no customer called to be checked in.
                    showAlertWithMessage(getResources().getString(R.string.error_no_customer_called));
                }
            } else {
                redirectToNoInternetConnection();
            }
        });
        retryGetLocation.setOnClickListener(v -> getAllLocations());
    }

    private void getCurrentFragment() {
        homeFragment = (HomeFragment) fragmentsListMap.get(mViewPager.getCurrentItem());
        listener = homeFragment;
    }

    private void getAllLocations() {
        if (!CheckForNetwork.isConnectionOn(this)) {
            redirectToNoInternetConnection();
        } else {
            getNumOfUnArrivedCustomers();
            mProgressBar.setVisibility(View.VISIBLE);

            mainViewModel.getLocationData(mainViewModel.getDataFromSharedPreference(PreferenceController.PREF_EMAIL), new OnDataChangedCallBackListener<LocationList>() {
                @Override
                public void onResponse(LocationList locationsList) {
                    mProgressBar.setVisibility(View.GONE);
                    if (locationsList != null) {
                        if (locationsList.getItems() != null) {
                            disableNoLocationLayout();

                            if (currentLocationList.size() > 0) {
                                boolean isTheSameList = false;

                                if (locationsList.getItems().size() == currentLocationList.size()) {
                                    for (int i = 0; i < currentLocationList.size(); i++) {
                                        if (currentLocationList.get(i).getSessionId().equals(locationsList.getItems().get(i).getSessionId())) {
                                            isTheSameList = true;
                                        } else
                                            isTheSameList = false;
                                    }
                                }
                                if (!isTheSameList) {
                                    currentLocationList = locationsList.getItems();
                                    setupViewPager(mViewPager);
                                } else if (mViewPagerAdapter == null)
                                    setupViewPager(mViewPager);
                            } else {

                                currentLocationList = locationsList.getItems();
                                setupViewPager(mViewPager);
                            }

                        } else if (locationsList.getStatus().equals("no data found")) {
                            enableLayoutForNoLocations();
                            hideButtonAndTabLayout();

                        }
                    } else {
                        //TODO :handle if there error in return response data was null or failure occured
                    }
                }
            });

        }
    }

    private void displayUserInfo() {
        mainViewModel.getFullName().observe(this, name -> mUserNameTxtView.setText(name));
        mainViewModel.getEmail().observe(this, email -> mEmailTxtView.setText(email));
        mainViewModel.getImageUrl().observe(this, imgUrl -> {
            // TODO: use glide to update image view src
            Glide.with(MainActivity.this)
                    .load(Constants.BASE_GOOGLE_URL_FOR_IMAGES + imgUrl)
                    .circleCrop()
                    .into(mImageView);
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart method");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume method");
        getAllLocations();

    }

    public void checkOnTheCurrentLanguage() {
        if (PreferenceController.getInstance(this).get(PreferenceController.LANGUAGE).equals(Constants.ARABIC)) {
            navigationView.getMenu().findItem(R.id.language_reference).setTitle(R.string.menu_english_language);
        } else if (PreferenceController.getInstance(this).get(PreferenceController.LANGUAGE).equals(Constants.ENGLISH)) {
            navigationView.getMenu().findItem(R.id.language_reference).setTitle(R.string.menu_arabic_language);

        }
    }

    private void animateStartOrEndBtn(String state) {

        if (state == Constants.STARTING_STATE) {
            changeColorAndSrcOfStartAndEndButton(DRAWABLE_RESOURCE_FOR_START_STATE, COLOR_ID_FOR_START_STATE);
            startOrEndFab.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation_start_to_finish_fab_with_rotation));

        } else if (state == Constants.ENDING_STATE) {
            changeColorAndSrcOfStartAndEndButton(DRAWABLE_RESOURCE_FOR_FINISH_STATE, COLOR_ID_FOR_FINISH_STATE);

            startOrEndFab.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation_finish_to_start_fab_with_rotation));

        }

    }

    public void initializeViews() {
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
        startOrEndFab = findViewById(R.id.start_fab);
        callFab = findViewById(R.id.call_fab);
        arrivalFab = findViewById(R.id.confirm_arrive_fab);
        mProgressBar = findViewById(R.id.loading_data_progress_bar);
        noLocationAvailableConstraintLayout = findViewById(R.id.noLocationAvaliable);
        retryGetLocation = findViewById(R.id.retryBtn);
    }

    private void setupViewPager(final ViewPager2 myViewPager) {

        myCallListenerList = new ArrayList<>();
        mViewPagerAdapter = new ViewPagerAdapter(this, currentLocationList);


        myViewPager.setAdapter(mViewPagerAdapter);
        myViewPager.setOffscreenPageLimit(1);
        new TabLayoutMediator(mTabLayout, mViewPager,
                (tab, position) -> tab.setText(currentLocationList.get(position).getFacilityId())
        ).attach();
        for (int i = 0; i < currentLocationList.size(); i++) {
            if (mTabLayout.getTabAt(i).getCustomView() == null)
                mTabLayout.getTabAt(i).setCustomView(updateTabTextView(i, Integer.parseInt(currentLocationList.get(i).getCount())));
        }
        fragmentsListMap = mViewPagerAdapter.getFragmentsList();
        if(fragmentsListMap.size()>0)
        for (int i = 0; i < currentLocationList.size(); i++) {
            homeFragment = (HomeFragment) fragmentsListMap.get(i);
            myCallListenerList.add(homeFragment);
            if (homeFragment != null) {

                Bundle bundle = new Bundle();
                bundle.putString(HomeFragment.ARG_LOCATION_CODE, currentLocationList.get(i).getFacilityId());
                bundle.putString(HomeFragment.ARG_SESSION_ID, currentLocationList.get(i).getSessionId());
                homeFragment.setArgumentsAfterCreation(bundle);

            }

        }


    }

    private View updateTabTextView(int pos, final int listSize) {
        customTabView = getLayoutInflater().inflate(R.layout.location_tab, null);
        tabTitle = customTabView.findViewById(R.id.location_tab_txt_view);
        tabCount = customTabView.findViewById(R.id.new_notifications_for_list_size);
        tabTitle.setText(currentLocationList.get(pos).getFacilityId());
        if (listSize != 0) {
            tabCount.setText(listSize + "");
        } else {

            tabCount.setVisibility(View.GONE);
        }
        Log.i(TAG, "tab count " + tabCount.getText().toString());
        return customTabView;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.language_reference:
                changeLanguage((String) menuItem.getTitle());
                break;

            case R.id.nav_location:
                Intent intent = new Intent(this, LocationsActivity.class);
                startActivity(intent);
                break;
            case R.id.logout:
                signOut();
                break;

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeLanguage(String language) {
        if (!PreferenceController.getInstance(this).get(PreferenceController.LANGUAGE).equals(language)) {
            if (PreferenceController.getInstance(this).get(PreferenceController.LANGUAGE).equals(Constants.ARABIC)) {
                PreferenceController.getInstance(this).persist(PreferenceController.LANGUAGE, Constants.ENGLISH);
                mTabLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            } else {
                PreferenceController.getInstance(this).persist(PreferenceController.LANGUAGE, Constants.ARABIC);
                mTabLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            finish();
            startActivity(new Intent(this, MainActivity.class));
            // recreate();

        }

    }


    public void signOut() {
        signOutFromGoogle();
        redirectToLogin();
    }


    public void redirectToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void redirectToNoInternetConnection() {
        Intent intent = new Intent(MainActivity.this, NoInternetConnectionActivity.class);
        startActivity(intent);

    }

    @Override
    public void onIconChanged(boolean isAppointmentStarted) {
        this.isAppointmentStarted = isAppointmentStarted;
        if (this.isAppointmentStarted) {
            changeColorAndSrcOfStartAndEndButton(DRAWABLE_RESOURCE_FOR_START_STATE, COLOR_ID_FOR_START_STATE);
            this.isAppointmentStarted = false;
        } else {
            changeColorAndSrcOfStartAndEndButton(DRAWABLE_RESOURCE_FOR_FINISH_STATE, COLOR_ID_FOR_FINISH_STATE);

            this.isAppointmentStarted = true;

        }
    }

    @Override
    public void onCallCustomer(int numOfCalls) {
        this.numOfCalls = numOfCalls;
    }

    @Override
    public void onNoDataReturned() {
        redirectToNoInternetConnection();
    }

    @Override
    public void notifyByListSize(final int listSize) {

        if (mTabLayout.getTabAt(mViewPager.getCurrentItem()).getCustomView() == null)
            mTabLayout.getTabAt(mViewPager.getCurrentItem()).setCustomView(updateTabTextView(mViewPager.getCurrentItem(), listSize));
        else
            ((TextView) mTabLayout.getTabAt(mViewPager.getCurrentItem()).getCustomView().findViewById(R.id.new_notifications_for_list_size)).setText("" + listSize);


    }

    @Override
    public void allowProgressBarToBeVisible() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void allowProgressBarToBeGone() {
        mProgressBar.setVisibility(View.GONE);
    }

    private void enableLayoutForNoLocations() {
        mViewPager.setVisibility(View.GONE);
        noLocationAvailableConstraintLayout.setVisibility(View.VISIBLE);

    }

    private void disableNoLocationLayout() {
        mViewPager.setVisibility(View.VISIBLE);
        enableButtonsAndLayoutToBeVisible();
        noLocationAvailableConstraintLayout.setVisibility(View.GONE);

    }

    @SuppressLint("RestrictedApi")
    private void hideButtonAndTabLayout() {
        callFab.setVisibility(View.GONE);
        arrivalFab.setVisibility(View.GONE);
        startOrEndFab.setVisibility(View.GONE);
        mTabLayout.setVisibility(View.GONE);
    }

    @SuppressLint("RestrictedApi")

    private void enableButtonsAndLayoutToBeVisible() {
        callFab.setVisibility(View.VISIBLE);
        arrivalFab.setVisibility(View.VISIBLE);
        startOrEndFab.setVisibility(View.VISIBLE);
        mTabLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissFloatingButtons() {
        callFab.setVisibility(View.GONE);
        arrivalFab.setVisibility(View.GONE);
        startOrEndFab.setVisibility(View.GONE);
    }

    @Override
    public void enableFloatingButtons() {
        callFab.setVisibility(View.VISIBLE);
        arrivalFab.setVisibility(View.VISIBLE);
        startOrEndFab.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy method");
        if (myCallListenerList != null)
            for (UpdateEventListener updateEventListener : myCallListenerList) {
                updateEventListener = null;
            }
        myCallListenerList = null;
        if (alertDialog != null)
            alertDialog.dismiss();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause method");
    }

    private void signOutFromGoogle() {

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mainViewModel.clearDataFromSharedPreference();
                    }
                });
        mGoogleSignInClient = null;

    }

    @Override
    public void animateStartOrFinishButton(String state) {

        animateStartOrEndBtn(state);

    }

    public void changeColorAndSrcOfStartAndEndButton(int drawableId, int colorId) {
        startOrEndFab.setImageDrawable(getResources().getDrawable(drawableId));
        startOrEndFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, colorId)));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void showAlertWithMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                alertDialog.dismiss();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();

    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResourceForEnableButton() {
        if (loadDataIdlingResource == null) {
            loadDataIdlingResource = new SimpleIdlingResource();

        }
        return loadDataIdlingResource;
    }

    int numOfUnArrivedData;

    private void getNumOfUnArrivedCustomers() {

        mainViewModel.getNumOfUnArrivedData("OPTH1").observe(this, (Observer<Integer>) o ->
                {
                    numOfUnArrivedData = o.intValue();
                }
        );

    }

    @VisibleForTesting
    public int getNumberofUnArrivedCustomer() {
        return numOfUnArrivedData;
    }

    @VisibleForTesting
    public int getNumberOfCustomerAtFirstLocation() {
        return Integer.parseInt(currentLocationList.get(0).getCount());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("locationList", currentLocationList);
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putParcelableArrayList("listenerList", (ArrayList<? extends Parcelable>) myCallListenerList);
        super.onSaveInstanceState(outState, outPersistentState);
    }
}

