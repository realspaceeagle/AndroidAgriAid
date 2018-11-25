package com.example.haran.agritec;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private TextView userName,userProfName,userStatus,userCountry,userGender,userRelation,userDOB;
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
        userStatus= (TextView)findViewById(R.id.my_profile_status);
        userCountry = (TextView)findViewById(R.id.my_profile_country);
        userGender = (TextView)findViewById(R.id.my_profile_gender);
        userRelation= (TextView)findViewById(R.id.my_profile_relationshipstatus);
        userDOB = (TextView)findViewById(R.id.my_profile_dob);
        userProfileImage =(CircleImageView) findViewById(R.id.my_profile_pic);
        viewlocationsettingsbutton=(Button) findViewById(R.id.my_profile_view_location);

        profileUserRef.addValueEventListener(new ValueEventListener() {
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



                    Picasso.get().load(myProfileImage).placeholder(R.drawable.profile).into(userProfileImage);
                    userName.setText("@"+myUserName);
                    userProfName .setText(myProfileName);
                    userStatus.setText(myProfilestatus);
                    userDOB.setText("DOB:"+myDOB);
                    userCountry.setText("Address:"+mycountry);
                    userGender.setText("Email:"+mygender);
                    userRelation.setText("Phone No:"+myrelationshipstatus );






                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }
}
