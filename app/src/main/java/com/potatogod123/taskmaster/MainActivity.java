package com.potatogod123.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskModelAmp;
import com.potatogod123.taskmaster.adapters.TaskRecycleAdapter;
import com.potatogod123.taskmaster.models.TaskModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TaskRecycleAdapter.ClickOnTaskButtonAble {
    static TaskRecycleAdapter taskRecycleAdapter;
    String TAG = "main.potatogod123";
    //set this up as a service so it can use else where with autowire :))))
    TaskDatabase taskDatabase;
    Handler mainThreadHandler;

//    public static List<TaskModel> allTask = new ArrayList<>();

    static{
//        allTask.add(new TaskModel("Buy a dog","Go and buy a dog, steal one whatever"));
//        allTask.add(new TaskModel("Sell a dog","Go and sell a dog, give it for free whatever"));
//        allTask.add(new TaskModel("Use the Restroom","quick! it's been a long day :("));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final int[] size = {0};

        mainThreadHandler = new Handler(this.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg){
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:{
                        taskRecycleAdapter.notifyDataSetChanged();
                        Log.i(TAG,"this is the size inside handler "+ taskRecycleAdapter.getItemCount());
                        size[0] =taskRecycleAdapter.getItemCount();

                    }
                }

            }
        };

        Button goToAddFormButton = findViewById(R.id.goToAddFormButton);
        Button goToAllTaskButton = findViewById(R.id.goToAllTaskButton);
        Button goSettingsButton = findViewById(R.id.goToSettingsButton);

        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());
            Log.i("main.potatogod123","configured amplify!");
        } catch (AmplifyException e) {
            e.printStackTrace();
        }


//        Button recyclerButton = findViewById(R.id.recycleButton);
//
//        recyclerButton.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this, RecyclerViewPractice.class);
//            startActivity(intent);
//        });



        goToAddFormButton.setOnClickListener(view->{

            Intent  intent = new Intent(MainActivity.this,AddTask.class);
            intent.putExtra("size", size[0]);
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

        taskDatabase= Room.databaseBuilder(getApplicationContext(), TaskDatabase.class,"potatogod123_task")
                .allowMainThreadQueries()
                .build();

        List<TaskModel> allTask = taskDatabase.taskModelDao().findAll();

//        List<TaskModelAmp> awsTaskList = helperQuery();


        RecyclerView recyclerView = findViewById(R.id.taskRecyclerViewMain);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskRecycleAdapter= new TaskRecycleAdapter(this,helperQuery(),0);
        recyclerView.setAdapter(taskRecycleAdapter);

    }


    protected void buttonHelper(TaskRecycleAdapter.TaskViewHolder holder){
        Intent intent = new Intent(MainActivity.this,TaskDetail.class);

        intent.putExtra("taskTitle",holder.getTitle());
        intent.putExtra("description", holder.getDescription());
        startActivity(intent);
    }

    @Override
    public void handleClickOnButton(TaskRecycleAdapter.TaskViewHolder taskViewHolder) {
        buttonHelper(taskViewHolder);
    }

    public List<TaskModelAmp> helperQuery(){
        List<TaskModelAmp> awsTaskList = new ArrayList<>();
        Amplify.API.query(
                ModelQuery.list(TaskModelAmp.class),
                res->{
                    Log.i("main.potatogod123","Getting stuff from db");
                    for(TaskModelAmp task:res.getData()){
                        Log.i("main.potatogod123","This is tha task->"+task);
                        awsTaskList.add(task);
                    }
                    mainThreadHandler.sendEmptyMessage(1);
                },
                r->{Log.i("main.potatogod123","failed to retrieve db");}
        );

        return awsTaskList;
    }
}