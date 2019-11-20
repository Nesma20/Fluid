package com.example.fluid.ui.locations;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.fluid.MainActivity;
import com.example.fluid.R;

import java.util.List;

public class LocationsActivity extends AppCompatActivity {
RecyclerView mLocationRecyclerView;
LocationAdapter mLocationAdapter;
List<String> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);
        Intent intent = getIntent();
        locations = intent.getStringArrayListExtra(MainActivity.LOCATIONS);
        mLocationRecyclerView = findViewById(R.id.locations_recycler_view);
        mLocationAdapter = new LocationAdapter(this,locations);
        mLocationRecyclerView.setAdapter(mLocationAdapter);
        mLocationRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


    }
}
