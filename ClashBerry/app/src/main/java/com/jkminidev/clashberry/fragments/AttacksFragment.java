package com.jkminidev.clashberry.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jkminidev.clashberry.R;
import com.jkminidev.clashberry.adapters.MemberAdapter;
import com.jkminidev.clashberry.models.WarData;

public class AttacksFragment extends Fragment {
    private static WarData staticWarData;
    
    public static AttacksFragment newInstance(WarData warData) {
        AttacksFragment fragment = new AttacksFragment();
        staticWarData = warData;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_members_list, container, false);
        
        RecyclerView recyclerView = view.findViewById(R.id.membersRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        if (staticWarData != null) {
            MemberAdapter adapter = new MemberAdapter(getContext());
            adapter.updateMembers(staticWarData.getClan().getMembers());
            recyclerView.setAdapter(adapter);
        }
        
        return view;
    }
}