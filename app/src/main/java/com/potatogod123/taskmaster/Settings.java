package com.potatogod123.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        Button saveButton = findViewById(R.id.userNameSaveButton);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("userdetails",MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();

        saveButton.setOnClickListener(view->{
            String usernameInput = ((EditText) findViewById(R.id.editTextUserName)).getText().toString();
             edit.putString("username",usernameInput);
             edit.apply();
            ((TextView)findViewById(R.id.textViewSettingName)).setText(usernameInput);
        });
    }

    @Override
    protected  void onResume(){
        super.onResume();

        SharedPreferences pref = getSharedPreferences("userdetails",MODE_PRIVATE);

        String username= pref.getString("username",null);

        if(username!=null){
            ((TextView) findViewById(R.id.textViewSettingName)).setText(username);
            ((EditText) findViewById(R.id.editTextUserName)).setText(username);
            Log.i("Settings.potato", username);
        }



    }

}