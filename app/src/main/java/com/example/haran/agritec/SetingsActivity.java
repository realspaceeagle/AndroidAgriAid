package com.example.haran.agritec;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetingsActivity extends AppCompatActivity {

    private Toolbar mToolabr;

    private EditText userName,userProfName,userStatus,userCountry,userGender,userRelation,userDOB;
    private Button UpdateAccountsettingbutton,locationsettingsbutton;

    private CircleImageView userProfImage;

    private DatabaseReference SettingsuserRef;
    private FirebaseAuth mAuth;

    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setings);


        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        SettingsuserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);



        mToolabr=(Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(mToolabr);
        getSupportActionBar().setTitle("Account Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        userName = (EditText)findViewById(R.id.settings_username);
        userProfName = (EditText)findViewById(R.id.settings_profile_full_name);
        userStatus= (EditText)findViewById(R.id.settings_status);
        userCountry = (EditText)findViewById(R.id.settings_country);
        userGender = (EditText)findViewById(R.id.settings_gender);
        userRelation= (EditText)findViewById(R.id.settings_relationship_status);
        userDOB = (EditText)findViewById(R.id.settings_dob);

        userProfImage =(CircleImageView) findViewById(R.id.settings_profile_image);

        UpdateAccountsettingbutton=(Button) findViewById(R.id.update_account_settings_buttons);
       locationsettingsbutton=(Button) findViewById(R.id.settings_location);

       SettingsuserRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot)
           {
               if(dataSnapshot.exists())
               {
                   String myProfileImage =dataSnapshot.child("profileimage").getValue().toString();
                   String myUserName =dataSnapshot.child("username").getValue().toString();
                   String myProfileName =dataSnapshot.child("fullname").getValue().toString();
                   String myProfilestatus =dataSnapshot.child("status").getValue().toString();
                   String myDOB =dataSnapshot.child("dob").getValue().toString();
                   String mycountry =dataSnapshot.child("country").getValue().toString();
                   String mygender =dataSnapshot.child("gender").getValue().toString();
                   String myrelationshipstatus =dataSnapshot.child("relationshipstatus").getValue().toString();

                   Picasso.get().load(myProfileImage).placeholder(R.drawable.profile).into(userProfImage);
                   userName.setText(myUserName);
                   userProfName .setText(myProfileName);
                   userStatus.setText(myProfilestatus);
                   userDOB.setText(myDOB);
                   userCountry.setText(mycountry);
                   userGender.setText(mygender);
                   userRelation.setText(myrelationshipstatus );

               }

           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });


    }
}
