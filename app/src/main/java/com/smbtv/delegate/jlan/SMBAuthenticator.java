package com.smbtv.delegate.jlan;

import org.alfresco.jlan.server.SrvSession;
import org.alfresco.jlan.server.auth.ClientInfo;
import org.alfresco.jlan.server.auth.LocalAuthenticator;

public class SMBAuthenticator extends LocalAuthenticator {

    @Override
    public int authenticateUser(ClientInfo client, SrvSession sess, int alg) {

        return AUTH_ALLOW;
    }
}
