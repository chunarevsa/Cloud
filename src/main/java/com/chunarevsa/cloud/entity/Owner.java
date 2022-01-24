package com.chunarevsa.cloud.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "OWNER")
public class Owner {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OWNER_ID")
    private Long id;

    @Column(name = "BUCKET_ID")
    private String bucketId;

    @Column(name = "DISPLAY_NAME")
    private String displayName;

    @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "OWNER_ID")
    private Set<Content> contents = new HashSet<>();
    
    public Owner() {
    }

    public Owner(Long id, String bucketId, String displayName, Set<Content> contents) {
        this.id = id;
        this.bucketId = bucketId;
        this.displayName = displayName;
        this.contents = contents;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<Content> getContents() {
        return this.contents;
    }

    public void setContents(Set<Content> contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", bucketId='" + getBucketId() + "'" +
            ", displayName='" + getDisplayName() + "'" +
            ", contents='" + getContents() + "'" +
            "}";
    }

}
