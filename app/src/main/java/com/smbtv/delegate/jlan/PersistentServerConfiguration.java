package com.smbtv.delegate.jlan;

import android.util.Log;

import com.smbtv.delegate.SMBConfigDelegate;
import com.smbtv.delegate.SMBShareDelegate;
import com.smbtv.delegate.exception.ServerConfigurationException;
import com.smbtv.model.SMBConfig;
import com.smbtv.model.SMBShare;

import org.alfresco.config.element.GenericConfigElement;
import org.alfresco.jlan.debug.ConsoleDebug;
import org.alfresco.jlan.debug.DebugConfigSection;
import org.alfresco.jlan.server.auth.CifsAuthenticator;
import org.alfresco.jlan.server.auth.UserAccountList;
import org.alfresco.jlan.server.auth.acl.DefaultAccessControlManager;
import org.alfresco.jlan.server.config.CoreServerConfigSection;
import org.alfresco.jlan.server.config.GlobalConfigSection;
import org.alfresco.jlan.server.config.InvalidConfigurationException;
import org.alfresco.jlan.server.config.SecurityConfigSection;
import org.alfresco.jlan.server.config.ServerConfiguration;
import org.alfresco.jlan.server.core.DeviceContextException;
import org.alfresco.jlan.server.filesys.DiskDeviceContext;
import org.alfresco.jlan.server.filesys.DiskInterface;
import org.alfresco.jlan.server.filesys.DiskSharedDevice;
import org.alfresco.jlan.server.filesys.FilesystemsConfigSection;
import org.alfresco.jlan.smb.server.CIFSConfigSection;
import org.alfresco.jlan.smb.server.disk.JavaFileDiskDriver;

import java.util.List;

import cryptix.jce.provider.CryptixCrypto;

public class PersistentServerConfiguration extends ServerConfiguration {

    private static final String TAG = PersistentServerConfiguration.class.getName();

    private static final int DefaultThreadPoolInit = 25;
    private static final int DefaultThreadPoolMax = 50;

    private static final int[] DefaultMemoryPoolBufSizes = {256, 4096, 16384, 66000};
    private static final int[] DefaultMemoryPoolInitAlloc = {20, 20, 5, 5};
    private static final int[] DefaultMemoryPoolMaxAlloc = {100, 50, 50, 50};

    private final SMBConfigDelegate mSMBConfigDelegate;
    private final SMBShareDelegate mSMBShareDelegate;

    private SecurityConfigSection mSecurityConfig;
    private DebugConfigSection mDebugConfig;
    private CoreServerConfigSection mCoreConfig;
    private GlobalConfigSection mGlobalConfig;
    private FilesystemsConfigSection mConfigSection;

    public PersistentServerConfiguration() throws InvalidConfigurationException, DeviceContextException {

        super(null);

        mSMBConfigDelegate = new SMBConfigDelegate();
        mSMBShareDelegate = new SMBShareDelegate();

        SMBConfig userConfig = mSMBConfigDelegate.get();
        setServerName(userConfig.getHostname());

        initDebugConfiguration();
        initCoreServerConfigSection();
        initGlobalConfigSection();
        initSecurityConfiguration();
        initFilesystemsConfiguration();
        initCIFSConfiguration();

    }

    private void initFilesystemsConfiguration() throws DeviceContextException {

        mConfigSection = new FilesystemsConfigSection(this);

        final GenericConfigElement driverCE = new GenericConfigElement("driver");
        final GenericConfigElement localPathCE = new GenericConfigElement("LocalPath");
        localPathCE.setValue(".");
        driverCE.addChild(localPathCE);

        List<SMBShare> shares = mSMBShareDelegate.findAll();

        for (SMBShare share : shares) {
            addShare(share);
        }
    }

    private void initSecurityConfiguration() throws InvalidConfigurationException {

        mSecurityConfig = new SecurityConfigSection(this);

        DefaultAccessControlManager accessControlManager = new DefaultAccessControlManager();
        accessControlManager.setDebug(true);
        mSecurityConfig.setAccessControlManager(DefaultAccessControlManager.class.getName(), new GenericConfigElement("aclManager"));
        mSecurityConfig.setJCEProvider(CryptixCrypto.class.getName());

        final UserAccountList userAccounts = new UserAccountList();
        mSecurityConfig.setUserAccounts(userAccounts);
    }

    private void initDebugConfiguration() throws InvalidConfigurationException {

        mDebugConfig = new DebugConfigSection(this);

        final GenericConfigElement debugCE = new GenericConfigElement("output");
        final GenericConfigElement logLevelCE = new GenericConfigElement("logLevel");
        logLevelCE.setValue("Debug");

        mDebugConfig.setDebug(ConsoleDebug.class.getName(), debugCE);
    }

    private void initCoreServerConfigSection() throws InvalidConfigurationException {

        mCoreConfig = new CoreServerConfigSection(this);
        mCoreConfig.setMemoryPool(DefaultMemoryPoolBufSizes, DefaultMemoryPoolInitAlloc, DefaultMemoryPoolMaxAlloc);
        mCoreConfig.setThreadPool(DefaultThreadPoolInit, DefaultThreadPoolMax);
        mCoreConfig.getThreadPool().setDebug(true);
    }


    private void initGlobalConfigSection() {

        mGlobalConfig = new GlobalConfigSection(this);
    }

    private void initCIFSConfiguration() throws InvalidConfigurationException {

        CIFSConfigSection cifsConfig = new CIFSConfigSection(this);

        SMBConfig userConfig = mSMBConfigDelegate.get();

        cifsConfig.setSessionPort(userConfig.getSessionPort());
        cifsConfig.setTcpipSMBPort(userConfig.getTcpIpSmbPort());
        cifsConfig.setNameServerPort(userConfig.getServerPort());
        cifsConfig.setDatagramPort(userConfig.getDatagramPort());
        cifsConfig.setServerName(userConfig.getHostname());
        cifsConfig.setDomainName(userConfig.getDomain());

        cifsConfig.setHostAnnounceInterval(5);
        cifsConfig.setHostAnnouncer(true);

        final GenericConfigElement authenticatorCE = new GenericConfigElement("authenticator");
        cifsConfig.setAuthenticator(SMBAuthenticator.class.getName(), authenticatorCE, CifsAuthenticator.SHARE_MODE, true);

        cifsConfig.setHostAnnounceDebug(true);
        cifsConfig.setNetBIOSDebug(true);
        cifsConfig.setSessionDebugFlags(-1);
        cifsConfig.setTcpipSMB(true);
    }

    private void addShare(SMBShare share) {

        if (mConfigSection.getShares().findShare(share.getName()) == null) {

            DiskInterface diskInterface = new JavaFileDiskDriver();
            DiskDeviceContext diskDeviceContext = new DiskDeviceContext(share.getPath(), share.getName());
            DiskSharedDevice diskDev = new DiskSharedDevice(share.getName(), diskInterface, diskDeviceContext);

            diskDev.setAccessControlList(mSecurityConfig.getGlobalAccessControls());
            try {
                diskDeviceContext.startFilesystem(diskDev);
            } catch (DeviceContextException e) {

                Log.e(TAG, Log.getStackTraceString(e));
                throw new ServerConfigurationException(e);
            }
            mConfigSection.addShare(diskDev);

        }
    }

    public void registerShare(SMBShare share) {

        addShare(share);
        mSMBShareDelegate.insert(share);
    }

    public void unregisterShare(SMBShare share) {

        mConfigSection.getShares().deleteShare(share.getName());
        mSMBShareDelegate.delete(share);
    }

    public void renameShare(int id, String newName) {

        SMBShare share = mSMBShareDelegate.findById(id);
        mConfigSection.getShares().deleteShare(share.getName());

        share.setName(newName);
        addShare(share);

        mSMBShareDelegate.update(share);
    }
}
