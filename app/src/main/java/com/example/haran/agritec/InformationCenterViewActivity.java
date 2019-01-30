package com.example.haran.agritec;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.wajahatkarim3.clapfab.ClapFAB;

import de.hdodenhof.circleimageview.CircleImageView;

public class InformationCenterViewActivity extends AppCompatActivity {

    private android.widget.ImageButton  AddNewPostButton;
    private RecyclerView postList;
    private Toolbar mToolbar;//home tool bar
    private RatingBar ratingbar;

    private FirebaseAuth mAuth;//firebase 1st
    private DatabaseReference UsersRef ,PostsRef,LikesRef, Informationcenterref,ratingbarref,Query,ratingbarrefnew;

    String currentUserID;
    Boolean Likechecker=false;
    RatingBar ratingBar;
    ClapFAB clapFAB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_center_view);

        mAuth =FirebaseAuth.getInstance();//firebase 1st
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
       Informationcenterref = FirebaseDatabase.getInstance().getReference().child("DiscussionForum");

        ratingbarref=FirebaseDatabase.getInstance().getReference().child("DiscussionForumRating");
        ratingbarrefnew=FirebaseDatabase.getInstance().getReference().child("DiscussionForumRating");



      //  queryrateref



        mToolbar =(Toolbar) findViewById(R.id.main_page_toolbar);

        setSupportActionBar(mToolbar);//setting up home tool bar
        getSupportActionBar().setTitle("Discussion Forum");//set title for action bar

      //  AddNewPostButton =(android.widget.ImageButton) findViewById(R.id.add_new_post_button);
    //   ratingbar=(RatingBar) findViewById(R.id.ratingBar);



        postList =(RecyclerView) findViewById(R.id.all_users_post_list);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this );
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);

        DisplayAllUserPosts();
        }

    private void DisplayAllUserPosts( ) {

        Query ServicesQuery =  Informationcenterref.orderByChild("Likes");


        FirebaseRecyclerAdapter<InformationCentergetset, InformationCenterViewActivity.PostViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<InformationCentergetset, InformationCenterViewActivity.PostViewHolder>
                        (
                                InformationCentergetset.class,
                                R.layout.information_center_layout,
                                InformationCenterViewActivity.PostViewHolder.class,
                                ServicesQuery

                        ) {
                    @Override
                    protected void populateViewHolder(InformationCenterViewActivity.PostViewHolder viewHolder, InformationCentergetset model, int position) {
                        final  String PostKey = getRef(position).getKey();



                        viewHolder.setFullname(model.getFullname());
                        viewHolder.setTime(model.getTime());
                        viewHolder.setDate(model.getDate());
                        viewHolder.setDescription(model.getDescription());
                        viewHolder.setProfileimage(model.getProfileimage());



                        viewHolder.setLikeButtonStatus(PostKey);


                        viewHolder.LikepostButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                Likechecker=true;

                                Informationcenterref .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot)
                                    {
                                        if(Likechecker.equals(true))
                                        {
                                            if (dataSnapshot.child(PostKey).hasChild("Likes")) {
                                                Long Like=(Long)dataSnapshot.child(PostKey).child("Likes").getValue();
                                                Like++;
                                                Informationcenterref .child(PostKey).child("Likes").setValue(Like);
                                               Informationcenterref.child(PostKey).child("Likes").setValue(Like);
                                                Likechecker = false;

                                            }
                                            else
                                            {
                                                //int Like=(int)dataSnapshot.child(PostKey).child("Likes").getValue();
                                              int  Like=1;

                                                Informationcenterref .child(PostKey).child("Likes").setValue(Like);
                                               // ratingbarref.child(PostKey).child("Likes").setValue(Like);
                                                Informationcenterref.child(PostKey).child("Likes").setValue(Like);
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



                        viewHolder.CommentPostButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                           public void onClick(View v)
                           {
                                Intent commentsIntent = new Intent (InformationCenterViewActivity.this,InformationCenterCommentActivity.class);
                                commentsIntent.putExtra( "PostKey",PostKey);
                                startActivity(commentsIntent);


                           }
                       });





                    }
                };
        postList.setAdapter(firebaseRecyclerAdapter);
    }


        public static class PostViewHolder extends RecyclerView.ViewHolder
        {
            View mView;
            ImageButton CommentPostButton;
            ImageButton LikepostButton;
            TextView DisplayNoOfLikes;
            long  countLikes,countrate;
            float rate;
            String currentUserId;
            DatabaseReference LikesRef,ratingbarref,ratingbarrefnew,Discussionref;
            RatingBar ratingBar;


            public PostViewHolder(View itemView) {
                super(itemView);
                mView= itemView;


           LikepostButton = (ImageButton) mView.findViewById(R.id.like_button);
           CommentPostButton = (ImageButton)mView.findViewById(R.id.comment_button);
                DisplayNoOfLikes =(TextView)mView.findViewById(R.id.display_no_of_likes);


               Discussionref=FirebaseDatabase.getInstance().getReference().child("DiscussionForum");
           currentUserId =FirebaseAuth.getInstance().getCurrentUser().getUid();

            }


            public void setLikeButtonStatus(final String PostKey)
            {
                Discussionref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.child(PostKey).hasChild("Likes"))
                        {
                        // countLikes = (int) dataSnapshot.child(PostKey).child("Likes").getValue();

                           countLikes= Long.parseLong( dataSnapshot.child(PostKey).child("Likes").getValue().toString()) ;

                            LikepostButton.setImageResource(R.drawable.like);
                            DisplayNoOfLikes.setText(Long.toString(countLikes)+(" Likes"));
                        }
                        else
                        {
                           // countLikes =(int )dataSnapshot.child(PostKey).child("Likes").getValue();
                            LikepostButton.setImageResource(R.drawable.dislike);
                            DisplayNoOfLikes.setText(Integer.toString(0)+(" Likes"));

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

        }




        private void SendUserToAgroshops(){

        Intent addNewPosstIntent = new Intent(InformationCenterViewActivity.this,InformationCentergetset.class);
        startActivity(addNewPosstIntent);

    }


}
