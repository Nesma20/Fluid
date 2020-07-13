package com.thetatechno.fluid.ui.listeners;


public interface UpdateEventListener {
     String action = "";

     void checkInPatient();
     void confirmArrived();
     void callPatient();
     void checkOutPatient();

}
