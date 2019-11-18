package com.example.fluid.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fluid.MyTabClickListener;
import com.example.fluid.R;
import com.example.fluid.model.pojo.Item;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements MyTabClickListener {

    private HomeViewModel homeViewModel;
    private List<Item> myList;
    private AppointmentListAdapter myAdapter;
    private View view;
    Item itemStarted;
    private RecyclerView myListView;
    private String clinicCode;
    private int numOfCalls = 0;
    private static String TAG = "AppointmentListFragment";


    final static int UPDATE_LIST = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        myListView = root.findViewById(R.id.appointmentRecyclerView);

        myList = new ArrayList<>();


        myAdapter = new AppointmentListAdapter(getActivity());

        homeViewModel.getAllItems("ENT10").observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                myList = items;
                numOfCalls = 0;
                for (int i = 0; i < myList.size(); i++) {
                    if (!myList.get(i).getCallingTime().isEmpty())
                        numOfCalls++;
                    if (!myList.get(i).getCheckinTime().isEmpty()) {
                        itemStarted = myList.get(i);
                    }
                }

                Log.i(TAG, "observe data");
                Log.i(TAG, "number of calls" + numOfCalls);
                myAdapter.setItems(myList);
                setAdapterTorecyclerView();

            }
        });
        return root;
    }


    private void setAdapterTorecyclerView() {

        myListView.setAdapter(myAdapter);
        myListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onTabedClickedAction(String clinicCode) {
        this.clinicCode = clinicCode;
        homeViewModel.getAllItems(clinicCode).observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                myList = items;
                numOfCalls = 0;
                for (int i = 0; i < myList.size(); i++) {
                    if (!myList.get(i).getCallingTime().isEmpty())
                        numOfCalls++;
                    if (!myList.get(i).getCheckinTime().isEmpty()) {
                        itemStarted = myList.get(i);
                    }
                }


            }
        });
    }
}