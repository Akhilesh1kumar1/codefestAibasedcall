package com.sr.capital.repository;

import com.sr.capital.entity.ProviderTemplateConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProviderTemplateConfigRepository extends JpaRepository<ProviderTemplateConfigEntity,Long> {

    List<ProviderTemplateConfigEntity> findByPartnerIdAndGroupAndIsEnabled(Long partnerId, String group, Boolean enabled);

    List<ProviderTemplateConfigEntity> findByPartnerIdAndGroupAndTypeAndIsEnabled(Long partnerId, String group, String type, Boolean enabled);
}
