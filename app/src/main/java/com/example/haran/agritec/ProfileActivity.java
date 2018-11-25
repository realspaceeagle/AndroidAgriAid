package com.example.haran.agritec;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private TextView userName,userProfName,userStatus,userCountry,userGender,userRelation,userDOB;
    private CircleImageView  userProfileImage;
    private Button viewlocationsettingsbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userName = (TextView) findViewById(R.id.my_profile_username);
        userProfName = (TextView)findViewById(R.id.my_profile_fullname);
        userStatus= (TextView)findViewById(R.id.my_profile_status);
        userCountry = (TextView)findViewById(R.id.my_profile_country);
        userGender = (TextView)findViewById(R.id.my_profile_gender);
        userRelation= (TextView)findViewById(R.id.my_profile_relationshipstatus);
        userDOB = (TextView)findViewById(R.id.my_profile_dob);
        userProfileImage =(CircleImageView) findViewById(R.id.my_profile_pic);
        viewlocationsettingsbutton=(Button) findViewById(R.id.my_profile_view_location)






    }
}
