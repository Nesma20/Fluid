package com.thetatechno.fluid.ui.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.thetatechno.fluid.R;
import com.thetatechno.fluid.ui.listeners.AlertActionListener;
import com.thetatechno.fluid.model.pojo.Appointement;
import com.thetatechno.fluid.utils.Constants;
import com.thetatechno.fluid.utils.PreferenceController;
import com.thetatechno.fluid.utils.StringUtil;

import java.util.List;

public class AppointmentListDialogAdapter extends RecyclerView.Adapter<AppointmentListDialogAdapter.myViewHolder> {
    Context mContext;
    List<Appointement> appointmentList;
    String state;
    AlertActionListener alertActionListener;

    public AppointmentListDialogAdapter(Context mContext, List<Appointement> appointmentList, AlertActionListener alertActionListener, String state) {
        this.mContext = mContext;
        this.appointmentList = appointmentList;
        this.alertActionListener = alertActionListener;
        this.state = state;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.alert_dialog_list_item, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        final Appointement appointement = appointmentList.get(position);
        String languageName = PreferenceController.getInstance(mContext).get(PreferenceController.LANGUAGE);
        if (languageName.contains(Constants.ENGLISH)) {
            holder.patientNameTxt.setText(StringUtil.toCamelCase(appointement.getEnglishName()));
        } else if (languageName.contains("العربية") || languageName.contains(Constants.ARABIC)) {
            holder.patientNameTxt.setText(appointement.getArabicName());
        }
        if (!appointement.getImagePath().isEmpty() && appointement.getSexCode().contains("F"))
            Glide.with(mContext)
                    .load(Constants.BASE_URL +Constants.BASE_EXTENSION_FOR_PHOTOS+ appointement.getImagePath())
                    .circleCrop()
                    .placeholder(R.drawable.ic_girl)
                    .into(holder.patientImg);
        else if(!appointement.getImagePath().isEmpty() && appointement.getSexCode().contains("M")){
            Glide.with(mContext)
                    .load(Constants.BASE_URL + Constants.BASE_EXTENSION_FOR_PHOTOS+ appointement.getImagePath())
                    .circleCrop()
                    .placeholder(R.drawable.man)
                    .into(holder.patientImg);
        }
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
                alertActionListener.updateData(appointement, state);
            }
        });

    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
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
