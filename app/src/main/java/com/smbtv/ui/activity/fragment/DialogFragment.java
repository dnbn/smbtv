package com.smbtv.ui.activity.fragment;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.util.Log;

import com.smbtv.R;
import com.smbtv.ui.activity.handler.DialogHandler;
import com.smbtv.ui.activity.tools.GuidedActionBuilder;
import com.smbtv.ui.components.DialogConfig;

import java.util.List;

public class DialogFragment extends GuidedStepFragment {

    private static final String TAG = DialogFragment.class.getName();

    private DialogConfig mConfig;
    private DialogHandler mHandler;

    public void setConfig(DialogConfig config) {
        this.mConfig = config;
    }

    public void setHandler(DialogHandler mHandler) {
        this.mHandler = mHandler;
    }

    @NonNull
    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {

        Log.d(TAG, "onCreateGuidance");

        Drawable icon = getActivity().getDrawable(R.drawable.ic_warning);
        return new GuidanceStylist.Guidance(mConfig.getTitle(), mConfig.getDescription(), mConfig.getBeatcrumb(), icon);
    }

    @Override
    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {

        Log.d(TAG, "onCreateActions");

        for (DialogConfig.DialogItem item : mConfig.getItems()) {
            actions.add(GuidedActionBuilder.text(item.getId(), item.getTitle(), item.getDescription()));
        }
    }

    @Override
    public void onGuidedActionClicked(GuidedAction action) {

        Log.d(TAG, "onGuidedActionClicked");

        for (DialogConfig.DialogItem item : mConfig.getItems()) {
            if (item.getId() == action.getId()) {
                mHandler.onSelection(item);
                break;
            }
        }
    }

}
