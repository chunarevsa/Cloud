package com.chunarevsa.cloud.repository;

import java.util.Optional;

import com.chunarevsa.cloud.entity.Owner;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

    Optional<Owner> findByBucketId(String bucketId);
    
}
