package com.sr.capital.repository;

import com.sr.capital.entity.CompanyKycDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyKycRepository extends JpaRepository<CompanyKycDetails,Long> {
}
