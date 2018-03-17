/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.background.sync;


import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class ReminderUtilities {
    // COMPLETED (15) Create three constants and one variable:
    //  - REMINDER_INTERVAL_SECONDS should be an integer constant storing the number of seconds in 15 minutes
    //  - SYNC_FLEXTIME_SECONDS should also be an integer constant storing the number of seconds in 15 minutes
    //  - REMINDER_JOB_TAG should be a String constant, storing something like "hydration_reminder_tag"
    //  - sInitialized should be a private static boolean variable which will store whether the job
    //    has been activated or not

    private static final int REMINDER_INTERVAL_SECONDS = 10;
    private static final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS;
    private static boolean sInitialized;
    private static int ID_REMINDER_JOB = 1351;


    // COMPLETED (16) Create a synchronized, public static method called scheduleChargingReminder that takes
    // in a context. This method will use FirebaseJobDispatcher to schedule a job that repeats roughly
    // every REMINDER_INTERVAL_SECONDS when the phone is charging. It will trigger WaterReminderFirebaseJobService
    // Checkout https://github.com/firebase/firebase-jobdispatcher-android for an example
    // COMPLETED (17) If the job has already been initialized, return
    // TODO (18) Create a new GooglePlayDriver
    // TODO (19) Create a new FirebaseJobDispatcher with the driver
    // TODO (20) Use FirebaseJobDispatcher's newJobBuilder method to build a job which:
    // - has WaterReminderFirebaseJobService as it's service
    // - has the tag REMINDER_JOB_TAG
    // - only triggers if the device is charging
    // - has the lifetime of the job as forever
    // - has the job recur
    // - occurs every 15 minutes with a window of 15 minutes. You can do this using a
    //   setTrigger, passing in a Trigger.executionWindow
    // - replaces the current job if it's already running
    // Finally, you should build the job.
    // TODO (21) Use dispatcher's schedule method to schedule the job
    // COMPLETED (22) Set sInitialized to true to mark that we're done setting up the job

    synchronized public static void scheduleChargingReminder(@NonNull Context context) {
        if (sInitialized)
            return;
        ComponentName jobService = new ComponentName(context, WaterReminderFirebaseJobService.class);
        JobInfo.Builder jobBuilder = new JobInfo.Builder(ID_REMINDER_JOB, jobService)
                .setRequiresCharging(true)
                .setPersisted(true);
        JobInfo jobInfo;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            jobInfo = jobBuilder.setPeriodic(TimeUnit.SECONDS.toMillis(REMINDER_INTERVAL_SECONDS),
//                    TimeUnit.SECONDS.toMillis(SYNC_FLEXTIME_SECONDS)).build();
//        } else {
//            jobInfo = jobBuilder.setPeriodic(TimeUnit.SECONDS.toMillis(REMINDER_INTERVAL_SECONDS)).build();
//        }
        jobInfo = jobBuilder.setPeriodic(5000).build();

        JobScheduler jobScheduler =
                (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);
        List<JobInfo> testJob = jobScheduler.getAllPendingJobs();
        for (int i = 0; i < testJob.size(); i++) {
            Log.e(TAG, "scheduleChargingReminder: " + testJob.get(i).getId());
            Log.e(TAG, "scheduleChargingReminder: Min:" + testJob.get(i).getMinLatencyMillis());
            Log.e(TAG, "scheduleChargingReminder: Interval:" + testJob.get(i).getIntervalMillis());

        }
        sInitialized = true;
    }
}
