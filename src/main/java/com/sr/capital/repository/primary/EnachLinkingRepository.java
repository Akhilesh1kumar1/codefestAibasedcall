package com.sr.capital.repository.primary;

import com.sr.capital.entity.primary.EnachLinkingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EnachLinkingRepository extends JpaRepository<EnachLinkingEntity, UUID> {


}
