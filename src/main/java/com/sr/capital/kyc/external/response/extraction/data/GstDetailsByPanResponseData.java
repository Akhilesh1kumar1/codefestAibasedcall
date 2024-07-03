package com.sr.capital.kyc.external.response.extraction.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GstDetailsByPanResponseData {

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
