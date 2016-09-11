package com.smbtv.model;

public class SMBConfig {

    private String hostname;
    private String sharename;
    private String domain;
    private int sessionPort;
    private int tcpIpSmbPort;
    private int serverPort;
    private int datagramPort;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getSharename() {
        return sharename;
    }

    public void setSharename(String sharename) {
        this.sharename = sharename;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getSessionPort() {
        return sessionPort;
    }

    public void setSessionPort(int sessionPort) {
        this.sessionPort = sessionPort;
    }

    public int getTcpIpSmbPort() {
        return tcpIpSmbPort;
    }

    public void setTcpIpSmbPort(int tcpIpSmbPort) {
        this.tcpIpSmbPort = tcpIpSmbPort;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getDatagramPort() {
        return datagramPort;
    }

    public void setDatagramPort(int datagramPort) {
        this.datagramPort = datagramPort;
    }

    @Override
    public String toString() {
        return "SMBConfig{" +
                "hostname='" + hostname + '\'' +
                ", sharename='" + sharename + '\'' +
                ", domain='" + domain + '\'' +
                ", sessionPort=" + sessionPort +
                ", tcpIpSmbPort=" + tcpIpSmbPort +
                ", serverPort=" + serverPort +
                ", datagramPort=" + datagramPort +
                '}';
    }

    @Override
    public boolean equals(Object other) {

        SMBConfig c = (SMBConfig) other;
        
        return hostname.equals(c.hostname)
                && sharename.equals(c.sharename)
                && domain.equals(c.domain)
                && sessionPort == c.sessionPort
                && tcpIpSmbPort == c.tcpIpSmbPort
                && serverPort == c.serverPort
                && datagramPort == c.datagramPort;
    }

}