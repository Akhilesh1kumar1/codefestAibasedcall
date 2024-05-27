package com.sr.capital.dto.request.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.opencsv.bean.CsvBindByPosition;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RowFailureReport {



    @CsvBindByPosition(position = 0)
    String action;

    @CsvBindByPosition(position = 1)
    String reason;

    @CsvBindByPosition(position = 2)
    String errorMessage;

    //TODO


    //TODO
    public RowFailureReport(String[] values, String locationId, String errorMessage) {
       /* this.locationId = StringUtils.isNotEmpty(locationId) ?locationId : StringUtils.EMPTY;
        this.sku = StringUtils.isNotEmpty(values[0]) ? values[0] : StringUtils.EMPTY;
        this.quantity = StringUtils.isNotEmpty(values[1]) ? values[1] : StringUtils.EMPTY;*/
        this.action = StringUtils.isNotEmpty(values[2]) ? values[2] : StringUtils.EMPTY;
        this.reason = StringUtils.isNotEmpty(values[3]) ? values[3] : StringUtils.EMPTY;
        this.errorMessage = errorMessage;
    }

    //TODO
    public RowFailureReport(String[] values, String errorMessage) {
        this.action = StringUtils.isNotEmpty(values[3]) ? values[3] : StringUtils.EMPTY;
        this.reason = StringUtils.isNotEmpty(values[4]) ? values[4] : StringUtils.EMPTY;
        this.errorMessage = errorMessage;
    }

    //TODO
    public RowFailureReport(String[] values) {

        this.action = StringUtils.isNotEmpty(values[3]) ? values[3] : StringUtils.EMPTY;
        this.reason = StringUtils.isNotEmpty(values[4]) ? values[4] : StringUtils.EMPTY;
        this.errorMessage = StringUtils.isNotEmpty(values[5]) ? values[5] : StringUtils.EMPTY;
    }

}
