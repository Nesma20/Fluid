package com.thetatechno.fluid.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.thetatechno.fluid.R;
import com.thetatechno.fluid.model.pojo.Appointement;
import com.thetatechno.fluid.ui.activities.appointmentdetails.AppointmentDetailsActivity;
import com.thetatechno.fluid.utils.PreferenceController;
import com.thetatechno.fluid.utils.StringUtil;
import com.thetatechno.fluid.utils.Constants;

import java.util.List;

public class AppointmentListAdapter extends RecyclerView.Adapter<AppointmentListAdapter.myViewHolder> {
    Context mContext;
    List<Appointement> myItems;
    private final String APPOINTMENT= "appointment";
    CircularProgressDrawable circularProgressDrawable;
    private final float STROKE_WIDTH = 5f;
    private final float CENTER_RADIUS = 30f;
    public AppointmentListAdapter(Context context) {
        mContext = context;
         circularProgressDrawable = new CircularProgressDrawable(mContext);

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
        String languageName = PreferenceController.getInstance(mContext).get(PreferenceController.LANGUAGE);
        if (languageName.contains(Constants.ENGLISH)) {
            holder.patientNameTxt.setText(StringUtil.toCamelCase(appointement.getEnglishName()));
        } else if (languageName.contains("العربية") || languageName.contains(Constants.ARABIC)) {
            holder.patientNameTxt.setText(appointement.getArabicName());
        }

        circularProgressDrawable.setStrokeWidth(STROKE_WIDTH);
        circularProgressDrawable.setCenterRadius(CENTER_RADIUS);
        circularProgressDrawable.start();
        if(!appointement.getImagePath().isEmpty() && appointement.getSexCode().contains("F"))
        Glide.with(mContext)
                .load( Constants.BASE_URL+ Constants.BASE_EXTENSION_FOR_PHOTOS + appointement.getImagePath())
                .circleCrop()
                .placeholder(circularProgressDrawable)
                .into(holder.patientAvtarImage);
        else if(!appointement.getImagePath().isEmpty() && appointement.getSexCode().contains("M")){
            Glide.with(mContext)
                    .load( Constants.BASE_URL+ Constants.BASE_EXTENSION_FOR_PHOTOS +appointement.getImagePath())
                    .circleCrop()
                    .placeholder(circularProgressDrawable)
                    .into(holder.patientAvtarImage);
        }
        else {
            if (appointement.getSexCode().contains(Constants.FEMALE)) {

                holder.patientAvtarImage.setImageResource(R.drawable.ic_girl);
            } else if(appointement.getSexCode().contains(Constants.MALE)) {
                holder.patientAvtarImage.setImageResource(R.drawable.man);
            }
        }
        if (appointement.getArrivalTime().isEmpty() && appointement.getCallingTime().isEmpty() && appointement.getCheckinTime().isEmpty()) {

            holder.patientStateImage.setImageResource(R.drawable.ic_chair);

        }
        if (!appointement.getArrivalTime().isEmpty()) {
            holder.patientStateImage.setImageResource(R.drawable.ic_arrive);
            holder.patientStateImage.setColorFilter(Color.argb(255, 0, 175, 254));

        }
        if (!appointement.getCallingTime().isEmpty() && appointement.getCheckinTime().isEmpty()) {
            holder.patientStateImage.setImageResource(R.drawable.ic_call);
            holder.patientStateImage.setColorFilter(Color.argb(255, 0, 175, 254));

        } else if (!appointement.getCheckinTime().isEmpty()) {
            holder.patientStateImage.setImageResource(R.drawable.ic_start);
            holder.patientStateImage.setColorFilter(Color.argb(255, 0, 175, 254));

        }
        if (appointement.getScheduledTime().isEmpty()) {
            holder.scheduledTimeTxt.setVisibility(View.GONE);
            holder.scheduledIcon.setVisibility(View.GONE);
        } else {
            if (appointement.getActiveBooking().equals(Constants.ACTIVE_BOOKING)) {
                holder.scheduledIcon.setVisibility(View.VISIBLE);
                holder.scheduledTimeTxt.setVisibility(View.VISIBLE);
                holder.scheduledTimeTxt.setText(StringUtil.displayTime(appointement.getScheduledTime()));
            }
            else{
                holder.scheduledTimeTxt.setVisibility(View.GONE);
                holder.scheduledIcon.setVisibility(View.GONE);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext.getApplicationContext(), AppointmentDetailsActivity.class);
                intent.putExtra(APPOINTMENT, (Parcelable) appointement);
                holder.itemView.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myItems.size();
    }


    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView mRNTxt;
        TextView patientNameTxt;
        ImageView patientStateImage;
        ImageView patientAvtarImage;
        TextView scheduledTimeTxt;
        ImageView scheduledIcon ;
        public myViewHolder(View itemView) {
            super(itemView);
            mRNTxt = itemView.findViewById(R.id.customer_id_txt);
            patientNameTxt = itemView.findViewById(R.id.customer_name_text);
            patientStateImage = itemView.findViewById(R.id.customer_state_img);
            patientAvtarImage = itemView.findViewById(R.id.customerAvatar);
            scheduledTimeTxt = itemView.findViewById(R.id.txt_scheduled_time_in_card);
            scheduledIcon = itemView.findViewById(R.id.imageView);

        }

    }

}
