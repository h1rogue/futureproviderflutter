package com.example.pubbyalpha;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PrivateAdapter extends RecyclerView.Adapter<PrivateAdapter.MyHolder> {
    public PrivateAdapter(List<PrivateMessage> privateMessageListList) {
        this.privateMessageListList = privateMessageListList;
    }

    private List<PrivateMessage> privateMessageListList;
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_item,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        PrivateMessage message = privateMessageListList.get(i);
        myHolder.nickname.setText(message.getNickname());
        myHolder.message.setText(message.getMessage());
    }

    @Override
    public int getItemCount() {
        return privateMessageListList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public TextView nickname;
        public TextView message;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
          nickname  = itemView.findViewById(R.id.nicknames);
          message = itemView.findViewById(R.id.messagesew);
        }
    }
}
