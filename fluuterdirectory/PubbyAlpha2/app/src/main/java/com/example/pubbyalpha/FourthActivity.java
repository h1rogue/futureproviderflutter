package com.example.pubbyalpha;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FourthActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PrivateAdapter adapter;
    private Socket socket;
    private Button sendbutt;
    private EditText writemsg;
    private ArrayList<PrivateMessage> grpList;
    private String yourname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);
        Intent intent = getIntent();
        yourname = intent.getStringExtra("yourname");

        loadData();
        //grpList = new ArrayList<>();
        ChatAplication chatAplication = (ChatAplication) getApplication();
        socket = chatAplication.getSocket();

        recyclerView = findViewById(R.id.recyclegroup);
        writemsg = findViewById(R.id.groupedit);
        sendbutt = findViewById(R.id.groupsend);


        LinearLayoutManager layoutManager = new LinearLayoutManager(FourthActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PrivateAdapter(grpList);
        recyclerView.setAdapter(adapter);
        socket.emit("join",yourname);

        sendbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = writemsg.getText().toString().trim();
                if(TextUtils.isEmpty(message)){
                    writemsg.setError("Write message");
                }
               else {
                    socket.emit("messagedetection",yourname,message);
                    writemsg.setText("");
                    /// TODO: 09-04-2019

                }
            }
        });

        socket.on("userjoinedthechat", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];
                        Toast.makeText(FourthActivity.this,data,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        socket.on("grpmessage", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        //Toast.makeText(FourthActivity.this,data.toString(),Toast.LENGTH_LONG).show();
                        try {
                            String nickname = data.getString("senderNicknamead");
                            String message = data.getString("messagead");
                            Toast.makeText(FourthActivity.this, nickname, Toast.LENGTH_SHORT).show();
                            PrivateMessage m = new PrivateMessage(nickname,message);
                            grpList.add(m);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
            savedData();
        super.onBackPressed();
    }

    private void savedData() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedprefered",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(grpList);
        editor.putString("group", json);
        editor.apply();
    }

    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("sharedprefered",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("group",null);
        Type type = new TypeToken<ArrayList<PrivateMessage>>() {}.getType();
       grpList = gson.fromJson(json,type);

        if(grpList == null){
            grpList = new ArrayList<>();
        }
    }
}
