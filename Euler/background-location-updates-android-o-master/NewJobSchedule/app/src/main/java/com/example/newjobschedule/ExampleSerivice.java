package com.example.newjobschedule;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class ExampleSerivice extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d("DSK_OPER","HELLO");
        jobFinished(params,false);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("DSK_OPER","job Finished unresponsive");
        return false;
    }
}
