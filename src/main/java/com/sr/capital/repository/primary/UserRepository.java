package com.sr.capital.repository.primary;

import com.sr.capital.entity.primary.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    User findBySrUserId(Long userId);
}
