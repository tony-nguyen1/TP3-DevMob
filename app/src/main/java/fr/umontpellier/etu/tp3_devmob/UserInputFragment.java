package fr.umontpellier.etu.tp3_devmob;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserInputFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserInputFragment extends Fragment {

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

        // button action
        myView.findViewById(R.id.submit_button).setOnClickListener(v -> {

            //Toast.makeText(v.getContext(),"Form was submited",Toast.LENGTH_SHORT).show();

            // data transmission
            // bundle result will hold the data
            Bundle result = new Bundle();

            // put data
            putDataInsideBundle(result, (EditText) myView.findViewById(R.id.edit_text_name), "inputName");
            putDataInsideBundle(result, (EditText) myView.findViewById(R.id.edit_text_surname), "inputSurname");
            putDataInsideBundle(result, (EditText) myView.findViewById(R.id.edit_text_birthdate), "inputBirthdate");
            putDataInsideBundle(result, (EditText) myView.findViewById(R.id.edit_text_number), "inputNumber");
            putDataInsideBundle(result, (EditText) myView.findViewById(R.id.edit_text_mail), "inputMail");

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