package com.sr.capital.repository;

import com.sr.capital.entity.EnachLinkingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EnachLinkingRepository extends JpaRepository<EnachLinkingEntity, UUID> {


}
