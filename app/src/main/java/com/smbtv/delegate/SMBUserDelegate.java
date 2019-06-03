package com.smbtv.delegate;

import android.content.Context;

import com.smbtv.delegate.base.SMBUserDAO;
import com.smbtv.model.SMBShare;
import com.smbtv.model.SMBUser;

import java.util.List;

public class SMBUserDelegate {

    private SMBUserDAO mDao;

    public SMBUserDelegate() {

        Context context = ApplicationDelegate.getContext();
        this.mDao = new SMBUserDAO(context);
    }

    public List<SMBUser> findAll() {

        return mDao.findAll();
    }

    public void insert(SMBUser user) {

        mDao.insert(user);
    }

    public SMBUser findById(int idUser) {

        return mDao.findById(idUser);
    }

    public List<SMBShare> findShares(SMBUser user) {

        return mDao.findShares(user);
    }

    public void update(SMBUser user) {

        mDao.update(user);
    }

    public void delete(SMBUser mUser) {

        mDao.delete(mUser);
    }
}
