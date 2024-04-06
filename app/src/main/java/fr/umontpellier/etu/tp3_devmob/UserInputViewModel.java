package fr.umontpellier.etu.tp3_devmob;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.Serializable;

public class UserInputViewModel extends ViewModel implements Serializable {
    private transient MutableLiveData<String> currentSurname= new MutableLiveData<>();
    private transient MutableLiveData<String> currentName= new MutableLiveData<>();
    private transient MutableLiveData<String> currentBirthdate= new MutableLiveData<>();
    private transient MutableLiveData<String> currentNumber= new MutableLiveData<>();
    private transient MutableLiveData<String> currentMail= new MutableLiveData<>();
    private transient MutableLiveData<String> currentHobby = new MutableLiveData<>();

    public MutableLiveData<String> getCurrentSurname() {
        return currentSurname;
    }

    public MutableLiveData<String> getCurrentName() {
        return currentName;
    }

    public MutableLiveData<String> getCurrentBirthdate() {
        return currentBirthdate;
    }

    public MutableLiveData<String> getCurrentNumber() {
        return currentNumber;
    }

    public MutableLiveData<String> getCurrentMail() {
        return currentMail;
    }
    public MutableLiveData<String> getCurrentHobby() {
        return currentHobby;
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
            case "hobby":
                return getCurrentHobby();
        }
        throw new RuntimeException("get of " + s + " in " + this.getClass().getSimpleName() + " is wrong ...");
    }

    public void setCurrentSurname(String surname) {
        currentSurname.setValue(surname);
    }

    public void setCurrentName(String name) {
        currentName.setValue(name);
    }

    public void setCurrentBirthdate(String birthdate) {
        currentBirthdate.setValue(birthdate);
    }

    public void setCurrentNumber(String number) {
        currentNumber.setValue(number);
    }

    public void setCurrentMail(String mail) {
        currentMail.setValue(mail);
    }
    public void setCurrentHobby(String hobby) {
        currentHobby.setValue(hobby);
    }
}
