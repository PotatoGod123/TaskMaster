package com.potatogod123.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button goToAddFormButton = findViewById(R.id.goToAddFormButton);
        Button goToAllTaskButton = findViewById(R.id.goToAllTaskButton);
        Button goSettingsButton = findViewById(R.id.goToSettingsButton);

        Button goToTaskButtonOne = findViewById(R.id.goToTaskButtonOne);
        Button goToTaskButtonTwo = findViewById(R.id.goToTaskButtonTwo);
        Button goToTaskButtonThree = findViewById(R.id.goToTaskButtonThree);

        Button recyclerButton = findViewById(R.id.recycleButton);

        recyclerButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecyclerViewPractice.class);
            startActivity(intent);
        });

        goToTaskButtonOne.setOnClickListener(view1 -> {
            String buttonText = goToTaskButtonOne.getText().toString();
            buttonHelper(view1, buttonText);
        });

        goToTaskButtonTwo.setOnClickListener(view1 -> {
            String buttonText = goToTaskButtonTwo.getText().toString();
            buttonHelper(view1, buttonText);
        });

        goToTaskButtonThree.setOnClickListener(view1 -> {
            String buttonText = goToTaskButtonThree.getText().toString();
            buttonHelper(view1, buttonText);
        });

        goToAddFormButton.setOnClickListener(view->{

            Intent  intent = new Intent(MainActivity.this,AddTask.class);
            startActivity(intent);
        });

        goToAllTaskButton.setOnClickListener(view->{
            Intent intent = new Intent(MainActivity.this, AllTask.class);
            startActivity(intent);
        });

        goSettingsButton.setOnClickListener(view->{
            Intent intent = new Intent(MainActivity.this, Settings.class);
            startActivity(intent);
        });

    }

    @Override
    protected  void onResume(){
        super.onResume();
        SharedPreferences pref = getSharedPreferences("userdetails",MODE_PRIVATE);

        String username = pref.getString("username",null);

        if(username!=null){
            ((TextView) findViewById(R.id.textViewMainMyTaskTitle)).setText(String.format(Locale.getDefault(),"Welcome %s, These are your task:",username));
        }

    }


    protected void buttonHelper(View view, String buttonText){
        Intent intent = new Intent(MainActivity.this,TaskDetail.class);
        intent.putExtra("taskTitle",buttonText);
        startActivity(intent);
    }

}