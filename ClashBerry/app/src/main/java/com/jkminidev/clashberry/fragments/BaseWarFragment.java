package com.jkminidev.clashberry.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.jkminidev.clashberry.models.WarData;

public abstract class BaseWarFragment extends Fragment {
    protected static final String ARG_WAR_DATA = "war_data";
    protected WarData warData;

    protected void extractWarData(Bundle arguments) {
        if (arguments != null) {
            // In a real implementation, you would serialize/deserialize WarData
            // For now, we'll pass it through a static field or use Parcelable
        }
    }
}