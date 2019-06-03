package com.smbtv.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepFragment;

import com.smbtv.R;
import com.smbtv.ui.activity.fragment.EditUserFragment;

public class EditUserActivity extends Activity {

    public static String ID_USER = "element";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edituser);

        if (null == savedInstanceState) {

            final EditUserFragment fragment = new EditUserFragment();
            int idUser = getIntent().getIntExtra(ID_USER, Integer.MIN_VALUE);
            fragment.defineUser(idUser);

            GuidedStepFragment.addAsRoot(this, fragment, android.R.id.content);
        }
    }
}
