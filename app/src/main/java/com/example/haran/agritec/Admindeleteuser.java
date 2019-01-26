package com.example.haran.agritec;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Admindeleteuser extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageButton SearchButton;
    private EditText SearchInputText;
    private String current_user_id;

    private RecyclerView SearchResultList;

    private DatabaseReference allUsersDatabaseRef,ClickPostRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admindeleteuser);
        allUsersDatabaseRef =  FirebaseDatabase.getInstance().getReference().child("Users");
        ClickPostRef = FirebaseDatabase.getInstance().getReference().child("Users");
        current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
     //   Toast.makeText(this, "" + current_user_id, Toast.LENGTH_SHORT).show();


        mToolbar = (Toolbar) findViewById(R.id.find_friends_app_bar_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Users");


        SearchResultList =(RecyclerView) findViewById(R.id.search_result_list);
        SearchResultList.setHasFixedSize(true);
        SearchResultList.setLayoutManager(new LinearLayoutManager(this));

        SearchButton =(ImageButton) findViewById(R.id.search_friends_people_button);
        SearchInputText=(EditText) findViewById(R.id.search_box_input);



        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String searchboxInput =SearchInputText.getText().toString();
                SearchPeopleAndFriends( searchboxInput);
            }
        });
    }
    private void SearchPeopleAndFriends(String  searchboxInput)
    {
        Toast.makeText(this,"Searching",Toast.LENGTH_LONG).show();

        Query searchPeopleandFriendsQuery =allUsersDatabaseRef.orderByChild("fullname")
                .startAt(searchboxInput).endAt(searchboxInput+ "\uf8ff");

        FirebaseRecyclerAdapter<FindFriends,FindFriendsActivity.FindFriendsViewViewHolder> firebaseRecyclerAdapter
                =new FirebaseRecyclerAdapter<FindFriends, FindFriendsActivity.FindFriendsViewViewHolder>
                (
                        FindFriends.class,
                        R.layout.all_users_display_layout,
                        FindFriendsActivity.FindFriendsViewViewHolder.class,
                        searchPeopleandFriendsQuery

                )
        {
            @Override
            protected void populateViewHolder(FindFriendsActivity.FindFriendsViewViewHolder viewHolder, FindFriends model, final int position)
            {
                viewHolder.setFullname(model.getFullname());
                viewHolder.setabout(model.getStatus());
                viewHolder.setProfileimage(model.getProfileimage());


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String visit_user_id = getRef(position).getKey();


                        if(getRef(position).getKey()==current_user_id){
                            Toast.makeText(Admindeleteuser.this, "Admin cannot remove", Toast.LENGTH_SHORT).show();
                        }
                       else {

                            AlertDialog.Builder builder = new AlertDialog.Builder(Admindeleteuser.this);
                            builder.setTitle("Remove user");
                            builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ClickPostRef.child(visit_user_id).removeValue();
                                    Toast.makeText(Admindeleteuser.this, "User deleted successsfully", Toast.LENGTH_SHORT).show();

                                }
                            });

                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            Dialog dialog = builder.create();
                            dialog.show();
                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.holo_green_dark);
                        }
                       // Intent profileIntent= new Intent(FindFriendsActivity.this,PersonalProfileActivity.class);
                      //  profileIntent.putExtra("visit_user_id",visit_user_id);
                        //startActivity(profileIntent);
                    }
                });

            }
        };
        SearchResultList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class FindFriendsViewViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public FindFriendsViewViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }


        public void setProfileimage(String profileimage)
        {

            CircleImageView myImage =(CircleImageView)mView.findViewById(R.id.all_users_profile_image);
            Picasso.get().load(profileimage).placeholder(R.drawable.profile).into(myImage);
        }

        public void setFullname(String fullname) {
            TextView myName =(TextView)mView.findViewById(R.id.all_users_profile_name);
            myName.setText(fullname);
        }

        public void setabout(String about) {
            TextView myStatus =(TextView)mView.findViewById(R.id.all_users_status);
            myStatus.setText(about);

        }


    }
}
