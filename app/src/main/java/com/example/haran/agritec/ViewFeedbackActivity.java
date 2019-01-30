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

public class ViewFeedbackActivity extends AppCompatActivity {
    private RecyclerView postList;
    private Toolbar mToolbar;//home tool bar
    private RatingBar ratingbar;


    private FirebaseAuth mAuth;//firebase 1st
    private DatabaseReference UsersRef ,PostsRef,LikesRef,feedbackref;

    String currentUserID;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feedback);


        mAuth =FirebaseAuth.getInstance();//firebase 1st
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        feedbackref = FirebaseDatabase.getInstance().getReference().child("Feedbacks and Complaints");





        mToolbar =(Toolbar) findViewById(R.id.main_page_toolbar);

        setSupportActionBar(mToolbar);//setting up home tool bar
        getSupportActionBar().setTitle("User Feedbacks");//set title for action bar



        postList =(RecyclerView) findViewById(R.id.all_users_post_list);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this );
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);

        DisplayAllUserPosts();
    }

    private void DisplayAllUserPosts( ) {

       // com.google.firebase.database.Query ServicesQuery = feedbackref.orderByChild("Likes");
        feedbackref = FirebaseDatabase.getInstance().getReference().child("Feedbacks and Complaints");


        FirebaseRecyclerAdapter<ViewFeedback, ViewFeedbackActivity.PostViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<ViewFeedback, ViewFeedbackActivity.PostViewHolder>
                        (
                                ViewFeedback.class,
                                R.layout.viewfeedback_layout,
                                ViewFeedbackActivity.PostViewHolder.class,
                                feedbackref
                        ) {
                    @Override
                    protected void populateViewHolder(ViewFeedbackActivity.PostViewHolder viewHolder,ViewFeedback model, int position) {
                        final  String PostKey = getRef(position).getKey();



                        viewHolder.setFullname(model.getName());
                        viewHolder.setEmail(model.getEmail());
                        viewHolder.setFeedback(model.getFeedback());
                        viewHolder.setComplaint(model.getComplaint());
                        viewHolder.setRating(model.getRating());
                      //  viewHolder.setProfileimage(model.getProfileimage());
                    }
                };
        postList.setAdapter(firebaseRecyclerAdapter);
    }



    public static class PostViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        TextView description,Complaint,feedback,name,Rating,email;
        String currentUserId;
        DatabaseReference Discussionref;



        public PostViewHolder(View itemView) {
            super(itemView);
            mView= itemView;



            Complaint =(TextView)mView.findViewById(R.id.feedback_complaint);
            feedback =(TextView)mView.findViewById(R.id.feedback);
            name =(TextView)mView.findViewById(R.id.feedback_user_name);
            Rating =(TextView)mView.findViewById(R.id.feedback_rating);
            email =(TextView)mView.findViewById(R.id.feedback_email);
         //   Discussionref= FirebaseDatabase.getInstance().getReference().child("DiscussionForum");
            currentUserId =FirebaseAuth.getInstance().getCurrentUser().getUid();

        }





        public void setFullname(String name)
        {
            TextView username =(TextView) mView.findViewById(R.id.feedback_user_name);
            username.setText(name);
        }


        public void setEmail(String email)
        {
            TextView PostTime =(TextView) mView.findViewById(R.id.feedback_email);
            PostTime.setText(email);
        }

        public void setFeedback(String feedback) {
            TextView PostDate = (TextView) mView.findViewById(R.id.feedback);
            PostDate.setText(feedback);
        }
        public void setComplaint(String complaint)
        {
            TextView PostDescription = (TextView) mView.findViewById(R.id.feedback_complaint);
            PostDescription.setText(complaint);
        }

        public void setRating(String rating)
        {
            TextView PostDescription = (TextView) mView.findViewById(R.id.feedback_rating);
            PostDescription.setText(rating);
        }

    }


}
