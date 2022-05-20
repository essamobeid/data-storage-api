package com.github.api.model;

public class RepositoryEntity {

    private String oid;
    private String data;
    private int size;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public RepositoryEntity(String oid, int size, String data) {
        this.oid = oid;
        this.size = size;
        this.data = data;
    }
}
