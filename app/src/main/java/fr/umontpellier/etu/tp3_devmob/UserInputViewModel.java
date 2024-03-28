package fr.umontpellier.etu.tp3_devmob;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.Serializable;

//TODO add hobby
public class UserInputViewModel extends ViewModel implements Serializable {
    private MutableLiveData<String> currentSurname;
    private MutableLiveData<String> currentName;
    private MutableLiveData<String> currentBirthdate;
    private MutableLiveData<String> currentNumber;
    private MutableLiveData<String> currentMail;
    private MutableLiveData<String> currentHobbies;

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

    public MutableLiveData<String> getCurrentHobbies() {
        if (currentHobbies == null) currentHobbies = new MutableLiveData<>();
        return currentHobbies;
    }

    public MutableLiveData<String> get(String s) {
        switch (s) {
            case "surname":
                return getCurrentSurname();
            case "name":
                return getCurrentName();
            case "birthdate":
                return getCurrentBirthdate();
            case "number":
                return getCurrentNumber();
            case "mail":
                return getCurrentMail();
        }
        throw new RuntimeException("get of " + s + " in " + this.getClass().getSimpleName() + " is wrong ...");
    }
}
