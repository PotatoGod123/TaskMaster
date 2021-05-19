package com.potatogod123.taskmasters.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;
import com.potatogod123.taskmasters.R;

import java.io.File;
import java.util.Locale;

public class TaskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);


    }


    @Override
    protected  void onResume(){
        super.onResume();
        Intent intent = getIntent();
        if(intent.getStringExtra("taskTitle")!=null && !intent.getStringExtra("taskTitle").equals("")){
            ((TextView) findViewById(R.id.textViewTaskDetailPageTitle)).setText(intent.getStringExtra("taskTitle"));

        }else{
            ((TextView) findViewById(R.id.textViewTaskDetailPageTitle)).setText(intent.getStringExtra("No Title Available"));
        }

        if(intent.getStringExtra("description")!=null){
            ((TextView) findViewById(R.id.textViewTaskDetailDescription)).setText(intent.getStringExtra("description"));
        }else {
            ((TextView) findViewById(R.id.textViewTaskDetailDescription)).setText(intent.getStringExtra("No Description Available"));
        }

        if(intent.getStringExtra("location")!=null){
            ((TextView) findViewById(R.id.locationTextView)).setText(String.format(Locale.getDefault(),"Location made at: %s",intent.getStringExtra("location")));
        }else {
            ((TextView) findViewById(R.id.locationTextView)).setText(String.format(Locale.getDefault(),"No location %s","Found"));
        }

        String pictureId= intent.getStringExtra("taskImageKey");
        if(pictureId!=null){
            Amplify.Storage.downloadFile(
                    pictureId,
                    new File(getApplicationContext().getFilesDir(),pictureId),
                    r->{
                        ImageView i = findViewById(R.id.imageViewTaskDetail);
                        i.setImageBitmap(BitmapFactory.decodeFile(r.getFile().getPath()));
                    },
                    r->{

                    }
            );
        }
    }
}