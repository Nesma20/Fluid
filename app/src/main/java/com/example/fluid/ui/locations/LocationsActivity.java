package com.example.fluid.ui.locations;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.fluid.model.pojo.Location;
import com.example.fluid.ui.activities.BaseActivity;
import com.example.fluid.ui.activities.MainActivity;
import com.example.fluid.R;

import java.util.List;

public class LocationsActivity extends BaseActivity {
RecyclerView mLocationRecyclerView;
LocationAdapter mLocationAdapter;
List<Location> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);
        Intent intent = getIntent();
        locations = intent.getParcelableArrayListExtra(MainActivity.LOCATIONS);
        mLocationRecyclerView = findViewById(R.id.locations_recycler_view);
        mLocationAdapter = new LocationAdapter(this,locations);
        mLocationRecyclerView.setAdapter(mLocationAdapter);
        mLocationRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
}
