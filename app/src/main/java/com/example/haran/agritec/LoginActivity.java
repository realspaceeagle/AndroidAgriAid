package com.example.haran.agritec;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button LoginButton;
    private EditText UserEmail,UserPassword;
    private TextView NeedNewAccountLink;
    private ProgressDialog loadingBar;

private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth =FirebaseAuth.getInstance();


        NeedNewAccountLink = (TextView) findViewById(R.id.register_account_link);
        UserEmail = (EditText) findViewById(R.id.Login_email);
        UserPassword = (EditText) findViewById(R.id.Login_password);
        LoginButton = (Button) findViewById(R.id.Login_button);
        loadingBar = new ProgressDialog(this);

        //1st
        NeedNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SendUserToRegisterActivity();
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AllowingUserToLogin();

            }
        });
    }


    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser currentUser =mAuth.getCurrentUser();

        if(currentUser != null)
        {
            SendUserToMainActivity();//if not user logged in send to login page

        }

    }





    private void AllowingUserToLogin()
    {
      String email = UserEmail.getText().toString();
      String password =UserPassword.getText().toString();

      if (TextUtils.isEmpty(email))
      {

          Toast.makeText(this, "please write your email...", Toast.LENGTH_SHORT).show();
      }

       else if (TextUtils.isEmpty(password))
        {

            Toast.makeText(this, "please write your password...", Toast.LENGTH_SHORT).show();
        }
        else
      {
          loadingBar.setTitle("Login");
          loadingBar.setMessage("Please wait,while we are allowing you to login into your new Account...");
          loadingBar.show();
          loadingBar.setCanceledOnTouchOutside(true);





          mAuth.signInWithEmailAndPassword(email ,password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful())
                    {
                        SendUserToMainActivity();

                        Toast.makeText(LoginActivity.this, "You are Logged In Successfully", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }

                    else
                        {

                            String message =task.getException().getMessage();
                            Toast.makeText(LoginActivity.this, "Error occured"+message, Toast.LENGTH_SHORT).show();
                       loadingBar.dismiss();
                        }
                }
            });
      }
    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent =new Intent(LoginActivity.this,MainActivity.class);
mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
startActivity(mainIntent);
    finish();
    }

    private void SendUserToRegisterActivity() {

        Intent registerIntent =new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(registerIntent);


    }



    }

