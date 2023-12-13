package com.example.moviehunter.profile;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.moviehunter.MainActivity;
import com.example.moviehunter.R;
import com.example.moviehunter.model.Users;
import com.example.moviehunter.model.db.DBHelper;
import com.example.moviehunter.register.LoginFragment;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    View v;
    EditText edtName, edtEmail, edtPhone;
    RadioGroup rgGender;
    RadioButton rdMale, rdFemale, rdOther;
    ImageView imgProfilePic;
    Button btnSubmit, btnBack;
    Intent cameraIntent;
    Bitmap photo;
    Users user;

    String strName, strEmail,strPhone, strGender;
    DBHelper dbh;
    public ProfileFragment(Users _user) {
        // Required empty public constructor
        user = _user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile, container, false);
        edtName = (EditText) v.findViewById(R.id.edtName);
        edtEmail = (EditText) v.findViewById(R.id.edtEmail);
        edtPhone = (EditText) v.findViewById(R.id.edtPhone);
        rgGender = (RadioGroup) v.findViewById(R.id.rgGender);
        rdMale = (RadioButton) v.findViewById(R.id.rdMale);
        rdFemale = (RadioButton) v.findViewById(R.id.rdFemale);
        rdOther = (RadioButton) v.findViewById(R.id.rdOther);
        imgProfilePic = (ImageView) v.findViewById(R.id.imgProfilePic);
        btnSubmit = (Button) v.findViewById(R.id.btnSubmit);
        btnBack = (Button) v.findViewById(R.id.btnBack);
        dbh = new DBHelper(getContext());

        edtName.setText(user.getName());
        edtEmail.setText(user.getEmail());
        edtPhone.setText(user.getPhone());

        imgProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionAndOpenCamera();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strName = edtName.getText().toString().trim();
                strEmail = edtEmail.getText().toString().trim();
                strPhone = edtPhone.getText().toString().trim();

                if (rdMale.isChecked()) {
                    strGender = rdMale.getText().toString();
                } else if (rdFemale.isChecked()) {
                    strGender = rdFemale.getText().toString();
                } else {
                    strGender = rdOther.getText().toString();
                }

                user.setName(strName);
                user.setEmail(strEmail);
                user.setPhone(strPhone);
                user.setGender(strGender);
                user.setImage(String.valueOf(photo));
                user.setIcon(photo);

//                Picasso.get()
//                        .load(user.getIcon())
//                        .into(imgProfilePic);

                imgProfilePic.setImageBitmap(user.getIcon());

                user.setUser(true);
                boolean val = dbh.registerTableHandler.updateUsers(dbh.getWritableDatabase(), user);

                if(val) {
                   Toast.makeText(getContext(), "Update User Successfull", Toast.LENGTH_SHORT).show();

                }else{
                   Toast.makeText(getContext(),"Update Failed",Toast.LENGTH_SHORT).show();
                }

                backToProfileDetail();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToProfileDetail();
            }
        });
        return v;

    }

    private void backToProfileDetail() {
        Fragment detail = new ProfileDetailsFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.nav_host_fragment_activity_tab, registerFragment ); // give your fragment container id in first parameter
        transaction.replace(R.id.nav_host_fragment_activity_tab, detail);
        transaction.setReorderingAllowed(true);
//        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
        MainActivity main = (MainActivity)getActivity();
        main.displayBottom(true);
    }

    private void checkPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 5);
        } else {
            takePhoto();
        }
    }

    private void takePhoto() {
        cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startForResult.launch(cameraIntent);
    }

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result != null && result.getResultCode() == RESULT_OK) {
                if (result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    photo = (Bitmap) extras.get("data");
                    imgProfilePic.setImageBitmap(photo);
                }
            }
        }
    });
}