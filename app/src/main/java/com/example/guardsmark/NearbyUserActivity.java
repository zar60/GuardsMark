package com.example.guardsmark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.guardsmark.databinding.ActivityNearbyUserBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NearbyUserActivity extends AppCompatActivity {

    List<UserInfoData> datas;
    ActiveUser_rcvAdapter adapter;
    private ActivityNearbyUserBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNearbyUserBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        UserInfoData data = new UserInfoData("John Doe", 23.4);
        datas = Collections.nCopies(20, data);
        Log.e("ANIK", datas.get(10).getName());

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ActiveUser_rcvAdapter(this, datas);
        recyclerView.setAdapter(adapter);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NearbyUserActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}