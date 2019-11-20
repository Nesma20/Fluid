package com.example.fluid.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fluid.MyTabHandler;
import com.example.fluid.R;
import com.example.fluid.dialogs.CustomAlertDialog;
import com.example.fluid.listeners.MyAlertActionListener;
import com.example.fluid.listeners.MyTabClickListener;
import com.example.fluid.listeners.UpdateEventListener;
import com.example.fluid.model.pojo.Item;
import com.example.fluid.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements MyTabClickListener, UpdateEventListener, MyAlertActionListener {
    private String clinicCode;
    private HomeViewModel homeViewModel;
    private List<Item> myList;
    private AppointmentListAdapter myAdapter;
    private View view;
    Item itemStarted;
    private RecyclerView myListView;
    private int numOfCalls = 0;
    private OnFragmentInteractionListener mListener;
    CustomAlertDialog alertDialog;
    private static String TAG = "AppointmentListFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
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
    public void onTabedClickedAction(String clinicCode, final MyTabHandler myTabHandler) {
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
                myTabHandler.onResponseReady(myList.size());


            }
        });
    }

    @Override
    public void checkInPatient(MyTabHandler myTabHandler) {
        checkInAction();
    }

    private void checkInAction() {
        if (numOfCalls == 1) {
            homeViewModel.updateWithCheckIn(clinicCode, myList.get(--numOfCalls).getSlotId());
        } else if (numOfCalls > 1) {

            buildAlertWithList(Constants.StartingState);
        }
    }

    @Override
    public void confirmArrived(MyTabHandler myTabHandler) {

        buildAlertWithList(Constants.ArrivedState);
        myTabHandler.onResponseReady(myList.size());
    }

    private void buildAlertWithList(String state) {


        List<Item> itemsList = new ArrayList<>();
        if (state.equals(Constants.StartingState)) {
            for (int i = 0; i < numOfCalls; i++) {

                if (myList.get(i).getCheckinTime().isEmpty() && !myList.get(i).getCallingTime().isEmpty()) {
                    itemsList.add(myList.get(i));
                }
            }
            numOfCalls--;

        } else if (state.equals(Constants.ArrivedState))
            for (int i = 0; i < myList.size(); i++) {
                if (myList.get(i).getArrivalTime().isEmpty()) {
                    itemsList.add(myList.get(i));
                }
            }
        alertDialog = new CustomAlertDialog(getContext(), itemsList, this, state);
        alertDialog.show();


    }

    @Override
    public void callPatient(MyTabHandler myTabHandler) {
        homeViewModel.updateWithCalling(clinicCode, myList.get(numOfCalls).getSlotId());
        myTabHandler.onResponseReady(myList.size());
    }

    @Override
    public void checkOutPatient(MyTabHandler myTabHandler) {
        homeViewModel.updateWithCheckOut(clinicCode, itemStarted.getSlotId());
        myTabHandler.onResponseReady(myList.size());

    }

    @Override
    public void changeNumberOfList(MyTabHandler myTabHandler) {
        myTabHandler.onResponseReady(myList.size());
    }

    @Override
    public void updateData(Item item, String state) {
        if (state.equals(Constants.StartingState)) {
            homeViewModel.updateWithCheckIn(clinicCode, item.getSlotId());

        } else if (state.equals((Constants.ArrivedState))) {
            homeViewModel.confirmArrival(clinicCode, item.getSlotId());
        }
        alertDialog.dismiss();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onIconChanged();

    }

}