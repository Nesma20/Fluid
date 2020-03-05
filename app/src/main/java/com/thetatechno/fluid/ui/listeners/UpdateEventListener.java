package com.thetatechno.fluid.ui.listeners;

import android.os.Parcelable;

public interface UpdateEventListener extends Parcelable {
     void checkInPatient();
     void confirmArrived();
     void callPatient();
     void checkOutPatient();

}
