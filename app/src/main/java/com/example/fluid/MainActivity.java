package com.example.fluid;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;

import com.example.fluid.listeners.MyTabClickListener;
import com.example.fluid.listeners.UpdateEventListener;
import com.example.fluid.ui.home.HomeFragment;
import com.example.fluid.ui.locations.LocationsActivity;
import com.example.fluid.ui.slideshow.SlideshowFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private HomeFragment myListAppointment = new HomeFragment();
    private MyTabClickListener myTabClickListener;
    UpdateEventListener updateEventListener;
    private ArrayList<String> locationList;
    boolean startAppointement =true;
    FloatingActionButton startOrEndFab, callFab, arrivalFab;
    public static final String LOCATIONS = "locations";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        startOrEndFab = findViewById(R.id.start_fab);
        callFab = findViewById(R.id.call_fab);
        arrivalFab = findViewById(R.id.confirm_arrive_fab);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        locationList = new ArrayList<>();
        locationList.add("ENT10");
        locationList.add("ORTH12");
        locationList.add("PED11");

        final TabLayout tabLayout = findViewById(R.id.tab_layout);
        for (int i = 0; i < locationList.size(); i++) {
            if (i == 0)
                tabLayout.addTab(tabLayout.newTab().setText(locationList.get(i)), true);
            else
                tabLayout.addTab(tabLayout.newTab().setText(locationList.get(i)));

        }
//        updateEventListener.changeNumberOfList(new MyTabHandler() {
//            @Override
//            public void onResponseReady(int listNumber) {
//
//            }
//        });



        callFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEventListener.callPatient(new MyTabHandler() {
                    @Override
                    public void onResponseReady(int listNumber) {
                        tabLayout.getTabAt(tabLayout.getSelectedTabPosition())
                                .setText(locationList.get(tabLayout.getSelectedTabPosition()) + "(" + listNumber);
                    }
                });
            }
        });
        arrivalFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 updateEventListener.confirmArrived(new MyTabHandler() {
                     @Override
                     public void onResponseReady(int listNumber) {

                     }
                 });

            }
        });

        startOrEndFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startAppointement) {
                    callFab.setEnabled(false);
                    callFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this,R.color.darkGrey)));
                    startOrEndFab.setImageResource(R.drawable.ic_start);
                }
                else
                {
                    callFab.setEnabled(true);
                    callFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this,R.color.colorAccent)));
                }

            }

        });


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
//                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frame_layout, myListAppointment).commit();
        myTabClickListener = myListAppointment;
        updateEventListener = myListAppointment;
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                getTabSelectedItem(tab, tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        System.out.println("tab that i'm in now " + tabLayout.getSelectedTabPosition());

    }

    private void getTabSelectedItem(final TabLayout.Tab tab, final int tabPosition) {
        myTabClickListener.onTabedClickedAction(locationList.get(tabPosition), new MyTabHandler() {
            @Override
            public void onResponseReady(int listNumber) {
                tab.setText(locationList.get(tabPosition) + "(" + listNumber + ")");
            }
        });
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
            case R.id.english_reference:
                setLocale("en");
                break;
            case R.id.arabic_reference:
                setLocale("ar");
                break;
            case R.id.nav_location:
                Intent intent = new Intent(this, LocationsActivity.class);
                intent.putStringArrayListExtra(LOCATIONS, locationList);
                startActivity(intent);
                break;
            case R.id.nav_slideshow:
                SlideshowFragment slideshowFragment = new SlideshowFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame_layout, slideshowFragment).commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }
    public void setLocale(String language_code) {
        Resources res = getResources();
// Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(language_code.toLowerCase())); // API 17+ only.
        res.updateConfiguration(conf, dm);

        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        finish();

    }

}
