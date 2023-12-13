package com.example.moviehunter.register;

import android.app.AlertDialog;
import android.content.Intent;
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

import com.example.moviehunter.LoginActivity;
import com.example.moviehunter.MainActivity;
import com.example.moviehunter.R;
import com.example.moviehunter.home.HomeFragment;
import com.example.moviehunter.register.RegistrationFragment;
import com.example.moviehunter.model.db.DBHelper;

public class LoginFragment extends Fragment {
    EditText edtUsername;
    EditText edtPassword;
    Button btnLogin;
    TextView txtRegister;
    View v;
    DBHelper dbh;
    Intent intent;
    //SharedPreferences sharedPreferences;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_login, container, false);
        edtUsername = v.findViewById(R.id.edtUserName);
        edtPassword = v.findViewById(R.id.edtPassword);
        btnLogin = v.findViewById(R.id.btnLogin);
        dbh = new DBHelper(getContext());
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                Boolean result = dbh.registerTableHandler.checkUser(user, password,dbh);
                if(result == true) {
//                    Fragment homeFragment = new HomeFragment();
//                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                    transaction.replace(R.id.nav_host_fragment_activity_tab, homeFragment ); // give your fragment container id in first parameter
//                    transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
//                    transaction.commit();
                    intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("USER", user);
                    startActivity(intent);
                }
                else{
                        //Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Error");
                        builder.setMessage("Login Credentials Invalid");
                        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                        builder.create().show();
                }
            }
        });

        txtRegister = v.findViewById(R.id.txtRegister);
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment registerFragment = new RegistrationFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.nav_host_fragment_activity_tab, registerFragment ); // give your fragment container id in first parameter
                transaction.replace(R.id.fragment_container_view, registerFragment);
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });
        // Inflate the layout for this fragment
        return v;
    }
}