package com.example.fluid.utils;;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceController {
  private static final String DATABASE_NAME = "FluidApp";
  public static final String LANGUAGE = "lang";
  public static final String PREF_EMAIL= "email";
  public static final String PREF_PASSWORD= "password";
  public static final String PREF_USERNAME= "username";


  private static PreferenceController instance;
  private SharedPreferences preferences;


  public static PreferenceController getInstance(Context context) {
    if (instance == null) instance = new PreferenceController(context, DATABASE_NAME);
    return instance;
  }

  private PreferenceController(Context context, String databaseName) {
    preferences = context.getSharedPreferences(databaseName, 0);
  }

  public void persist(String key, String val) {
    preferences.edit().putString(key, val).apply();
  }

  public String get(String key) {
    return preferences.getString(key, "");
  }

  public void clear(String key) {
    persist(key, "");
  }



}

