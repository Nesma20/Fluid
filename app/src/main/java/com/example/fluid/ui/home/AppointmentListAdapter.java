package com.example.fluid.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fluid.R;
import com.example.fluid.model.pojo.Appointement;
import com.example.fluid.ui.activities.AppointmentDetailsActivity;

import java.util.List;

public class AppointmentListAdapter extends RecyclerView.Adapter<AppointmentListAdapter.myViewHolder> {
    Context mContext;
    List<Appointement> myItems;
    private final String APPOINTMENT= "appointment";
    public AppointmentListAdapter(Context context) {
        mContext = context;

    }

    public void setItems(List<Appointement> items) {
        myItems = items;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.customer_list_item, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        final Appointement appointement = myItems.get(position);
        System.out.println("from appointement in adapter" + appointement.getArabicName());
        holder.mRNTxt.setText(appointement.getMRN());
        String languageName = mContext.getResources().getConfiguration().locale.getDisplayName();
        if (languageName.contains("English")) {
            holder.patientNameTxt.setText(appointement.getEnglishName());

        } else if (languageName.contains("العربية") || languageName.contains("Arabic")) {
            holder.patientNameTxt.setText(appointement.getArabicName());
        }
        if (appointement.getSexCode().contains("F")) {

            holder.patientAvtarImage.setImageResource(R.drawable.ic_girl);
        } else {
            holder.patientAvtarImage.setImageResource(R.drawable.man);
        }
        if (!appointement.getArrivalTime().isEmpty()) {
            holder.patientStateImage.setImageResource(R.drawable.ic_arrive);
            holder.patientStateImage.setColorFilter(R.color.colorAccent, PorterDuff.Mode.SRC_IN);
        }
        if (!appointement.getCallingTime().isEmpty() && appointement.getCheckinTime().isEmpty()) {
            holder.patientStateImage.setImageResource(R.drawable.ic_call);

            holder.patientStateImage.setColorFilter(R.color.colorAccent);
        } else if (!appointement.getCheckinTime().isEmpty()) {
            holder.patientStateImage.setImageResource(R.drawable.ic_start);
            holder.patientStateImage.setColorFilter(R.color.colorAccent);
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(mContext.getApplicationContext(), AppointmentDetailsActivity.class);
                intent.putExtra(APPOINTMENT, (Parcelable) appointement);
                mContext.startActivity(intent);
                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return myItems.size();
    }


    class myViewHolder extends RecyclerView.ViewHolder{

        TextView mRNTxt;
        TextView patientNameTxt;
        ImageView patientStateImage;
        ImageView patientAvtarImage;

        public myViewHolder(View itemView) {
            super(itemView);

            mRNTxt = itemView.findViewById(R.id.customer_id_txt);
            patientNameTxt = itemView.findViewById(R.id.customer_name_text);
            patientStateImage = itemView.findViewById(R.id.customer_state_img);
            patientAvtarImage = itemView.findViewById(R.id.customerAvatar);
        }



    }
}
