package edu.fsu.cs.homework5;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private LoginFragment loginFragment;
    UserContentProvider userContentProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userContentProvider = new UserContentProvider();
        // Init fragments
        loginFragment = new LoginFragment();

        // Open chooseFragment in Login Activity
        fragmentManager = getFragmentManager(); //getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.a_login_content, loginFragment,
                loginFragment.getClass().getName()).commit();
    }

}