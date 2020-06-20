package com.thetatechno.fluid.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.thetatechno.fluid.R;
import com.thetatechno.fluid.model.pojo.Appointement;
import com.thetatechno.fluid.model.pojo.AppointmentItems;
import com.thetatechno.fluid.ui.EspressoTestingIdlingResource;
import com.thetatechno.fluid.ui.dialogs.ArriveOrCheckinListDialog;
import com.thetatechno.fluid.ui.listeners.AlertActionListener;
import com.thetatechno.fluid.ui.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluid.ui.listeners.UpdateEventListener;
import com.thetatechno.fluid.utils.CheckForNetwork;
import com.thetatechno.fluid.utils.Constants;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ParcelCreator")
public class HomeProviderFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private AppointmentListAdapter appointmentListAdapter;
    private View view;
    private Activity mainActivity;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ConstraintLayout noAppointmentsHereLayout;
    private ArriveOrCheckinListDialog alertDialog;

    private List<Appointement> appointmentList = new ArrayList<>();
    Appointement itemStarted = new Appointement();
    private boolean isAppointmentStarted = false;
    private RecyclerView appointmentListView;
    private String locationCode;
    private String sessionId;
    private int numOfCalls = 0;
    boolean isFragmentVisible = false;

    public static final String ARG_LOCATION_CODE = "LOCATION_CODE";
    public static final String ARG_SESSION_ID = "SESSION_ID";
    private static final String TAG = "AppointmentListFragment";

    public HomeProviderFragment() {

    }

    public static HomeProviderFragment newInstance(String clinicCode, String sessionId) {
        HomeProviderFragment fragment = new HomeProviderFragment();
        Log.i(TAG, "new Instance method");
        Bundle args = new Bundle();
        args.putString(ARG_LOCATION_CODE, clinicCode);
        args.putString(ARG_SESSION_ID, sessionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate method");
        if (getArguments() != null) {
            locationCode = getArguments().getString(ARG_LOCATION_CODE);
            sessionId = getArguments().getString(ARG_SESSION_ID);
        }
        if(savedInstanceState !=null){
            appointmentList = savedInstanceState.getParcelableArrayList("appointmentList");
        }


    }

    public void setArgumentsAfterCreation(Bundle bundle) {
        Log.i(TAG, "setArgumentsAfterCreation method");
        if (bundle != null) {
            if (!locationCode.equals(bundle.getString(ARG_LOCATION_CODE)))
                locationCode = bundle.getString(ARG_LOCATION_CODE);
            if (!sessionId.equals(bundle.getString(ARG_SESSION_ID)))
                sessionId = bundle.getString(ARG_SESSION_ID);
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onstart method " + locationCode);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume method " + locationCode);

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause method " + locationCode);


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(outState!=null){
            outState.putParcelableArrayList("appointmentList", (ArrayList<? extends Parcelable>) appointmentList);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy method");
        if (alertDialog != null)
            alertDialog.dismiss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView method");
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        appointmentListView = view.findViewById(R.id.appointmentRecyclerView);
        appointmentListAdapter = new AppointmentListAdapter(getActivity());
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        mSwipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        noAppointmentsHereLayout = view.findViewById(R.id.layout_no_appointments);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated method");
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (isFragmentVisible)

                if (checkForNetworkConnection()) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    homeViewModel.getAllProviderItems(locationCode).observe(getActivity(), appointmentItems -> {
                        setAdapterTorecyclerView(appointmentItems);
                    });
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);

                }

        });
    }

    private void setAdapterTorecyclerView(AppointmentItems appointmentItems) {
        Log.i(TAG, "setup adapter");

        appointmentListView.setAdapter(appointmentListAdapter);
        appointmentListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    @Override
    public void onDetach() {
        super.onDetach();
        locationCode = null;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        mainActivity = activity;
    }

    private boolean checkForNetworkConnection() {
        if (CheckForNetwork.isConnectionOn(mainActivity))
            return true;
        else
            return false;
    }


}