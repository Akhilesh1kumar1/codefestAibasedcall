package com.sr.capital.repository.primary;

import com.sr.capital.entity.primary.RSAKeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface RSAKeysRepo extends JpaRepository<RSAKeys, Long> {

    @Query("SELECT ke from RSAKeys ke")
    RSAKeys findKeyPair();

}
