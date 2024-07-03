package com.sr.capital.entity.mongo.kyc.child;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GstByPanDocDetails {

    List<GstDetails> gstDetails;

    @Data
    @Builder
    public static class GstDetails implements Serializable {

        private String authStatus;           // Authorization status
        private String gstinStatus;          // GSTIN status (Active/Inactive)
        private String applicationStatus;    // Current status of the application under GST (MIG, DFT, etc.)
        private String emailId;              // Email ID of the registered entity linked with the GSTIN
        private String gstinId;              // Unique 15-character GSTIN corresponding to the given PAN
        private String gstinRefId;           // Unique GST application reference ID
        private String mobNum;               // Mobile number of the registered entity linked with the GSTIN
        private String pan;                  // PAN number of the registered entity
        private String regType;              // Registration type under GST (V=VAT, S=Service Tax)
        private String registrationName;     // Registered name of the entity as per GST
        private String tinNumber;            // Old VAT or Service Tax Tin associated with the GSTIN
        private String state;
    }
}
