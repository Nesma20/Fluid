package com.example.fluid.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fluid.R;
import com.example.fluid.model.pojo.Appointement;

public class AppointmentDetailsActivity extends BaseActivity  {
private final String TAG = "AppointmentDetails";
private final String APPOINTMENT = "appointment";
ImageView customerImageView;
TextView mRNTxt;
TextView customerNameTxt;
TextView scheduledTimeTxt;
TextView expectedTimeTxt;
TextView arrivalTimeTxt;
TextView callingTimeTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);
        Intent intent = getIntent();
        Appointement appointement = intent.getParcelableExtra(APPOINTMENT);
        Log.i(TAG,appointement.getMRN());
        setUpViews();
        mRNTxt.setText(appointement.getMRN());
        String languageName = getResources().getConfiguration().locale.getDisplayName();
        if (languageName.contains("English")) {
            customerNameTxt.setText(appointement.getEnglishName());

        } else if (languageName.contains("العربية") || languageName.contains("Arabic")) {
            customerNameTxt.setText(appointement.getArabicName());
        }
        scheduledTimeTxt.setText(appointement.getScheduledTime());
        expectedTimeTxt.setText(appointement.getExpectedTime());
        callingTimeTxt.setText(appointement.getCallingTime());
        arrivalTimeTxt.setText(appointement.getArrivalTime());
    }
    public void setUpViews(){
     customerImageView = findViewById(R.id.customer_img);
     mRNTxt = findViewById(R.id.customer_mrn);
     customerNameTxt = findViewById(R.id.customer_name);
     scheduledTimeTxt = findViewById(R.id.scheduled_time_txt);
     expectedTimeTxt = findViewById(R.id.expected_time_txt);
     arrivalTimeTxt = findViewById(R.id.arrival_time_txt);
     callingTimeTxt = findViewById(R.id.calling_time_txt);

    }
}
