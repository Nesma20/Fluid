package com.example.fluid.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.fluid.model.pojo.Location;
import com.example.fluid.ui.home.HomeFragment;
import com.example.fluid.utils.App;
import com.example.fluid.utils.Constants;
import com.example.fluid.utils.PreferenceController;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> mFragmentList = new ArrayList<>();
    List<String> mFragmentTitleList = new ArrayList<>();
   List<Location> locations= new ArrayList<>();
   private FragmentManager fragmentManager;
    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        fragmentManager = fm;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

//    public HomeFragment addFragment(Fragment fragment, String title) {
//
//        mFragmentList.add(fragment);
//        mFragmentTitleList.add(title);
//        return (HomeFragment) fragment;
//    }
    public void addFragment(Fragment fragment, String title) {

        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);

    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
