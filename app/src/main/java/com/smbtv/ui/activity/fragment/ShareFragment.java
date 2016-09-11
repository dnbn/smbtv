package com.smbtv.ui.activity.fragment;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.util.Log;

import com.smbtv.R;
import com.smbtv.delegate.SMBServerDelegate;
import com.smbtv.delegate.SMBShareDelegate;
import com.smbtv.model.SMBShare;
import com.smbtv.ui.activity.tools.GuidedActionBuilder;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ShareFragment extends GuidedStepFragment {

    private static final String TAG = ShareFragment.class.getName();

    private final int ACTION_NAME = 0;
    private final int ACTION_REMOVE = 1;
    private final int ACTION_CONTINUE = 2;

    private final SMBServerDelegate mSmbService;
    private final SMBShareDelegate mShareDelegate;
    private SMBShare mShare;
    private String editedName;

    public ShareFragment() {

        mSmbService = SMBServerDelegate.getInstance();
        mShareDelegate = new SMBShareDelegate();
    }

    public void defineShare(int idShare) {

        mShare = mShareDelegate.findById(idShare);
    }

    @NonNull
    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {

        Log.d(TAG, "onCreateGuidance");

        Drawable icon = getActivity().getDrawable(R.drawable.ic_folder);
        return new GuidanceStylist.Guidance(getString(R.string.share_title) + " " + mShare.getName(), mShare.getPath(), null, icon);
    }

    @Override
    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {

        Log.d(TAG, "onCreateActions");

        actions.add(GuidedActionBuilder.editText(ACTION_NAME, getString(R.string.share_name), mShare.getName()));
        actions.add(GuidedActionBuilder.text(ACTION_REMOVE, getString(R.string.share_remove), ""));
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

        if (ACTION_REMOVE == action.getId()) {

            mSmbService.removeShare(mShare);
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();

        } else if (ACTION_CONTINUE == action.getId()) {

            if (!StringUtils.isEmpty(editedName) && !editedName.equals(mShare.getName())) {
                mSmbService.renameShare(mShare.getId(), editedName);
            }
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }

    @Override
    public long onGuidedActionEditedAndProceed(GuidedAction action) {

        Log.d(TAG, "onGuidedActionEditedAndProceed : " + action.getTitle());

        onActionEdited(action);

        return GuidedAction.ACTION_ID_CURRENT;
    }

    public void onGuidedActionEditCanceled(GuidedAction action) {

        Log.d(TAG, "onGuidedActionEditCanceled : " + action.getTitle());

        onActionEdited(action);
    }

    private void onActionEdited(GuidedAction action) {

        String value = action.getDescription().toString();

        if (ACTION_NAME == action.getId()) {

            if (!StringUtils.isEmpty(value)) {
                editedName = value;
            }
        }
    }

}