package com.thetatechno.fluid.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.thetatechno.fluid.model.pojo.Location;
import com.thetatechno.fluid.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewPagerAdapter extends FragmentStateAdapter {

    Map<Integer, Fragment> fragmentsListMap = new HashMap<>();
   List<Location> mLocationList= new ArrayList<>();
   private FragmentManager fragmentManager;
    public ViewPagerAdapter(@NonNull FragmentActivity fm, List<Location>mLocationList) {
        super(fm);
        this.mLocationList = mLocationList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = HomeFragment.newInstance(mLocationList.get(position).getFacilityId(),mLocationList.get(position).getSessionId());
        fragmentsListMap.put(position,fragment);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return mLocationList.size();
    }
    public Map<Integer,Fragment> getFragmentsList(){
        return fragmentsListMap;
    }



}
