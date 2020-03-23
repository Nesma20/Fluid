package com.thetatechno.fluid.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.thetatechno.fluid.model.pojo.CurrentLocation;
import com.thetatechno.fluid.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewPagerAdapter extends FragmentStateAdapter {

    Map<Integer, Fragment> fragmentsListMap = new HashMap<>();
   List<CurrentLocation> mCurrentLocationList = new ArrayList<>();
   private FragmentManager fragmentManager;
    public ViewPagerAdapter(@NonNull FragmentActivity fm, List<CurrentLocation> mCurrentLocationList) {
        super(fm);
        this.mCurrentLocationList = mCurrentLocationList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = HomeFragment.newInstance(mCurrentLocationList.get(position).getFacilityId(), mCurrentLocationList.get(position).getSessionId());
        fragmentsListMap.put(position,fragment);

        return fragment;
    }

    @Override
    public int getItemCount() {
        return mCurrentLocationList.size();
    }
    public Map<Integer,Fragment> getFragmentsList(){
        return fragmentsListMap;
    }


}
