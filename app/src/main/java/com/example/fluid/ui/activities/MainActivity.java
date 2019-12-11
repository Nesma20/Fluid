package com.example.fluid.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.fluid.R;
import com.example.fluid.model.pojo.Location;
import com.example.fluid.model.pojo.LocationList;
import com.example.fluid.ui.NoLocationAvailableFragment;
import com.example.fluid.ui.adapters.ViewPagerAdapter;
import com.example.fluid.ui.home.HomeFragment;
import com.example.fluid.ui.listeners.OnDataChangedCallBackListener;
import com.example.fluid.ui.listeners.UpdateEventListener;
import com.example.fluid.ui.locations.LocationsActivity;
import com.example.fluid.utils.CheckForNetwork;
import com.example.fluid.utils.Constants;
import com.example.fluid.utils.PreferenceController;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener {

    private ArrayList<Location> locationList = new ArrayList<>();
    boolean isAppointmentStarted = true;
    private FloatingActionButton startOrEndFab, callFab, arrivalFab;
    private Toolbar toolbar;
    private ViewPager mViewPager;
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
    AnimationDrawable startOrEndAnimation;
    public static final String LOCATIONS = "locations";
    public static final String TAG = "MainActivity";
    MainViewModel mainViewModel = new MainViewModel();
    NoLocationAvailableFragment noLocationAvailableFragment;
    // user data UI
    ImageView mImageView;
    TextView mEmailTxtView;
    TextView mUserNameTxtView;
    FragmentManager manager;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("574842241815-r9t9g16s08jflvunfu9rjdd99uscvfir.apps.googleusercontent.com")
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        if (!isUserLoggedIn()) {
            redirectToLogin();
        }


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        initializeViews();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        checkOnTheCurrentLanguage();
        View header = navigationView.getHeaderView(0);
        mImageView = header.findViewById(R.id.userImageView);
        mUserNameTxtView = header.findViewById(R.id.userNameTxt);
        mEmailTxtView = header.findViewById(R.id.emailTxtView);
        mUserNameTxtView.setText(mainViewModel.getDataFromSharedPreference(PreferenceController.PREF_USER_NAME));
        mEmailTxtView.setText(mainViewModel.getDataFromSharedPreference(PreferenceController.PREF_EMAIL));
        noLocationAvailableFragment = NoLocationAvailableFragment.newInstance();

        Glide.with(this)
                .load(Constants.BASE_GOOGLE_URL_FOR_IMAGES + mainViewModel.getDataFromSharedPreference(PreferenceController.PREF_IMAGE_PROFILE_URL))
                .circleCrop()
                .into(mImageView);

        if (!CheckForNetwork.isConnectionOn(this)) {
            redirectToNoInternetConnection();
        } else {
            mainViewModel.getLocationData(mainViewModel.getDataFromSharedPreference(PreferenceController.PREF_EMAIL), new OnDataChangedCallBackListener<LocationList>() {
                @Override
                public void onResponse(LocationList dataChanged) {
                    if(dataChanged.getItems() != null) {
                        locationList = (ArrayList<Location>) dataChanged.getItems();
                        setupViewPager(mViewPager);
                    }
                    else {
                        locationList = null;
                        redirectTONoLocationAvailableFragment();

                    }

                }
            });


        }

        callFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MainActivity", mViewPager.getCurrentItem() + " current fragment");

                if (CheckForNetwork.isConnectionOn(MainActivity.this)) {
                    myCallListenerList.get(mViewPager.getCurrentItem()).callPatient();
                } else {
                    redirectToNoInternetConnection();
                }
            }
        });
        arrivalFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckForNetwork.isConnectionOn(MainActivity.this)) {

                    myCallListenerList.get(mViewPager.getCurrentItem()).confirmArrived();
                } else {
                    redirectToNoInternetConnection();
                }

            }
        });
        startOrEndFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckForNetwork.isConnectionOn(MainActivity.this)) {

                    if (isAppointmentStarted) {
                        myCallListenerList.get(mViewPager.getCurrentItem()).checkInPatient();

                    } else {

                        startOrEndFab.setImageDrawable(getResources().getDrawable(R.drawable.animation_fab_start));
                        startOrEndFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary)));

                        myCallListenerList.get(mViewPager.getCurrentItem()).checkOutPatient();
                        callFab.setEnabled(true);
                        callFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary)));
                        animateStartOrEndBtn();
                        startOrEndAnimation.stop();
                    }

                } else {
                    redirectToNoInternetConnection();
                }
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void checkOnTheCurrentLanguage() {
        if (PreferenceController.getInstance(this).get(PreferenceController.LANGUAGE).equals(Constants.ARABIC)) {
            navigationView.getMenu().findItem(R.id.language_reference).setTitle(R.string.menu_english_language);
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else if (PreferenceController.getInstance(this).get(PreferenceController.LANGUAGE).equals(Constants.ENGLISH)) {
            navigationView.getMenu().findItem(R.id.language_reference).setTitle(R.string.menu_arabic_language);
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
    }

    private void animateStartOrEndBtn() {
        startOrEndFab.startAnimation(AnimationUtils.loadAnimation(this, R.anim.animation_fab_with_rotation));
        startOrEndAnimation = (AnimationDrawable) startOrEndFab.getDrawable();
        startOrEndAnimation.start();
    }

    public void initializeViews() {
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
        startOrEndFab = findViewById(R.id.start_fab);
        callFab = findViewById(R.id.call_fab);
        arrivalFab = findViewById(R.id.confirm_arrive_fab);
        mProgressBar = findViewById(R.id.loading_data_progress_bar);
    }

    private void setupViewPager(final ViewPager myViewPager) {

            enableButtonsAndLayoutToBeVisible();
            if(noLocationAvailableFragment != null) {
                transaction = manager.beginTransaction();
                transaction.remove(noLocationAvailableFragment);
                transaction.commit();
                manager.popBackStack();
            }
            myCallListenerList = new ArrayList<>();
            HomeFragment homeFragment;
            mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            UpdateEventListener listener;

            for (int i = 0; i < locationList.size(); i++) {
                homeFragment = mViewPagerAdapter.addFragment(HomeFragment.newInstance(locationList.get(i).getFacilityId()), locationList.get(i).getFacilityId());
                myViewPager.setOffscreenPageLimit(1);
                listener = homeFragment;
                myCallListenerList.add(listener);

            }
            myViewPager.setAdapter(mViewPagerAdapter);

            mTabLayout.setupWithViewPager(myViewPager);
            for(int i=0 ;i<locationList.size();i++){
                if (mTabLayout.getTabAt(i).getCustomView() == null)
                    mTabLayout.getTabAt(i).setCustomView(updateTabTextView(i, Integer.parseInt(locationList.get(i).getCount())));
            }

        }

    private View updateTabTextView(int pos, final int listSize) {
        customTabView = getLayoutInflater().inflate(R.layout.location_tab, null);
        tabTitle = customTabView.findViewById(R.id.location_tab_txt_view);
        tabCount = customTabView.findViewById(R.id.new_notifications_for_list_size);
        tabTitle.setText(locationList.get(pos).getFacilityId());
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
                intent.putParcelableArrayListExtra(LOCATIONS, locationList);
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

    private boolean isUserLoggedIn() {
        if (mGoogleSignInClient == null && PreferenceController.getInstance(this).get(PreferenceController.PREF_EMAIL).length() == 0)
            return false;
        else {
            return true;
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
        finish();

    }

    @Override
    public void onIconChanged(boolean isAppointmentStarted) {
        this.isAppointmentStarted = isAppointmentStarted;
        if (this.isAppointmentStarted) {
            startOrEndFab.setImageDrawable(getResources().getDrawable(R.drawable.animation_fab_finish));
            callFab.setEnabled(false);
            callFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.darkGrey)));
            startOrEndFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorAccent)));
            animateStartOrEndBtn();
            startOrEndAnimation.stop();
            this.isAppointmentStarted = false;
        } else {
            startOrEndFab.setImageDrawable(getResources().getDrawable(R.drawable.animation_fab_start));
            startOrEndFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary)));

            this.isAppointmentStarted = true;

        }
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

    private void redirectTONoLocationAvailableFragment() {
       hideButtonAndTabLayout();
       if(!noLocationAvailableFragment.isVisible())
        transaction.add(R.id.frame_layout, noLocationAvailableFragment).commit();

    }
    @SuppressLint("RestrictedApi")
    private void hideButtonAndTabLayout(){
        callFab.setVisibility(View.GONE);
        arrivalFab.setVisibility(View.GONE);
        startOrEndFab.setVisibility(View.GONE);
        mTabLayout.setVisibility(View.GONE);
    }
    @SuppressLint("RestrictedApi")

    private void enableButtonsAndLayoutToBeVisible(){
        callFab.setVisibility(View.VISIBLE);
        arrivalFab.setVisibility(View.VISIBLE);
        startOrEndFab.setVisibility(View.VISIBLE);
        mTabLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myCallListenerList != null)
            for (UpdateEventListener updateEventListener : myCallListenerList) {
                updateEventListener = null;
            }
        myCallListenerList = null;


    }

    private void signOutFromGoogle() {

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mainViewModel.clearDataFromSharedPreference();
                    }
                });
    }
}

