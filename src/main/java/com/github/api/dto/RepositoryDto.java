package com.github.api.dto;
import com.github.api.model.RepositoryEntity;

public class RepositoryDto {
    private String oid;
    private int size;

    public RepositoryDto(RepositoryEntity repositoryEntity){
        oid = repositoryEntity.getOid();
        size = repositoryEntity.getSize();
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
}
