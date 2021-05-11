package com.potatogod123.taskmaster.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;
import com.potatogod123.taskmaster.R;

import java.util.ArrayList;

import static com.potatogod123.taskmaster.activities.MainActivity.allTeams;

public class Settings extends AppCompatActivity {
    AuthUser authUser;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        handler = new Handler(this.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    Toast.makeText(Settings.this,"Logged Out Successfully!",Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    startActivity(intent);
                }
            }
        };


        Button saveButton = findViewById(R.id.userNameSaveButton);
        Button login = findViewById(R.id.settingsLoginButton);
        Button signUp = findViewById(R.id.settingsSignUpButton);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("userdetails",MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();

//        String[] nameArr = new String[allTeams.size()];
        ArrayList<String> nameArr = new ArrayList<>();
        for(Team team: allTeams){
            nameArr.add(team.getName());
        }
        int count=0;
        String teamStr= pref.getString("team",null);

        Spinner coolSpinner = findViewById(R.id.teamSpinner);
        ArrayAdapter adapt = new ArrayAdapter(this,R.layout.fragment_spinner_setting_fragment,R.id.textViewFragementSpinner,nameArr);
        coolSpinner.setAdapter(adapt);
        if(teamStr!=null){
            for(String names: nameArr){
                if(teamStr.contains(names)){
                    break;
                }
                count++;
            }
            coolSpinner.setSelection(count);
        }

        saveButton.setOnClickListener(view->{
            String teamChoice = ((TextView) findViewById(R.id.textViewFragementSpinner)).getText().toString();
             edit.putString("team", teamChoice);
             edit.apply();
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this,SignUp.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, Login.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected  void onResume(){
        super.onResume();
        SharedPreferences pref = getSharedPreferences("userdetails",MODE_PRIVATE);
        authUser= Amplify.Auth.getCurrentUser();
        Button login = findViewById(R.id.settingsLoginButton);
        Button signUp = findViewById(R.id.settingsSignUpButton);
        Button logout = findViewById(R.id.logoutButton);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amplify.Auth.signOut(
                        ()-> handler.sendEmptyMessage(1) ,
                        r->Log.i("potatogod123.Settings","Log out failed")
                );
            }
        });


        if(authUser!=null){
            login.setVisibility(View.GONE);
            signUp.setVisibility(View.GONE);
            ((TextView) findViewById(R.id.textViewSettingName)).setText(String.format("Your settings, %s.",authUser.getUsername()));
        }else {
            logout.setVisibility(View.GONE);
        }



    }

}