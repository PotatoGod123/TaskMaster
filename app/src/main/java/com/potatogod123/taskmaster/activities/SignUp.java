package com.potatogod123.taskmaster.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignOutOptions;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.potatogod123.taskmaster.R;

public class SignUp extends AppCompatActivity {
    String TAG = "potatgod123.SignUp";
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button signUpButton = findViewById(R.id.signUpInfromationButton);

        handler = new Handler(this.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    Intent intent = new Intent(SignUp.this,SignUpConfirmation.class);
                    EditText username= (EditText) findViewById(R.id.editTextSignUpUsername);
                    intent.putExtra("username",username.getText().toString());
                    startActivity(intent);
                }
            }
        };


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username= (EditText) findViewById(R.id.editTextSignUpUsername);
                EditText password= (EditText) findViewById(R.id.editTextSignUpPassword);
                EditText email= (EditText) findViewById(R.id.editTextSignUpEmailAddress);
                if(username.getText().toString().isEmpty())username.setError("Username is required! Max Characters:12");
                else if(password.getText().toString().isEmpty())password.setError("Password is required!");
                else if(email.getText().toString().isEmpty())email.setError("Email is required!");
                else if(password.getText().toString().length()<8)password.setError("Password must be at least 8 characters long!");
                else{
                    AuthSignUpOptions options= AuthSignUpOptions.builder()
                            .userAttribute(AuthUserAttributeKey.email(),email.getText().toString())
                            .build();

                    Amplify.Auth.signUp(
                            username.getText().toString(),
                            password.getText().toString(),
                            options,
                            r->{
                                Log.i(TAG,"SUCESSES NEW USER CAN BE MADE, off to confirm");
                                handler.sendEmptyMessage(1);
                            },
                            r->{
                                Log.i(TAG,"Failure, something happened"+ r.toString());
                            }

                    );
                }
            }
        });

    }
}