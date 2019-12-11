package com.example.fluid.ui.activities;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.fluid.utils.StringUtil;

public class AppointmentDetailsViewModel {

    public String capitalizeName(String name) {
       return StringUtil.toCamelCase(name);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String displayTime(String time){
      return  StringUtil.displayTime(time);
    }


}
