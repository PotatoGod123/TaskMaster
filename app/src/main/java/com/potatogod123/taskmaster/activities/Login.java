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
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.potatogod123.taskmaster.R;

public class Login extends AppCompatActivity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        handler= new Handler(this.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    Toast.makeText(Login.this,"Login Successful!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this,MainActivity.class);
                    startActivity(intent);
                }else if(msg.what==2){
                    Toast.makeText(Login.this,"Login Failed, Please Try Again!",Toast.LENGTH_LONG).show();
                }
            }
        };


        Button login = findViewById(R.id.loginInformationButton);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = findViewById(R.id.editTextLoginUserName);
                EditText password = findViewById(R.id.editTextLoginPassword);

                if(username.getText().toString().isEmpty())username.setError("Username is required!");
                else if(password.getText().toString().isEmpty())password.setError("Password is required!");
                else{
                    Amplify.Auth.signIn(
                            username.getText().toString(),
                            password.getText().toString(),
                            r->{
                                if(r.isSignInComplete()){
                                    Log.i("potatogod.Login","Login Worked! --> "+r.toString());
                                    handler.sendEmptyMessage(1);
                                }else{
                                    Log.i("potatogod123.Login","Got a login response but login didn't work -->>  "+r.toString());
                                    handler.sendEmptyMessage(2);
                                }
                            },
                            r->{
                                Log.i("potatogod123.Login","auth sign in failed -->> "+r.toString());
                                handler.sendEmptyMessage(2);
                            }
                    );
                }
            }
        });
    }
}