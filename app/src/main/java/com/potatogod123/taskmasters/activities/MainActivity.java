package com.potatogod123.taskmasters.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;


import com.amplifyframework.AmplifyException;
import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.analytics.pinpoint.AWSPinpointAnalyticsPlugin;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskModelAmp;
import com.amplifyframework.datastore.generated.model.Team;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.potatogod123.taskmasters.R;
import com.potatogod123.taskmasters.TaskDatabase;
import com.potatogod123.taskmasters.adapters.TaskRecycleAdapter;
import com.potatogod123.taskmasters.analytics.AnalyticsTools;
import com.potatogod123.taskmasters.models.TaskModel;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Queue;

import static com.potatogod123.taskmasters.utilities.ConfigUtilities.configPlugins;
import static com.potatogod123.taskmasters.utilities.ConfigUtilities.subscribeToLocationUpdates;

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
    LocalDateTime resumedTime;
    FusedLocationProviderClient locationProviderClient;
    Geocoder geocoder;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final int[] size = {0};

        mainThreadHandler = new Handler(this.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1: {
                        taskRecycleAdapter.notifyDataSetChanged();
//                        Log.i(TAG,"this is the size inside handler "+ taskRecycleAdapter.getItemCount());
                        size[0] = taskRecycleAdapter.getItemCount();
                        break;
                    }
                    case 2: {
                        for (Team team : allTeams) {
                            if (team.getName().contains(selectedTeam)) {
                                currentTeam = team;
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


        configPlugins(getApplication(), getApplicationContext());
        configureNotificationChannel();

        registerWithFirebaseAndPinpoint();
        requestLocationPermissions();
        loadLocationProviderClient();
        subscribeToLocationUpdates(geocoder,TAG,getApplicationContext(),locationProviderClient,getMainLooper());
        goToAddFormButton.setOnClickListener(view -> {

            Intent intent = new Intent(MainActivity.this, AddTask.class);
            intent.putExtra("size", size[0]);
            startActivity(intent);
        });

        goToAllTaskButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AllTask.class);
            startActivity(intent);
        });

        goSettingsButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Settings.class);
            startActivity(intent);
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getSharedPreferences("userdetails", MODE_PRIVATE);
        AuthUser username = Amplify.Auth.getCurrentUser();
        selectedTeam = pref.getString("team", null);
        if (currentTeamTask.size() != 0) currentTeamTask.clear();
        if (selectedTeam == null) {
            selectedTeam = "Blue";
        }

        if (username != null) {
            ((TextView) findViewById(R.id.textViewMainMyTaskTitle)).setText(String.format(Locale.getDefault(), "Welcome %s, These are your team,%s ,task:", username.getUsername(), selectedTeam));
        } else {
            ((TextView) findViewById(R.id.textViewMainMyTaskTitle)).setText(String.format(Locale.getDefault(), "My %s", "Task"));
        }

        taskDatabase = Room.databaseBuilder(getApplicationContext(), TaskDatabase.class, "potatogod123_task")
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
                r -> {
                }
        );


        RecyclerView recyclerView = findViewById(R.id.taskRecyclerViewMain);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskRecycleAdapter = new TaskRecycleAdapter(this, currentTeamTask, 0);
        recyclerView.setAdapter(taskRecycleAdapter);
        mainThreadHandler.sendEmptyMessage(1);
        AnalyticsTools.getAnalytics();
        if (username != null) {
            AnalyticsEvent e = AnalyticsEvent.builder()
                    .name("Opened Task Master")
                    .build();
            Amplify.Analytics.recordEvent(e);
        }


        resumedTime = LocalDateTime.now();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AnalyticsTools.getAnalytics().timeOnPage(resumedTime, LocalDateTime.now(), MainActivity.class.getSimpleName());

    }

    protected void buttonHelper(TaskRecycleAdapter.TaskViewHolder holder) {
        Intent intent = new Intent(MainActivity.this, TaskDetail.class);
        if (holder.getTask().getS3StorageId() != null) {
            intent.putExtra("taskImageKey", holder.getTask().getS3StorageId());
        }
        intent.putExtra("location",holder.getTask().getLocationCreation());
        intent.putExtra("taskTitle", holder.getTitle());
        intent.putExtra("description", holder.getDescription());
        startActivity(intent);
    }

    @Override
    public void handleClickOnButton(TaskRecycleAdapter.TaskViewHolder taskViewHolder) {
        buttonHelper(taskViewHolder);
    }

    public void helperQuery(String id) {

        Amplify.API.query(
                ModelQuery.get(Team.class, id),
                res -> {
                    currentTeamTask.addAll(res.getData().getTasks());
                    mainThreadHandler.sendEmptyMessage(1);
                },
                r -> {
                }
        );

    }

    void registerWithFirebaseAndPinpoint() {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }                        // Get new FCM registration token
                        String token = task.getResult();
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void configureNotificationChannel() {
        String CHANNEL_ID = "929";

        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "New Task",
                NotificationManager.IMPORTANCE_HIGH
        );
        channel.enableLights(true);
        channel.canShowBadge();
        channel.enableVibration(true);
        channel.setLockscreenVisibility(3);
        channel.setDescription("Notifications for new Task!");
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);


    }


    void requestLocationPermissions() {
        requestPermissions(
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },
                1
        );
    }

    void loadLocationProviderClient() {
        locationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

    }




}