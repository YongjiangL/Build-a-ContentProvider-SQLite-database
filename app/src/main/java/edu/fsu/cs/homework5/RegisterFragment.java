package edu.fsu.cs.homework5;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class RegisterFragment extends Fragment{

    Button loginButton;
    Button registerButton;
    private EditText empId, empName, email, accessCode, confirmCode;
    private RadioButton maleButton, femaleButton;
    private Spinner department;
    private CheckBox allowed;
    FragmentManager fragmentManager;
    LoginFragment loginFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_layout, container, false);
        empId = (EditText) v.findViewById(R.id.emp_id);
        empName = (EditText) v.findViewById(R.id.emp_name);
        email = (EditText) v.findViewById(R.id.email);
        accessCode = (EditText) v.findViewById(R.id.access_code);
        confirmCode = (EditText) v.findViewById(R.id.confirm_code);
        maleButton = (RadioButton) v.findViewById(R.id.male);
        femaleButton = (RadioButton) v.findViewById(R.id.female);
        department = (Spinner) v.findViewById(R.id.department);
        allowed = (CheckBox) v.findViewById(R.id.allowed);

        fragmentManager = getActivity().getFragmentManager();

        loginButton = (Button) v.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginFragment = new LoginFragment();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.a_login_content, loginFragment,
                        loginFragment.getClass().getName());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        registerButton = (Button) v.findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               register(view);
            }
        });
        return v;
    }

    public void register(View view) {
        // check if any fields are empty
        boolean flag = false;
        if (empId.getText().equals("")) {
            flag = true;
            empId.setBackgroundColor(Color.RED);
        }
        if (empName.getText().equals("")) {
            flag = true;
            empName.setBackgroundColor(Color.RED);
        }
        if (email.getText().equals("")) {
            flag = true;
            email.setBackgroundColor(Color.RED);
        }
        //Check if email is valid
        else if (!email.getText().toString().contains("@") || !email.getText().toString().contains(".")) {
            flag = true;
            //email.setBackgroundColor(Color.RED);
            Toast.makeText(getActivity(), "Error: invalid email address", Toast.LENGTH_SHORT).show();
        }
        if (accessCode.getText().equals("")) {
            flag = true;
            accessCode.setBackgroundColor(Color.RED);
        }
        if (confirmCode.getText().equals("")) {
            flag = true;
            confirmCode.setBackgroundColor(Color.RED);
        }
        if(maleButton.isChecked() == false && femaleButton.isChecked() == false) {
            flag = true;
        }
        if (flag){
            Toast.makeText(getActivity(), "Error: one or more fields are empty.", Toast.LENGTH_SHORT).show();
        }

        // check if access code and confirm access code match
        boolean codeMatch = false;
        if (!accessCode.getText().toString().equals(confirmCode.getText().toString())) {
            confirmCode.setBackgroundColor(Color.RED);
            Toast.makeText(getActivity(), "Error: confirm access code does not match.", Toast.LENGTH_SHORT).show();
        }

        ContentValues values = new ContentValues();
        values.put(UserContentProvider.empId, empId.getText().toString());
        values.put(UserContentProvider.password, accessCode.getText().toString());
        values.put(UserContentProvider.name, empName.getText().toString());
        values.put(UserContentProvider.email, email.getText().toString());
        values.put(UserContentProvider.department, department.getSelectedItem().toString());
        values.put(UserContentProvider.gender, "Male");
        UserContract.addUser(getActivity(), values);
    }
}
