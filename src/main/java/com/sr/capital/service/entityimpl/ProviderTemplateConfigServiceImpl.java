package com.sr.capital.service.entityimpl;

import com.sr.capital.entity.ProviderTemplateConfigEntity;
import com.sr.capital.repository.ProviderTemplateConfigRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor(onConstructor =@__(@Autowired))
public class ProviderTemplateConfigServiceImpl {

    final ProviderTemplateConfigRepository providerTemplateConfigRepository;


    public List<ProviderTemplateConfigEntity> getProviderTemplate(String group, Long partnerId, Boolean enabled) {

        return providerTemplateConfigRepository.findByPartnerIdAndGroupAndIsEnabled(partnerId,group,enabled);
    }

    public List<ProviderTemplateConfigEntity> getProviderTemplate(String group, String type, Long partnerId,Boolean enabled) {
        return providerTemplateConfigRepository.findByPartnerIdAndGroupAndTypeAndIsEnabled(partnerId,group,type,enabled);
    }
}
