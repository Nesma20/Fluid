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
import com.example.fluid.ui.listeners.MyAlertActionListener;
import com.example.fluid.model.pojo.Appointement;

import java.util.List;

public class CustomAlertDialog extends Dialog{
    List<Appointement> items ;
    Context context;
    public AlertDialogAdapter alertDialogAdapter;
    RecyclerView mRecyclerView;
    MyAlertActionListener myAlertActionListener;
    String state;
    public CustomAlertDialog(@NonNull Context context, List<Appointement> items, MyAlertActionListener myAlertActionListener, String state)  {
        super(context);
        this.context = context;
        this.items = items;
        this.myAlertActionListener =myAlertActionListener;
        this.state = state;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog_layout);
        Window window = getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mRecyclerView = findViewById(R.id.alert_recyclerview);
        alertDialogAdapter = new AlertDialogAdapter(context,items,myAlertActionListener,state);
        mRecyclerView.setAdapter(alertDialogAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));


    }
}
