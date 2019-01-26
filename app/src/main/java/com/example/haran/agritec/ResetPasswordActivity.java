package com.example.haran.agritec;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private Button ResetEmailPasswordButton;
    private EditText ResetEmailInput;
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mAuth=FirebaseAuth.getInstance();


        ResetEmailPasswordButton = (Button) findViewById(R.id.reset_password_button) ;
        ResetEmailInput = (EditText) findViewById(R.id.reset_password_email);

        mToolbar =(Toolbar) findViewById(R.id.forget_password_toolbar);
        setSupportActionBar(mToolbar);//setting up home tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Reset Password");//set title for action bar



        ResetEmailPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String userEmail =ResetEmailInput.getText().toString();

               if(TextUtils.isEmpty(userEmail))
               {
                   Toast.makeText(ResetPasswordActivity.this,"Please write your valid email address",Toast.LENGTH_SHORT).show();
               }

               else
               {
                   mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {

                           if(task.isSuccessful())
                           {
                               Toast.makeText(ResetPasswordActivity.this,"Please check your mail address,to reset your password",Toast.LENGTH_SHORT).show();
                               startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));

                           }
                           else
                           {
                               String message=task.getException().getMessage();
                               Toast.makeText(ResetPasswordActivity.this,"Error occured  "+message,Toast.LENGTH_SHORT).show();

                           }
                       }
                   });
               }

            }
        });



    }
}
