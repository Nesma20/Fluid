package com.example.fluid.utils;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.fluid.BuildConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StringUtil {
    public static String toCamelCase(final String words) {
        if (words == null)
            return null;

        final StringBuilder builder = new StringBuilder(words.length());

        for (final String word : words.split(" ")) {
            if (!word.isEmpty()) {
                builder.append(Character.toUpperCase(word.charAt(0)));
                builder.append(word.substring(1).toLowerCase());
            }
            if (!(builder.length() == words.length()))
                builder.append(" ");
        }

        return builder.toString();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String displayTime(String time)  {
        String amOrPmTxt = "";
        int hours;
        int minutes;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            Date date = simpleDateFormat.parse(time);
            Log.i("StringUtil",date.getHours()+" date hours");

            if(date.getHours()> 12) {
                amOrPmTxt = "PM";

                hours = date.getHours() %12;

            }

            else if(date.getHours() == 12)
            {
                amOrPmTxt = "PM";
                hours = date.getHours();
            }
            else{
                amOrPmTxt = "AM";
                hours = date.getHours();
            }
            if(date.getMinutes() ==0)
            return hours + ":" + date.getMinutes() + "0" +" "+amOrPmTxt;
            else
                return hours + ":" + date.getMinutes() + " "+amOrPmTxt;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";

    }
}
