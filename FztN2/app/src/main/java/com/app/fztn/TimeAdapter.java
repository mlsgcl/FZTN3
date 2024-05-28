package com.app.fztn;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.TimeViewHolder> {
    private List<String> timeList;
    private OnItemListener onItemListener;

    public interface OnItemListener {
        void onItemClick(String time);
    }

    public TimeAdapter(List<String> timeList, OnItemListener onItemListener) {
        this.timeList = timeList;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public TimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time, parent, false);
        return new TimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeViewHolder holder, int position) {
        String time = timeList.get(position);
        holder.timeTextView.setText(time);
        holder.itemView.setOnClickListener(v -> onItemListener.onItemClick(time));
    }

    @Override
    public int getItemCount() {
        return timeList.size();
    }

    public static class TimeViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView;

        public TimeViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.textViewTime);
        }
    }
}
