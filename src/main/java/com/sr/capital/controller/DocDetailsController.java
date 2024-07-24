package com.sr.capital.controller;


import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.kyc.dto.request.BankDetailsRequest;
import com.sr.capital.kyc.dto.request.DocDetailsRequest;
import com.sr.capital.kyc.dto.request.UpdateDocsDetailsRequest;
import com.sr.capital.kyc.dto.request.VerifyGstOtpRequest;
import com.sr.capital.kyc.service.DocDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/kyc/doc")
@Validated
public class DocDetailsController {

    @Autowired
    private DocDetailsService docDetailsService;

    @PostMapping(value = "/fetch-doc-details", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> fetchTenantDocDetailsList(
        @RequestBody DocDetailsRequest docDetailsRequest)
        throws CustomException {
        return docDetailsService.fetchDocDetailsByTenantId(docDetailsRequest);
    }

    @PostMapping(value = "/bank-details", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> insertBankDetails(@RequestBody BankDetailsRequest bankDetailsRequest) throws CustomException {
        return docDetailsService.insertBankDetails(bankDetailsRequest);
    }

    @PostMapping(value = "/fetch-gst-details", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> fetchCompleteGstDetails(
            @RequestBody DocDetailsRequest docDetailsRequest)
            throws CustomException {
        return docDetailsService.fetchGstDocByTenantId(docDetailsRequest);
    }


   /* @PostMapping(value = "/update", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updateDetails(@RequestBody UpdateDocsDetailsRequest updateDocsDetailsRequest) throws Exception {
        DocVerificationResponseKafkaPayload payload = docDetailsService.updateDocDetails(updateDocsDetailsRequest);
        GenericResponse<DocVerificationResponseKafkaPayload> response = new GenericResponse<>();
        response.setStatusCode(HttpStatusCode.OK);
        response.setData(payload);

        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }*/

    @GetMapping(value = "/verified-doc-details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> fetchVerifiedDocData(@RequestParam(name = "tenant_id") String tenantId) throws CustomException {
        return docDetailsService.verifiedDocData(tenantId);
    }

}
