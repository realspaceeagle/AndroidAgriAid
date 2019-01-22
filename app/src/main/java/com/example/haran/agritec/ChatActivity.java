package com.example.haran.agritec;

import android.content.Context;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private Toolbar chattolbar;
    private ImageButton SendMessageButton,SendImagefileButton;
    private EditText userMessageInput;

    private RecyclerView userMessageList;
    private final List<Messages> messagesList=new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessagesAdapter  messagesAdapter;

    private String messageReceiverID,messageReceiverName,messageSenderID, saveCurrentDate,saveCurrentTime;
    private TextView receiverName;
    private CircleImageView receiverProfileImage;
    private DatabaseReference  RootRef;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mauth =FirebaseAuth.getInstance();
        messageSenderID= mauth.getCurrentUser().getUid();

        RootRef = FirebaseDatabase.getInstance().getReference();

        messageReceiverID=getIntent().getExtras().get("visit_user_id").toString();
        messageReceiverName=getIntent().getExtras().get("userName").toString();


        InitializerFields();
        DisplayReceiverInfo();

        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
              SendMessage();
            }
        });

        FetchMessages();











    }

    private void FetchMessages()
    {
        RootRef.child("Messages").child(messageSenderID).child(messageReceiverID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        if(dataSnapshot.exists())
                        {

                            Messages messages =dataSnapshot.getValue(Messages.class);
                            messagesList.add(messages);
                            messagesAdapter.notifyDataSetChanged();



                        }

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




    }

    private void SendMessage() {

        String messageText=userMessageInput.getText().toString();
        if(TextUtils.isEmpty(messageText))
        {
            Toast.makeText(ChatActivity.this,"please type a message first",Toast.LENGTH_SHORT).show();

        }
        else
        {
            String message_sender_ref="Messages/"+messageSenderID+"/"+messageReceiverID;
            String message_receiver_ref="Messages/"+messageReceiverID+"/"+messageSenderID;

            DatabaseReference user_message_key = RootRef.child("Messages").child(messageSenderID)
                    .child(messageReceiverID).push();
            String messgae_push_id=user_message_key.getKey();

            Calendar calFordDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-YYYY");
            saveCurrentDate = currentDate.format(calFordDate.getTime());

            Calendar calFordTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm aa");
            saveCurrentTime = currentTime.format(calFordDate.getTime());

            Map messageTextBody = new HashMap();
            messageTextBody.put("message",messageText);
            messageTextBody.put("Time",saveCurrentTime);
            messageTextBody.put("date",saveCurrentDate);
            messageTextBody.put("type","text");
            messageTextBody.put("from",messageSenderID);


            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put(message_sender_ref+"/"+messgae_push_id , messageTextBody);
            messageBodyDetails.put(message_receiver_ref+"/"+messgae_push_id , messageTextBody);

            RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if(task.isSuccessful())
                    {
                        Toast.makeText(ChatActivity.this,"Message Send Successfully",Toast.LENGTH_SHORT).show();
                        userMessageInput.setText("");
                    }
                    else
                        {
                           String message=task.getException().getMessage();
                            Toast.makeText(ChatActivity.this,"Eror"+message,Toast.LENGTH_SHORT).show();
                            userMessageInput.setText("");
                        }
                        //userMessageInput.setText("");

                }
            });

        }

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
        SendImagefileButton=(ImageButton)findViewById(R.id.send_image_file_button);
        userMessageInput =(EditText) findViewById(R.id.input_message);


        messagesAdapter =new MessagesAdapter(messagesList);
        userMessageList =(RecyclerView) findViewById(R.id.message_list_users);
        linearLayoutManager=new LinearLayoutManager(this);
        userMessageList.setHasFixedSize(true);
        userMessageList.setLayoutManager(linearLayoutManager);
        userMessageList.setAdapter(messagesAdapter);




    }
}
