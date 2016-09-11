package com.smbtv.ui.activity.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.util.Log;

import com.smbtv.R;
import com.smbtv.delegate.SMBServerDelegate;
import com.smbtv.model.SMBShare;
import com.smbtv.service.SMBTVService;
import com.smbtv.ui.components.MenuItem;

import org.apache.commons.lang3.StringUtils;

import java.io.File;


public class MainFragment extends BrowseFragment {

    private static final String TAG = MainFragment.class.getSimpleName();

    private Intent mIntent;
    boolean mBounded;
    private SMBTVService mService;

    private ArrayObjectAdapter mRowsAdapter;
    private ListRow mItemShares;
    private ListRow mItemServer;

    public enum UIAction {INIT, UPDATE_SHARES, UPDATE_SERVER}

    public ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceDisconnected(ComponentName name) {

            mBounded = false;
            mService = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {

            mBounded = true;
            SMBTVService.SMBTVBinder binder = (SMBTVService.SMBTVBinder) service;
            mService = binder.getServiceInstance();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        mIntent = new Intent(getActivity(), SMBTVService.class);
    }

    @Override
    public void onStart() {

        Log.d(TAG, "onStart");
        super.onStart();

        getActivity().bindService(mIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        Log.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        setupUIElements(UIAction.INIT);
        bindEvents();
    }

    public void setupUIElements(UIAction action) {

        Log.d(TAG, "setupUIElements");

        MainFragmentUIBuilder uiB = new MainFragmentUIBuilder(getActivity());

        if (action == UIAction.INIT) {
            mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

            setTitle(getString(R.string.app_name));

            setHeadersState(HEADERS_ENABLED);
            setHeadersTransitionOnBackEnabled(true);

            setBrandColor(getResources().getColor(R.color.fastlane_background));
            setSearchAffordanceColor(getResources().getColor(R.color.search_opaque));

            mItemShares = uiB.buildShares();
            mItemServer = uiB.buildServerConfiguration();

            mRowsAdapter.add(mItemShares);
            mRowsAdapter.add(mItemServer);

            setAdapter(mRowsAdapter);

        } else if (action == UIAction.UPDATE_SERVER) {

            mItemServer = uiB.buildServerConfiguration();
            mRowsAdapter.replace(1, mItemServer);

        } else if (action == UIAction.UPDATE_SHARES) {

            mItemShares = uiB.buildShares();
            mRowsAdapter.replace(0, mItemShares);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MainFragmentEventsHandler.ADD_SHARE_RESULT && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();

            if (uri != null) {

                String path = uri.getPath();

                SMBShare share = new SMBShare();
                share.setPath(path);
                share.setName(StringUtils.substringAfterLast(path, File.separator));
                share.setAccessMode(SMBShare.AccessMode.WRITEABLE);
                if (StringUtils.isEmpty(share.getName())) {
                    share.setName(path);
                }

                SMBServerDelegate service = SMBServerDelegate.getInstance();
                service.registerShare(share);

                setupUIElements(UIAction.UPDATE_SHARES);
            }
        } else if (requestCode == MainFragmentEventsHandler.EDIT_SHARE_RESULT && resultCode == Activity.RESULT_OK) {

            setupUIElements(UIAction.UPDATE_SHARES);
        }
    }

    private void bindEvents() {

        Log.d(TAG, "bindEvents");

        final MainFragment caller = this;
        setOnItemViewClickedListener(new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {

                Log.d(TAG, "onItemClicked");

                MainFragmentEventsHandler maeh = new MainFragmentEventsHandler(caller);
                MenuItem menuItem = (MenuItem) item;

                switch (menuItem.getAction()) {
                    case Settings:
                        maeh.onSettingsButtonClick();
                        break;
                    case StartServer:
                        maeh.onStartServerButtonClick();
                        break;
                    case StopServer:
                        maeh.onStopServerButtonClick();
                        break;
                    case AddShare:
                        maeh.onAddShareButtonClick();
                        break;
                    case Share:
                        maeh.onShareButtonClick(menuItem);
                        break;
                }
            }
        });

    }

    @Override
    public void onStop() {

        Log.d(TAG, "onStop");

        super.onStop();

        if (mBounded) {
            getActivity().unbindService(mConnection);
            mBounded = false;
        }

    }


}
