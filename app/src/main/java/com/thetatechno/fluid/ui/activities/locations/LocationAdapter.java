package com.thetatechno.fluid.ui.activities.locations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thetatechno.fluid.R;
import com.thetatechno.fluid.model.pojo.CurrentLocation;
import com.thetatechno.fluid.model.pojo.Facility;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.MyViewHolder> {

    List<Facility> currentLocations;
    Context mContext;

    public LocationAdapter(Context mContext,List<Facility> currentLocations){
        this.mContext = mContext ;
        this.currentLocations = currentLocations;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.location_item, parent, false);
        return new LocationAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       String location = currentLocations.get(position).getDescription();
       holder.locationName.setText(location);
    }

    @Override
    public int getItemCount() {
        return currentLocations.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
       TextView locationName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            locationName = itemView.findViewById(R.id.location_txt_view);

        }
    }

}
