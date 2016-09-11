package com.smbtv.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v17.leanback.app.GuidedStepFragment;

import com.smbtv.R;
import com.smbtv.ui.activity.fragment.ShareFragment;

public class ShareActivity extends Activity {

    public static String ID_SHARE = "element";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        if (null == savedInstanceState) {

            int idShare = getIntent().getIntExtra(ID_SHARE, Integer.MIN_VALUE);
            ShareFragment shareFragment = new ShareFragment();
            shareFragment.defineShare(idShare);

            GuidedStepFragment.addAsRoot(this, shareFragment, android.R.id.content);
        }
    }
}