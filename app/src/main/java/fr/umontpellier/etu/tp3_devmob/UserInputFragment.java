package fr.umontpellier.etu.tp3_devmob;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserInputFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserInputFragment extends Fragment {

    private UserInputViewModel model;
    private boolean isSynchronousWithOutput = false;
    private final ArrayList<Integer> langList = new ArrayList<>();
    private final String[] hobbiesArray = new String[]{"Sport", "Musique", "Lecture"};

    ///////////////////

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserInputFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserInputFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserInputFragment newInstance(String param1, String param2) {
        UserInputFragment fragment = new UserInputFragment();
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
        View myView =  inflater.inflate(R.layout.fragment_user_input, container, false);

        this.setHintPlaceholder(myView, R.id.edit_text_surname, "Nguyen");
        this.setHintPlaceholder(myView, R.id.edit_text_name, "Tony");
        this.setHintPlaceholder(myView, R.id.edit_text_birthdate, "19/06/2000");
        this.setHintPlaceholder(myView, R.id.edit_text_number, "+33123456789");
        this.setHintPlaceholder(myView, R.id.edit_text_mail, "tony.nguyen@etu.umontpellier.fr");

        model = new ViewModelProvider(this).get(UserInputViewModel.class);
        //Log.v("debug",model.toString());

        // listening for input modification
        addTextChangedListener(R.id.edit_text_surname,myView,"surname");
        addTextChangedListener(R.id.edit_text_name,myView,"name");
        addTextChangedListener(R.id.edit_text_birthdate,myView,"birthdate");
        addTextChangedListener(R.id.edit_text_number,myView,"number");
        addTextChangedListener(R.id.edit_text_mail,myView,"mail");
        //addTextChangedListener(R.id.edit_hobby,myView,"mail");

        // button action
        myView.findViewById(R.id.submit_button).setOnClickListener(v -> {

            Toast.makeText(v.getContext(),"Form was submited",Toast.LENGTH_SHORT).show();

            // data transmission
            // bundle result will hold the data
            Bundle result = new Bundle();

            // put data
            putDataInsideBundle(result, (EditText) myView.findViewById(R.id.edit_text_name), "inputName");
            putDataInsideBundle(result, (EditText) myView.findViewById(R.id.edit_text_surname), "inputSurname");
            putDataInsideBundle(result, (EditText) myView.findViewById(R.id.edit_text_birthdate), "inputBirthdate");
            putDataInsideBundle(result, (EditText) myView.findViewById(R.id.edit_text_number), "inputNumber");
            putDataInsideBundle(result, (EditText) myView.findViewById(R.id.edit_text_mail), "inputMail");
            putDataInsideBundle(result, (TextView) myView.findViewById(R.id.edit_hobby), "inputHobbies");

            this.isSynchronousWithOutput = ((SwitchMaterial) myView.findViewById(R.id.switch_sync)).isChecked();
            result.putString("isSynchron", String.valueOf(this.isSynchronousWithOutput));
            // setting up to transmit ViewModel with LiveData for synchronisation form/output
            result.putSerializable("theModel",model);

            // send
            // notifying fragment manager of change
            getParentFragmentManager().setFragmentResult("requestKey", result);
        });

        // multiple choice selector
        this.set(myView);

        return myView;
    }

    // for a nice form
    private void setHintPlaceholder(View v, int r, String placeholder) {
        final EditText editText = (EditText) v.findViewById(r);
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

    /**
     * for a nice selection
     * Source : https://www.geeksforgeeks.org/how-to-implement-multiselect-dropdown-in-android/
     *
     * @param myView
     */
    private void set(View myView) {
        TextView textView = myView.findViewById(R.id.edit_hobby);

        // initialize selected language array
        boolean[] selectedHobbies = new boolean[hobbiesArray.length];

        textView.setOnClickListener(new View.OnClickListener() {
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
                        //TODO when change occur, notify ModelView
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
                        textView.setText(stringBuilder.toString());
                        Log.v("debug input","text="+textView.getText().toString());
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
                            textView.setText("");
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
        Log.v("debug input","textToSend="+textView.getText().toString());
        theDataHolder.putString(dataName, textView.getText().toString());
    }

    // when editing form, if this.isSynchronousWithOutput is true, notify the model
    private void addTextChangedListener(int id, View view, String aString) {
        ((EditText) view.findViewById(id)).addTextChangedListener(new TextWatcher() {
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
                    model.
                            get(aString).
                            postValue(((EditText) view.findViewById(id)).
                                    getText().
                                    toString());
                }
            }
        });
    }
}