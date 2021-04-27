package com.potatogod123.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button goToAddFormButton = findViewById(R.id.goToAddFormButton);
        Button goToAllTaskButton = findViewById(R.id.goToAllTaskButton);

        goToAddFormButton.setOnClickListener(view->{

            Intent  intent = new Intent(MainActivity.this,AddTask.class);
            startActivity(intent);
        });

        goToAllTaskButton.setOnClickListener(view->{
            Intent intent = new Intent(MainActivity.this, AllTask.class);
            startActivity(intent);
        });
    }
}