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
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.fluid.adapters.ViewPagerAdapter;
import com.example.fluid.listeners.MyTabHandler;
import com.example.fluid.R;
import com.example.fluid.listeners.MyTabClickListener;
import com.example.fluid.listeners.UpdateEventListener;
import com.example.fluid.ui.home.HomeFragment;
import com.example.fluid.ui.locations.LocationsActivity;
import com.example.fluid.ui.slideshow.SlideshowFragment;
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
        setupViewPager(mViewPager);


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
                    callFab.setEnabled(false);
                    callFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.darkGrey)));
                } else {
                    startOrEndFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_start));
                    myCallListenerList.get(mViewPager.getCurrentItem()).checkOutPatient();
                    callFab.setEnabled(true);
                    callFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this, R.color.colorAccent)));
                }

            }

        });
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
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        UpdateEventListener listener;
        for (int i = 0; i < locationList.size(); i++) {
//                mTabLayout.addTab(mTabLayout.newTab().setText(locationList.get(i)), true);
            listener = mViewPagerAdapter.addFragment(HomeFragment.newInstance(locationList.get(i)), locationList.get(i));
            myCallListenerList.add(listener);
        }
        myViewPager.setAdapter(mViewPagerAdapter);
        myViewPager.setOffscreenPageLimit(0);
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
//            case R.id.arabic_reference:
//                changeLanguage(Constants.ARABIC);
//                break;
            case R.id.nav_location:
                Intent intent = new Intent(this, LocationsActivity.class);
                intent.putStringArrayListExtra(LOCATIONS, locationList);
                startActivity(intent);
                break;
//            case R.id.nav_slideshow:
//                SlideshowFragment slideshowFragment = new SlideshowFragment();
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.frame_layout, slideshowFragment).commit();

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
    public void onIconChanged() {
        this.isAppointmentStarted = false;
        startOrEndFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_start));
        callFab.setEnabled(false);
        callFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.darkGrey)));
    }
}
