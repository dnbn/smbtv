package com.smbtv.ui.activity.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v17.leanback.app.GuidedStepFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.util.Log;

import com.smbtv.R;
import com.smbtv.delegate.SMBUserDelegate;
import com.smbtv.model.SMBShare;
import com.smbtv.model.SMBUser;
import com.smbtv.ui.activity.tools.GuidedActionBuilder;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class EditUserFragment extends GuidedStepFragment {

    private static final String TAG = EditUserFragment.class.getName();

    private final int ACTION_PASSWORD = 0;
    private final int ACTION_DELETE = 1;
    private final int ACTION_CONTINUE = 2;

    private SMBUser mUser;
    private List<SMBShare> mShares;
    private SMBUserDelegate mUserDelegate = new SMBUserDelegate();

    public void defineUser(int idUser) {

        mUser = mUserDelegate.findById(idUser);
        mShares = mUserDelegate.findShares(mUser);
    }

    @NonNull
    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {

        Log.d(TAG, "onCreateGuidance");

        String title = mUser.getLogin();
        String description = mShares.size() + " " + getString(R.string.user_shares_count);
        Drawable icon = getActivity().getDrawable(R.drawable.ic_settings);
        return new GuidanceStylist.Guidance(title, description, "", icon);
    }

    @Override
    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {

        Log.d(TAG, "onCreateActions");

        actions.add(GuidedActionBuilder.editText(ACTION_PASSWORD, getString(R.string.users_password), mUser.getPassword()));

        if (mShares.size() == 0) {
            actions.add(new GuidedAction.Builder()
                    .id(ACTION_DELETE)
                    .title(getString(R.string.users_delete))
                    .description("")
                    .build());
        }
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

            mUserDelegate.update(mUser);
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();

        } else if (ACTION_DELETE == action.getId()) {

            mUserDelegate.delete(mUser);
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }

    }

    @SuppressLint("LongLogTag")
    @Override
    public long onGuidedActionEditedAndProceed(GuidedAction action) {

        Log.d(TAG, "onGuidedActionEditedAndProceed : " + action.getTitle());

        onActionEdited(action);
        return GuidedAction.ACTION_ID_CURRENT;
    }

    private void onActionEdited(GuidedAction action) {

        String value = action.getDescription().toString();

        if (ACTION_PASSWORD == action.getId()) {
            if (!StringUtils.isEmpty(value)) {
                mUser.setPassword(value);
            }
        }
    }
}
