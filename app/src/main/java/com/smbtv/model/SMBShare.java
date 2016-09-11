package com.smbtv.model;

public class SMBShare {

    public enum AccessMode {
        READ_ONLY(0),
        WRITEABLE(1);

        private final int value;

        AccessMode(int value) {

            this.value = value;
        }

        public String toStringValue() {

            return Integer.toString(this.value);
        }
    }

    private int id;
    private String path;
    private String name;
    private AccessMode accessMode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccessMode getAccessMode() {
        return accessMode;
    }

    public void setAccessMode(AccessMode accessMode) {
        this.accessMode = accessMode;
    }
}
