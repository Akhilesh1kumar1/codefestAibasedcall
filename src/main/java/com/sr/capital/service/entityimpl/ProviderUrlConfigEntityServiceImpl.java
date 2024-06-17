package com.sr.capital.service.entityimpl;

import com.sr.capital.entity.primary.ProviderUrlConfigEntity;
import com.sr.capital.repository.primary.ProviderUrlConfigEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor(onConstructor =@__(@Autowired))
public class ProviderUrlConfigEntityServiceImpl {

    final ProviderUrlConfigEntityRepository providerUrlConfigEntityRepository;


    public List<ProviderUrlConfigEntity> getUrlConfig(Long partnerId,String group ,Boolean enabled){
        return providerUrlConfigEntityRepository.findByPartnerIdAndPartnerConfigGroupsAndIsEnabled(partnerId,group,enabled);
    }

    public List<ProviderUrlConfigEntity> getUrlConfig(Long partnerId,String group){
        return providerUrlConfigEntityRepository.findByPartnerIdAndPartnerConfigGroups(partnerId,group);
    }
}
