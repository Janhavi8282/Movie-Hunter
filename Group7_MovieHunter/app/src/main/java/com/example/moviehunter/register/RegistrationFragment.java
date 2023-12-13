package com.example.moviehunter.register;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviehunter.R;
import com.example.moviehunter.model.Users;
import com.example.moviehunter.model.db.DBHelper;
import com.google.android.material.navigation.NavigationView;

public class RegistrationFragment extends Fragment {
    View v;
    EditText edtUsername;
    EditText edtPassword;
    EditText edtConfirmPassword;
    Button btnRegister;
    TextView txtLogin;
    NavigationView navView;
    SharedPreferences sharedPreferences;
    DBHelper dbh;

    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_registration, container, false);
        dbh = new DBHelper(getContext());

        edtUsername = v.findViewById(R.id.edtUserName);
        edtPassword = v.findViewById(R.id.edtPassword);
        edtConfirmPassword = v.findViewById(R.id.edtConfirmPassword);

        btnRegister = v.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String confirm = edtConfirmPassword.getText().toString().trim();

                //check if password and confirm password is same
                if(password.equals(confirm))
                {
                    Users user = new Users();
                    user.setName(userName);
                    user.setPassword(password);
                    user.setUser(true);
                    boolean val = dbh.registerTableHandler.insertUsers(dbh.getWritableDatabase(), user);
                    if(val) {
                        Toast.makeText(getContext(), "Registration Successfull", Toast.LENGTH_SHORT).show();
                        Fragment loginFragment = new LoginFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                        transaction.replace(R.id.nav_host_fragment_activity_tab, loginFragment ); // give your fragment container id in first parameter
                        transaction.replace(R.id.fragment_container_view, loginFragment);
                        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                        transaction.commit();
                        //Intent intent = new Intent(getContext(), MainActivity.class);
                        //startActivity(intent);*/
                    }else{
                        Toast.makeText(getContext(),"Registration Failed",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getContext(),"Password and confirm password should be same",Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtLogin = v.findViewById(R.id.txtLogin);
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //move from one fragment to another
                Fragment loginFragment = new LoginFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                //transaction.replace(R.id.nav_host_fragment_activity_tab, loginFragment ); // give your fragment container id in first parameter
                transaction.replace(R.id.fragment_container_view, loginFragment);

                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
                //Intent registerIntent = new Intent(getContext(),MainActivity.class);
                //startActivity(registerIntent);
            }
        });
        // Inflate the layout for this fragment
        return v;
    }
}