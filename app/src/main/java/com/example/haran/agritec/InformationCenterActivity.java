package com.example.haran.agritec;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class InformationCenterActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ProgressDialog loadingBar;
    private Button UpdatePostButton, ViewAgropostButton;
    private EditText PostDescription;
    private String Description;
    private StorageReference PostsimagesReference;
    private DatabaseReference UsersRef, PostsRef,rateref;

    private String saveCurrentDate, saveCurrentTime, postRandomName, downloadurl, current_user_id, PostKey;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_center_new);
        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        PostsimagesReference = FirebaseStorage.getInstance().getReference();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

    //    PostKey=getIntent().getExtras().get("PostKey").toString();
       // rateref=FirebaseDatabase.getInstance().getReference().child(PostKey);



        PostsRef = FirebaseDatabase.getInstance().getReference().child("DiscussionForum");
        UpdatePostButton = (Button) findViewById(R.id.update_post_button);
        ViewAgropostButton = (Button) findViewById(R.id.view_post_button);
        PostDescription = (EditText) findViewById(R.id.post_description);


        loadingBar = new ProgressDialog(this);

        mToolbar = (Toolbar) findViewById(R.id.update_post_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Update Post");

        UpdatePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ValidatePostInfo();
            }
        });

        ViewAgropostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendUsertoInformationcenterview();
            }
        });
    }
    private void SendUsertoInformationcenterview() {
        Intent LoginIntent = new Intent(InformationCenterActivity.this,InformationCenterViewActivity.class);
        startActivity(LoginIntent);
    }

    private void ValidatePostInfo() {
        Description = PostDescription.getText().toString();
        if (TextUtils.isEmpty(Description)) {
            Toast.makeText(this, "Please say something about your Post...", Toast.LENGTH_SHORT).show();
        }

        else {
            loadingBar.setTitle("Add New Post");
            loadingBar.setMessage("Please wait,while we are updating your new Post...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);
            StoringDateToFirebase();
            }

    }

    private void StoringDateToFirebase() {

        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-YYYY");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calFordDate.getTime());
        postRandomName = saveCurrentDate + saveCurrentTime;

        SavingPostInformationToDatabase();

    }


    private void SavingPostInformationToDatabase() {
        UsersRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userfullName = dataSnapshot.child("fullname").getValue().toString();
                    String userProfileImage = dataSnapshot.child("profileimage").getValue().toString();

                    HashMap postsMap = new HashMap();
                    postsMap.put("uid", current_user_id);
                    postsMap.put("date", saveCurrentDate);
                    postsMap.put("time", saveCurrentTime);
                    postsMap.put("description", Description);
                    postsMap.put("profileimage", userProfileImage);
                    postsMap.put("fullname", userfullName);

                    PostsRef.child(current_user_id + postRandomName).updateChildren(postsMap)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {

                                    if (task.isSuccessful()) {
                                        //SendUserToMainActivity();
                                        Toast.makeText(InformationCenterActivity.this, "New Post is updated successfully", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                       //SendUsertoInformationCenterctivity();
                                    } else {
                                        Toast.makeText(InformationCenterActivity.this, "Error Occured while updating your post", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });


                }

            }




            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        private void SendUsertoInformationCenterctivity() {
//            Intent LoginIntent = new Intent(InformationCenterActivity.this,InformationCenterViewActivity.class);
//            startActivity(LoginIntent);
//
//        }

    }








}
