package com.sr.capital.repository;

import com.sr.capital.entity.DirectorKycEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectorKycRepository  extends JpaRepository<DirectorKycEntity,Long> {
}
