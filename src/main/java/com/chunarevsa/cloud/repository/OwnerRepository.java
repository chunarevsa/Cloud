package com.chunarevsa.cloud.repository;

import com.chunarevsa.cloud.entity.Owner;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    
}
