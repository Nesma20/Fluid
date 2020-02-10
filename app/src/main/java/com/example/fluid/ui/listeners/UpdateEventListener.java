package com.example.fluid.ui.listeners;

import com.example.fluid.ui.activities.main.SimpleIdlingResource;

import javax.annotation.Nullable;

public interface UpdateEventListener {
     void checkInPatient();
     void confirmArrived();
     void callPatient();
     void checkOutPatient();

}
