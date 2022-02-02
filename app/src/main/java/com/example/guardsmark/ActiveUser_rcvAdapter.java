package com.example.guardsmark;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ActiveUser_rcvAdapter extends RecyclerView.Adapter<ActiveUser_rcvAdapter.ViewHolder>{

    Context context;
    List<UserInfoData> datas;
    private LayoutInflater mInflater;
    public ActiveUser_rcvAdapter(Context context, List<UserInfoData> datas) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.datas = datas;
    }

    @NonNull
    @Override
    public ActiveUser_rcvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_user_view_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ActiveUser_rcvAdapter.ViewHolder holder, int position) {
        holder.name.setText(datas.get(position).getName());
        holder.distance.setText(datas.get(position).getDistance() + " miles");
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, distance;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_tv);
            distance = itemView.findViewById(R.id.distance_tv);
        }
    }
}
