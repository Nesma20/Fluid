package com.example.fluid.ui.listeners;

import android.os.Parcelable;

import com.example.fluid.ui.activities.main.SimpleIdlingResource;

import javax.annotation.Nullable;

public interface UpdateEventListener extends Parcelable {
     void checkInPatient();
     void confirmArrived();
     void callPatient();
     void checkOutPatient();

}
