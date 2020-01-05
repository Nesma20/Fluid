package com.example.fluid.utils;;


import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.LocaleList;
import android.util.Log;

import java.util.Locale;

public class LanguageUtil {
    public static void changeLanguageType(Context context, Locale newLocale) {

        Resources res = context.getResources();
        Configuration configuration = res.getConfiguration();

        if (VersionUtils.isAfter24()) {
            configuration.setLocale(newLocale);

            LocaleList localeList = new LocaleList(newLocale);
            LocaleList.setDefault(localeList);
            configuration.setLocales(localeList);

        } else if (VersionUtils.isAfter17()) {
            configuration.setLocale(newLocale);

        } else {
            configuration.locale = newLocale;
            res.updateConfiguration(configuration, res.getDisplayMetrics());
        }
        Locale.setDefault(newLocale);
        configuration.locale = LanguageUtil.getLanguageType(App.getContext()); // or whichever locale you desire
        configuration.setLocale(LanguageUtil.getLanguageType(App.getContext()));
        Resources.getSystem().updateConfiguration(configuration, res.getDisplayMetrics());

    }

    public static Locale getLanguageType(Context context) {
        Log.i("=======", "context = " + context);
//        Resources resources = context.getResources();
        Resources resources = App.getContext().getResources();
        Configuration config = resources.getConfiguration();

        if (VersionUtils.isAfter24()) {
            return config.getLocales().get(0);
        } else {
            return config.locale;
        }
    }
}
