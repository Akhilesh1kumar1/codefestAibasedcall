package com.sr.capital.entity.mongo.kyc.child;

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
public class BankDocDetails implements Serializable {

    @Field("account_name")
    private String accountName;

    @Field("account_no")
    private String accountNo;

    @Field("micr_code")
    private String micrCode;

    @Field("micr_cheque_number")
    private String micrChequeNumber;

    @Field("date_of_issue")
    private String dateOfIssue;

    @Field("bank_name")
    private String bankName;

    @Field("ifsc_code")
    private String ifscCode;

    @Field("bank_address")
    private String bankAddress;

    @Field("is_scanned")
    private Boolean isScanned;

    @Field("images")
    private List<String> images;

    @Field("bank_account_type")
    private String bankAccountType;


    String statementPassword;

    Boolean isAccountVerified = false;

    String bankAccountUserName;

    String bankAccountPassword;


}
