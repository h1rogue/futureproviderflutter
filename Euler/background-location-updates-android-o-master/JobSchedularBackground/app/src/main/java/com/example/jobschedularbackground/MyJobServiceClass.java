package com.example.jobschedularbackground;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;
import android.widget.Toast;

public class MyJobServiceClass extends JobService {
Boolean jobcan=false;
    @Override
    public boolean onStartJob(JobParameters params) {

        doInBackground(params);
        return true;
    }

    private void doInBackground(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                for(int i=0;i<10;i++){
                    Log.d("DSK_OPER","RUN: "+i);
                    try {
                        Thread.sleep(1000);
                        if(jobcan)
                            return;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("DSK_OPER","Job Finished");
                jobFinished(params,false);
            }
        }).start();
    }
    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("DSK_OPER","Job Finished before completion");
        jobcan=true;
        return true;
    }
}
