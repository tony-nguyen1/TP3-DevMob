package fr.umontpellier.etu.tp3_devmob;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.Serializable;

public class UserInputViewModel extends ViewModel implements Serializable {
    private MutableLiveData<String> currentSurname;
    private MutableLiveData<String> currentName;
    private MutableLiveData<String> currentBirthdate;
    private MutableLiveData<String> currentNumber;
    private MutableLiveData<String> currentMail;

    public MutableLiveData<String> getCurrentSurname() {
        if (currentSurname == null) currentSurname = new MutableLiveData<>();
        return currentSurname;
    }

    public MutableLiveData<String> getCurrentName() {
        if (currentName == null) currentName = new MutableLiveData<>();
        return currentName;
    }

    public MutableLiveData<String> getCurrentBirthdate() {
        if (currentBirthdate == null) currentBirthdate = new MutableLiveData<>();
        return currentBirthdate;
    }

    public MutableLiveData<String> getCurrentNumber() {
        if (currentNumber == null) currentNumber = new MutableLiveData<>();
        return currentNumber;
    }

    public MutableLiveData<String> getCurrentMail() {
        if (currentMail == null) currentMail = new MutableLiveData<>();
        return currentMail;
    }
}
