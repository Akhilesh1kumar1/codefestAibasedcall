package com.sr.capital.repository.primary;

import com.sr.capital.entity.primary.ProviderTemplateConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProviderTemplateConfigRepository extends JpaRepository<ProviderTemplateConfigEntity,Long> {

    List<ProviderTemplateConfigEntity> findByPartnerIdAndGroupAndIsEnabled(Long partnerId, String group, Boolean enabled);

    List<ProviderTemplateConfigEntity> findByPartnerIdAndGroupAndTypeAndIsEnabled(Long partnerId, String group, String type, Boolean enabled);
}
