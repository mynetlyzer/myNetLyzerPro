package com.keepics.mynetlyzer;


import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends PreferenceFragment {


    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }


}
