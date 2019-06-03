package com.smbtv.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepFragment;

import com.smbtv.R;
import com.smbtv.ui.activity.fragment.AddUserFragment;

public class AddUserActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adduser);

        if (null == savedInstanceState) {
            GuidedStepFragment.addAsRoot(this, new AddUserFragment(), android.R.id.content);
        }
    }
}
