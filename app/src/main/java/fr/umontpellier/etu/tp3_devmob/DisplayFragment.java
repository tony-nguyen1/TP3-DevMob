package fr.umontpellier.etu.tp3_devmob;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Objects;


public class DisplayFragment extends Fragment {

    private UserInputViewModel model;
    private View myView;
    private TextView TextView_surname;
    private TextView TextView_name;
    private TextView TextView_birthdate;
    private TextView TextView_number;
    private TextView TextView_mail;
    private TextView TextView_hobbies;

    public DisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_display, container, false);

        TextView_surname = myView.findViewById(R.id.surnameText);
        TextView_name = myView.findViewById(R.id.nameText);
        TextView_birthdate = myView.findViewById(R.id.birthdateText);
        TextView_number = myView.findViewById(R.id.numberText);
        TextView_mail = myView.findViewById(R.id.mailText);
        TextView_hobbies = myView.findViewById(R.id.hobbiesText);

        getParentFragmentManager().setFragmentResultListener("Hello", this, new FragmentResultListener() {
            @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                Log.v("debug display","received model");

                // so both fragment source and target have same UserInputViewModel
                model = (UserInputViewModel) bundle.getSerializable("the_model");

                // We listen for change through our ViewModel, and edit our TextView accordingly inside onChanged()

                // Create the observer which updates the UI.
                // done inside createCustomObserver()

                // Observe the LiveData, passing in this activity
                // as the LifecycleOwner and the observer.
                model.getCurrentSurname().observe(
                        getViewLifecycleOwner(),
                        DisplayFragment.this.createCustomObserver(R.id.surnameText));
                model.getCurrentName().observe(
                        getViewLifecycleOwner(),
                        DisplayFragment.this.createCustomObserver(R.id.nameText));
                model.getCurrentBirthdate().observe(
                        getViewLifecycleOwner(),
                        DisplayFragment.this.createCustomObserver(R.id.birthdateText));
                model.getCurrentNumber().observe(
                        getViewLifecycleOwner(),
                        DisplayFragment.this.createCustomObserver(R.id.numberText));
                model.getCurrentMail().observe(
                        getViewLifecycleOwner(),
                        DisplayFragment.this.createCustomObserver(R.id.mailText));
                model.getCurrentHobby().observe(
                        getViewLifecycleOwner(),
                        DisplayFragment.this.createCustomObserver(R.id.hobbiesText));

                Log.v("debug", "listening for updates");
            }
        });


        return myView;
    }

    // Create the observer which updates the UI.
    private Observer<String> createCustomObserver(int id) {
        return new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newName) {

                //Log.v("debug","display has observed a change");
                // Update the UI, in this case, a TextView.
                TextView myText = myView.findViewById(id);

                //Log.v("debug current display surname",myText.getText().toString());
                //Log.v("debug display wanted surname ",myText.getText().toString());
                myText.setText(newName);
            }
        };
    }

    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Broadcast", "Received broadcast...");
            String surname = intent.getStringExtra("surname");
            String name = intent.getStringExtra("name");
            String birthdate = intent.getStringExtra("birthdate");
            String number = intent.getStringExtra("number");
            String mail = intent.getStringExtra("mail");
            String hobbies = intent.getStringExtra("hobbies");

            TextView_surname.setText(surname);
            TextView_name.setText(name);
            TextView_birthdate.setText(birthdate);
            TextView_number.setText(number);
            TextView_mail.setText(mail);
            TextView_hobbies.setText(hobbies);
        }
    };

    @Override
    public void onStart() {
        Log.v("debug whenDoesItWork","onStart()");
        super.onStart();
        IntentFilter intentFilter = new IntentFilter("action.UPDATE_DATA");
        LocalBroadcastManager
                .getInstance(requireContext())
                .registerReceiver(mMessageReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        Log.v("debug whenDoesItWork","onStop()");
        super.onStop();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mMessageReceiver);
    }
}