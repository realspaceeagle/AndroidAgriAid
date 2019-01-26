package com.example.haran.agritec;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalProfileActivity extends AppCompatActivity {

    private TextView userName,userProfName,userAbout,useraddress,useremail,userphoneno,userDOB;
    private CircleImageView userProfileImage;
    private Button viewlocationsettingsbutton,SendFriendRequestbutton,DeclineFriendRequestbutton;

    private DatabaseReference FriendRequestRef,UsersRef,FriendsRef;
    private FirebaseAuth mAuth;
    private  String senderUserId,reciverUserId,current_state;
    private String saveCurrentDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile);
        mAuth =FirebaseAuth.getInstance();
        reciverUserId =getIntent().getExtras().get("visit_user_id").toString();
        UsersRef= FirebaseDatabase.getInstance().getReference().child("Users");
         senderUserId=mAuth.getCurrentUser().getUid();
         FriendRequestRef=FirebaseDatabase.getInstance().getReference().child("FriendRequest");
         FriendsRef = FirebaseDatabase.getInstance().getReference().child("Friends");


        InitializeFields();

        UsersRef.child(reciverUserId).addValueEventListener(new ValueEventListener() {
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


                    MaintainanceofButton();



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        DeclineFriendRequestbutton.setVisibility(View.INVISIBLE);
        DeclineFriendRequestbutton.setEnabled(false);


        if(!senderUserId.equals(reciverUserId))
        {
          SendFriendRequestbutton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                 SendFriendRequestbutton.setEnabled(false);
                 if(current_state.equals("not_friends"))
                 {
                   SendFriendRequestToPerson();  
                 }
                 if(current_state.equals("request_sent"))
                  {
                      CancelFriendrequest();
                  }

                  if(current_state.equals("request_received"))
                  {
                      AcceptFriendrequest();
                  }

                  if(current_state.equals("friends"))
                  {
                      Unfriend();
                  }


              }
          }) ;

        }
        else
        {

            DeclineFriendRequestbutton.setVisibility(View.INVISIBLE);
            SendFriendRequestbutton.setVisibility(View.INVISIBLE);
        }

    }

    private void Unfriend()
    {
        FriendsRef.child(senderUserId).child(reciverUserId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                           FriendsRef.child(reciverUserId).child(senderUserId)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                SendFriendRequestbutton.setEnabled(true);
                                                current_state="not_friends";
                                                SendFriendRequestbutton.setText("Send Friend Request");


                                                DeclineFriendRequestbutton.setVisibility(View.INVISIBLE);
                                                DeclineFriendRequestbutton.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });




    }

    private void AcceptFriendrequest()
    {
        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-YYYY");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        FriendsRef.child(senderUserId).child(reciverUserId).child("date").setValue(saveCurrentDate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            FriendsRef.child(reciverUserId).child(senderUserId).child("date").setValue(saveCurrentDate)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                FriendRequestRef.child(senderUserId).child(reciverUserId)
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task)
                                                            {
                                                                if(task.isSuccessful())
                                                                {
                                                                    FriendRequestRef.child(reciverUserId).child(senderUserId)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task)
                                                                                {
                                                                                    if(task.isSuccessful())
                                                                                    {
                                                                                        SendFriendRequestbutton.setEnabled(true);
                                                                                        current_state="friends";
                                                                                        SendFriendRequestbutton.setText("Unfriend");


                                                                                        DeclineFriendRequestbutton.setVisibility(View.INVISIBLE);
                                                                                        DeclineFriendRequestbutton.setEnabled(false);
                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });



                                            }


                                        }
                                    });



                        }


                    }
                });



    }

    private void CancelFriendrequest()
    {

        FriendRequestRef.child(senderUserId).child(reciverUserId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            FriendRequestRef.child(reciverUserId).child(senderUserId)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                SendFriendRequestbutton.setEnabled(true);
                                                current_state="not_friends";
                                                SendFriendRequestbutton.setText("Send Friend Request");


                                                DeclineFriendRequestbutton.setVisibility(View.INVISIBLE);
                                                DeclineFriendRequestbutton.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });




    }

    private void MaintainanceofButton()
    {
        FriendRequestRef.child(senderUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.hasChild(reciverUserId))
                        {
                            String  request_type=dataSnapshot.child(reciverUserId).child("request_type").getValue().toString();
                             if(request_type.equals("sent"))
                             {
                                 current_state="request_sent";
                                 SendFriendRequestbutton.setText("Cancel Friend request");
                                 DeclineFriendRequestbutton.setVisibility(View.INVISIBLE);
                                 DeclineFriendRequestbutton.setEnabled(false);
                             }

                             else if(request_type.equals("received"))
                             {
                              current_state="request_received";
                              SendFriendRequestbutton.setText("Accept friend request");
                              DeclineFriendRequestbutton.setVisibility(View.VISIBLE);
                              DeclineFriendRequestbutton.setEnabled(true);

                             }
                        }


                        else{
                            FriendsRef.child(senderUserId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot)
                                        {
                                            if(dataSnapshot.hasChild(reciverUserId))
                                            {
                                                current_state="friends";
                                                SendFriendRequestbutton.setText("Unfriend");
                                                DeclineFriendRequestbutton.setVisibility(View.INVISIBLE);
                                                DeclineFriendRequestbutton.setEnabled(false);



                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {

                    }
                });

    }

    private void SendFriendRequestToPerson() {

        FriendRequestRef.child(senderUserId).child(reciverUserId)
                .child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                          FriendRequestRef.child(reciverUserId).child(senderUserId)
                                  .child("request_type").setValue("received")
                                  .addOnCompleteListener(new OnCompleteListener<Void>() {
                                      @Override
                                      public void onComplete(@NonNull Task<Void> task)
                                      {
                                          if(task.isSuccessful())
                                          {
                                              SendFriendRequestbutton.setEnabled(true);
                                              current_state="request_sent";
                                              SendFriendRequestbutton.setText("Cancel Friend request");


                                              DeclineFriendRequestbutton.setVisibility(View.INVISIBLE);
                                              DeclineFriendRequestbutton.setEnabled(false);
                                          }
                                      }
                                  });
                        }
                    }
                });

        }

    private void InitializeFields() {


        userName = (TextView) findViewById(R.id.Person_profile_username);
        userProfName = (TextView)findViewById(R.id.Person_profile_fullname);
        userAbout= (TextView)findViewById(R.id.Person_profile_status);
        useraddress = (TextView)findViewById(R.id.Person_profile_country);
        useremail = (TextView)findViewById(R.id.Person_profile_gender);
        userphoneno= (TextView)findViewById(R.id.Person_profile_relationshipstatus);
        userDOB = (TextView)findViewById(R.id.Person_profile_dob);
        userProfileImage =(CircleImageView) findViewById(R.id.Person_profile_pic);
        viewlocationsettingsbutton=(Button) findViewById(R.id.Person_profile_view_location);
        SendFriendRequestbutton=(Button) findViewById(R.id.Person_send_friend_request_button);
        DeclineFriendRequestbutton=(Button) findViewById(R.id.Person_decline_friend_request_button);

        current_state="not_friends";

        }
}
