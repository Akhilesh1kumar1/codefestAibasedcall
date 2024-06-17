package com.sr.capital.repository.primary;

import com.sr.capital.entity.primary.DirectorKycEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectorKycRepository  extends JpaRepository<DirectorKycEntity,Long> {
}
