package com.smbtv.delegate;

import android.content.Context;

import com.smbtv.delegate.base.SMBConfigDAO;
import com.smbtv.model.SMBConfig;

public class SMBConfigDelegate {

    private SMBConfigDAO mDao;

    public SMBConfigDelegate() {

        Context context = ApplicationDelegate.getContext();
        this.mDao = new SMBConfigDAO(context);
    }

    public SMBConfig get() {

        return mDao.find();
    }

    public void update(SMBConfig config) {

        mDao.update(config);
    }
}
