package com.example.haran.agritec;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Feedbacks extends AppCompatActivity {
    Float ratingNumber;
    private static final int PICK_IMAGE_REQUEST = 71;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    StorageReference FeedbackImage;
    Task FeedbackRef;
    RatingBar ratingBar;
    EditText title,msg,msg2,msg3;
    ImageView image;
    Button send;
    String rate;

    String title_value,msg_value,msg2_value,msg3_value,current_user_id,current_date_time,image_url;
    private FirebaseAuth mAuth;
    private Uri filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedbacks);

        title = findViewById(R.id.editText);
        msg = findViewById(R.id.editText2);
        msg2 = findViewById(R.id.editText3);
        msg3 = findViewById(R.id.editText4);
        ratingBar = findViewById(R.id.ratingBar);

        image = findViewById(R.id.imageView2);
        send = findViewById(R.id.button4);

        mAuth = FirebaseAuth.getInstance();
        FeedbackImage = FirebaseStorage.getInstance().getReference();
        current_user_id = mAuth.getCurrentUser().getUid();

        Calendar c = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMMM-YYYY HH:mm:ss");
        current_date_time = dateFormat.format(c.getTime());


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(Feedbacks.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(Feedbacks.this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                }

                if (ContextCompat.checkSelfPermission(Feedbacks.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

                    chooseImage();
                }

            }
        });



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title_value = title.getText().toString();
                msg_value = msg.getText().toString();
                msg2_value = msg2.getText().toString();
                msg3_value = msg3.getText().toString();

                double rate= (int) ratingBar.getRating();

                if(!title_value.isEmpty()){
                    HashMap FdMap = new HashMap();

                    FdMap.put("Name",title_value);
                    FdMap.put("Email",msg_value);
                    FdMap.put("Feedback",msg2_value);
                    FdMap.put("Complaint",msg3_value);
                    FdMap.put("Rating", String.valueOf(rate)+" rating(s)");




                    FdMap.put("image_url",image_url);

                    FeedbackRef = FirebaseDatabase.getInstance().getReference().child("Feedbacks and Complaints").child(current_user_id+"_"+current_date_time)
                            .updateChildren(FdMap)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {

                                    Toast.makeText(Feedbacks.this,"success",Toast.LENGTH_SHORT).show();
                                }
                            });

                }else{
                    title.setError("cannot be blank");
                }
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            IamgeHandling im = new IamgeHandling();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                String filepp = im.getRealPathFromURI(Feedbacks.this,filePath);
                Bitmap bitmapmodified = IamgeHandling.modifyOrientation(bitmap,filepp);

                image.setImageBitmap(bitmapmodified);
                uploadImage();
            }
            catch (IOException e)
            {
                Toast.makeText(Feedbacks.this,""+e,Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            FeedbackImage.child("Feedback images/"+current_user_id+"-"+current_date_time)
                    .putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            image_url = FeedbackImage.getRoot().toString();


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Feedbacks.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }

    }

}
