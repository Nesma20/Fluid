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

    public static String displayTime(String time)  {
        String amOrPmTxt = "";
        String hours="";
        String minutes="";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            Date date = simpleDateFormat.parse(time);
            Log.i("StringUtil",date.getHours()+" date hours");

            if(date.getHours()> 12) {
                amOrPmTxt = "PM";
               if((date.getHours() %12) >=1 && (date.getHours()%12) <10)
                hours = 0 +String.valueOf(date.getHours() %12);
               else
                   hours = String.valueOf(date.getHours() %12);
            }

            else if(date.getHours() == 12)
            {
                amOrPmTxt = "PM";
                hours = String.valueOf(date.getHours());
            }
            else if(date.getHours()<12 && date.getHours()>=10){
                amOrPmTxt = "AM";

                hours = String.valueOf(date.getHours());
            }
            else if (date.getMinutes() >=0 && date.getHours()<10)
            {
                amOrPmTxt = "AM";

                hours = 0+ String.valueOf(date.getHours());
            }

            if(date.getMinutes() >=0 && date.getMinutes() <10)
                minutes = 0+ String.valueOf(date.getMinutes());
            else
                minutes = String.valueOf(date.getMinutes());
            return hours + ":" + minutes +" "+amOrPmTxt;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";

    }
}
