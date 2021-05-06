package com.potatogod123.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskModelAmp;
import com.potatogod123.taskmaster.adapters.TaskRecycleAdapter;
//import com.potatogod123.taskmaster.models.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class AllTask extends AppCompatActivity implements TaskRecycleAdapter.ClickOnTaskButtonAble{
    private static final String TAG ="AllTask.potatogod123";
    TaskDatabase taskDatabase;
    public List<TaskModelAmp> allTask = new ArrayList<>();
    Handler mainThreadHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_task);

        mainThreadHandler = new Handler(this.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg){
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:{
                        RecyclerView recyclerView = findViewById(R.id.allTaskRecyclerView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(AllTask.this));
                        recyclerView.setAdapter(new TaskRecycleAdapter(AllTask.this,allTask,1));

                    }
                }

            }
        };

    }


    @Override
    protected void onResume() {
        super.onResume();

        taskDatabase= Room.databaseBuilder(getApplicationContext(), TaskDatabase.class,"potatogod123_task")
                .allowMainThreadQueries()
                .build();
//        List<TaskModel> allTask = taskDatabase.taskModelDao().findAll();
        if(allTask.size()!=0)allTask.clear();
        Amplify.API.query(
                ModelQuery.list(TaskModelAmp.class),
                r->{
                    Log.i(TAG,"Pog it worked");
                    for(TaskModelAmp task:r.getData()){
                        allTask.add(task);
                    }
                    mainThreadHandler.sendEmptyMessage(1);
                },
                r->Log.i(TAG,"pog it didnt work")
        );

        Log.i("AllTask.potatogod123" , ""+allTask.size());


//
    }

    @Override
    public void handleClickOnButton(TaskRecycleAdapter.TaskViewHolder taskViewHolder) {
        buttonHelper(taskViewHolder);
    }

    protected void buttonHelper(TaskRecycleAdapter.TaskViewHolder holder){
        Intent intent = new Intent(this,TaskDetail.class);

        intent.putExtra("taskTitle",holder.getTitle());
        intent.putExtra("description", holder.getDescription());
        startActivity(intent);
    }
}