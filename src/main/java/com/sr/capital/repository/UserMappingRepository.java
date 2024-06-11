package com.sr.capital.repository;

import com.sr.capital.entity.UserMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMappingRepository extends JpaRepository<UserMapping,Long> {

}
