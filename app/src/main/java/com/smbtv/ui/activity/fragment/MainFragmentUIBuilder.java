package com.smbtv.ui.activity.fragment;


import android.app.Activity;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.util.Log;

import com.smbtv.R;
import com.smbtv.delegate.SMBServerDelegate;
import com.smbtv.delegate.SMBShareDelegate;
import com.smbtv.delegate.SMBUserDelegate;
import com.smbtv.model.SMBShare;
import com.smbtv.model.SMBUser;
import com.smbtv.ui.components.MenuAction;
import com.smbtv.ui.components.MenuItem;
import com.smbtv.ui.components.MenuItemPresenter;

import java.util.List;

public class MainFragmentUIBuilder {

    private static final String TAG = MainFragmentUIBuilder.class.getSimpleName();

    private Activity mParent;
    private int cpt;

    public MainFragmentUIBuilder(Activity parent) {

        this.mParent = parent;
    }

    public ListRow buildShares() {

        Log.d(TAG, "buildShares");

        ArrayObjectAdapter rowAdapter = new ArrayObjectAdapter(new MenuItemPresenter());

        rowAdapter.add(new MenuItem(MenuAction.AddShare, getLabel(R.string.add), R.drawable.ic_add));

        final SMBShareDelegate shareDeleg = new SMBShareDelegate();
        List<SMBShare> shares = shareDeleg.findAll();

        for (SMBShare share : shares) {
            MenuItem menuItem = new MenuItem();
            menuItem.setAction(MenuAction.Share);
            menuItem.setTitle(share.getName());
            menuItem.setIcon(R.drawable.ic_folder);
            menuItem.setElement(share);
            rowAdapter.add(menuItem);
        }

        HeaderItem header = new HeaderItem(cpt++, getLabel(R.string.share));

        return new ListRow(header, rowAdapter);
    }

    public ListRow buildUsers() {

        Log.d(TAG, "buildUsers");

        ArrayObjectAdapter rowAdapter = new ArrayObjectAdapter(new MenuItemPresenter());

        rowAdapter.add(new MenuItem(MenuAction.AddUser, getLabel(R.string.add), R.drawable.ic_add));

        final SMBUserDelegate userDeleg = new SMBUserDelegate();
        List<SMBUser> users = userDeleg.findAll();

        for (SMBUser user : users) {
            MenuItem menuItem = new MenuItem();
            menuItem.setAction(MenuAction.User);
            menuItem.setTitle(user.getLogin());
            menuItem.setIcon(R.drawable.ic_user);
            menuItem.setElement(user);
            rowAdapter.add(menuItem);
        }
        HeaderItem header = new HeaderItem(cpt++, getLabel(R.string.users_title));

        return new ListRow(header, rowAdapter);
    }

    public ListRow buildServerConfiguration() {

        Log.d(TAG, "buildServerConfiguration");

        ArrayObjectAdapter rowAdapter = new ArrayObjectAdapter(new MenuItemPresenter());

        final SMBServerDelegate service = SMBServerDelegate.getInstance();
        if (service.isServerActive() || service.isServerStarting()) {
            rowAdapter.add(new MenuItem(MenuAction.StopServer, getLabel(R.string.stop), R.drawable.ic_stop));
        } else {
            rowAdapter.add(new MenuItem(MenuAction.StartServer, getLabel(R.string.start), R.drawable.ic_start));
        }

        rowAdapter.add(new MenuItem(MenuAction.Settings, getLabel(R.string.settings), R.drawable.ic_settings));

        HeaderItem header = new HeaderItem(cpt++, getLabel(R.string.server));

        return new ListRow(header, rowAdapter);
    }

    private String getLabel(int key) {

        return mParent.getString(key);
    }
}
