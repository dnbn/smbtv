package com.smbtv.model;

import java.net.InetAddress;

public class ServerInfo {

    private int port;
    private String name;
    private String domain;
    private InetAddress[] addresses;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public InetAddress[] getAddresses() {
        return addresses;
    }

    public void setAddresses(InetAddress[] addresses) {
        this.addresses = addresses;
    }

    public String getIPv4() {

        String ipv4 = "";

        for (InetAddress address : addresses) {
            if (!address.isLoopbackAddress()) {
                String ip = address.getHostAddress();

                if (!ip.contains(":")) {
                    ipv4 = ip;
                    break;
                }
            }
        }

        return ipv4;
    }
}
