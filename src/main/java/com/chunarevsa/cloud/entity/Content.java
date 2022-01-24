package com.chunarevsa.cloud.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/*  <Contents>
            <Key>file_2015-08-06.txt</Key>
            <LastModified>2020-11-30T09:38:22.000Z</LastModified>
            <ETag>"090228db8da1203d89d73341c95932b4"</ETag>
            <Size>11</Size>
        <Owner>
            <ID>14fbada9d6aac53a2d851e6c777ffea7cd9ac4d213bee68af9f5d9b247c20c04</ID>
            <DisplayName>malammik</DisplayName>
        </Owner>
        <StorageClass>STANDARD</StorageClass>
    </Contents> */

@Entity
@Table(name = "CONTENT")
public class Content {
    
    @Id
    @Column(name = "CONTENT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CONTENT_KEY")
    private String contentKey; 

    @Column(name = "LAST_MODIFIDED")
    private String lastModifided;

    @Column(name = "ETAG")
    private String eTag;

    @Column(name = "SIZE")
    private String size;

    @Column(name = "TYPE", nullable = false)
	@Enumerated (EnumType.STRING)
    private StorageClass storageClass;

    @JsonIgnore
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "OWNER_ID", insertable = false, updatable = false)
    private Owner owner;

    public Content() {
    }

    public Content(Long id, String contentKey, String lastModifided, String eTag, String size, StorageClass storageClass, Owner owner) {
        this.id = id;
        this.contentKey = contentKey;
        this.lastModifided = lastModifided;
        this.eTag = eTag;
        this.size = size;
        this.storageClass = storageClass;
        this.owner = owner;
    }


    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getContentKey() {
        return this.contentKey;
    }

    public void setContentKey(String contentKey) {
        this.contentKey = contentKey;
    }

    public String getLastModifided() {
        return this.lastModifided;
    }

    public void setLastModifided(String lastModifided) {
        this.lastModifided = lastModifided;
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

    public StorageClass getStorageClass() {
        return this.storageClass;
    }

    public void setStorageClass(StorageClass storageClass) {
        this.storageClass = storageClass;
    }

    public Owner getOwner() {
        return this.owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", contentKey='" + getContentKey() + "'" +
            ", lastModifided='" + getLastModifided() + "'" +
            ", eTag='" + getETag() + "'" +
            ", size='" + getSize() + "'" +
            ", storageClass='" + getStorageClass() + "'" +
            ", owner='" + getOwner() + "'" +
            "}";
    }

} 
