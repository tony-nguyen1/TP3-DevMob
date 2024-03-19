package fr.umontpellier.etu.tp3_devmob;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //UserInputFragment myFragment = new UserInputFragment();
//        fragmentTransaction.add(,myFragment);
        fragmentTransaction.commit();

        //getSupportFragmentManager().beginTransaction()
        //        .setReorderingAllowed(true)
          //      .add(R.id.myLayout, UserInputFragment.class, savedInstanceState)
            //    .commit();

    }
}