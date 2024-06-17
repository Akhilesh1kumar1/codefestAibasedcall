package com.sr.capital.repository.primary;

import com.sr.capital.entity.primary.CompanyKycDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyKycRepository extends JpaRepository<CompanyKycDetails,Long> {
}
