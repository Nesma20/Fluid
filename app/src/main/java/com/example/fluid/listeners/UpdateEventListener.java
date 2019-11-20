package com.example.fluid.listeners;

import com.example.fluid.MyTabHandler;

public interface UpdateEventListener {
    public void checkInPatient(MyTabHandler myTabHandler);
    public void confirmArrived(MyTabHandler myTabHandler);
    public void callPatient(MyTabHandler myTabHandler);
    public void checkOutPatient(MyTabHandler myTabHandler);
    public void changeNumberOfList(MyTabHandler myTabHandler);

 }
