package fr.umontpellier.etu.tp3_devmob;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;

public class UserInputFragment extends Fragment {

    private UserInputViewModel model;
    private View myView;
    private boolean isSynchronousWithOutput = false;
    private final ArrayList<Integer> langList = new ArrayList<>();
    private final String[] hobbiesArray = new String[]{"Sport", "Musique", "Lecture"};
    private EditText surname;
    private EditText name;
    private EditText birthdate;
    private EditText number;
    private EditText mail;
    private TextView hobby;


    public UserInputFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView =  inflater.inflate(R.layout.fragment_user_input, container, false);

        surname= myView.findViewById(R.id.edit_text_surname);
        name = myView.findViewById(R.id.edit_text_name);
        birthdate = myView.findViewById(R.id.edit_text_birthdate);
        number = myView.findViewById(R.id.edit_text_number);
        mail = myView.findViewById(R.id.edit_text_mail);
        hobby = myView.findViewById(R.id.edit_hobby);


        this.setHintPlaceholder(myView, surname, "Nguyen");
        this.setHintPlaceholder(myView, name, "Tony");
        this.setHintPlaceholder(myView, birthdate, "19/06/2000");
        this.setHintPlaceholder(myView, number, "+33123456789");
        this.setHintPlaceholder(myView, mail, "tony.nguyen@etu.umontpellier.fr");

        model = new ViewModelProvider(this).get(UserInputViewModel.class);

        // listening for input modification
        addTextChangedListener( surname,myView,"surname");
        addTextChangedListener( name,myView,"name");
        addTextChangedListener( birthdate,myView,"birthdate");
        addTextChangedListener( number,myView,"number");
        addTextChangedListener( mail,myView,"mail");

        // button SUBMIT
        myView.findViewById(R.id.submit_button).setOnClickListener(v -> {
            // data transmission
            // bundle result will hold the data
            Bundle result = new Bundle();

            // put data
            putDataInsideBundle(result, name, "inputName");
            putDataInsideBundle(result, surname, "inputSurname");
            putDataInsideBundle(result, birthdate, "inputBirthdate");
            putDataInsideBundle(result, number, "inputNumber");
            putDataInsideBundle(result, mail, "inputMail");
            putDataInsideBundle(result, hobby, "inputHobbies");

            this.isSynchronousWithOutput = ((SwitchMaterial) myView.findViewById(R.id.switch_sync)).isChecked();
            result.putString("isSynchron", String.valueOf(this.isSynchronousWithOutput));
            // setting up to transmit ViewModel with LiveData for synchronisation form/output
            result.putSerializable("theModel",model);

            // send
            // notifying fragment manager of change
            getParentFragmentManager().setFragmentResult("requestKey", result);

            Toast.makeText(getContext(), "Input submitted" + (this.isSynchronousWithOutput ? " (w/ sync on)" :""), Toast.LENGTH_SHORT).show();
        });

        // button SAVE
        myView.findViewById(R.id.validate_button).setOnClickListener(v -> {
            saveDataToFile();

        });
        // multiple choice selector
        this.set(myView);


        myView.findViewById(R.id.upload_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent serviceIntent = new Intent(getActivity(), FileDownloadService.class);
                getActivity().startService(serviceIntent);
            }
        });
        return myView;
    }

    // for a nice form
    private void setHintPlaceholder(View v, EditText editText, String placeholder) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    editText.setHint(placeholder);
                } else {
                    editText.setHint("");
                }
            }
        });
    }

    private void set(View myView) {
        // initialize selected language array
        boolean[] selectedHobbies = new boolean[hobbiesArray.length];

        hobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(myView.getContext());

                // set title
                builder.setTitle("Select Language");

                // set dialog non cancelable
                builder.setCancelable(false);

                builder.setMultiChoiceItems(hobbiesArray, selectedHobbies, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        // check condition
                        if (b) {
                            // when checkbox selected
                            // Add position  in lang list
                            langList.add(i);
                            // Sort array list
                            Collections.sort(langList);
                        } else {
                            // when checkbox unselected
                            // Remove position from langList
                            langList.remove(Integer.valueOf(i));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Initialize string builder
                        StringBuilder stringBuilder = new StringBuilder();
                        // use for loop
                        for (int j = 0; j < langList.size(); j++) {
                            // concat array value
                            stringBuilder.append(hobbiesArray[langList.get(j)]);
                            // check condition
                            if (j != langList.size() - 1) {
                                // When j value  not equal
                                // to lang list size - 1
                                // add comma
                                stringBuilder.append(", ");
                            }
                        }
                        // set text on textView
                        hobby.setText(stringBuilder.toString());
                        Log.v("debug input","text="+hobby.getText().toString());
                        Log.v("debug input","isSynchronousWithOutput="+isSynchronousWithOutput);
                        if (UserInputFragment.this.isSynchronousWithOutput) {
                            String updatedHobbiesString = hobby.getText().toString();
                            UserInputFragment.this.model.getCurrentHobby().postValue(updatedHobbiesString);
                            Log.v("debug input","updating live");
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // use for loop
                        for (int j = 0; j < selectedHobbies.length; j++) {
                            // remove all selection
                            selectedHobbies[j] = false;
                            // clear language list
                            langList.clear();
                            // clear text view value
                            hobby.setText("");
                        }
                    }
                });
                // show dialog
                builder.show();
            }
        });
    }

    // helper function //////////////////////////////////////
    private void putDataInsideBundle(Bundle theDataHolder, EditText editText, String dataName) {
        theDataHolder.putString(dataName, editText.getText().toString());
    }

    private void putDataInsideBundle(Bundle theDataHolder, TextView textView, String dataName) {
        Log.v("debug input","textToSend="+textView.getText().toString()+" key="+dataName);
        theDataHolder.putString(dataName, textView.getText().toString());
    }

    // when editing form, if this.isSynchronousWithOutput is true, notify the model
    private void addTextChangedListener(EditText editText, View view, String aString) {
        (editText).addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* not needed */ }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { /* not needed */ }
            @Override
            public void afterTextChanged(Editable s) {
                //Log.v("debug","after text of name changed");

                //Log.v("debug isChecked", String.valueOf(UserInputFragment.this.isSynchronousWithOutput));
                if (UserInputFragment.this.isSynchronousWithOutput) {
                    //Log.v("debug","sychrone");
                    //Log.v("debug","posting change");
                    //Log.v("debug",s.toString());
                    model.get(aString).postValue(s.toString());
                }
            }
        });
    }
/*  This part is to synchronise it instantly without cliquing submit
    private void setupSynchronizationSwitch() {
        SwitchMaterial syncSwitch = myView.findViewById(R.id.switch_sync);
        syncSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isSynchronousWithOutput = isChecked;
            // Optionally force-update all fields upon enabling the switch
            if(isSynchronousWithOutput) {
                forceUpdateViewModel();
            }
        });
    }
*/

    private void saveDataToFile() {
        JSONObject userData = constructJsonFromData();
        String filename = "userData.txt";

        try (FileOutputStream fos = getActivity().openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(userData.toString().getBytes());
            Toast.makeText(getContext(), "Data saved successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("DisplayFragment", "File write failed", e);
            Toast.makeText(getContext(), "Failed to save data", Toast.LENGTH_SHORT).show();
        }
    }

    private JSONObject constructJsonFromData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("surname", name.getText().toString());
            jsonObject.put("name", surname.getText().toString());
            jsonObject.put("birthdate", birthdate.getText().toString());
            jsonObject.put("number", number.getText().toString());
            jsonObject.put("mail", mail.getText().toString());
            // from the TextView, get the string shown on screen
            jsonObject.put("hobbies", hobby.getText().toString());
        } catch (JSONException e) {
            Log.e("DisplayFragment", "Error creating JSON", e);
        }
        return jsonObject;
    }



}