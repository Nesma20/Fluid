package com.example.fluid.ui.activities;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fluid.R;
import com.example.fluid.model.pojo.Appointement;

public class AppointmentDetailsActivity extends BaseActivity  {
private final String TAG = "AppointmentDetails";
private final String APPOINTMENT = "appointment";
private ImageView customerImageView;
private TextView mRNTxt;
private TextView customerNameTxt;
private TextView scheduledTimeTxt;
private TextView expectedTimeTxt;
private TextView arrivalTimeTxt;
private TextView callingTimeTxt;
AppointmentDetailsViewModel appointmentDetailsViewModel = new AppointmentDetailsViewModel();

    private final int flag_no_time = 1;
    private final int flag_no_schedule_time = 2;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);
        setTitle(getString(R.string.Appointment_details));
        Intent intent = getIntent();
        Appointement appointement = intent.getParcelableExtra(APPOINTMENT);
        Log.i(TAG,appointement.getMRN());
        setUpViews();
        customerImageView.setImageResource(R.drawable.user_avatar);
        mRNTxt.setText(appointement.getMRN());
        String languageName = getResources().getConfiguration().locale.getDisplayName();
        if (languageName.contains("English")) {

            customerNameTxt.setText(appointmentDetailsViewModel.capitalizeName(appointement.getEnglishName()));

        } else if (languageName.contains("العربية") || languageName.contains("Arabic")) {
            customerNameTxt.setText(appointement.getArabicName());
        }

        if(!appointement.getArrivalTime().isEmpty()) {
            arrivalTimeTxt.setText(appointmentDetailsViewModel.displayTime(appointement.getArrivalTime()));

        }
        else {
            changeColorFontAndTextOfTxtView(arrivalTimeTxt,flag_no_time);

        }
        if(!appointement.getExpectedTime().isEmpty()) {
            expectedTimeTxt.setText(appointmentDetailsViewModel.displayTime(appointement.getExpectedTime()));
        }
        else
        {
            changeColorFontAndTextOfTxtView(expectedTimeTxt,flag_no_time);

        }
        if(!appointement.getCallingTime().isEmpty()) {
            callingTimeTxt.setText(appointmentDetailsViewModel.displayTime(appointement.getCallingTime()));
        }
        else{
            changeColorFontAndTextOfTxtView(callingTimeTxt,flag_no_time);

        }
        if(!appointement.getScheduledTime().isEmpty()) {

            scheduledTimeTxt.setText(appointmentDetailsViewModel.displayTime(appointement.getScheduledTime()));
        }
        else{
            changeColorFontAndTextOfTxtView(scheduledTimeTxt,flag_no_schedule_time);
        }
    }

    public void setUpViews(){
     customerImageView = findViewById(R.id.customer_img_view);
     mRNTxt = findViewById(R.id.customer_mrn);
     customerNameTxt = findViewById(R.id.customer_name);
     scheduledTimeTxt = findViewById(R.id.scheduled_time_txt);
     expectedTimeTxt = findViewById(R.id.expected_time_txt);
     arrivalTimeTxt = findViewById(R.id.arrive_time_txt);
     callingTimeTxt = findViewById(R.id.called_time_txt);

    }
    public void changeColorFontAndTextOfTxtView(TextView mTxtView,int flag){
        int COLOR_WHE_NO_TIME_FOUND = Color.GRAY;

        if(flag == 1)
        mTxtView.setText(getResources().getString(R.string.when_there_is_no_time_txt));
        else
            mTxtView.setText(getResources().getString(R.string.when_there_is_no_scheduled_time));
        mTxtView.setTextSize(12);
        mTxtView.setTextColor(COLOR_WHE_NO_TIME_FOUND);
    }
}
