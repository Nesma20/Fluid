package com.thetatechno.fluid.ui.activities.locations;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.thetatechno.fluid.model.pojo.Facility;
import com.thetatechno.fluid.model.pojo.FacilityList;
import com.thetatechno.fluid.ui.activities.BaseActivity;
import com.thetatechno.fluid.ui.activities.NoInternetConnectionActivity;
import com.thetatechno.fluid.R;
import com.thetatechno.fluid.utils.CheckForNetwork;

import java.util.List;

public class LocationsActivity extends BaseActivity {
RecyclerView mLocationRecyclerView;
LocationAdapter mLocationAdapter;
List<Facility> facilities;
LocationsViewModel locationsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);
        setTitle(getString(R.string.locations_activity));
        locationsViewModel = ViewModelProviders.of(this).get(LocationsViewModel.class);
        mLocationRecyclerView = findViewById(R.id.locations_recycler_view);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(LocationsActivity.this, 2);
        mLocationRecyclerView.setLayoutManager(mGridLayoutManager);
        locationsViewModel.getAllFacilities("1").observe(this, new Observer<FacilityList>() {
            @Override
            public void onChanged(FacilityList facilityList) {
                if(facilityList!=null) {
                    if (facilityList.getFacilities() != null) {
                        facilities = facilityList.getFacilities();
                        mLocationAdapter = new LocationAdapter(LocationsActivity.this, facilities);
                        mLocationRecyclerView.setAdapter(mLocationAdapter);

                    }
                    else if(facilityList.getStatus() != null && facilityList.getStatus().equals(getResources().getString(R.string.error_no_data_found))) {
                        Toast.makeText(LocationsActivity.this,"there is no locations",Toast.LENGTH_SHORT).show();
                    }
                }
                else if(!CheckForNetwork.isConnectionOn(LocationsActivity.this)){
                    finish();
                    redirectToNoInternetAvailable();
                }
                else {
                    Toast.makeText(LocationsActivity.this,"Connection Error",Toast.LENGTH_SHORT).show();

                }


            }

        });

    }

    private void redirectToNoInternetAvailable() {
        Intent intent = new Intent(this, NoInternetConnectionActivity.class);
        startActivity(intent);
    }
}
