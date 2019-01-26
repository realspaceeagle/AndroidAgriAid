package com.example.haran.agritec;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

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
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class FarmersPage extends AppCompatActivity {

    private static final int Gallery_Pick = 1 ;
    private Toolbar mToolbar;
    private ProgressDialog loadingBar;

    private ImageButton SelectPostImage;
    private Button UpdatePostButton,viewpost;
    private EditText PostDescription;

    private StorageReference PostsimagesReference;
    private DatabaseReference UsersRef,PostsRef;

    private String saveCurrentDate,saveCurrentTime,postRandomName,downloadurl,current_user_id,Description;
    private FirebaseAuth mAuth;

    private Uri ImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmers_page);

        SelectPostImage=(ImageButton) findViewById(R.id.select_post_image);
        UpdatePostButton=(Button) findViewById(R.id.update_post_button);
        viewpost = findViewById(R.id.view_farmers_post);
        PostDescription=(EditText) findViewById(R.id.post_description);

        loadingBar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        PostsimagesReference = FirebaseStorage.getInstance().getReference();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("FarmersPage");


        SelectPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OpenGallery();
            }
        });

        UpdatePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ValidatePostInfo();
            }
        });

        viewpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FarmersPage.this,FarmersPostView.class);
                startActivity(intent);
            }
        });

    }


    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,Gallery_Pick);

    }

    private void ValidatePostInfo()
    {
        Description = PostDescription.getText().toString();
        if(ImageUri==null)
        {
            Toast.makeText(this,"Please select post image...",Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(Description))
        {
            Toast.makeText(this,"Please say something about your image...",Toast.LENGTH_SHORT).show();

        }
        else{
            loadingBar.setTitle("Add New Post");
            loadingBar.setMessage("Please wait,while we are updating your new Post...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            StoringImageToFirebaseStorage();

        }

    }

    //-----
    private void StoringImageToFirebaseStorage() {

        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-YYYY");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calFordDate.getTime());

        postRandomName = saveCurrentDate+saveCurrentTime;


        StorageReference filePath = PostsimagesReference.child("Post Images").child(ImageUri.getLastPathSegment()+postRandomName+".jpg");
        filePath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                if(task.isSuccessful())
                {
                    downloadurl = task.getResult().getDownloadUrl().toString();
                    Toast.makeText(FarmersPage.this,"image Uploaded successfully to storage ...",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    SavingPostInformationToDatabase();

                }
                else{
                    String message = task.getException().getMessage();
                    Toast.makeText(FarmersPage.this,"Error occured:",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SavingPostInformationToDatabase()
    {
        UsersRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Toast.makeText(FarmersPage.this,"plll",Toast.LENGTH_SHORT).show();
                    String userfullName=dataSnapshot.child("fullname").getValue().toString();
                    String userProfileImage =dataSnapshot.child("profileimage").getValue().toString();

                    HashMap postsMap = new HashMap();
                    postsMap.put("uid",current_user_id);
                    postsMap.put("date",saveCurrentDate);
                    postsMap.put("time",saveCurrentTime);
                    postsMap.put("description",Description);
                    postsMap.put("postimage",downloadurl);
                    postsMap.put("profileimage",userProfileImage);
                    postsMap.put("fullname",userfullName);

                    PostsRef.child(current_user_id+postRandomName).updateChildren(postsMap)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {

                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(FarmersPage.this,"New Post is updated successfully",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                    else
                                    {
                                        Toast.makeText(FarmersPage.this,"Error Occured while updating your post",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });


                }

                Toast.makeText(FarmersPage.this,"bhhh",Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Gallery_Pick && resultCode ==RESULT_OK && data!=null)
        {
            ImageUri =data.getData();
            SelectPostImage.setImageURI(ImageUri);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home)
        {
               }
        return super.onOptionsItemSelected(item);
    }


}
