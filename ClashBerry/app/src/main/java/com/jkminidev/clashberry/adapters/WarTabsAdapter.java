package com.jkminidev.clashberry.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.jkminidev.clashberry.fragments.AttacksFragment;
import com.jkminidev.clashberry.fragments.DefensesFragment;
import com.jkminidev.clashberry.fragments.OverviewFragment;
import com.jkminidev.clashberry.fragments.RosterFragment;
import com.jkminidev.clashberry.models.WarData;

public class WarTabsAdapter extends FragmentStateAdapter {
    
    private WarData warData;
    private static final int NUM_TABS = 4;

    public WarTabsAdapter(@NonNull FragmentActivity fragmentActivity, WarData warData) {
        super(fragmentActivity);
        this.warData = warData;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return OverviewFragment.newInstance(warData);
            case 1:
                return AttacksFragment.newInstance(warData);
            case 2:
                return DefensesFragment.newInstance(warData);
            case 3:
                return RosterFragment.newInstance(warData);
            default:
                return OverviewFragment.newInstance(warData);
        }
    }

    @Override
    public int getItemCount() {
        return NUM_TABS;
    }
}