package com.example.jobschedularbackground;

import android.os.AsyncTask;

public class JobExecutor extends AsyncTask<Void,Void,String> {
    @Override
    protected String doInBackground(Void... voids) {
        return "Background Long Running Fininshes";
    }
}
