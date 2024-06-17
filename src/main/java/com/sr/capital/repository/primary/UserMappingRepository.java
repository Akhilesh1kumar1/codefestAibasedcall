package com.sr.capital.repository.primary;

import com.sr.capital.entity.primary.UserMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMappingRepository extends JpaRepository<UserMapping,Long> {

}
