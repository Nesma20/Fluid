package com.example.fluid.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fluid.R;
import com.example.fluid.model.pojo.Item;

import java.util.List;
import java.util.Locale;

public class AppointmentListAdapter extends RecyclerView.Adapter<AppointmentListAdapter.myViewHolder> {
    Context mContext;
    List<Item> myItems;

    public AppointmentListAdapter(Context context) {
        mContext = context;

    }

    public void setItems(List<Item> items) {
        myItems = items;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.patient_list_item, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        final Item item = myItems.get(position);
        System.out.println("from item in adapter" + item.getArabicName());
        holder.mRNTxt.setText(item.getMRN());
        String languagename = mContext.getResources().getConfiguration().locale.getDisplayName();
        if (languagename.contains("English")) {
            holder.patientNameTxt.setText(item.getEnglishName());

        } else if (languagename.contains("العربية") || languagename.contains("Arabic")) {
            holder.patientNameTxt.setText(item.getArabicName());
        }
        if (item.getSexCode().contains("F")) {

            holder.patientAvtarImage.setImageResource(R.drawable.girl);
        } else {
            holder.patientAvtarImage.setImageResource(R.drawable.man);
        }
        if (!item.getArrivalTime().isEmpty()) {
            holder.patientStateImage.setImageResource(R.drawable.arrive);
        }
        if (!item.getCallingTime().isEmpty() && item.getCheckinTime().isEmpty()) {
            holder.patientStateImage.setImageResource(R.drawable.ic_call);
        } else if (!item.getCheckinTime().isEmpty()) {
            holder.patientStateImage.setImageResource(R.drawable.ic_start);
        } else if (item.getCallingTime().isEmpty() && item.getCheckinTime().isEmpty() & item.getArrivalTime().isEmpty()) {
            holder.patientStateImage.setImageResource(R.drawable.hourglass);

        }


    }

    @Override
    public int getItemCount() {
        return myItems.size();
    }


    class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView mRNTxt;
        TextView patientNameTxt;
        ImageView patientStateImage;
        ImageView patientAvtarImage;

        public myViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mRNTxt = itemView.findViewById(R.id.patient_id_txt);
            patientNameTxt = itemView.findViewById(R.id.patient_name_text);
            patientStateImage = itemView.findViewById(R.id.patient_state_img);
            patientAvtarImage = itemView.findViewById(R.id.patientAvtar);
        }
//TODO : Clicking actions
        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {

            Toast.makeText(mContext, "pressed", Toast.LENGTH_SHORT).show();
            return false;
        }


    }
}
