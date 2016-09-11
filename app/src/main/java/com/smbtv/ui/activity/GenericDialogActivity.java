package com.smbtv.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepFragment;

import com.smbtv.R;
import com.smbtv.ui.activity.fragment.DialogFragment;
import com.smbtv.ui.activity.handler.DialogHandler;
import com.smbtv.ui.components.DialogConfig;

public abstract class GenericDialogActivity extends Activity implements DialogHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dialog);

        if (null == savedInstanceState) {

            DialogFragment fragment = new DialogFragment();
            fragment.setConfig(createDialog());
            fragment.setHandler(this);

            GuidedStepFragment.add(getFragmentManager(), fragment);
        }
    }

    protected abstract DialogConfig createDialog();
}
