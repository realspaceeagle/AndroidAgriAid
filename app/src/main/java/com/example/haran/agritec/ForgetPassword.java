package com.example.haran.agritec;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    EditText pw;
    Button reset;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        pw = findViewById(R.id.editText3);
        reset = findViewById(R.id.button2);

        mAuth = FirebaseAuth.getInstance();




        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(pw.getText()!=null){


                    mAuth.sendPasswordResetEmail(pw.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){

                                        Toast.makeText(ForgetPassword.this,"sucessfully reset",Toast.LENGTH_SHORT).show();
                                    }
                                    else{

                                    }

                                }
                            });
                }

            }
        });

    }
}
