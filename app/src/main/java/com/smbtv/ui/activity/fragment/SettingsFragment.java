package com.smbtv.ui.activity.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.util.Log;
import android.widget.Toast;

import com.smbtv.R;
import com.smbtv.delegate.SMBConfigDelegate;
import com.smbtv.model.SMBConfig;
import com.smbtv.delegate.SMBServerDelegate;
import com.smbtv.ui.activity.RestartServerDialogActivity;
import com.smbtv.ui.activity.tools.GuidedActionBuilder;

import java.util.List;

public class SettingsFragment extends GuidedStepFragment {

    private static final String TAG = SettingsFragment.class.getName();

    private final int ACTION_HOSTNAME = 0;
    private final int ACTION_SHARENAME = 1;
    private final int ACTION_DOMAIN = 2;
    private final int ACTION_CONTINUE = 3;
    private final int ACTION_SERVERPORT = 4;
    private final int ACTION_SMBPORT = 5;
    private final int ACTION_SESSIONPORT = 6;
    private final int ACTION_DATAGRAM = 7;

    private SMBConfig mConfig;
    private SMBConfigDelegate mConfigDelegate = new SMBConfigDelegate();

    public SettingsFragment() {
        super();

        mConfig = mConfigDelegate.get();
    }

    @NonNull
    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {

        Log.d(TAG, "onCreateGuidance");

        String title = getString(R.string.settings_title);
        String description = getString(R.string.settings_desc);
        Drawable icon = getActivity().getDrawable(R.drawable.ic_settings);
        return new GuidanceStylist.Guidance(title, description, "", icon);
    }

    @Override
    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {

        Log.d(TAG, "onCreateActions");

        actions.add(GuidedActionBuilder.editText(ACTION_HOSTNAME, getString(R.string.settings_hostname), mConfig.getHostname()));
        actions.add(GuidedActionBuilder.editText(ACTION_SHARENAME, getString(R.string.settings_sharename), mConfig.getSharename()));
        actions.add(GuidedActionBuilder.editText(ACTION_DOMAIN, getString(R.string.settings_domain), mConfig.getDomain()));

        actions.add(GuidedActionBuilder.editNumber(ACTION_SMBPORT, getString(R.string.settings_smbport), mConfig.getTcpIpSmbPort()));
        actions.add(GuidedActionBuilder.editNumber(ACTION_SERVERPORT, getString(R.string.settings_serverport), mConfig.getServerPort()));
        actions.add(GuidedActionBuilder.editNumber(ACTION_SESSIONPORT, getString(R.string.settings_sessionport), mConfig.getSessionPort()));
        actions.add(GuidedActionBuilder.editNumber(ACTION_DATAGRAM, getString(R.string.settings_datagramport), mConfig.getDatagramPort()));
    }

    @Override
    public void onCreateButtonActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {

        Log.d(TAG, "onCreateButtonActions");
        super.onCreateButtonActions(actions, savedInstanceState);

        actions.add(new GuidedAction.Builder()
                .id(ACTION_CONTINUE)
                .title(getString(R.string.settings_validate))
                .description("")
                .build());
    }

    @Override
    public void onGuidedActionClicked(GuidedAction action) {

        Log.d(TAG, "onGuidedActionClicked");
        super.onGuidedActionClicked(action);

        if (ACTION_CONTINUE == action.getId()) {

            SMBConfig initialConfig = mConfigDelegate.get();

            if (!initialConfig.equals(mConfig)) {

                mConfigDelegate.update(mConfig);

                if (SMBServerDelegate.getInstance().isServerActive()) {
                    Intent dialog = new Intent(getActivity(), RestartServerDialogActivity.class);
                    startActivity(dialog);
                }
            }

            getActivity().finish();
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public long onGuidedActionEditedAndProceed(GuidedAction action) {

        Log.d(TAG, "onGuidedActionEditedAndProceed : " + action.getTitle());

        try {
            onActionEdited(action);
        } catch (Exception e) {

            return GuidedAction.ACTION_ID_CURRENT;
        }

        return super.onGuidedActionEditedAndProceed(action);
    }

    @Override
    public void onGuidedActionEditCanceled(GuidedAction action) {

        Log.d(TAG, "onGuidedActionEditCanceled : " + action.getTitle());

        onActionEdited(action);
    }

    private void onActionEdited(GuidedAction action) {

        String value = action.getDescription().toString();

        switch ((int) action.getId()) {
            case ACTION_HOSTNAME:
                mConfig.setHostname(value);
                break;
            case ACTION_SHARENAME:
                mConfig.setSharename(value);
                break;
            case ACTION_DOMAIN:
                mConfig.setDomain(value);
                break;
            case ACTION_SERVERPORT:
                int serverPort = getIntFromAction(action, mConfig.getServerPort());
                mConfig.setServerPort(serverPort);
                break;
            case ACTION_SESSIONPORT:
                int sessionPort = getIntFromAction(action, mConfig.getSessionPort());
                mConfig.setSessionPort(sessionPort);
                break;
            case ACTION_SMBPORT:
                int smbPort = getIntFromAction(action, mConfig.getTcpIpSmbPort());
                mConfig.setTcpIpSmbPort(smbPort);
                break;
            case ACTION_DATAGRAM:
                int dtPort = getIntFromAction(action, mConfig.getDatagramPort());
                mConfig.setDatagramPort(dtPort);
                break;
        }
    }

    private int getIntFromAction(GuidedAction action, int originalValue) {

        try {
            return Integer.parseInt(action.getDescription().toString());

        } catch (NumberFormatException e) {

            action.setDescription(Integer.toString(originalValue));
            Toast.makeText(getActivity(), getString(R.string.settings_numberformat), Toast.LENGTH_SHORT).show();
            throw e;
        }
    }
}