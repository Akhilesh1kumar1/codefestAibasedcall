package com.sr.capital.repository;

import com.sr.capital.entity.AdharDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdharRepository extends JpaRepository<AdharDetails,Long> {
}
