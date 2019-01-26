package com.example.haran.agritec;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AgroServiceClickPostActivity extends AppCompatActivity {

    private ImageView postImage;
    private TextView postDescription;
    private Button DeletePostButton,EditPostButton;
    private DatabaseReference ClickPostRef;

    private FirebaseAuth mAuth;

    private String PostKey, currentUserID ,databaseUserID ,Description,image,location,itemname,price,Offers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agro_service_click_post);
        mAuth=FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();


        PostKey=getIntent().getExtras().get("PostKey").toString();
        ClickPostRef = FirebaseDatabase.getInstance().getReference().child("AgroShops").child(PostKey);


        postImage=(ImageView) findViewById(R.id.click_post_image);
        postDescription=(TextView) findViewById(R.id.click_post_description);
        DeletePostButton=(Button) findViewById(R.id.delete_post_button);
        EditPostButton=(Button) findViewById(R.id.edit_post_button);

        DeletePostButton.setVisibility(View.INVISIBLE);
        EditPostButton.setVisibility(View.INVISIBLE);


        ClickPostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                if(dataSnapshot.exists()) {
                    Description = dataSnapshot.child("description").getValue().toString();
                    location=  dataSnapshot.child("location").getValue().toString();
                    itemname=  dataSnapshot.child("Item_name").getValue().toString();
                   price=  dataSnapshot.child("Price").getValue().toString();
                   Offers=  dataSnapshot.child("Offers").getValue().toString();
                    image = dataSnapshot.child("postimage").getValue().toString();

                    databaseUserID = dataSnapshot.child("uid").getValue().toString();


                    postDescription.setText(Description);
                    Picasso.get().load(image).into(postImage);


                    if (currentUserID.equals(databaseUserID)) {

                        DeletePostButton.setVisibility(View.VISIBLE);
                        EditPostButton.setVisibility(View.VISIBLE);
                    }

                    EditPostButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            EditCurrentPost(Description);


                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {


            }
        });

        DeletePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DeleteCurrentPost();
            }
        });

    }

    private void EditCurrentPost(String description)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(AgroServiceClickPostActivity.this);
        builder.setTitle("Edit Post");

        LinearLayout layout = new LinearLayout(AgroServiceClickPostActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText inputField1 = new EditText(AgroServiceClickPostActivity.this);
        inputField1.setText("description: "+Description);
        layout.addView(inputField1);

        final EditText inputField2 = new EditText(AgroServiceClickPostActivity.this);
        inputField2.setText("Location:  "+location);
        layout.addView(inputField2);

        final EditText inputField3 = new EditText(AgroServiceClickPostActivity.this);
        inputField3.setText("Itemname:  "+itemname);
        layout.addView(inputField3);

        final EditText inputField4 = new EditText(AgroServiceClickPostActivity.this);
        inputField4.setText("Price:  "+price);
        layout.addView(inputField4);

        final EditText inputField5 = new EditText(AgroServiceClickPostActivity.this);
        inputField5.setText("Offers:  "+Offers);
        layout.addView(inputField5);



        builder.setView(layout);


        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                ClickPostRef.child("description").setValue(inputField1.getText().toString());
                ClickPostRef.child("location").setValue(inputField2.getText().toString());
                ClickPostRef.child("Item_name").setValue(inputField3.getText().toString());
                ClickPostRef.child("Price").setValue(inputField4.getText().toString());
                ClickPostRef.child("Offers").setValue(inputField5.getText().toString());
                Toast.makeText(AgroServiceClickPostActivity.this,"Post has been updated successsfully",Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        Dialog dialog=builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.holo_green_dark);

    }

    private void DeleteCurrentPost()
    {
        ClickPostRef.removeValue();
        SendUserToMainActivity();

        Toast.makeText(this,"Post has been deleted",Toast.LENGTH_SHORT).show();

    }
    private void SendUserToMainActivity()
    {
        Intent mainIntent =new Intent(AgroServiceClickPostActivity.this,AgroServiceViewActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
