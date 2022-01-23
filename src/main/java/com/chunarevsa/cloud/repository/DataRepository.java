package com.chunarevsa.cloud.repository;

import com.chunarevsa.cloud.entity.Contents;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DataRepository extends JpaRepository<Contents, Long>  {

}
