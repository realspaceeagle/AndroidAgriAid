package com.example.haran.agritec;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private TextView userName,userProfName,userAbout,useraddress,useremail,userphoneno,userDOB;
    private CircleImageView  userProfileImage;
    private Button viewlocationsettingsbutton;

    private DatabaseReference profileUserRef;
    private FirebaseAuth mAuth;

    private  String currentUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        mAuth =FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        profileUserRef= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

        userName = (TextView) findViewById(R.id.my_profile_username);
        userProfName = (TextView)findViewById(R.id.my_profile_fullname);
        userAbout= (TextView)findViewById(R.id.my_profile_status);
        useraddress = (TextView)findViewById(R.id.my_profile_country);
        useremail = (TextView)findViewById(R.id.my_profile_gender);
        userphoneno= (TextView)findViewById(R.id.my_profile_relationshipstatus);
        userDOB = (TextView)findViewById(R.id.my_profile_dob);
        userProfileImage =(CircleImageView) findViewById(R.id.my_profile_pic);
        viewlocationsettingsbutton=(Button) findViewById(R.id.my_profile_view_location);

        viewlocationsettingsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this,LocationShow.class);
                startActivity(i);


            }
        });

        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String myProfileImage =dataSnapshot.child("profileimage").getValue().toString();
                    String myUserName =dataSnapshot.child("username").getValue().toString();
                    String myProfileName =dataSnapshot.child("fullname").getValue().toString();
                    String myProfileabout =dataSnapshot.child("about").getValue().toString();
                    String myDOB =dataSnapshot.child("dob").getValue().toString();
                    String myaddress =dataSnapshot.child("address").getValue().toString();
                    String myemail =dataSnapshot.child("email").getValue().toString();
                    String myphoneno =dataSnapshot.child("phoneno").getValue().toString();



                    Picasso.get().load(myProfileImage).placeholder(R.drawable.profile).into(userProfileImage);
                    userName.setText("@"+myUserName);
                    userProfName .setText(myProfileName);
                    userAbout.setText(myProfileabout);
                    userDOB.setText("DOB:"+myDOB);
                    useraddress.setText("Address:"+myaddress);
                    useremail.setText("Email:"+myemail);
                    userphoneno.setText("Phone No:"+myphoneno );






                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }
}
