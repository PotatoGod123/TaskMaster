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

public class SignUpConfirmation extends AppCompatActivity {
    String TAG= "potatogod123.SignUpConfirmation";
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_confirmation);

        handler = new Handler(this.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    Toast.makeText(SignUpConfirmation.this,"Success, new account confirmed", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpConfirmation.this,MainActivity.class);
                    startActivity(intent);
                }else if(msg.what==2){
                    Toast.makeText(SignUpConfirmation.this,"An Error Occurred, Please Try Again", Toast.LENGTH_LONG).show();
                }
            }
        };


        Button confirmButton = findViewById(R.id.confirmationButton);


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText code = (EditText)findViewById(R.id.editTextConfirmationCode);
                if(code.getText().toString().isEmpty())code.setError("Type in Code!");
                else{
                    Intent i = getIntent();
                    String username = i.getStringExtra("username");

                    Amplify.Auth.confirmSignUp(
                            username,
                            code.getText().toString(),
                            r->{
                                Log.i(TAG,"Success?"+r);
                                if(r.isSignUpComplete()){
                                    handler.sendEmptyMessage(1);
                                }
                            },
                            r->{
                                Log.e(TAG,"Failed ->> "+r.toString());
                            }
                    );
                }

            }
        });


    }
}