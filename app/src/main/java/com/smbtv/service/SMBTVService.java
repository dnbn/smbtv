package com.smbtv.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.smbtv.delegate.SMBServerDelegate;

public class SMBTVService extends Service {

    private static final String TAG = SMBTVService.class.getName();

    private IBinder mBinder;
    private SMBServerDelegate mSmbService;

    @Override
    public void onCreate() {

        super.onCreate();

        mBinder = new SMBTVBinder();
        mSmbService = SMBServerDelegate.getInstance();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    public class SMBTVBinder extends Binder {

        public SMBTVService getServiceInstance() {
            return SMBTVService.this;
        }
    }

    public SMBServerDelegate getSMBService() {

        return mSmbService;
    }


}
