package com.example.fluid.ui.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.fluid.ui.adapters.ViewPagerAdapter;
import com.example.fluid.R;
import com.example.fluid.ui.listeners.OnPageChangedListener;
import com.example.fluid.ui.listeners.UpdateEventListener;
import com.example.fluid.ui.home.HomeFragment;
import com.example.fluid.ui.locations.LocationsActivity;
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
    private List<OnPageChangedListener> onPageChangedListeners;
    private int state = 0;

    public static final String LOCATIONS = "locations";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

         navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        locationList = new ArrayList<>();
        locationList.add("ENT10");
        locationList.add("ORTH12");
       locationList.add("PED11");
        initializeViews();
        checkOnTheCurrentLanguage();
        callFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MainActivity",mViewPager.getCurrentItem()+" current fragment");
                myCallListenerList.get(mViewPager.getCurrentItem()).callPatient();
            }
        });
        arrivalFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCallListenerList.get(mViewPager.getCurrentItem()).confirmArrived();
            }
        });
        startOrEndFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAppointmentStarted) {
                    myCallListenerList.get(mViewPager.getCurrentItem()).checkInPatient();
//                    callFab.setEnabled(false);
//                    callFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.darkGrey)));
                } else {
                   startOrEndFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_start));
                    myCallListenerList.get(mViewPager.getCurrentItem()).checkOutPatient();
                    callFab.setEnabled(true);
                   callFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.colorAccent)));
                }

            }

        });
        setupViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //region Don't Open
                if (state==0){
                    onPageChangedListeners.get(0).onPageChange();
                    state ++;
                    //RRRRRRR
                }
                //endregion

            }

            @Override
            public void onPageSelected(int position) {
                onPageChangedListeners.get(position).onPageChange();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void checkOnTheCurrentLanguage(){
        if (PreferenceController.getInstance(this).get(PreferenceController.LANGUAGE).equals(Constants.ARABIC)) {
         navigationView.getMenu().findItem(R.id.language_reference).setTitle(R.string.menu_english_language);
        }
        else if(PreferenceController.getInstance(this).get(PreferenceController.LANGUAGE).equals(Constants.ENGLISH))
        {
            navigationView.getMenu().findItem(R.id.language_reference).setTitle(R.string.menu_arabic_language);
        }
    }

    public void initializeViews() {

        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
        startOrEndFab = findViewById(R.id.start_fab);
        callFab = findViewById(R.id.call_fab);
        arrivalFab = findViewById(R.id.confirm_arrive_fab);

    }

    private void setupViewPager(ViewPager myViewPager) {
        myCallListenerList = new ArrayList<>();
        onPageChangedListeners = new ArrayList<>();
        HomeFragment homeFragment;
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        UpdateEventListener listener;
        OnPageChangedListener onPageChangedListener;
        for (int i = 0; i < locationList.size(); i++) {
//                mTabLayout.addTab(mTabLayout.newTab().setText(locationList.get(i)), true);
            homeFragment = mViewPagerAdapter.addFragment(HomeFragment.newInstance(locationList.get(i)), locationList.get(i));
            myViewPager.setOffscreenPageLimit(0);
            listener = homeFragment;
            onPageChangedListener = homeFragment;
            myCallListenerList.add(listener);
            onPageChangedListeners.add(onPageChangedListener);
        }
        myViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setupWithViewPager(myViewPager);
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
            }
            recreate();
        }
    }

    @Override
    public void onIconChanged(boolean isAppointmentStarted) {
        this.isAppointmentStarted = isAppointmentStarted;
        if(this.isAppointmentStarted){
        startOrEndFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_logout));
        callFab.setEnabled(false);
        callFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.darkGrey)));
        this.isAppointmentStarted = false;
        }
        else{
            callFab.setEnabled(true);
            callFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorAccent)));
            this.isAppointmentStarted = true;

        }
    }
}
