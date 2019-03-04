package com.example.sipkfinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class TanggapanActivity extends AppCompatActivity {
    private RecyclerView cRecyclerView;
    private ChatAdapter cAdapter;
    private RecyclerView.LayoutManager cLayoutManager;

    private ArrayList<ChatItem> chatItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tanggapan);

        chatItems = new ArrayList<>();
        cRecyclerView = findViewById(R.id.recycler_chat);

        cLayoutManager = new LinearLayoutManager(this);
        cAdapter = new ChatAdapter(chatItems);

        cRecyclerView.setLayoutManager(cLayoutManager);
        cRecyclerView.setAdapter(cAdapter);

        chatItems.add(new ChatItem("aa","bb","cc"));
        chatItems.add(new ChatItem("aa","bb","cc"));
        chatItems.add(new ChatItem("aa","bb","cc"));
        chatItems.add(new ChatItem("aa","bb","cc"));
        chatItems.add(new ChatItem("aa","bb","cc"));
        cAdapter.notifyDataSetChanged();
    }
}
