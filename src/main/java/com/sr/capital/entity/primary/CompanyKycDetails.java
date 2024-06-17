package com.sr.capital.entity.primary;

import com.sr.capital.config.AttributeEncryptor;
import com.sr.capital.helpers.enums.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import static com.sr.capital.helpers.constants.Constants.EntityNames.COMPANY_DETAILS;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = COMPANY_DETAILS)
public class CompanyKycDetails extends UUIDBaseEntity{

    @Column(name = "sr_company_id")
    Long srCompanyId;

    @Column(name = "user_id")
    Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "company_type")
    CompanyType companyType;

    @Enumerated(EnumType.STRING)
    @Column(name = "proof_of_identity")
    ProofOfIdentity proofOfIdentity;

    @Column(name = "adhar_id")
    Long adharId;

    @Column(name = "pan_id")
    Long panId;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "identity_image_link")
    String proofOfIdentityImageLink;

    @Enumerated(EnumType.STRING)
    @Column(name = "proof_of_address")
    ProofOfAddress proofOfAddress;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "address_proof_link")
    String addressProofLink;

    @Enumerated(EnumType.STRING)
    @Column(name = "proof_of_business")
    ProofOfBusiness proofOfBusiness;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "business_proof_link")
    String businessProofLink;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "registration_certificate_link")
    String registrationCertificateLink;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "partnership_deed_link")
    String partnerShipDeedLink;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "pan_of_business_entity")
    String panOfBusinessEntity;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "poa_grade_to_transact")
    String poaGradeToTransact;

    @Enumerated(EnumType.STRING)
    @Column(name = "proof_of_current_address")
    ProofOfCurrentAddress proofOfCurrentAddress;


    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "proof_of_current_address_link")
    String proofOfCurrentAddressLink;

    @Enumerated(EnumType.STRING)
    @Column(name = "certificate_of_incorporation_type")
    CertificateOfIncorporationTypes certificateOfIncorporationTypes;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "certificate_of_incorporation_link")
    String certificateOfIncorporationLink;

    @Convert(converter = AttributeEncryptor.class)
    @Column(name = "memorandum_of_association_link")
    String memorandumOfAssociationLink;

    @Column(name = "article_of_association_link")
    String articleOfAssociationLink;
}
