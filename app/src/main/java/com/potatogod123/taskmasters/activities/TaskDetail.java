package com.potatogod123.taskmasters.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;
import com.google.android.gms.ads.AdError;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.potatogod123.taskmasters.R;

import java.io.File;
import java.util.Locale;

public class TaskDetail extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;

    private final String TAG = "potatogod123.TaskDetail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        configureFullAdd();

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


    @Override
    protected  void onStop(){
        super.onStop();

        if(mInterstitialAd!=null){
            mInterstitialAd.show(TaskDetail.this);
        }

    }



    private void configureFullAdd(){
        MobileAds.initialize(getApplicationContext(), initializationStatus -> {
        });
        String testAddString = "ca-app-pub-8077611097501749/6505059711";
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(getApplicationContext(), testAddString, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                mInterstitialAd=interstitialAd;
                populateAd();
                Log.i(TAG, "onAdLoaded: it is loaded?"+interstitialAd.getResponseInfo());
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                mInterstitialAd=null;
                Log.i(TAG, "onAdFailedToLoad: "+loadAdError.getMessage());
            }
        });




    }
    private void populateAd(){
        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                super.onAdFailedToShowFullScreenContent(adError);
                Log.d(TAG, "onAdFailedToShowFullScreenContent: -> "+adError.getMessage());
            }

            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
                Log.i(TAG, "onAdShowedFullScreenContent: ad was shown!");
                mInterstitialAd=null;
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent();
                Log.i(TAG, "onAdDismissedFullScreenContent: ad was dimissed");
            }
        });
    }

}