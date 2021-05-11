package com.potatogod123.taskmaster.activities;

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
import android.widget.Button;
import android.widget.TextView;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskModelAmp;
import com.amplifyframework.datastore.generated.model.Team;
import com.potatogod123.taskmaster.R;
import com.potatogod123.taskmaster.TaskDatabase;
import com.potatogod123.taskmaster.adapters.TaskRecycleAdapter;
import com.potatogod123.taskmaster.models.TaskModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TaskRecycleAdapter.ClickOnTaskButtonAble {
     TaskRecycleAdapter taskRecycleAdapter;
    String TAG = "main.potatogod123";
    //set this up as a service so it can use else where with autowire :))))
    TaskDatabase taskDatabase;
    Handler mainThreadHandler;
    String selectedTeam;
    Team currentTeam;
    List<TaskModelAmp> currentTeamTask = new ArrayList<>();
    static List<Team> allTeams = new ArrayList<>();


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
//                        Log.i(TAG,"this is the size inside handler "+ taskRecycleAdapter.getItemCount());
                        size[0] =taskRecycleAdapter.getItemCount();
                        break;
                    }
                    case 2:{
                        for(Team team:allTeams){
                            if(team.getName().contains(selectedTeam)){
                                currentTeam=team;
                            }
//                            Log.i(TAG,"This the team in the all teams"+ team.toString());
                        }

//                        Log.i(TAG,"This is the current team "+currentTeam.toString());
                        helperQuery(currentTeam.getId());
                        break;
                    }

                }

            }
        };

        Button goToAddFormButton = findViewById(R.id.goToAddFormButton);
        Button goToAllTaskButton = findViewById(R.id.goToAllTaskButton);
        Button goSettingsButton = findViewById(R.id.goToSettingsButton);

        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());

        } catch (AmplifyException e) {
            e.printStackTrace();
        }



        goToAddFormButton.setOnClickListener(view->{

            Intent  intent = new Intent(MainActivity.this, AddTask.class);
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
        AuthUser username = Amplify.Auth.getCurrentUser();
        selectedTeam= pref.getString("team",null);
        if(currentTeamTask.size()!=0)currentTeamTask.clear();
        if(selectedTeam==null) {
            selectedTeam="Blue";
        }

        if(username!=null){
            ((TextView) findViewById(R.id.textViewMainMyTaskTitle)).setText(String.format(Locale.getDefault(),"Welcome %s, These are your team,%s ,task:",username.getUsername(),selectedTeam));
        }else {
            ((TextView) findViewById(R.id.textViewMainMyTaskTitle)).setText("My Task");
        }

        taskDatabase= Room.databaseBuilder(getApplicationContext(), TaskDatabase.class,"potatogod123_task")
                .allowMainThreadQueries()
                .build();

        List<TaskModel> allTask = taskDatabase.taskModelDao().findAll();




            allTeams.clear();

            Amplify.API.query(
                    ModelQuery.list(Team.class),
                    r -> {
                        for (Team team : r.getData()) {
                            allTeams.add(team);
//                            Log.i(TAG,"this is that list of the teams 167>>> "+team.getTasks().toString());
                        }
                        mainThreadHandler.sendEmptyMessage(2);
                    },
                    r -> {}
            );


                            RecyclerView recyclerView = findViewById(R.id.taskRecyclerViewMain);
                            recyclerView.setLayoutManager(new LinearLayoutManager(this));

                            taskRecycleAdapter= new TaskRecycleAdapter(this,currentTeamTask,0);
                            recyclerView.setAdapter(taskRecycleAdapter);


    }


    protected void buttonHelper(TaskRecycleAdapter.TaskViewHolder holder){
        Intent intent = new Intent(MainActivity.this, TaskDetail.class);

        intent.putExtra("taskTitle",holder.getTitle());
        intent.putExtra("description", holder.getDescription());
        startActivity(intent);
    }

    @Override
    public void handleClickOnButton(TaskRecycleAdapter.TaskViewHolder taskViewHolder) {
        buttonHelper(taskViewHolder);
    }

    public void helperQuery(String id){

        Amplify.API.query(
                ModelQuery.get(Team.class,id),
                res->{
                    currentTeamTask.addAll(res.getData().getTasks());
                    mainThreadHandler.sendEmptyMessage(1);
                },
                r->{}
        );

    }
}