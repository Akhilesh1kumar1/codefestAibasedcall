package com.sr.capital.repository.primary;

import com.sr.capital.entity.primary.ProviderUrlConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProviderUrlConfigEntityRepository extends JpaRepository<ProviderUrlConfigEntity,Long> {

    List<ProviderUrlConfigEntity> findByPartnerIdAndPartnerConfigGroupsAndIsEnabled(Long partnerId, String group, Boolean enabled);

    List<ProviderUrlConfigEntity> findByPartnerIdAndPartnerConfigGroups(Long partnerId, String group );

}
