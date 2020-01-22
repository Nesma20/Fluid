package com.example.fluid.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.fluid.R;
import com.example.fluid.model.pojo.AppointmentItems;
import com.example.fluid.ui.dialogs.CustomAlertDialog;
import com.example.fluid.ui.listeners.MyAlertActionListener;
import com.example.fluid.ui.listeners.OnDataChangedCallBackListener;
import com.example.fluid.ui.listeners.UpdateEventListener;
import com.example.fluid.model.pojo.Appointement;
import com.example.fluid.utils.CheckForNetwork;
import com.example.fluid.utils.Constants;
import com.example.fluid.utils.PreferenceController;
import com.example.fluid.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements UpdateEventListener, MyAlertActionListener {
    private HomeViewModel homeViewModel;
    private List<Appointement> myList = new ArrayList<>();
    private AppointmentListAdapter myAdapter;
    private View view;
    Appointement itemStarted;
    Activity activity1;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isAppointmentStarted = false;
    private RecyclerView myListView;
    private String clinicCode;
    private String sessionId;
    private static final String ARG_LOCATION_CODE = "LOCATION_CODE";
    private static final String ARG_SESSION_ID = "SESSION_ID";
    private int numOfCalls = 0;
    private static String TAG = "AppointmentListFragment";
    private OnFragmentInteractionListener mListener;
    CustomAlertDialog alertDialog;
    boolean isFragmentVisible = false;
ConstraintLayout noAppointmentsHereLayout;

    public HomeFragment() {

    }

    public static HomeFragment newInstance(String clinicCode, String sessionId) {
        HomeFragment fragment = new HomeFragment();
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
            clinicCode = getArguments().getString(ARG_LOCATION_CODE);
            sessionId = getArguments().getString(ARG_SESSION_ID);
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
        myAdapter = new AppointmentListAdapter(getActivity());
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        mSwipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        noAppointmentsHereLayout = view.findViewById(R.id.layout_no_appointments);
        if (myList.size() == 0 && isFragmentVisible) {

            onDataChanged();

        }
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isFragmentVisible)

                if(checkForNetworkConnection()) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    homeViewModel.getAllItems(clinicCode);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    mListener.onNoDataReturned();
                }

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

            for (int counterToFindTheCalledOne = 0; counterToFindTheCalledOne < myList.size(); counterToFindTheCalledOne++) {
                if (!myList.get(counterToFindTheCalledOne).getCallingTime().isEmpty()) {
                    updateData(myList.get(counterToFindTheCalledOne), Constants.STARTING_STATE);
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
            for (int i = 0; i < myList.size(); i++) {

                if (myList.get(i).getCheckinTime().isEmpty()
                        && !myList.get(i).getCallingTime().isEmpty()) {
                    itemsList.add(myList.get(i));
                }
            }

        } else if (state.equals(Constants.ARRIVED_STATE))
            for (int i = 0; i < myList.size(); i++) {
                if (myList.get(i).getArrivalTime().isEmpty()) {
                    itemsList.add(myList.get(i));
                }
            }
        if (itemsList.size() > 0) {
            alertDialog = new CustomAlertDialog(getContext(), itemsList, this, state);
            alertDialog.getWindow().setLayout(getView().getWidth(), getView().getHeight() / 2);
            alertDialog.show();

        } else {
            // TODO : alert for that all customers already arrived.
            mListener.showAlertWithMessage(getContext().getResources().getString(R.string.error_all_customers_arrived));
        }

    }

    @Override
    public void updateData(Appointement appointement, final String state) {
        if (state.equals(Constants.STARTING_STATE)) {

            homeViewModel.updateWithCheckIn(appointement.getSlotId(), new OnDataChangedCallBackListener<Boolean>() {
                @Override
                public void onResponse(Boolean dataChanged) {
                    if (dataChanged.booleanValue()) {
                        homeViewModel.getAllItems(clinicCode);
                        mListener.animateStartOrFinishButton(state);
                        numOfCalls--;
                    } else {
                        mListener.showAlertWithMessage(getContext().getResources().getString(R.string.error_connection_whle_retrieve_data));
                    }

                }
            });

        } else if (state.equals((Constants.ARRIVED_STATE))) {
            homeViewModel.confirmArrival(appointement.getSlotId(), new OnDataChangedCallBackListener<Boolean>() {
                @Override
                public void onResponse(Boolean dataChanged) {
                    if (dataChanged.booleanValue()) {
                        //refresh data
                        homeViewModel.getAllItems(clinicCode);
                    } else {
                        mListener.showAlertWithMessage(getContext().getResources().getString(R.string.error_connection_whle_retrieve_data));
                    }
                }
            });
        }
        if (alertDialog != null)
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
        clinicCode = null;
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
                if (sloteId.intValue()>0) {
                    {
                        for(Appointement appointement : myList)
                        {
                            if(appointement.getSlotId().contains(sloteId.intValue()+"")) {
                                Log.i(TAG, appointement.getEnglishName() + " called");
                                homeViewModel.getAllItems(clinicCode);
                            }

                        }

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
                    homeViewModel.getAllItems(clinicCode);
                } else {
                    mListener.showAlertWithMessage(getContext().getResources().getString(R.string.error_connection_whle_retrieve_data));
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        activity1 = activity;
    }

    private boolean checkForNetworkConnection() {
        if (CheckForNetwork.isConnectionOn(activity1))
            return true;
        else
            return false;
    }

    public void onDataChanged() {
        mListener.allowProgressBarToBeVisible();

        if (getActivity() != null)

            if (CheckForNetwork.isConnectionOn(activity1)) {
                mListener.allowProgressBarToBeGone();

                homeViewModel.getAllItems(clinicCode).observe(this, new Observer<AppointmentItems>() {
                    @Override
                    public void onChanged(AppointmentItems items) {
                        if(items.getItems()!=null) {
                            noAppointmentsHereLayout.setVisibility(View.GONE);
                            myList = items.getItems();
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
                            mListener.enableFloatingButtons();
                            mListener.onCallCustomer(numOfCalls);
                            mListener.onIconChanged(isAppointmentStarted);
                            myAdapter.setItems(myList);
                            setAdapterTorecyclerView();
                            mListener.notifyByListSize(items.getItems().size());
                        }
                        else
                        {
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
        if (getContext() != null && menuVisible) {
            mListener.allowProgressBarToBeVisible();
            onDataChanged();
            mListener.allowProgressBarToBeGone();
        }
    }

}