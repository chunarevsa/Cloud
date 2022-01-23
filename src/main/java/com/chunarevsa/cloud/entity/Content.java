package com.chunarevsa.cloud.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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

    //private Owner owner;

    public Content() {
    }

    public Content(String contentKey, String lastModifided, String eTag, String size) {
        this.contentKey = contentKey;
        this.lastModifided = lastModifided;
        this.eTag = eTag;
        this.size = size;
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
  

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", contentKey='" + getContentKey() + "'" +
            ", lastModifided='" + getLastModifided() + "'" +
            ", eTag='" + getETag() + "'" +
            ", size='" + getSize() + "'" +
            "}";
    }



} 
