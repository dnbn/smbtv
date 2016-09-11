package com.smbtv.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepFragment;

import com.smbtv.R;
import com.smbtv.ui.activity.fragment.SettingsFragment;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (null == savedInstanceState) {
            GuidedStepFragment.addAsRoot(this, new SettingsFragment(), android.R.id.content);
        }
    }
}
