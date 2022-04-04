package com.example.TRASPASOS_APP.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.TRASPASOS_APP.GlobalClass;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        String mns="BIENVENIDO DE NUEVO " + GlobalClass.nomEmpleado;
        mText.setValue(mns);
    }

    public LiveData<String> getText() {
        return mText;
    }
}