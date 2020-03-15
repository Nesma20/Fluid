package com.thetatechno.fluid.ui.activities.locations;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluid.model.pojo.FacilityList;
import com.thetatechno.fluid.model.services.repositories.LocationsRepository;
import com.thetatechno.fluid.utils.App;
import com.thetatechno.fluid.utils.PreferenceController;

public class LocationsViewModel extends ViewModel {
    LocationsRepository locationsRepository = new LocationsRepository();
    public MutableLiveData<FacilityList> getAllFacilities(String id){
        String langId = PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase();
        return locationsRepository.getAppointmentListData(id,langId);
    }
}
