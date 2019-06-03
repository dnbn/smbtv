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
import android.widget.Toast;

import com.smbtv.R;
import com.smbtv.delegate.SMBUserDelegate;
import com.smbtv.model.SMBUser;
import com.smbtv.ui.activity.tools.GuidedActionBuilder;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class AddUserFragment extends GuidedStepFragment {

    private static final String TAG = AddUserFragment.class.getName();

    private SMBUserDelegate mUserDelegate = new SMBUserDelegate();

    private final int ACTION_LOGIN = 0;
    private final int ACTION_PASSWORD = 1;
    private final int ACTION_CONTINUE = 2;

    private String login;
    private String password;

    @NonNull
    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {

        Log.d(TAG, "onCreateGuidance");

        String title = getString(R.string.users_title);
        String description = getString(R.string.users_create_desc);
        Drawable icon = getActivity().getDrawable(R.drawable.ic_settings);
        return new GuidanceStylist.Guidance(title, description, "", icon);
    }

    @Override
    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {

        Log.d(TAG, "onCreateActions");

        actions.add(GuidedActionBuilder.editText(ACTION_LOGIN, getString(R.string.users_login), ""));
        actions.add(GuidedActionBuilder.editPassword(ACTION_PASSWORD, getString(R.string.users_password), ""));
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

            if (StringUtils.isEmpty(login) || StringUtils.isEmpty(password)) {
                Toast.makeText(getActivity(), getString(R.string.users_mandatory_fields), Toast.LENGTH_LONG).show();
            } else {
                boolean conflicted = false;
                final List<SMBUser> existingUsers = mUserDelegate.findAll();
                for (SMBUser user : existingUsers) {
                    if (user.getLogin().equalsIgnoreCase(login)) {
                        conflicted = true;
                        break;
                    }
                }

                if (conflicted) {
                    Toast.makeText(getActivity(), getString(R.string.users_exists), Toast.LENGTH_LONG).show();
                } else {
                    SMBUser user = new SMBUser();
                    user.setPassword(password);
                    user.setLogin(login);
                    mUserDelegate.insert(user);

                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                }
            }

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
            case ACTION_LOGIN:
                login = value;
                break;
            case ACTION_PASSWORD:
                password = value;
                break;
        }
    }


}
