package com.smbtv.ui.activity.fragment;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.smbtv.R;
import com.smbtv.delegate.SMBServerDelegate;
import com.smbtv.delegate.SMBShareDelegate;
import com.smbtv.delegate.SMBUserDelegate;
import com.smbtv.model.SMBShare;
import com.smbtv.model.SMBUser;
import com.smbtv.ui.activity.tools.GuidedActionBuilder;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ShareFragment extends GuidedStepFragment {

    private static final String TAG = ShareFragment.class.getName();

    private final int ACTION_NAME = 0;
    private final int ACTION_REMOVE = 1;
    private final int ACTION_CONTINUE = 2;
    private final int ACTION_USER = 3;

    private final SMBServerDelegate mSmbService;
    private final SMBShareDelegate mShareDelegate;
    private final SMBUserDelegate mSMBUserDelegate;

    private SMBUser mUser;
    private SMBShare mShare;
    private String editedName;

    public ShareFragment() {

        mSmbService = SMBServerDelegate.getInstance();
        mShareDelegate = new SMBShareDelegate();
        mSMBUserDelegate = new SMBUserDelegate();
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
        addUsers(actions);
        actions.add(GuidedActionBuilder.text(ACTION_REMOVE, getString(R.string.share_remove), ""));
    }

    private void addUsers(@NonNull List<GuidedAction> actions) {

        final List<SMBUser> usrs = mShareDelegate.findUsers(mShare);
        if (usrs.size() > 0) {
            mUser = usrs.get(0);
        }

        List<GuidedAction> selectionActions = new ArrayList<>();
        final List<SMBUser> users = mSMBUserDelegate.findAll();
        selectionActions.add(GuidedActionBuilder.checkedSubAction(-1, getString(R.string.share_public), null, mUser == null));
        for (SMBUser user : users) {
            boolean checked = false;
            if (mUser != null && mUser.getId() == user.getId()) {
                checked = true;
            }
            selectionActions.add(GuidedActionBuilder.checkedSubAction(user.getId(), user.getLogin(), null, checked));
        }

        final String desc = mUser == null ? getString(R.string.share_public) : mUser.getLogin();

        actions.add(GuidedActionBuilder.dropDownAction(ACTION_USER, getString(R.string.share_user), desc, selectionActions));
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
    public boolean onSubGuidedActionClicked(GuidedAction action) {

        final long idAction = action.getId();
        if (idAction > 0) {
            mUser = mSMBUserDelegate.findById((int) idAction);
        } else {
            mUser = null;
        }


        findActionById(ACTION_USER).setDescription("test");
        findActionById(ACTION_NAME).setDescription("toto");

        getActionItemView(0).postInvalidate();
        getActionItemView(1).postInvalidate();
        getActionItemView(0).invalidate();;
        getActionItemView(1).invalidate();

        return true;
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