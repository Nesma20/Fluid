package com.example.fluid.ui.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.fluid.R;
import com.example.fluid.ui.listeners.MyAlertActionListener;
import com.example.fluid.model.pojo.Appointement;
import com.example.fluid.utils.Constants;
import com.example.fluid.utils.PreferenceController;
import com.example.fluid.utils.StringUtil;

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
        String languageName = PreferenceController.getInstance(mContext).get(PreferenceController.LANGUAGE);
        if (languageName.contains(Constants.ENGLISH)) {
            holder.patientNameTxt.setText(StringUtil.toCamelCase(appointement.getEnglishName()));
        } else if (languageName.contains("العربية") || languageName.contains(Constants.ARABIC)) {
            holder.patientNameTxt.setText(appointement.getArabicName());
        }
        if(!appointement.getImagePath().isEmpty())
            Glide.with(mContext)
                    .load( PreferenceController.getInstance(mContext).get(Constants.IP)
                            +":"+PreferenceController.getInstance(mContext).get(Constants.PORT)+ appointement.getImagePath())
                    .circleCrop()
                    .into(holder.patientImg);
        else {
            if (appointement.getSexCode().contains("F")) {

                holder.patientImg.setImageResource(R.drawable.ic_girl);
            } else {
                holder.patientImg.setImageResource(R.drawable.man);
            }
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
        ImageView patientImg;
        public myViewHolder(View itemView) {
            super(itemView);
            patientNameTxt = itemView.findViewById(R.id.patientNameTxt);
            patientImg = itemView.findViewById(R.id.img_customer_for_dialog);

        }




    }
}
