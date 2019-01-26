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

public class AdvertisementPost extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 71;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    StorageReference AdvImage;
    Task AdvRef;

    EditText title,msg,msg2,msg3,msg4;
    ImageView image;
    Button send;

    String title_value,msg_value,msg2_value,msg3_value,msg4_value,current_user_id,current_date_time,image_url;
    private FirebaseAuth mAuth;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement_post);

        title = findViewById(R.id.editText);
        msg = findViewById(R.id.editText2);
        msg2 = findViewById(R.id.editText3);
        msg3 = findViewById(R.id.editText4);
        msg4 = findViewById(R.id.editText5);

        image = findViewById(R.id.imageView2);
        send = findViewById(R.id.button4);

        mAuth = FirebaseAuth.getInstance();
        AdvImage = FirebaseStorage.getInstance().getReference();
        current_user_id = mAuth.getCurrentUser().getUid();

        Calendar c = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMMM-YYYY HH:mm:ss");
        current_date_time = dateFormat.format(c.getTime());


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(AdvertisementPost.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(AdvertisementPost.this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                }

                if (ContextCompat.checkSelfPermission(AdvertisementPost.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

                    chooseImage();
                }

            }
        });



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg4_value = msg4.getText().toString();
                title_value = title.getText().toString();
                msg_value = msg.getText().toString();
                msg2_value = msg2.getText().toString();
                msg3_value = msg3.getText().toString();
                ;



                if(!title_value.isEmpty()){
                    HashMap advsMap = new HashMap();
                    advsMap.put("Contact Phone Number",msg4_value);
                    advsMap.put("Title",title_value);
                    advsMap.put("Location",msg_value);
                    advsMap.put("Description",msg2_value);
                    advsMap.put("Price",msg3_value);



                    advsMap.put("image_url",image_url);

                    AdvRef = FirebaseDatabase.getInstance().getReference().child("Advertisement-details").child(current_user_id+"-"+current_date_time)
                            .updateChildren(advsMap)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {

                                    Toast.makeText(AdvertisementPost.this,"sucess",Toast.LENGTH_SHORT).show();
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
                                String filepp = im.getRealPathFromURI(AdvertisementPost.this,filePath);
                                Bitmap bitmapmodified = IamgeHandling.modifyOrientation(bitmap,filepp);

                                image.setImageBitmap(bitmapmodified);
                                uploadImage();
                            }
                            catch (IOException e)
                            {
                                Toast.makeText(AdvertisementPost.this,""+e,Toast.LENGTH_SHORT).show();
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

            AdvImage.child("Advertisement images/"+current_user_id+"-"+current_date_time)
            .putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            image_url = AdvImage.getRoot().toString();


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AdvertisementPost.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
