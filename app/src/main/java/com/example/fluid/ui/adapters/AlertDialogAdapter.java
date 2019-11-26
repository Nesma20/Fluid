package com.example.fluid.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.fluid.R;
import com.example.fluid.ui.listeners.MyAlertActionListener;
import com.example.fluid.model.pojo.Appointement;

import java.util.List;
import java.util.Locale;

public class AlertDialogAdapter extends RecyclerView.Adapter<AlertDialogAdapter.myViewHolder> {
Context mContext;
List<Appointement> items;
String state;
MyAlertActionListener myAlertActionListener;
public AlertDialogAdapter(Context mContext, List<Appointement> items, MyAlertActionListener myAlertActionListener, String state){
    this.mContext = mContext;
    this.items = items;
    this.myAlertActionListener =myAlertActionListener;
    this.state = state;
}
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.alert_dialog_list_item, parent, false);
        return new myViewHolder(view)   ; }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        final Appointement appointement = items.get(position);
        String languagename = Locale.getDefault().getDisplayLanguage();
        if(languagename.contains("English")){
            holder.patientNameTxt.setText(appointement.getEnglishName());
        }
        else if(languagename.contains("العربية") || languagename.contains("Arabic")) {
            holder.patientNameTxt.setText(appointement.getArabicName());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAlertActionListener.updateData(appointement, state);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class myViewHolder extends RecyclerView.ViewHolder {

        TextView patientNameTxt;

        public myViewHolder(View itemView) {
            super(itemView);
            patientNameTxt = itemView.findViewById(R.id.patientNameTxt);

        }




    }
}
