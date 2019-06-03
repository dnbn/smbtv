package com.smbtv.delegate;

import android.content.Context;

import com.smbtv.delegate.base.SMBShareDAO;
import com.smbtv.model.SMBShare;
import com.smbtv.model.SMBUser;

import java.util.List;

public class SMBShareDelegate {

    private SMBShareDAO mDao;

    public SMBShareDelegate() {

        Context context = ApplicationDelegate.getContext();
        this.mDao = new SMBShareDAO(context);
    }

    public void delete(SMBShare share) {

        mDao.delete(share);
    }

    public void insert(SMBShare share) {

        mDao.insert(share);
    }

    public List<SMBShare> findAll() {

        return mDao.findAll();
    }

    public SMBShare findById(int idShare) {

        return mDao.findById(idShare);
    }

    public void update(SMBShare share) {

        mDao.update(share);
    }

    public List<SMBUser> findUsers(SMBShare share) {

        return mDao.findUsers(share);
    }
}
