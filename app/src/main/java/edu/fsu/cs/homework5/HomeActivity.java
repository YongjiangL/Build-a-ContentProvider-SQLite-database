package edu.fsu.cs.homework5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    TextView empId;
    TextView empName;
    EditText password;
    Button showButton;
    TextView email;
    TextView department;
    ListView listView;

    int transform = 0;
    UserContentProvider userContentProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userContentProvider = new UserContentProvider();
        empId = findViewById(R.id.home_emp_id);
        empName = findViewById(R.id.home_emp_name);
        password = findViewById(R.id.home_password);
        showButton = findViewById(R.id.show_password);
        email = findViewById(R.id.home_email);
        department = findViewById(R.id.home_department);

        listView = findViewById(R.id.register_users);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String eid = bundle.getString(UserContentProvider.empId);
        displayUser(eid);
        displayOtherUser(eid);
    }

    public void displayUser(String eid) {
        Cursor cursor = UserContract.queryOneUser(this, eid);
        if (cursor != null && cursor.moveToFirst()) {
            empId.setText(cursor.getString(0));
            empName.setText(cursor.getString(1));
            password.setText(cursor.getString(2));
            email.setText(cursor.getString(3));
            department.setText(cursor.getString(4));
        }
        cursor.close();
    }

    public void displayOtherUser(String eid) {
        Cursor cursor = UserContract.queryOtherUsers(this, eid);

        if (cursor.getCount() > 0) {
            ArrayList<Map<String,String>> list = new ArrayList<>();
            int ind = 0;
            while (cursor.moveToNext()) {
                HashMap<String, String> item = new HashMap<>();
                item.put(UserContentProvider.email, cursor.getString(1));
                item.put(UserContentProvider.lastlogin, cursor.getString(2));
                Log.i("tag", cursor.getString(1));
                Log.i("tag", cursor.getString(2));
                list.add(item);
                ind++;
            }
            String[] mListColumns = new String[] {
                    UserContentProvider.email,
                    UserContentProvider.lastlogin
            };
            int[] mListItems = new int[]{android.R.id.text1, android.R.id.text2}; //new int[] {R.id.list_email, R.id.list_lastlogin};

            SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_list_item_2, mListColumns, mListItems);
//            SimpleCursorAdapter mCursorAdapter = new SimpleCursorAdapter(this, R.layout.users_list_view, cursor, mListColumns, mListItems, 0);
            listView.setAdapter(adapter);
        }
        cursor.close();
    }

    // overriding on create options menu to show menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id) {
            case R.id.logout_item:
                break;
            case R.id.delete_item:
                UserContract.deleteUser(this, empId.getText().toString());
                break;
        }
        backToMainActivity();
        return true;
    }

    public void backToMainActivity() {
       Intent intent = new Intent(this, MainActivity.class);
       startActivity(intent);
    }

    public void showPassword(View view) {
        if (transform%2==0) {
            showButton.setText("HIDE PASSWORD");
            password.setTransformationMethod(null);
            transform = 1;
        }
        else {
            showButton.setText("SHOW PASSWORD");
            password.setTransformationMethod(new PasswordTransformationMethod());
            transform = 0;
        }
    }
}