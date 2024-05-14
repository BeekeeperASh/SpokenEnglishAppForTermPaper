package com.example.spokenenglishappfortermpaper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SharedViewModel extends ViewModel {

    private MutableLiveData<String> _test = new MutableLiveData<>("Some text");
    public LiveData<String> test = _test;
    private MutableLiveData<Boolean> _isCurrentUser = new MutableLiveData<>(false);
    public LiveData<Boolean> isCurrentUser = _isCurrentUser;

    void setTest(String newInstance){
        _test.setValue(newInstance);
    }

    void  setIsCurrentUser(Boolean newInstance){
        _isCurrentUser.setValue(newInstance);
    }

}
