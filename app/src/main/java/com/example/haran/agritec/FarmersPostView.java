package com.example.haran.agritec;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FarmersPostView extends AppCompatActivity {

    private FirebaseAuth mAuth;//firebase 1st
    private DatabaseReference UsersRef ,PostsRef,LikesRef;

    String currentUserID;
    private RecyclerView postList;
    Boolean Likechecker=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmers_post_view);

        mAuth =FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef=FirebaseDatabase.getInstance().getReference().child("FarmersPage");
        LikesRef=FirebaseDatabase.getInstance().getReference().child("FarmerLikes");

        postList =(RecyclerView) findViewById(R.id.all_users_post_list);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this );
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);

        DisplayAllUserPosts();

    }

    private void DisplayAllUserPosts()
    {
        FirebaseRecyclerAdapter<Posts,MainActivity.PostViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Posts, MainActivity.PostViewHolder>
                        (
                                Posts.class,
                                R.layout.all_posts_layout,
                                MainActivity.PostViewHolder.class,
                                PostsRef
                        )
                {
                    @Override
                    protected void populateViewHolder(MainActivity.PostViewHolder viewHolder, Posts model, int position)
                    {
                        final  String  PostKey= getRef(position).getKey();

                        viewHolder.setFullname(model.getFullname());
                        viewHolder.setTime(model.getTime());
                        viewHolder.setDate(model.getDate());
                        viewHolder.setDescription(model.getDescription());
                        viewHolder.setProfileimage(model.getProfileimage());
                        viewHolder.setPostimage(model.getPostimage());


                        viewHolder.setLikeButtonStatus(PostKey);

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                Intent clickPostIntent = new Intent (FarmersPostView.this,ClickPostActivity.class);
                                clickPostIntent.putExtra( "PostKey",PostKey);
                                startActivity(clickPostIntent);


                            }
                        });

                        viewHolder.setLikeButtonStatus(PostKey);

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                Intent clickPostIntent = new Intent (FarmersPostView.this,ClickPostActivity.class);
                                clickPostIntent.putExtra( "PostKey",PostKey);
                                startActivity(clickPostIntent);


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


                    }
                };
        postList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        View mView;
        ImageButton LikepostButton, CommentPostButton;
        TextView DisplayNoOfLikes;
        int countLikes;
        String currentUserId;
        DatabaseReference LikesRef;


        public PostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            LikepostButton = (ImageButton) mView.findViewById(R.id.like_button);
            CommentPostButton = (ImageButton) mView.findViewById(R.id.comment_button);
            DisplayNoOfLikes = (TextView) mView.findViewById(R.id.display_no_of_likes);

            LikesRef =FirebaseDatabase.getInstance().getReference().child("FarmerLikes");
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
                        LikepostButton.setImageResource(R.drawable.like);
                        DisplayNoOfLikes.setText(Integer.toString(countLikes)+(" Likes"));
                    }
                    else
                    {
                        countLikes =(int )dataSnapshot.child(PostKey).getChildrenCount();
                        LikepostButton.setImageResource(R.drawable.dislike);
                        DisplayNoOfLikes.setText(Integer.toString(countLikes)+(" Likes"));

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }



        public void setFullname(String fullname) {
            TextView username = (TextView) mView.findViewById(R.id.post_user_name);
            username.setText(fullname);
        }

        public void setProfileimage(String profileimage) {
            CircleImageView image = (CircleImageView) mView.findViewById(R.id.post_profile_image);
            Picasso.get().load(profileimage).into(image);
        }

        public void setTime(String time) {
            TextView PostTime = (TextView) mView.findViewById(R.id.post_time);
            PostTime.setText("  " + time);
        }

        public void setDate(String date) {
            TextView PostDate = (TextView) mView.findViewById(R.id.post_date);
            PostDate.setText("  " + date);
        }

        public void setDescription(String description) {
            TextView PostDescription = (TextView) mView.findViewById(R.id.post_description);
            PostDescription.setText(description);
        }

        public void setPostimage(String postimage) {
            ImageView Postimage = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.get().load(postimage).into(Postimage);
        }
    }
}
