package com.smbtv.ui.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.nononsenseapps.filepicker.FilePickerActivity;
import com.smbtv.R;
import com.smbtv.delegate.SMBServerDelegate;
import com.smbtv.model.SMBShare;
import com.smbtv.model.SMBUser;
import com.smbtv.model.ServerInfo;
import com.smbtv.ui.activity.AddUserActivity;
import com.smbtv.ui.activity.EditUserActivity;
import com.smbtv.ui.activity.SettingsActivity;
import com.smbtv.ui.activity.ShareActivity;
import com.smbtv.ui.components.MenuItem;

import org.alfresco.jlan.server.NetworkServer;
import org.alfresco.jlan.server.ServerListener;

public class MainFragmentEventsHandler {

    private static final String TAG = MainFragmentUIBuilder.class.getSimpleName();

    public static final int ADD_SHARE_RESULT = 0;
    public static final int EDIT_SHARE_RESULT = 1;
    public static final int EDIT_USER_RESULT = 2;

    private MainFragment mParent;
    private SMBServerDelegate mSmbService;
    private ServerListener mServerListener;

    public MainFragmentEventsHandler(MainFragment parent) {

        this.mParent = parent;
        this.mServerListener = new UIServerListener(mParent);
        this.mSmbService = SMBServerDelegate.getInstance();
    }

    public void onStartServerButtonClick() {

        Log.d(TAG, "onStartServerButtonClick");

        final Context context = mParent.getActivity().getApplicationContext();

        if (mParent.mBounded) {

            if (!mSmbService.isServerActive()) {
                mSmbService.startServer(mServerListener);
            } else {
                mParent.setupUIElements(MainFragment.UIAction.UPDATE_SERVER);
            }

        } else {
            String text = getLabel(R.string.error_service_unbounded);
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }

    }

    public void onSettingsButtonClick() {

        Log.d(TAG, "onSettingsButtonClick");

        Intent intent = new Intent(mParent.getActivity(), SettingsActivity.class);
        mParent.startActivity(intent);
    }

    public void onStopServerButtonClick() {

        Log.d(TAG, "onStopServerButtonClick");

        if (mSmbService.isServerActive()) {
            mSmbService.stopServer();
        }
    }

    public void onAddShareButtonClick() {

        Log.d(TAG, "onAddShareButtonClick");

        Intent intent = new Intent(mParent.getActivity(), FilePickerActivity.class);

        intent.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
        intent.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
        intent.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);
        intent.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());

        mParent.startActivityForResult(intent, ADD_SHARE_RESULT);
    }

    public void onShareButtonClick(MenuItem menuItem) {

        Log.d(TAG, "onShareButtonClick");

        SMBShare share = (SMBShare) menuItem.getElement();

        Intent intent = new Intent(mParent.getActivity(), ShareActivity.class);
        intent.putExtra(ShareActivity.ID_SHARE, share.getId());

        mParent.startActivityForResult(intent, EDIT_SHARE_RESULT);
    }

    public void onAddUserButtonClick() {

        Log.d(TAG, "onAddUserButtonClick");

        Intent intent = new Intent(mParent.getActivity(), AddUserActivity.class);
        mParent.startActivityForResult(intent, EDIT_USER_RESULT);
    }

    public void onUserButtonClick(MenuItem menuItem) {

        Log.d(TAG, "onUserButtonClick");

        SMBUser user = (SMBUser) menuItem.getElement();

        Intent intent = new Intent(mParent.getActivity(), EditUserActivity.class);
        intent.putExtra(ShareActivity.ID_SHARE, user.getId());

        mParent.startActivityForResult(intent, EDIT_USER_RESULT);
    }

    private String getLabel(int key) {

        return mParent.getString(key);
    }

    private class UIServerListener implements ServerListener {

        private MainFragment mParent;
        private SMBServerDelegate mSmbService;

        public UIServerListener(MainFragment parent) {

            this.mParent = parent;
            this.mSmbService = SMBServerDelegate.getInstance();
        }

        @Override
        public void serverStatusEvent(NetworkServer networkServer, int state) {

            String text = null;
            switch (state) {
                case ServerActive:
                    ServerInfo si = mSmbService.getServerInfo();
                    text = getLabel(R.string.server_started);
                    text = String.format(text, si.getIPv4(), si.getPort());
                    break;
                case ServerShutdown:
                    text = getLabel(R.string.server_shutdown);
                    break;
                case ServerError:
                    text = getLabel(R.string.server_error);
                    break;
            }

            if (text != null) {
                final String toastMessage = text;
                mParent.getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(mParent.getActivity(), toastMessage, Toast.LENGTH_LONG).show();
                    }
                });
            }
            mParent.setupUIElements(MainFragment.UIAction.UPDATE_SERVER);

        }

    }
}

