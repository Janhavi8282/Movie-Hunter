package com.example.moviehunter.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.moviehunter.MainActivity;
import com.example.moviehunter.R;
import com.example.moviehunter.model.Movie;
import com.example.moviehunter.model.Users;
import com.example.moviehunter.model.db.DBHelper;
import com.example.moviehunter.register.RegistrationFragment;

import java.util.List;

public class ProfileDetailsFragment extends Fragment {
    View v;
    TextView txtName, txtEmail, txtPhone, txtGender;
    ImageView imgEdit, imgProfile;
    Intent intent;
    DBHelper dbHelper;
    String strUserName;
    SharedPreferences sharedPref1;

    Users user;

    public ProfileDetailsFragment() {
        // Required empty public constructor
    }

//    public ProfileDetailsFragment(Users _user) { user = _user; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_profile_details, container, false);

        sharedPref1 = getActivity().getSharedPreferences("login_details", Context.MODE_PRIVATE);
        txtName = (TextView) v.findViewById(R.id.txtName);
        txtEmail = (TextView) v.findViewById(R.id.txtEmail);
        txtPhone = (TextView) v.findViewById(R.id.txtPhone);
        txtGender = (TextView) v.findViewById(R.id.txtGender);
        imgEdit = (ImageView) v.findViewById(R.id.imgEdit);
        imgProfile = (ImageView) v.findViewById(R.id.imgProfile);

        dbHelper = new DBHelper(getContext());

        intent = getActivity().getIntent();
        strUserName = intent.getStringExtra("USER");

        setup(strUserName);
        return v;
    }

    private void setup(String strUserName) {
        if (user == null)
            user = dbHelper.registerTableHandler.getUsers(dbHelper.getReadableDatabase(), strUserName);
        txtName.setText(user.getName());
        txtEmail.setText(user.getEmail());
        txtGender.setText(user.getGender());
        txtPhone.setText(user.getPhone());
        if (user.getIcon() != null)
            imgProfile.setImageBitmap(user.getIcon());

        // edit action
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment profileFragment = new ProfileFragment(user);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.nav_host_fragment_activity_tab, registerFragment ); // give your fragment container id in first parameter
                transaction.replace(R.id.nav_host_fragment_activity_tab, profileFragment);
                transaction.setReorderingAllowed(true);
//                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
                MainActivity main = (MainActivity)getActivity();
                main.displayBottom(false);
            }
        });
    }
}