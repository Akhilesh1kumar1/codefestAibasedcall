package com.sr.capital.repository;

import com.sr.capital.entity.PANDetails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PANRepository extends JpaRepository<PANDetails,Long> {

    Optional<PANDetails> findByUserId (Long userId);

    Boolean existsByUserId (Long userId);
}
