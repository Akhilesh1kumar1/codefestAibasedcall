package com.sr.capital.entity.mongo.kyc.child;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PanDocDetails implements Serializable {

    @Field("name_on_card")
    private String nameOnCard;

    @Field("fathers_name")
    private String fathersName;

    @Field("date_of_birth")
    private String dateOfBirth;

    @Field("age")
    private String age;

    @Field("minor")
    private boolean minor;

    @Field("id_number")
    private String idNumber;

    @Field("pan_type")
    private String panType;

    @Field("date_of_issue")
    private String dateOfIssue;

    @Field("is_scanned")
    private boolean isScanned;

}
