package com.example.fluid.ui.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fluid.R;
import com.example.fluid.model.pojo.Appointement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

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
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);
        Intent intent = getIntent();
        Appointement appointement = intent.getParcelableExtra(APPOINTMENT);
        Log.i(TAG,appointement.getMRN());
        setUpViews();
        customerImageView.setImageResource(R.drawable.user_avatar);
        mRNTxt.setText(appointement.getMRN());
        String languageName = getResources().getConfiguration().locale.getDisplayName();
        if (languageName.contains("English")) {
            customerNameTxt.setText(appointement.getEnglishName());

        } else if (languageName.contains("العربية") || languageName.contains("Arabic")) {
            customerNameTxt.setText(appointement.getArabicName());
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss a", Locale.ENGLISH);
        if(!appointement.getArrivalTime().isEmpty()) {

            LocalDateTime localDateTime = LocalDateTime.parse(appointement.getArrivalTime(), formatter);
            arrivalTimeTxt.setText(localDateTime.getHour()+":"+localDateTime.getMinute());
        }
        else {
            arrivalTimeTxt.setText("");

        }
        if(!appointement.getExpectedTime().isEmpty()) {

            LocalDateTime localDateTime = LocalDateTime.parse(appointement.getExpectedTime(), formatter);
            expectedTimeTxt.setText(localDateTime.getHour()+":"+localDateTime.getMinute());
        }
        else
        {
            expectedTimeTxt.setText("");

        }
        if(!appointement.getCallingTime().isEmpty()) {

            LocalDateTime localDateTime = LocalDateTime.parse(appointement.getCallingTime(), formatter);
            callingTimeTxt.setText(localDateTime.getHour()+":"+localDateTime.getMinute());
        }
        else{
            callingTimeTxt.setText("");

        }
        if(!appointement.getScheduledTime().isEmpty()) {

            LocalDateTime localDateTime = LocalDateTime.parse(appointement.getScheduledTime(), formatter);
            scheduledTimeTxt.setText(localDateTime.getHour()+":"+localDateTime.getMinute());
        }
        else{
            scheduledTimeTxt.setText("");
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
}
