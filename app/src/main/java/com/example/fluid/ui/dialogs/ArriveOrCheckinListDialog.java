package com.example.fluid.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.fluid.R;
import com.example.fluid.ui.listeners.AlertActionListener;
import com.example.fluid.model.pojo.Appointement;

import java.util.List;

public class ArriveOrCheckinListDialog extends Dialog{
    List<Appointement> appointmentList ;
    Context context;
    public AppointmentListDialogAdapter alertDialogAdapter;
    RecyclerView appointmentListRecyclerView;
    AlertActionListener alertActionListener;
    String state;
    public ArriveOrCheckinListDialog(@NonNull Context context, List<Appointement> appointmentList, AlertActionListener alertActionListener, String state)  {
        super(context);
        this.context = context;
        this.appointmentList = appointmentList;
        this.alertActionListener = alertActionListener;
        this.state = state;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog_layout);
        Window window = getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        appointmentListRecyclerView = findViewById(R.id.alert_recyclerview);
        alertDialogAdapter = new AppointmentListDialogAdapter(context,appointmentList, alertActionListener,state);
        appointmentListRecyclerView.setAdapter(alertDialogAdapter);
        appointmentListRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));


    }
}
