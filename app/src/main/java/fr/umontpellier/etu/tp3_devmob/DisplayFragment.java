package fr.umontpellier.etu.tp3_devmob;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DisplayFragment extends Fragment {

    private UserInputViewModel model;
    private View myView;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DisplayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DisplayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DisplayFragment newInstance(String param1, String param2) {
        DisplayFragment fragment = new DisplayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_display, container, false);



        // Subscribing to wait for change
        // When another fragment setResult() with same requestKey, do this ->
        // After click of "Submit" button
        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                Log.v("debug display","received");

                // We set the text inside the empty TextView
                addToView(bundle,"inputSurname",R.id.surnameText);
                addToView(bundle,"inputName",R.id.nameText);
                addToView(bundle,"inputBirthdate",R.id.birthdateText);
                addToView(bundle,"inputNumber",R.id.numberText);
                addToView(bundle,"inputMail",R.id.mailText);
                addToView(bundle,"inputHobbies", R.id.hobbiesText);
                //addToView(bundle,"isSynchron");

                // so both fragment source and target have same UserInputViewModel
                model = (UserInputViewModel) bundle.getSerializable("theModel");


                // Then, we listen for change through our ViewModel, and edit our TextView accordingly inside onChanged()
                assert model != null;
                //Log.v("debug",model.toString());

                // Create the observer which updates the UI.
                // done inside createCustomObserver()

                // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
                model.getCurrentSurname().observe(getViewLifecycleOwner(), DisplayFragment.this.createCustomObserver(R.id.surnameText));
                model.getCurrentName().observe(getViewLifecycleOwner(), DisplayFragment.this.createCustomObserver(R.id.nameText));
                model.getCurrentBirthdate().observe(getViewLifecycleOwner(), DisplayFragment.this.createCustomObserver(R.id.birthdateText));
                model.getCurrentNumber().observe(getViewLifecycleOwner(), DisplayFragment.this.createCustomObserver(R.id.numberText));
                model.getCurrentMail().observe(getViewLifecycleOwner(), DisplayFragment.this.createCustomObserver(R.id.mailText));
                model.getCurrentHobbies().observe(getViewLifecycleOwner(), DisplayFragment.this.createCustomObserver(R.id.hobbiesText));
                //TODO observe hobbies
            }
        });

        return myView;
    }

    // set text programmatically of a TextView using data from a bundle
    private void addToView(Bundle theDataHolder, String dataName, int idTextView) {
        Log.v("debug","adding to view");

        TextView myText = myView.findViewById(idTextView);//new TextView(linearLayout.getContext());

        Log.v("debug","text received="+theDataHolder.getString(dataName));

        myText.setText(theDataHolder.getString(dataName));
        //myText.setId(View.generateViewId());
        myText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        //linearLayout.addView(myText);
    }

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
}