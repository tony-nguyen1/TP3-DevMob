package fr.umontpellier.etu.tp3_devmob;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserInputFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserInputFragment extends Fragment {

    private UserInputViewModel model;
    private boolean isSynchronousWithOutput = false;

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
        Log.v("debug",model.toString());
        EditText monEditText = myView.findViewById(R.id.edit_text_name);
        monEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //TODO si isSynchronized true, quand le text change, post la valuer
                Log.v("debug","after text of name changed");

                Log.v("debug isChecked", String.valueOf(UserInputFragment.this.isSynchronousWithOutput));
                if (UserInputFragment.this.isSynchronousWithOutput) {
                    Log.v("debug","sychrone");
                    Log.v("debug","posting change");
                    Log.v("debug",s.toString());
                    model.
                            getCurrentName().
                            postValue(((EditText) myView.findViewById(R.id.edit_text_name)).
                                    getText().
                                    toString());
                } else {

                }
                //TextView t = (TextView) myView.findViewById(R.id.nameText);
            }
        });

        // button action
        myView.findViewById(R.id.submit_button).setOnClickListener(v -> {

            //Toast.makeText(v.getContext(),"Form was submited",Toast.LENGTH_SHORT).show();

            // FIXME LiveData
            model.getCurrentName().postValue(((EditText) myView.findViewById(R.id.edit_text_name)).getText().toString());

            // data transmission
            // bundle result will hold the data
            Bundle result = new Bundle();

            // put data
            putDataInsideBundle(result, (EditText) myView.findViewById(R.id.edit_text_name), "inputName");
            putDataInsideBundle(result, (EditText) myView.findViewById(R.id.edit_text_surname), "inputSurname");
            putDataInsideBundle(result, (EditText) myView.findViewById(R.id.edit_text_birthdate), "inputBirthdate");
            putDataInsideBundle(result, (EditText) myView.findViewById(R.id.edit_text_number), "inputNumber");
            putDataInsideBundle(result, (EditText) myView.findViewById(R.id.edit_text_mail), "inputMail");
            putDataInsideBundle(result, (EditText) myView.findViewById(R.id.edit_text_mail), "inputMail");

            SwitchMaterial mySwitch = myView.findViewById(R.id.switch_sync);
            this.isSynchronousWithOutput = mySwitch.isChecked();
            result.putString("isSynchron", String.valueOf(this.isSynchronousWithOutput));
            // setting up to transmit ViewModel with LiveData for synchronisation form/output
            result.putSerializable("theModel",model);

            // send
            // notifying fragment manager of change
            getParentFragmentManager().setFragmentResult("requestKey", result);
        });

        return myView;
    }

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

    private void putDataInsideBundle(Bundle theDataHolder, EditText editText, String dataName) {
        theDataHolder.putString(dataName, editText.getText().toString());
    }
}