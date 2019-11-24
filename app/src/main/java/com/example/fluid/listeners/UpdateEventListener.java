package com.example.fluid.listeners;

public interface UpdateEventListener {
    public void checkInPatient();
    public void confirmArrived();
    public void callPatient();
    public void checkOutPatient();
    public void changeNumberOfList(MyTabHandler myTabHandler);

 }
