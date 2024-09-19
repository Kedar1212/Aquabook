package com.example.aquabook;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<List<ServiceModel>> searchResults = new MutableLiveData<>();

    public LiveData<List<ServiceModel>> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<ServiceModel> results) {
        searchResults.setValue(results);
    }
}
