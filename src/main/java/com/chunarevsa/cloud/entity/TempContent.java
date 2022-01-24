package com.chunarevsa.cloud.entity;

public class TempContent {
    
    private String contentKey;
    private String lastModified;
    private String eTag;
    private String size;
    private String bucketId;
    private String displayName;
    private String storageClass;

    public TempContent() {
    }

    public TempContent(String contentKey, String lastModified, String eTag, String size, String bucketId, String displayName, String storageClass) {
        this.contentKey = contentKey;
        this.lastModified = lastModified;
        this.eTag = eTag;
        this.size = size;
        this.bucketId = bucketId;
        this.displayName = displayName;
        this.storageClass = storageClass;
    }


    public String getContentKey() {
        return this.contentKey;
    }

    public void setContentKey(String contentKey) {
        this.contentKey = contentKey;
    }

    public String getLastModified() {
        return this.lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getETag() {
        return this.eTag;
    }

    public void setETag(String eTag) {
        this.eTag = eTag;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBucketId() {
        return this.bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getStorageClass() {
        return this.storageClass;
    }

    public void setStorageClass(String storageClass) {
        this.storageClass = storageClass;
    }

    @Override
    public String toString() {
        return "{" +
            " contentKey='" + getContentKey() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", eTag='" + getETag() + "'" +
            ", size='" + getSize() + "'" +
            ", bucketId='" + getBucketId() + "'" +
            ", displayName='" + getDisplayName() + "'" +
            ", storageClass='" + getStorageClass() + "'" +
            "}";
    }
    
}
