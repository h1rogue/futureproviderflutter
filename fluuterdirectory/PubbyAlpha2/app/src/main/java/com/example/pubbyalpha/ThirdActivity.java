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


public class ThirdActivity extends AppCompatActivity {
    private EditText writemsg;
    private Button sendbutt;
    private Socket socket;
    private PrivateAdapter adapter;
    private RecyclerView recyclerViewed;

    private String destinationid;
    String message;
    String usernamed,yourname;
    private ArrayList<PrivateMessage> privateMessageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);




        ChatAplication chatAplication = (ChatAplication) getApplication();
        socket = chatAplication.getSocket();

        Intent intent = getIntent();
        usernamed = intent.getStringExtra("username");
        destinationid = intent.getStringExtra("destiny");
        yourname = intent.getStringExtra("yourname");
        writemsg = findViewById(R.id.messagesed);
        sendbutt = findViewById(R.id.sendingss);
        loadData();

        recyclerViewed = findViewById(R.id.recyclmess);


        final LinearLayoutManager layoutManager = new LinearLayoutManager(ThirdActivity.this);
        recyclerViewed.setLayoutManager(layoutManager);
        adapter = new PrivateAdapter(privateMessageList);
        recyclerViewed.setAdapter(adapter);
        if(adapter.getItemCount() != 0){
            layoutManager.smoothScrollToPosition(recyclerViewed,null,adapter.getItemCount() - 1);
        }


//        recyclerViewed.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                if(bottom<oldBottom){
//                    final int lastAdapterItem = adapter.getItemCount() - 1;
//                    recyclerViewed.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            int recyclerViewPositionOffset = -1000000;
//                            View bottomView = layoutManager.findViewByPosition(lastAdapterItem);
//                            if (bottomView != null) {
//                                recyclerViewPositionOffset = 0 - bottomView.getHeight();
//                            }
//                            layoutManager.scrollToPositionWithOffset(lastAdapterItem, recyclerViewPositionOffset);
//                        }
//                    });
//                }
//            }
//        });

        sendbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = writemsg.getText().toString().trim();
                if(TextUtils.isEmpty(message)){
                    writemsg.setError("Write message");
                }else{
                    PrivateMessage m = new PrivateMessage(yourname,message);
                    privateMessageList.add(m);
                    adapter.notifyDataSetChanged();
                    socket.emit("message",destinationid,message);
                    writemsg.setText("");
                }

            }
        });


        socket.on("newmessage", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
           runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   JSONObject data = (JSONObject) args[0];
                   try {
                       String message = data.getString("message");
                       PrivateMessage m = new PrivateMessage(usernamed,message);
                    //   Toast.makeText(ThirdActivity.this,message,Toast.LENGTH_LONG).show();
                       privateMessageList.add(m);
                       adapter.notifyDataSetChanged();
                       layoutManager.smoothScrollToPosition(recyclerViewed,null,adapter.getItemCount() - 1);

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

   //     Toast.makeText(getApplicationContext(), "On back pressed", Toast.LENGTH_SHORT).show();
    }

    private void savedData() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedprefered",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(privateMessageList);
        editor.putString(usernamed, json);
        editor.apply();
    }

    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("sharedprefered",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(usernamed,null);
        Type type = new TypeToken<ArrayList<PrivateMessage>>() {}.getType();
        privateMessageList = gson.fromJson(json,type);

        if(privateMessageList == null){
            privateMessageList = new ArrayList<>();
        }
    }
}
