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

public class DefensesFragment extends Fragment {
    private static WarData staticWarData;
    
    public static DefensesFragment newInstance(WarData warData) {
        DefensesFragment fragment = new DefensesFragment();
        staticWarData = warData;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_content, container, false);
        
        TextView contentText = view.findViewById(R.id.tabContentText);
        contentText.setText("Defenses information will be displayed here.\n\nThis shows attacks made by the opponent clan against your clan members.");
        
        return view;
    }
}