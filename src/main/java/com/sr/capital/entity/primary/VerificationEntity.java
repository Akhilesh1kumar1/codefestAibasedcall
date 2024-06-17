package com.sr.capital.entity.primary;

import com.sr.capital.helpers.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "verification")
public class VerificationEntity extends LongBaseEntity {

    @Builder.Default
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private VerificationType type = VerificationType.OTP;

    @Column(name = "sr_company_id")
    private String srCompanyId;

    @Column(name = "data")
    private String data;

    @Column(name = "callback")
    @Enumerated(EnumType.STRING)
    private CallbackType callback;

    @Builder.Default
    @Column(name = "channel")
    @Enumerated(EnumType.STRING)
    private CommunicationChannels channel = CommunicationChannels.SMS;

    @Builder.Default
    @Column(name = "request_counter")
    private Integer requestCounter = 1;

    @Builder.Default
    @Column(name = "failed_counter")
    private Integer failedCounter = 0;

    @Builder.Default
    @Column(name = "expires_at")
    private LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(15);



    @Builder.Default
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private VerificationStatus status = VerificationStatus.PROCESSING;

    @Column(name = "remarks")
    private String remarks;

    public boolean hasExpired(LocalDateTime currentTime) {
        return getExpiresAt().isBefore(currentTime);
    }
}
