package edu.fsu.cs.homework5;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class LoginFragment extends Fragment{

    Button loginButton;
    Button registerButton;
    private EditText empId, empName, email, accessCode, confirmCode;
    private RadioButton maleButton, femaleButton;
    private Spinner department;
    private CheckBox allowed;
    FragmentManager fragmentManager;
    RegisterFragment registerFragment;

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
                login(view);
            }
        });

        registerButton = (Button) v.findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerFragment = new RegisterFragment();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.a_login_content, registerFragment,
                        registerFragment.getClass().getName());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        return v;
    }

    public void reset(View view) {
        empId.setText("");
        empName.setText("");
        email.setText("");
        accessCode.setText("");
        confirmCode.setText("");
        maleButton.setChecked(false);
        femaleButton.setChecked(false);
        allowed.setChecked(false);
    }

    public void login(View view) {
        // check if any fields are empty
        boolean flag = false;
        if (empId.getText().equals("")) {
            flag = true;
            empId.setBackgroundColor(Color.RED);
        }
        if (accessCode.getText().equals("")) {
            flag = true;
            accessCode.setBackgroundColor(Color.RED);
        }

        // check if employee id exists
        if(!flag && UserContract.checkUser(getActivity(), empId.getText().toString(), accessCode.getText().toString())){
            // created a bundle to add the username and password
            Bundle user = new Bundle();
            user.putString(UserContentProvider.empId, empId.getText().toString());
            user.putString(UserContentProvider.password, accessCode.getText().toString());
            Intent signIn = new Intent(getActivity(), HomeActivity.class);
            // sending the user with the intent
            signIn.putExtras(user);
            signIn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(signIn);
        }
        else {
            empId.setBackgroundColor(Color.RED);
            Toast.makeText(getActivity(), "Error: no employee ID match.", Toast.LENGTH_SHORT).show();
        }
    }


    public void setupLayout(View view) {

//        resetButton.setOnClickListener( new View.OnClickListener() {
//            public void onClick(View v){
//                // Toast.makeText(MainActivity.this, "Reset clicked", Toast.LENGTH_SHORT).show();
//                empId.setText("");
//                empName.setText("");
//                email.setText("");
//                accessCode.setText("");
//                confirmCode.setText("");
//                maleButton.setChecked(false);
//                femaleButton.setChecked(false);
//                checkBox.setChecked(false);
//                // reset colors of text views back to black
//                empIdTextView.setTextColor(Color.BLACK);
//                confirmAccessCode.setTextColor(Color.BLACK);
//                empNameTextView.setTextColor(Color.BLACK);
//                radioGroupTextView.setTextColor(Color.BLACK);
//                emailTextView.setTextColor(Color.BLACK);
//                accessCodeTextView.setTextColor(Color.BLACK);
//            }
//        });

    }
}
