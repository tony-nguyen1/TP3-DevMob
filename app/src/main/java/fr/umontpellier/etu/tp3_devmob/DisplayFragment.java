package fr.umontpellier.etu.tp3_devmob;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                LinearLayout myLayot = (LinearLayout) myView.findViewById(R.id.displayLayout);

                addToView(myLayot,bundle,"inputSurname",R.id.surnameText);
                //addToView(myLayot,bundle,"inputName");
                addToView(myLayot,bundle,"inputBirthdate",R.id.birthdateText);
                addToView(myLayot,bundle,"inputNumber",R.id.numberText);
                addToView(myLayot,bundle,"inputMail",R.id.mailText);
                //addToView(myLayot,bundle,"isSynchron");

                model = (UserInputViewModel) bundle.getSerializable("theModel");


                assert model != null;
                Log.v("debug",model.toString());

                // Create the observer which updates the UI.
                final Observer<String> nameObserver = new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable final String newName) {

                        Log.v("debug","display has observed a change");
                        // Update the UI, in this case, a TextView.
                        //nameTextView.setText(newName);
                        TextView myText = myView.findViewById(R.id.nameText);

                        //myText.setText(bundle.getString("inputName"));
                        /*myText.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
*/
                        //myLayot.removeAllViews();
                        //myLayot.addView(myText);

                        Log.v("debug current display",myText.getText().toString());
                        Log.v("debug display wanted",myText.getText().toString());

//                        addToView(myLayot, bundle, "inputName", R.id.nameText);
                        myText.setText(newName);

                    }
                };

                // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
                model.getCurrentName().observe(getViewLifecycleOwner(), nameObserver);
            }
        });

        return myView;
    }

    // add programatically a TextView to linearLayout by using data from a bundle
    private void addToView(LinearLayout linearLayout, Bundle theDataHolder, String dataName, int idTextView) {
        TextView myText = myView.findViewById(idTextView);//new TextView(linearLayout.getContext());

        myText.setText(theDataHolder.getString(dataName));
        //myText.setId(View.generateViewId());
        myText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        //linearLayout.addView(myText);
    }
}