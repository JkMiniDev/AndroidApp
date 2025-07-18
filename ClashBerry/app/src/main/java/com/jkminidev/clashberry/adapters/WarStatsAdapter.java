package com.jkminidev.clashberry.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jkminidev.clashberry.R;
import com.jkminidev.clashberry.models.WarStat;

import java.util.ArrayList;
import java.util.List;

public class WarStatsAdapter extends RecyclerView.Adapter<WarStatsAdapter.WarStatViewHolder> {
    
    private List<WarStat> warStats = new ArrayList<>();

    @NonNull
    @Override
    public WarStatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_war_stat, parent, false);
        return new WarStatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WarStatViewHolder holder, int position) {
        WarStat stat = warStats.get(position);
        holder.bind(stat);
    }

    @Override
    public int getItemCount() {
        return warStats.size();
    }

    public void updateStats(List<WarStat> newStats) {
        this.warStats.clear();
        this.warStats.addAll(newStats);
        notifyDataSetChanged();
    }

    static class WarStatViewHolder extends RecyclerView.ViewHolder {
        private TextView statValue;
        private TextView statLabel;

        public WarStatViewHolder(@NonNull View itemView) {
            super(itemView);
            statValue = itemView.findViewById(R.id.statValue);
            statLabel = itemView.findViewById(R.id.statLabel);
        }

        public void bind(WarStat stat) {
            statValue.setText(stat.getValue());
            statLabel.setText(stat.getLabel());
        }
    }
}