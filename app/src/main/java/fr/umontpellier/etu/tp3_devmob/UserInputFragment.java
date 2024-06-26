package fr.umontpellier.etu.tp3_devmob;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.switchmaterial.SwitchMaterial;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("debug onCreate",savedInstanceState != null ? "savedInstanceState is not null" : "savedInstanceState is null");
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
        // data transmission
        // bundle myMessage will hold the data
        Bundle myMessage = new Bundle();
        // setting up to transmit ViewModel with LiveData for synchronisation form/output
        myMessage.putSerializable("the_model",model);
        // send
        // notifying fragment manager of change
        getParentFragmentManager().setFragmentResult("Hello", myMessage);

        // listening for input modification
        addTextChangedListener( surname,myView,"surname");
        addTextChangedListener( name,myView,"name");
        addTextChangedListener( birthdate,myView,"birthdate");
        addTextChangedListener( number,myView,"number");
        addTextChangedListener( mail,myView,"mail");

        // button SUBMIT
        myView.findViewById(R.id.submit_button).setOnClickListener(v -> {
            Log.v("debug","posting change");
            model.get("surname").postValue(surname.getText().toString());
            model.get("name").postValue(name.getText().toString());
            model.get("birthdate").postValue(birthdate.getText().toString());
            model.get("number").postValue(number.getText().toString());
            model.get("mail").postValue(mail.getText().toString());
            model.get("hobby").postValue(hobby.getText().toString());

            this.isSynchronousWithOutput = ((SwitchMaterial) myView.findViewById(R.id.switch_sync)).isChecked();

            Toast.makeText(getContext(), "Input submitted" + (this.isSynchronousWithOutput ? " (w/ sync on)" :""), Toast.LENGTH_SHORT).show();
        });

        Bundle result = new Bundle();
        result.putSerializable("theModel",model);
        getParentFragmentManager().setFragmentResult("firstExchange", result);

        // button SAVE
        myView.findViewById(R.id.validate_button).setOnClickListener(v -> {
            saveJsonToFile();

        });
        // multiple choice selector
        this.set(myView);


        myView.findViewById(R.id.load_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent serviceIntent = new Intent(getActivity(), FileDownloadService.class);
                getActivity().startService(serviceIntent);
            }
        });
        Log.v("debug onCreateView",savedInstanceState != null ? "savedInstanceState is not null" : "savedInstanceState is null");
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

    private JSONObject constructJsonFromData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("surname", surname.getText().toString());
            jsonObject.put("name", name.getText().toString());
            jsonObject.put("birthdate", birthdate.getText().toString());
            jsonObject.put("number", number.getText().toString());
            jsonObject.put("mail", mail.getText().toString());
            jsonObject.put("hobbies", hobby.getText().toString());
        } catch (JSONException e) {
            Log.e("DisplayFragment", "Error creating JSON", e);
        }
        return jsonObject;
    }
    private void saveJsonToFile() {
        Context context = getContext();
        if(context == null) {
            Log.e("YourFragment", "Context is null, cannot save JSON");
            return;
        }

        // Construct your JSON object
        JSONObject jsonObject = constructJsonFromData();

        // Get the external files directory
        File  basePath = context.getExternalFilesDir(null);
        File file = new File(basePath , "test.json");
        Log.d("YourFragment", "Starting to write JSON to " + file.getAbsolutePath());
        Toast.makeText(getContext(), "Wrote JSON to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        // Write the JSON object to the file
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(jsonObject.toString()  );
            Log.d("YourFragment", "Successfully written JSON to " + file.getAbsolutePath());
        } catch (IOException e) {
            Log.e("YourFragment", "Error writing JSON to file", e);
        }
    }

    /**********  Saving data for presestance (when we leave the app)  *******/
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.v("debug presistancy","Starting to save...");
        super.onSaveInstanceState(outState);
        // Save the current state of your form or other data you need to preserve
        outState.putString("KEY_SURNAME", surname.getText().toString());
        outState.putString("KEY_NAME", name.getText().toString());
        outState.putString("KEY_BIRTHDATE", birthdate.getText().toString());
        outState.putString("KEY_NUMBER", number.getText().toString());
        outState.putString("KEY_MAIL", mail.getText().toString());
        outState.putString("KEY_HOBBIES", hobby.getText().toString());
        Log.v("debug presistancy","Values are saved...");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize your views here if not already done
        //Log.v("debug presistancy","Recovering the values...");
        if (savedInstanceState != null) {
            // Restore state here

            surname.setText(savedInstanceState.getString("KEY_SURNAME"));
            name.setText(savedInstanceState.getString("KEY_NAME"));
            birthdate.setText(savedInstanceState.getString("KEY_BIRTHDATE"));
            number.setText(savedInstanceState.getString("KEY_NUMBER"));
            mail.setText(savedInstanceState.getString("KEY_MAIL"));
            hobby.setText(savedInstanceState.getString("KEY_HOBBIES"));
        }
        //Log.v("debug presistancy","Values recoverd...");
    }
}