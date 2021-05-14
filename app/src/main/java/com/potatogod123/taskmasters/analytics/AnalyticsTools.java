package com.potatogod123.taskmasters.analytics;

import android.util.Log;

import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.analytics.AnalyticsProperties;
import com.amplifyframework.analytics.UserProfile;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.core.Amplify;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;


public class AnalyticsTools {
    private static AnalyticsTools analytics;
    static List<AuthUserAttribute> currentUserDetails;
    Handler handler;
    static String TAG = "potatogod123.AnalyticsTools";


    public static AnalyticsTools getAnalytics(){
        if(analytics == null){
            analytics= new AnalyticsTools();

            Amplify.Analytics.registerGlobalProperties(
                    AnalyticsProperties.builder()
                            .add("App","Task Masters App")
                            .build()
            );

            if(Amplify.Auth.getCurrentUser() !=null){
                String[] email = new String[1];
                Amplify.Auth.fetchUserAttributes(
                        r-> {
                            currentUserDetails = r;
                            Log.i(TAG, "This is the  response for  attri ->> " + r.toString());
                            Log.i(TAG, "This is currentUserDeatials" + currentUserDetails.toString());

                            for (AuthUserAttribute atti : r) {
                                if(atti.getKey().getKeyString().equals("email"))email[0]=atti.getValue();
                            }
                                if(email[0]!=null) {
                                    UserProfile userProfile = UserProfile.builder()
                                            .email(email[0])
                                            .build();
                                    Amplify.Analytics.identifyUser(Amplify.Auth.getCurrentUser().getUserId(), userProfile);
                                    Log.i(TAG,"user identified??");
                                }

                            },
                        r->{}
                );
            }

        }
        return analytics;
    }

    public void timeOnPage(LocalDateTime start, LocalDateTime end, String currentActivity){

        Duration d = Duration.between(start,end);
        long milli = d.toMillis();
        String formattedTimeString = String.format(Locale.getDefault(),
                "%02d Hours, %02d Minutes, %02d seconds",
                TimeUnit.MILLISECONDS.toHours(milli),
                TimeUnit.MILLISECONDS.toMinutes(milli),
                TimeUnit.MILLISECONDS.toSeconds(milli)
                );


        Amplify.Analytics.recordEvent(
                AnalyticsEvent.builder()
                .name("Time Spent On Activity")
                .addProperty("Activity", currentActivity)
                .addProperty("Duration on this activity", formattedTimeString)
                .build()
        );

        Log.i(TAG," duration -> " + formattedTimeString);
    }



}
