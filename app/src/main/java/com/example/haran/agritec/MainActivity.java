package com.example.haran.agritec;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;//home action bar toggle
    private RecyclerView postList;
    private Toolbar mToolbar;//home tool bar

    private CircleImageView NavProfileImage;
    private TextView NavProfileUserName;

    private FirebaseAuth mAuth;//firebase 1st
    private DatabaseReference UsersRef ;

    String currentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth =FirebaseAuth.getInstance();//firebase 1st
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");


        mToolbar =(Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);//setting up home tool bar
        getSupportActionBar().setTitle("Home");//set title for action bar


        drawerLayout = (DrawerLayout) findViewById(R.id.drawable_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle( MainActivity.this,drawerLayout,R.string.drawer_open,R.string.drawer_close);//home action bar toggle
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView =(NavigationView)findViewById(R.id.navigation_view);

        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);
        NavProfileImage =(CircleImageView) navView.findViewById(R.id.nav_profile_imge);
        NavProfileUserName = (TextView) navView.findViewById(R.id.nav_profile_full_name);


        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                   if(dataSnapshot.hasChild("fullname")) {
                       String fullname = dataSnapshot.child("fullname").getValue().toString();
                       NavProfileUserName.setText(fullname);
                   }
                   if(dataSnapshot.hasChild("profileimage")) {
                       String image = dataSnapshot.child("profileimage").getValue().toString();

                       //NavProfileUserName.setText(fullname);
                       Picasso.get().load(image).placeholder(R.drawable.profile).into(NavProfileImage);
                   }
                   else
                   {
                       Toast.makeText(MainActivity.this, "Profile name donot exists...", Toast.LENGTH_SHORT).show();
                   }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserMenuSelector(item);
                return false;
            }
        });
    }





    @Override
    //firebase 1st
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser =mAuth.getCurrentUser();

        if(currentUser == null)
        {
            SendUserToLoginActivity();//if not user logged in send to login page

        }
        else
        {
            CheckUserExistence();

        }
    }

    private void CheckUserExistence()
    {

        final String current_user_id =mAuth.getCurrentUser().getUid();
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.hasChild(current_user_id))
                {
                    SendUserToSetupActivity();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void SendUserToSetupActivity()
    {
        Intent setupIntent = new Intent(MainActivity.this,SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();

    }


    //firebase 1st
    private void SendUserToLoginActivity() {

        Intent LoginIntent = new Intent(MainActivity.this,LoginActivity.class);
         LoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(LoginIntent);
        finish();
    }







    @Override
    //home toggle action bar button if it is selected
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void UserMenuSelector(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.nav_profile:
            Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show();
            break;

            case R.id.nav_home:
                Toast.makeText(this, "home", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_friends:
                Toast.makeText(this, "friendlist", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_find_friends:
                Toast.makeText(this, "findfriends", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_Messages:
                Toast.makeText(this, "messages", Toast.LENGTH_SHORT).show();
                break;


            case R.id.nav_Settings:
                Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
                break;


            case R.id.nav_Logout:
               // Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
               mAuth.signOut();
               SendUserToLoginActivity();
                break;



        }
    }
}
