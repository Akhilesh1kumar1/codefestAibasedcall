package com.sr.capital.entity.primary;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "rsa_keys")
public class RSAKeys extends LongBaseEntity {

    @Column(name = "public_key")
    @Lob
    private String publicKey;

    @Column(name = "private_key")
    @Lob
    private String privateKey;
}
