package com.thetatechno.fluid.ui.locations;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.thetatechno.fluid.model.pojo.Location;
import com.thetatechno.fluid.ui.activities.BaseActivity;
import com.thetatechno.fluid.ui.activities.main.MainActivity;
import com.thetatechno.fluid.R;

import java.util.List;

public class LocationsActivity extends BaseActivity {
RecyclerView mLocationRecyclerView;
LocationAdapter mLocationAdapter;
List<Location> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);
        setTitle(getString(R.string.locations_activity));
        Intent intent = getIntent();
        locations = intent.getParcelableArrayListExtra(MainActivity.LOCATIONS);
        mLocationRecyclerView = findViewById(R.id.locations_recycler_view);
        mLocationAdapter = new LocationAdapter(this,locations);
        mLocationRecyclerView.setAdapter(mLocationAdapter);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(LocationsActivity.this, 2);
        mLocationRecyclerView.setLayoutManager(mGridLayoutManager);
    }
}
