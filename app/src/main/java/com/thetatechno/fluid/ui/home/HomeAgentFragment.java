package com.thetatechno.fluid.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
import com.thetatechno.fluid.model.pojo.AppointmentItems;
import com.thetatechno.fluid.ui.EspressoTestingIdlingResource;
import com.thetatechno.fluid.ui.dialogs.ArriveOrCheckinListDialog;
import com.thetatechno.fluid.ui.listeners.AlertActionListener;
import com.thetatechno.fluid.ui.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluid.ui.listeners.OnUpdateDataEvent;
import com.thetatechno.fluid.ui.listeners.UpdateEventListener;
import com.thetatechno.fluid.model.pojo.Appointement;
import com.thetatechno.fluid.utils.CheckForNetwork;
import com.thetatechno.fluid.utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ParcelCreator")
public class HomeAgentFragment extends Fragment implements UpdateEventListener, AlertActionListener {
    private HomeViewModel homeViewModel;
    private AppointmentListAdapter appointmentListAdapter;
    private View view;
    private Activity mainActivity;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ConstraintLayout noAppointmentsHereLayout;
    private ArriveOrCheckinListDialog alertDialog;
    private OnFragmentInteractionListener mListener;
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

    public HomeAgentFragment() {

    }

    public static HomeAgentFragment newInstance(String clinicCode, String sessionId) {
        HomeAgentFragment fragment = new HomeAgentFragment();
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
        EventBus.getDefault().register(this);

        Log.i(TAG, "onCreate method");
        if (getArguments() != null) {
            locationCode = getArguments().getString(ARG_LOCATION_CODE);
            sessionId = getArguments().getString(ARG_SESSION_ID);
        }
        if(savedInstanceState !=null){
            appointmentList = savedInstanceState.getParcelableArrayList("appointmentList");
        }


    }

    public void resetSessionId(String sessionId) {
        Log.i(TAG, "setArgumentsAfterCreation method");
            if (sessionId!=null && !sessionId.equals(this.sessionId))
                this.sessionId =sessionId;

        if (appointmentList.size() == 0 && isFragmentVisible) {
            onDataChanged();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onstart method " + locationCode);


    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

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
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated method");
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (isFragmentVisible)

                if (checkForNetworkConnection()) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    homeViewModel.getAllItems(locationCode);
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    mListener.onNoDataReturned();
                }

        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public void setAdapterTorecyclerView() {
        Log.i(TAG, "setup adapter");

        appointmentListView.setAdapter(appointmentListAdapter);
        appointmentListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }


    private void checkInAction() {
        if (numOfCalls == 1) {
            for (int counterToFindTheCalledOne = 0; counterToFindTheCalledOne < appointmentList.size(); counterToFindTheCalledOne++) {
                if (!appointmentList.get(counterToFindTheCalledOne).getCallingTime().isEmpty()) {
                    updateData(appointmentList.get(counterToFindTheCalledOne), Constants.STARTING_STATE);
                    break;
                }

            }
        } else if (numOfCalls > 1) {

            buildAlertWithList(Constants.STARTING_STATE);
        }
    }

    private void buildAlertWithList(String state) {
        List<Appointement> itemsList = new ArrayList<>();
        if (state.equals(Constants.STARTING_STATE)) {
            for (int i = 0; i < appointmentList.size(); i++) {
                if (appointmentList.get(i).getCheckinTime().isEmpty()
                        && !appointmentList.get(i).getCallingTime().isEmpty()) {
                    itemsList.add(appointmentList.get(i));
                }
            }

        } else if (state.equals(Constants.ARRIVED_STATE)) {
            for (int i = 0; i < appointmentList.size(); i++) {
                if (appointmentList.get(i).getArrivalTime().isEmpty()) {
                    itemsList.add(appointmentList.get(i));
                }
            }
        }
        if (itemsList.size() > 0) {
            alertDialog = new ArriveOrCheckinListDialog(getContext(), itemsList, this, state);
            alertDialog.getWindow().setLayout(getView().getWidth(), getView().getHeight() / 2);
            alertDialog.show();
        } else {
            mListener.showAlertWithMessage(getContext().getResources().getString(R.string.error_all_customers_arrived));
        }

    }

    @Override
    public void updateData(final Appointement appointement, final String state) {
        if (state.equals(Constants.STARTING_STATE)) {
            EspressoTestingIdlingResource.increment();
            homeViewModel.updateWithCheckIn(appointement.getSlotId(), new OnDataChangedCallBackListener<Boolean>() {
                @Override
                public void onResponse(Boolean dataChanged) {
                    if (dataChanged.booleanValue()) {
                        homeViewModel.getAllItems(locationCode);
                        mListener.animateStartOrFinishButton(state);
                        numOfCalls--;
                        EspressoTestingIdlingResource.decrement();
                    } else {
                        mListener.showAlertWithMessage(getContext().getResources().getString(R.string.error_connection_whle_retrieve_data));
                    }

                }
            });

        } else if (state.equals((Constants.ARRIVED_STATE))) {
            EspressoTestingIdlingResource.increment();
            homeViewModel.confirmArrival(appointement.getSlotId(), new OnDataChangedCallBackListener<Boolean>() {
                @Override
                public void onResponse(Boolean dataChanged) {

                    if (dataChanged.booleanValue()) {
                        //refresh data
                        homeViewModel.getAllItems(locationCode);

                        Toast.makeText(getContext(), appointement.getEnglishName() + " arrived", Toast.LENGTH_SHORT).show();
                        EspressoTestingIdlingResource.decrement();


                    } else {
                        mListener.showAlertWithMessage(getContext().getResources().getString(R.string.error_connection_whle_retrieve_data));
                    }

                }
            });
        }

        if (alertDialog != null) {
            alertDialog.dismiss();

        }
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
        locationCode = null;
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
        homeViewModel.updateWithCalling(sessionId, new OnDataChangedCallBackListener<Integer>() {
            @Override
            public void onResponse(Integer sloteId) {
                if (sloteId.intValue() > 0) {
                    {
                        EspressoTestingIdlingResource.increment();
                        for (Appointement appointement : appointmentList) {
                            if (appointement.getSlotId().contains(sloteId.intValue() + "")) {
                                homeViewModel.getAllItems(locationCode);
                                Toast.makeText(getContext(), appointement.getEnglishName() + " called", Toast.LENGTH_SHORT).show();

                            }

                        }

                        EspressoTestingIdlingResource.decrement();
                    }


                } else {
                    mListener.showAlertWithMessage(getContext().getResources().getString(R.string.error_connection_whle_retrieve_data));
                }
            }
        });

    }

    @Override
    public void checkOutPatient() {
        homeViewModel.updateWithCheckOut(itemStarted.getSlotId(), new OnDataChangedCallBackListener<Boolean>() {
            @Override
            public void onResponse(Boolean dataChanged) {
                if (dataChanged.booleanValue()) {
                    // animate the button
                    mListener.animateStartOrFinishButton(Constants.ENDING_STATE);
                    // refresh data
                    homeViewModel.getAllItems(locationCode);
                } else {
                    mListener.showAlertWithMessage(getContext().getResources().getString(R.string.error_connection_whle_retrieve_data));
                }
            }
        });


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

    public void onDataChanged() {
        mListener.allowProgressBarToBeVisible();

        if (getActivity() != null)

            if (CheckForNetwork.isConnectionOn(mainActivity)) {
                mListener.allowProgressBarToBeGone();
                homeViewModel.getAllItems(locationCode).observe(this, new Observer<AppointmentItems>() {
                    @Override
                    public void onChanged(AppointmentItems items) {
                        if (items.getItems() != null) {
                            EspressoTestingIdlingResource.increment();
                            noAppointmentsHereLayout.setVisibility(View.GONE);
                            appointmentList = items.getItems();
                            numOfCalls = 0;
                            isAppointmentStarted = false;
                            for (int i = 0; i < appointmentList.size(); i++) {
                                if (!appointmentList.get(i).getCallingTime().isEmpty())
                                    numOfCalls++;
                                if (!appointmentList.get(i).getCheckinTime().isEmpty()) {
                                    isAppointmentStarted = true;
                                    itemStarted = appointmentList.get(i);
                                }
                            }
                            mListener.enableFloatingButtons();
                            mListener.onCallCustomer(numOfCalls);
                            mListener.onIconChanged(isAppointmentStarted);
                            appointmentListAdapter.setItems(appointmentList);
                            setAdapterTorecyclerView();
                            mListener.notifyByListSize(items.getItems().size());
                            EspressoTestingIdlingResource.decrement();
                        } else {
                            mSwipeRefreshLayout.setVisibility(View.GONE);
                            noAppointmentsHereLayout.setVisibility(View.VISIBLE);
                            mListener.dismissFloatingButtons();

                        }



                    }
                });

            } else {
                mListener.onNoDataReturned();
            }


    }




    public interface OnFragmentInteractionListener {
        void onIconChanged(boolean isAppointmentStarted);

        void onCallCustomer(int numOfCalls);

        void onNoDataReturned();

        void notifyByListSize(int listSize);

        void allowProgressBarToBeVisible();

        void allowProgressBarToBeGone();

        void animateStartOrFinishButton(String state);

        void showAlertWithMessage(String message);

        void dismissFloatingButtons();

        void enableFloatingButtons();

    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        isFragmentVisible = menuVisible;
        if (getContext() != null && menuVisible ) {
            EspressoTestingIdlingResource.increment();
            mListener.allowProgressBarToBeVisible();
            onDataChanged();
            mListener.allowProgressBarToBeGone();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateData(OnUpdateDataEvent listener) {
        if(listener.getFacilityCode().equals(locationCode)){
            switch (listener.getAction()){
                case Constants.ACTION_CALL:
                    callPatient();
                    break;
                case Constants.ACTION_ARRIVE :
                    confirmArrived();
                    break;
                case Constants.ACTION_CHECK_IN:
                    checkInPatient();
                    break;
                case Constants.ACTION_CHECK_OUT:
                    checkOutPatient();
                    break;
                case Constants.ACTION_UPDATE_SESSION_ID :
                    resetSessionId(listener.getSessionId());

            }
        }
    }

}