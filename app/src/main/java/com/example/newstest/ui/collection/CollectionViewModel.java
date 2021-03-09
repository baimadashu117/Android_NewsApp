package com.example.newstest.ui.collection;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CollectionViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<String> pText;

    public MutableLiveData<String> getText() {

        if (mText == null) {
            mText = new MutableLiveData<>();
        }
        return mText;
    }

    public MutableLiveData<String> getpText() {
        if (pText == null) {
            pText = new MutableLiveData<>();
        }
        return pText;
    }
}