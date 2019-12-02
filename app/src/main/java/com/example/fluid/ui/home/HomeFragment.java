package com.example.fluid.ui.home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.fluid.R;
import com.example.fluid.ui.activities.MainActivity;
import com.example.fluid.ui.dialogs.CustomAlertDialog;
import com.example.fluid.ui.listeners.MyAlertActionListener;
import com.example.fluid.ui.listeners.MyTabHandler;
import com.example.fluid.ui.listeners.OnPageChangedListener;
import com.example.fluid.ui.listeners.UpdateEventListener;
import com.example.fluid.model.pojo.Appointement;
import com.example.fluid.utils.CheckForNetwork;
import com.example.fluid.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements  UpdateEventListener, MyAlertActionListener, OnPageChangedListener {
    private HomeViewModel homeViewModel;
    private List<Appointement> myList;
    private AppointmentListAdapter myAdapter;
    private View view;
    Appointement itemStarted;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private  boolean isAppointmentStarted = false;
    private RecyclerView myListView;
    private  String clinicCode;
    private int position;
    private static final String ARG_LOCATION_CODE="LOCATION_CODE";
    private static final String POSITION = "POSITION";
    private int numOfCalls = 0;
    private static String TAG = "AppointmentListFragment";
    private OnFragmentInteractionListener mListener;
    CustomAlertDialog alertDialog;


    public HomeFragment() {

    }


    public static HomeFragment newInstance(int position,String clinicCode) {
        HomeFragment fragment = new HomeFragment();
        Log.i(TAG, "new Instance method");
        Bundle args = new Bundle();
        args.putString(ARG_LOCATION_CODE, clinicCode);
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate method");
        if (getArguments() != null) {
            clinicCode = getArguments().getString(ARG_LOCATION_CODE);
            position = getArguments().getInt(POSITION);


        }

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onstart method");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy method");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        myListView = view.findViewById(R.id.appointmentRecyclerView);
        myList = new ArrayList<>();
        myAdapter = new AppointmentListAdapter(getActivity());
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        mSwipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
       if(myList.size() ==0)
           onPageChange(position);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onPageChange(position);
                mSwipeRefreshLayout.setRefreshing(false);

            }
        });
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setAdapterTorecyclerView() {
        Log.i(TAG, "setup adapter");

        myListView.setAdapter(myAdapter);
        myListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }


    private void checkInAction() {
        if (numOfCalls == 1) {
            homeViewModel.updateWithCheckIn(clinicCode, myList.get(--numOfCalls).getSlotId());
        } else if (numOfCalls > 1) {

            buildAlertWithList(Constants.STARTING_STATE);
        }
    }

    private void buildAlertWithList(String state) {

        List<Appointement> itemsList = new ArrayList<>();
        if (state.equals(Constants.STARTING_STATE)) {
            for (int i = 0; i < numOfCalls; i++) {

                if (myList.get(i).getCheckinTime().isEmpty()
                        && !myList.get(i).getCallingTime().isEmpty()) {
                    itemsList.add(myList.get(i));
                }
            }
            numOfCalls--;

        } else if (state.equals(Constants.ARRIVED_STATE))
            for (int i = 0; i < myList.size(); i++) {
                if (myList.get(i).getArrivalTime().isEmpty()) {
                    itemsList.add(myList.get(i));
                }
            }
        alertDialog = new CustomAlertDialog(getContext(), itemsList, this, state);
        alertDialog.show();
    }

    @Override
    public void updateData(Appointement appointement, String state) {
        if (state.equals(Constants.STARTING_STATE)) {
            homeViewModel.updateWithCheckIn(clinicCode, appointement.getSlotId());

        } else if (state.equals((Constants.ARRIVED_STATE))) {
            homeViewModel.confirmArrival(clinicCode, appointement.getSlotId());
        }
        alertDialog.dismiss();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        homeViewModel = null;
    }


    @Override
    public void checkInPatient() {
        checkInAction();
    }

    @Override
    public void confirmArrived() {
        buildAlertWithList(Constants.ARRIVED_STATE);
    }

    @Override
    public void callPatient() {
        homeViewModel.updateWithCalling(clinicCode);
    }

    @Override
    public void checkOutPatient() {
        homeViewModel.updateWithCheckOut(clinicCode, itemStarted.getSlotId());
    }

    @Override
    public void changeNumberOfList(MyTabHandler myTabHandler) {

    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
         activity1 = activity;
    }
    Activity activity1;

    @Override
    public void onPageChange(final int position) {
        if(getActivity() != null)
        if(CheckForNetwork.isConnectionOn(activity1)){
            if(position==this.position)
        homeViewModel.getAllItems(clinicCode).observe(this, new Observer<List<Appointement>>() {
            @Override
            public void onChanged(List<Appointement> items) {
                mSwipeRefreshLayout.setRefreshing(false);
                if(myList == null)
                    myList = new ArrayList<>();

                myList = items;
                numOfCalls = 0;
                isAppointmentStarted = false;
                for (int i = 0; i < myList.size(); i++) {
                    if (!myList.get(i).getCallingTime().isEmpty())
                        numOfCalls++;
                    if (!myList.get(i).getCheckinTime().isEmpty()) {
                        isAppointmentStarted = true;
                        itemStarted = myList.get(i);
                    }
                }
                mListener.onIconChanged(isAppointmentStarted);
                Log.i(TAG, "observe data");
                Log.i(TAG, "number of calls" + numOfCalls);
                myAdapter.setItems(myList);
                setAdapterTorecyclerView();
                mListener.notifyByListSize(items.size(),position);
            }
        });

        }
        else{
            mListener.onNoDataReturned();
        }

        }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onIconChanged(boolean isAppointmentStarted);
        void onNoDataReturned();
        void notifyByListSize(int listSize, int tabPosition);

    }
}