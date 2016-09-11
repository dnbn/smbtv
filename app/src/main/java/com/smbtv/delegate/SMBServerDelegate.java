package com.smbtv.delegate;

import android.util.Log;

import com.smbtv.delegate.exception.ServiceInstancationException;
import com.smbtv.delegate.jlan.PersistentServerConfiguration;
import com.smbtv.model.SMBShare;
import com.smbtv.model.ServerInfo;

import org.alfresco.jlan.netbios.server.NetBIOSNameServer;
import org.alfresco.jlan.server.NetworkServer;
import org.alfresco.jlan.server.ServerListener;
import org.alfresco.jlan.smb.server.SMBServer;

public class SMBServerDelegate {

    private static final String TAG = SMBServerDelegate.class.getName();
    private static SMBServerDelegate INSTANCE = null;

    private PersistentServerConfiguration mCfg;
    private SMBServer mSmbServer;
    private InternalServerListener mSMBListener;
    private int mSMBServerState = -1;

    private SMBServerDelegate() {

        try {
            mCfg = new PersistentServerConfiguration();
        } catch (Exception e) {

            Log.e(TAG, Log.getStackTraceString(e));
            throw new ServiceInstancationException(e);
        }
    }

    public static SMBServerDelegate getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new SMBServerDelegate();
        }
        return INSTANCE;
    }

    public void startServer(ServerListener listener) {

        try {
            mSMBListener = new InternalServerListener(listener);

            NetBIOSNameServer netBIOSNameServer = new NetBIOSNameServer(mCfg);

            mCfg.addServer(netBIOSNameServer);
            mSmbServer = new SMBServer(mCfg);
            mCfg.addServer(mSmbServer);

            mSmbServer.addServerListener(mSMBListener);

            for (int i = 0; i < mCfg.numberOfServers(); i++) {
                NetworkServer server = mCfg.getServer(i);
                server.startServer();
            }

        } catch (Exception e) {

            Log.e(TAG, Log.getStackTraceString(e));
            throw new ServiceInstancationException(e);
        }
    }

    public ServerInfo getServerInfo() {

        ServerInfo serverInfo = new ServerInfo();

        serverInfo.setName(mSmbServer.getServerName());
        serverInfo.setPort(mSmbServer.getCIFSConfiguration().getTcpipSMBPort());
        serverInfo.setDomain(mSmbServer.getCIFSConfiguration().getDomainName());
        serverInfo.setAddresses(mSmbServer.getServerAddresses());

        return serverInfo;
    }

    public boolean isServerStarting() {

        return mSMBServerState == ServerListener.ServerStartup;
    }

    public boolean isServerActive() {

        return mSmbServer != null && mSmbServer.isActive();
    }

    public void restartServer() {

        ServerListener listener = null;

        if (isServerActive()) {
            stopServer();
            listener = mSMBListener;
        }

        startServer(listener);
    }

    public void stopServer() {

        for (int i = 0; i < mCfg.numberOfServers(); i++) {
            NetworkServer server = mCfg.getServer(i);
            server.shutdownServer(true);
        }
    }

    public void registerShare(SMBShare share) {

        mCfg.registerShare(share);
    }

    public void removeShare(SMBShare share) {

        mCfg.unregisterShare(share);
    }

    public void renameShare(int id, String newName) {

        mCfg.renameShare(id, newName);
    }

    private class InternalServerListener implements ServerListener {

        private ServerListener mDelegate;

        public InternalServerListener(ServerListener listener) {

            this.mDelegate = listener;
        }

        @Override
        public void serverStatusEvent(NetworkServer networkServer, int state) {

            mSMBServerState = state;

            if (this.mDelegate != null) {
                this.mDelegate.serverStatusEvent(networkServer, state);
            }
        }
    }
}
