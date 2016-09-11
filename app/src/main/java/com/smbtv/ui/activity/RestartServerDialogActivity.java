package com.smbtv.ui.activity;

import android.util.Log;

import com.smbtv.R;
import com.smbtv.delegate.SMBServerDelegate;
import com.smbtv.ui.components.DialogConfig;

public class RestartServerDialogActivity extends GenericDialogActivity {

    private static final String TAG = RestartServerDialogActivity.class.getName();

    private DialogConfig.DialogItem mItemRestart;
    private DialogConfig.DialogItem mItemSkip;

    @Override
    public void onSelection(DialogConfig.DialogItem itemSelected) {

        Log.d(TAG, "onSelection");

        if (itemSelected.getId() == mItemRestart.getId()) {
            SMBServerDelegate service = SMBServerDelegate.getInstance();
            if (service.isServerActive()) {
                service.restartServer();
            }
        }

        finish();
    }

    protected DialogConfig createDialog() {

        Log.d(TAG, "createDialog");

        DialogConfig config = new DialogConfig();

        mItemRestart = new DialogConfig.DialogItem(0, getString(R.string.settins_restart_ok));
        mItemSkip = new DialogConfig.DialogItem(1, getString(R.string.settins_restart_skip));

        config.setTitle(getString(R.string.settings_configuration_changes));
        config.setDescription(getString(R.string.settings_restart_server));
        config.addItem(mItemRestart);
        config.addItem(mItemSkip);

        return config;
    }
}
