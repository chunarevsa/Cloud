package com.chunarevsa.cloud.repository;

import java.util.Optional;

import com.chunarevsa.cloud.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentsRepository extends JpaRepository<Content, Long>  {

    Optional<Content> findByContentKey(String contentKey);

} 
