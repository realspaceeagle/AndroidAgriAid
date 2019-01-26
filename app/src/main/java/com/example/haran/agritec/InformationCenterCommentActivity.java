package com.example.haran.agritec;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class InformationCenterCommentActivity extends AppCompatActivity {

    private RecyclerView CommentsList;
    private ImageButton farmercommentButton;
    private EditText CommentInputText;
    private String Post_Key,current_user_id;

    private DatabaseReference UsersRef,PostsRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_center_comment);


        Post_Key =getIntent().getExtras().get("PostKey").toString();

        mAuth = FirebaseAuth.getInstance();
        current_user_id =mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("DiscussionForum").child(Post_Key).child("comments");



        CommentsList = (RecyclerView) findViewById(R.id.farmer_comments_list);
        CommentsList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        CommentsList.setLayoutManager(linearLayoutManager);

        CommentInputText =(EditText)findViewById(R.id.farmer_comment_input);
        farmercommentButton = (ImageButton) findViewById(R.id.farmer_comment_button);

        farmercommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                UsersRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.exists())
                        {
                            String userName =dataSnapshot.child("username").getValue().toString();
                            ValidateComment(userName);
                            CommentInputText.setText("");


                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        }

    protected void  onStart()
    {
        super.onStart();
        FirebaseRecyclerAdapter<InformationCenterComment,InformationCenterCommentActivity.CommentsViewHolder> firebaseRecyclerAdapter
                =new FirebaseRecyclerAdapter<InformationCenterComment, InformationCenterCommentActivity.CommentsViewHolder>
                (
                        InformationCenterComment.class,
                        R.layout.all_comments_layout,
                        InformationCenterCommentActivity.CommentsViewHolder.class,
                        PostsRef

                )
        {
            @Override
            protected void populateViewHolder(InformationCenterCommentActivity.CommentsViewHolder viewHolder, InformationCenterComment model, int position)
            {
                viewHolder.setUsername(model.getUsername());
                viewHolder. setComment(model.getComment());
                viewHolder.setDate(model.getDate());
                viewHolder.setTime(model.getTime());

            }
        };

        CommentsList.setAdapter(firebaseRecyclerAdapter);


    }



    public  static class CommentsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public  CommentsViewHolder (View itemView)
        {
            super(itemView);
            mView = itemView;

        }

        public void setUsername(String username)
        {
            TextView myUserName =(TextView ) mView.findViewById(R.id.farmer_comment_username);
            myUserName.setText("@"+username+"  ");
        }
        public void setComment(String comment)
        {

            TextView myComment =(TextView ) mView.findViewById(R.id.farmer_comment_text);
            myComment.setText(comment);
        }

        public void setTime(String time)
        {
            TextView myTime =(TextView ) mView.findViewById(R.id.farmer_comment_time);
            myTime.setText("  Time:  "+time+"  ");

        }
        public void setDate(String date)
        {

            TextView myDate =(TextView ) mView.findViewById(R.id.farmer_comment_date);
            myDate.setText("Date:"+date);
        }




    }

    private void ValidateComment(String userName)
    {
        String  commentText = CommentInputText.getText().toString();
        if(TextUtils.isEmpty(commentText))
        {
            Toast.makeText(this,"please write text to comment",Toast.LENGTH_SHORT).show();
        }

        else
        {
            Calendar calFordDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-YYYY");
            final String saveCurrentDate = currentDate.format(calFordDate.getTime());



            Calendar calFordTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
            final String saveCurrentTime = currentTime.format(calFordDate.getTime());

            final String RandomKey =current_user_id+saveCurrentDate+saveCurrentTime;

            HashMap commentsMap =new HashMap();
            commentsMap.put("uid",current_user_id);
            commentsMap.put("comment",commentText);
            commentsMap.put("date",saveCurrentDate);
            commentsMap.put("time",saveCurrentTime);
            commentsMap.put("username",userName);

            PostsRef.child(RandomKey).updateChildren(commentsMap)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task)
                        {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(InformationCenterCommentActivity.this,"You have commented successfully",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(InformationCenterCommentActivity.this,"Error occured ,try again ...",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });


        }

    }
}
