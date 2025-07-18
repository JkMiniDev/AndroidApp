package com.jkminidev.clashberry.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jkminidev.clashberry.R;
import com.jkminidev.clashberry.models.WarData;

public class OverviewFragment extends Fragment {
    private static WarData staticWarData; // Temporary solution for passing data
    
    public static OverviewFragment newInstance(WarData warData) {
        OverviewFragment fragment = new OverviewFragment();
        staticWarData = warData; // Store temporarily
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_content, container, false);
        
        TextView contentText = view.findViewById(R.id.tabContentText);
        
        if (staticWarData != null) {
            StringBuilder overview = new StringBuilder();
            overview.append("War Overview\n\n");
            overview.append("War State: ").append(staticWarData.getState()).append("\n");
            overview.append("Team Size: ").append(staticWarData.getTeamSize()).append("\n");
            overview.append("War Type: ").append(staticWarData.getWarType()).append("\n");
            
            if (staticWarData.getTimeRemaining() != null) {
                overview.append("Time Remaining: ").append(staticWarData.getTimeRemaining())
                        .append(" ").append(staticWarData.getTimeLabel()).append("\n");
            }
            
            overview.append("\nClan: ").append(staticWarData.getClan().getName()).append("\n");
            overview.append("Stars: ").append(staticWarData.getClan().getStars()).append("\n");
            overview.append("Destruction: ").append(staticWarData.getClan().getDestructionPercentage()).append("%\n");
            
            overview.append("\nOpponent: ").append(staticWarData.getOpponent().getName()).append("\n");
            overview.append("Stars: ").append(staticWarData.getOpponent().getStars()).append("\n");
            overview.append("Destruction: ").append(staticWarData.getOpponent().getDestructionPercentage()).append("%\n");
            
            contentText.setText(overview.toString());
        } else {
            contentText.setText("War Overview");
        }
        
        return view;
    }
}