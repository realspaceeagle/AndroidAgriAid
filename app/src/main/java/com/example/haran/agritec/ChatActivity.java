package com.example.haran.agritec;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private Toolbar chattolbar;
    private ImageButton SendMessageButton,SendImagefileButton;
    private EditText userMessageInput;
    private RecyclerView userMessageList;
    private String messageReceiverID,messageReceiverName;
    private TextView receiverName;
    private CircleImageView receiverProfileImage;
    private DatabaseReference  RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        RootRef = FirebaseDatabase.getInstance().getReference();

        messageReceiverID=getIntent().getExtras().get("visit_user_id").toString();
        messageReceiverName=getIntent().getExtras().get("userName").toString();


        InitializerFields();
        DisplayReceiverInfo();
    }

    private void DisplayReceiverInfo() {

       receiverName.setText(messageReceiverName);
       RootRef.child("Users").child(messageReceiverID).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {

               if(dataSnapshot.exists())
               {
                   final String profileImage =dataSnapshot.child("profileimage").getValue().toString();
                   Picasso.get().load(profileImage).into(receiverProfileImage);

               }


           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });

    }

    private void InitializerFields() {
        chattolbar =(Toolbar) findViewById(R.id.chat_bar_layout);
        setSupportActionBar(chattolbar);

        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater LayoutInflater=(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view=LayoutInflater.inflate(R.layout.chat_custom_bar,null);
        actionBar.setCustomView(action_bar_view);


        receiverName=(TextView) findViewById(R.id.custom_profile_name);
        receiverProfileImage = (CircleImageView)findViewById(R.id.custom_profile_image);
        SendMessageButton =(ImageButton)findViewById(R.id.send_message_button);
        SendMessageButton =(ImageButton)findViewById(R.id.send_image_file_button);
        userMessageInput =(EditText) findViewById(R.id.input_message);



    }
}
