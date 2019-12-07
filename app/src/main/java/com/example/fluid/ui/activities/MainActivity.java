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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.fluid.R;
import com.example.fluid.ui.NoLocationAvailableFragment;
import com.example.fluid.ui.adapters.ViewPagerAdapter;
import com.example.fluid.ui.home.HomeFragment;
import com.example.fluid.ui.listeners.OnPageChangedListener;
import com.example.fluid.ui.listeners.UpdateEventListener;
import com.example.fluid.ui.locations.LocationsActivity;
import com.example.fluid.utils.CheckForNetwork;
import com.example.fluid.utils.Constants;
import com.example.fluid.utils.PreferenceController;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener {

    private ArrayList<String> locationList;
    boolean isAppointmentStarted = true;
    private FloatingActionButton startOrEndFab, callFab, arrivalFab;
    private Toolbar toolbar;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private TabLayout mTabLayout;
    DrawerLayout drawer;
    NavigationView navigationView;
    private List<UpdateEventListener> myCallListenerList;

    TextView tabTitle;
    TextView tabCount;
    View customTabView;
    AnimationDrawable startOrEndAnimation;
    public static final String LOCATIONS = "locations";
    public static final String TAG = "MainActivity";
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        locationList = new ArrayList<>();
        locationList.add("ENT10");
        locationList.add("ORTH12");
        locationList.add("PED11");
        initializeViews();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        checkOnTheCurrentLanguage();


        if (!CheckForNetwork.isConnectionOn(this)) {
            redirectToNoInternetConnection();
        } else {
            setupViewPager(mViewPager);
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
    }

    private void setupViewPager(ViewPager myViewPager) {
        if (locationList.size() != 0) {
            myCallListenerList = new ArrayList<>();

            HomeFragment homeFragment;
            mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            UpdateEventListener listener;

            for (int i = 0; i < locationList.size(); i++) {
                homeFragment = mViewPagerAdapter.addFragment(HomeFragment.newInstance(locationList.get(i)), locationList.get(i));
                myViewPager.setOffscreenPageLimit(0);
                listener = homeFragment;
                myCallListenerList.add(listener);


            }
            myViewPager.setAdapter(mViewPagerAdapter);
            mTabLayout.setupWithViewPager(myViewPager);
        } else {
            redirectTONoLocationAvailableFragment();
        }
    }

    private View updateTabTextView(int pos, final int listSize) {
        customTabView = getLayoutInflater().inflate(R.layout.location_tab, null);
        tabTitle = customTabView.findViewById(R.id.location_tab_txt_view);
        tabCount = customTabView.findViewById(R.id.new_notifications_for_list_size);
        tabTitle.setText(locationList.get(pos));
        tabCount.setVisibility(View.VISIBLE);
        if (listSize != 0) {
            tabCount.setText(listSize + "");
        } else {
            tabCount.setText("");
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
                intent.putStringArrayListExtra(LOCATIONS, locationList);
                startActivity(intent);
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
            } else {
                PreferenceController.getInstance(this).persist(PreferenceController.LANGUAGE, Constants.ARABIC);
                mTabLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
            finish();
            startActivity(new Intent(this,MainActivity.class));
           // recreate();

        }

    }

    private boolean isUserLoggedIn() {
        if (PreferenceController.getInstance(this).get(PreferenceController.PREF_EMAIL).isEmpty())

            return false;
        else {
            return true;
        }

    }

    public void redirectToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void redirectToNoInternetConnection() {
        Intent intent = new Intent(MainActivity.this, NoInternetConnectionActivity.class);
        startActivity(intent);
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
//            callFab.setEnabled(true);
//            callFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary)));
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
            ((TextView) mTabLayout.getTabAt(mViewPager.getCurrentItem()).getCustomView().findViewById(R.id.new_notifications_for_list_size)).setText(" " + listSize);

    }

    @SuppressLint("RestrictedApi")
    private void redirectTONoLocationAvailableFragment() {
        callFab.setVisibility(View.GONE);
        arrivalFab.setVisibility(View.GONE);
        startOrEndFab.setVisibility(View.GONE);
        mTabLayout.setVisibility(View.GONE);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.frame_layout, NoLocationAvailableFragment.newInstance()).commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (UpdateEventListener updateEventListener : myCallListenerList) {
            updateEventListener = null;
        }
        myCallListenerList = null;


    }
}

