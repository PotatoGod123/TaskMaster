package com.potatogod123.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;

import static com.potatogod123.taskmaster.MainActivity.allTeams;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        Button saveButton = findViewById(R.id.userNameSaveButton);

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
            String usernameInput = ((EditText) findViewById(R.id.editTextUserName)).getText().toString();
            String teamChoice = ((TextView) findViewById(R.id.textViewFragementSpinner)).getText().toString();
             edit.putString("team", teamChoice);
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
        }



    }

}