package com.sr.capital.external.corpveda.dto.data;

import com.sr.capital.external.corpveda.entity.BasicDetails;
import com.sr.capital.external.corpveda.entity.ContactDetails;
import com.sr.capital.external.corpveda.entity.Director;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FreeDataToBeSavedInDocDetails {

    private ContactDetails contactDetails;
    private BasicDetails basicDetails;
    private List<Director> director;

}
