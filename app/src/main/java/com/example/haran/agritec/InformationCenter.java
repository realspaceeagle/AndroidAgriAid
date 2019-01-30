package com.example.haran.agritec;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class InformationCenter extends AppCompatActivity {

    private DatabaseReference UsersRef ,PostsRef,LikesRef, CommentRef,UnLikeRef;
    Boolean Likechecker=false;
    String currentUserID;
    private FirebaseAuth mAuth;//firebase 1st
    private RecyclerView postList;
    private Toolbar mToolbar;
    private android.widget.ImageButton  AddNewPostButton;
    String comment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_center);



        postList =(RecyclerView) findViewById(R.id.rvAnimals);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this );
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);

        mToolbar =(Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);//setting up home tool bar
        getSupportActionBar().setTitle("Information Center");//set title for action bar

        AddNewPostButton =(android.widget.ImageButton) findViewById(R.id.add_new_post_button);

        mAuth = FirebaseAuth.getInstance();//firebase 1st
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef=FirebaseDatabase.getInstance().getReference().child("Posts");
        LikesRef=FirebaseDatabase.getInstance().getReference().child("InformationcenterLikes").child("Likes");
        UnLikeRef=FirebaseDatabase.getInstance().getReference().child("InformationcenterLikes").child("Unlikes");
        CommentRef = FirebaseDatabase.getInstance().getReference().child("Comments");

        AddNewPostButton.setOnClickListener(new View.OnClickListener (){
            @Override
            public void onClick(View v)
            {
                Intent addNewPosstIntent = new Intent(InformationCenter.this,PostActivity.class);
                startActivity(addNewPosstIntent);
            }
        });
        DisplayAllUserPosts();

    }
    private void DisplayAllUserPosts()
    {

        FirebaseRecyclerAdapter<Posts,PostViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Posts, PostViewHolder>
                        (
                                Posts.class,
                                R.layout.cell_information_center,
                                PostViewHolder.class,
                                PostsRef
                        )
                {
                    @Override
                    protected void populateViewHolder(final PostViewHolder viewHolder, Posts model, int position)
                    {
                        final  String  PostKey= getRef(position).getKey();

                        viewHolder.setFullname(model.getFullname());
                        viewHolder.setTime(model.getTime());
                        viewHolder.setDate(model.getDate());
                        viewHolder.setDescription(model.getDescription());
                        viewHolder.setDescription2(model.getDescription2());
                        viewHolder.setProfileimage(model.getProfileimage());
                        viewHolder.setPostimage(model.getPostimage());


                        viewHolder.setLikeButtonStatus(PostKey);
                        viewHolder.setUnLikeButtonStatus(PostKey);

//                        viewHolder.setComments(PostKey);

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                Intent clickPostIntent = new Intent (InformationCenter.this,ClickPostActivity.class);
                                clickPostIntent.putExtra( "PostKey",PostKey);
                                startActivity(clickPostIntent);


                            }
                        });




                        viewHolder.comment_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if(TextUtils.isEmpty(viewHolder.comment_text.getText().toString())){
                                    Toast.makeText(getApplicationContext(), "No comments", Toast.LENGTH_SHORT).show();
                                }else {
                                    CommentRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot)
                                        {
                                            CommentRef.child(PostKey).child(currentUserID).setValue(viewHolder.comment_text.getText().toString());
                                            Toast.makeText(getApplicationContext(), "Comment saved", Toast.LENGTH_SHORT).show();
                                            viewHolder.comment_text.setText("");

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        });


                        viewHolder.LikepostButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                Likechecker=true;

                                LikesRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot)
                                    {
                                        if(Likechecker.equals(true))
                                        {
                                            if (dataSnapshot.child(PostKey).hasChild(currentUserID)) {
                                                LikesRef.child(PostKey).child(currentUserID).removeValue();
                                                Likechecker = false;
                                            }
                                            else
                                            {
                                                LikesRef.child(PostKey).child(currentUserID).setValue(true);
                                                Likechecker = false;
                                            }
                                        }


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        });




                        viewHolder.UnLikepostButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                Likechecker=true;

                                UnLikeRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot)
                                    {
                                        if(Likechecker.equals(true))
                                        {
                                            if (dataSnapshot.child(PostKey).hasChild(currentUserID)) {
                                                UnLikeRef.child(PostKey).child(currentUserID).removeValue();
                                                Likechecker = false;
                                            }
                                            else
                                            {
                                                UnLikeRef.child(PostKey).child(currentUserID).setValue(true);
                                                Likechecker = false;
                                            }
                                        }


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        });


                    }
                };
        postList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        ImageButton LikepostButton,UnLikepostButton,CommentPostButton;
        TextView DisplayNoOfLikes,DisplayNoOfUnLikes, dis_comment;
        int  countLikes;
        int  countUnLikes;
        String currentUserId;
        DatabaseReference LikesRef,CommentsRef,UnLikesRef,InfoRef;
        LinearLayout comment_section , dis_com_lay;
        EditText comment_text;
        Button comment_btn;


        public PostViewHolder(View itemView) {
            super(itemView);
            mView= itemView;

            LikepostButton = (ImageButton)mView.findViewById(R.id.like_button);
            UnLikepostButton = (ImageButton)mView.findViewById(R.id.unlike_button);
            CommentPostButton = (ImageButton)mView.findViewById(R.id.comment_button);
            DisplayNoOfLikes =(TextView)mView.findViewById(R.id.display_no_of_likes);
            DisplayNoOfUnLikes =(TextView)mView.findViewById(R.id.display_no_of_unlikes);
            comment_section = (LinearLayout) mView.findViewById(R.id.comment_section);
            comment_text = (EditText) mView.findViewById(R.id.comment_text);
            //title = mView.findViewById(R.id.textView9);
            comment_btn = (Button) mView.findViewById(R.id.comment_btn);
            dis_comment = (TextView) mView.findViewById(R.id.in_comment_text);
            dis_com_lay = (LinearLayout) mView.findViewById(R.id.in_comment_section);

            LikesRef = FirebaseDatabase.getInstance().getReference().child("InformationcenterLikes").child("Likes");
            UnLikesRef= FirebaseDatabase.getInstance().getReference().child("InformationcenterLikes").child("Unlikes");

            CommentsRef = FirebaseDatabase.getInstance().getReference().child("Comments");
            InfoRef = FirebaseDatabase.getInstance().getReference().child("Information Center");
            currentUserId =FirebaseAuth.getInstance().getCurrentUser().getUid();

            CommentPostButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    comment_section.setVisibility(View.VISIBLE);

                }
            });
        }

        public void setLikeButtonStatus(final String PostKey)
        {
            LikesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    if(dataSnapshot.child(PostKey).hasChild(currentUserId))
                    {
                        countLikes =(int )dataSnapshot.child(PostKey).getChildrenCount();
                       // countUnLikes =(int )dataSnapshot.child(PostKey).getChildrenCount();
                        LikepostButton.setImageResource(R.drawable.likeinfo);
                       // UnLikepostButton.setImageResource(R.drawable.dislikeinfo);
                        DisplayNoOfLikes.setText(Integer.toString(countLikes)+(" Upvotes"));
                       // DisplayNoOfUnLikes.setText(Integer.toString(countUnLikes)+(" Downvotes"));
                }
                    else
                    {
                        countLikes =(int )dataSnapshot.child(PostKey).getChildrenCount();
                     //   countUnLikes =(int )dataSnapshot.child(PostKey).getChildrenCount();
                       // LikepostButton.setImageResource(R.drawable.upvote);
                        LikepostButton.setImageResource(R.drawable.upvote);
                      // UnLikepostButton.setImageResource(R.drawable.downvote);
                        DisplayNoOfLikes.setText(Integer.toString(countLikes)+(" Upvotes"));
                     //   DisplayNoOfUnLikes.setText(Integer.toString(countUnLikes)+(" Downvotes"));

                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


        public void setUnLikeButtonStatus(final String PostKey)
        {
            UnLikesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    if(dataSnapshot.child(PostKey).hasChild(currentUserId))
                    {
                        //countLikes =(int )dataSnapshot.child(PostKey).getChildrenCount();
                        countUnLikes =(int )dataSnapshot.child(PostKey).getChildrenCount();
                       // LikepostButton.setImageResource(R.drawable.likeinfo);
                        UnLikepostButton.setImageResource(R.drawable.dislikeinfo);
                        //DisplayNoOfLikes.setText(Integer.toString(countLikes)+(" Upvotes"));
                        DisplayNoOfUnLikes.setText(Integer.toString(countUnLikes)+(" Downvotes"));
                    }
                    else
                    {
                        //countLikes =(int )dataSnapshot.child(PostKey).getChildrenCount();
                          countUnLikes =(int )dataSnapshot.child(PostKey).getChildrenCount();
                        // LikepostButton.setImageResource(R.drawable.upvote);
                       // UnLikepostButton.setImageResource(R.drawable.dislikeinfo);
                          UnLikepostButton.setImageResource(R.drawable.downvote);
                       // DisplayNoOfLikes.setText(Integer.toString(countLikes)+(" Upvotes"));
                          DisplayNoOfUnLikes.setText(Integer.toString(countUnLikes)+(" Downvotes"));

                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        public void setComments(final String PostKey){
            CommentsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    if(dataSnapshot.child(PostKey).hasChild(currentUserId))
                    {
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            dis_com_lay.setVisibility(View.VISIBLE);
                            dis_comment.setText(snapshot.child(currentUserId).getValue().toString());
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        public void setFullname(String fullname)
        {
            TextView username =(TextView) mView.findViewById(R.id.post_user_name);
            username.setText(fullname);
        }
        public void setProfileimage(String profileimage)
        {
            CircleImageView image =(CircleImageView) mView.findViewById(R.id.post_profile_image);
            Picasso.get().load(profileimage).into(image);
        }

        public void setTime(String time)
        {
            TextView PostTime =(TextView) mView.findViewById(R.id.post_time);
            PostTime.setText("  "+time);
        }

        public void setDate(String date) {
            TextView PostDate = (TextView) mView.findViewById(R.id.post_date);
            PostDate.setText("  "+date);
        }
        public void setDescription(String description)
        {
            TextView PostDescription = (TextView) mView.findViewById(R.id.post_description);
            PostDescription.setText(description);
        }

        public void setDescription2(String description2)
        {
            TextView PostDescription2 = (TextView) mView.findViewById(R.id.post_description2);
            PostDescription2.setText(description2);
        }

        public void setPostimage(String postimage)
        {
            ImageView Postimage =(ImageView) mView.findViewById(R.id.post_image);
            Picasso.get().load(postimage).into(Postimage);
        }

    }

}