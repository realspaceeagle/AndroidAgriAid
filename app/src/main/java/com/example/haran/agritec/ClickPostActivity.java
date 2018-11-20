package com.example.haran.agritec;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ClickPostActivity extends AppCompatActivity {

private ImageView postImage;
private TextView postDescription;
private Button DeletePostButton,EditPostButton;
private DatabaseReference ClickPostRef;


private String PostKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_post);


        PostKey=getIntent().getExtras().get("PostKey").toString();
        ClickPostRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(PostKey);


        postImage=(ImageView) findViewById(R.id.click_post_image);
        postDescription=(TextView) findViewById(R.id.click_post_description);
        DeletePostButton=(Button) findViewById(R.id.delete_post_button);
        EditPostButton=(Button) findViewById(R.id.edit_post_button);

        ClickPostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                String description = dataSnapshot.child("description").getValue().toString();
                String image  = dataSnapshot.child("postimage").getValue().toString();

                postDescription.setText(description);
                Picasso.get().load(image).into(postImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {


            }
        });

    }
}
