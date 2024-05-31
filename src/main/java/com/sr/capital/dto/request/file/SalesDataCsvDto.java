package com.sr.capital.dto.request.file;

import com.opencsv.bean.CsvBindByName;
import lombok.Builder;

@Builder
public class SalesDataCsvDto {

    @CsvBindByName(column = "MID")
    private String mid;

    @CsvBindByName(column= "Vintage on platform (M)")
    private String vintageOnPlatform;

    @CsvBindByName(column= "org_type")
    private String orgType;

    @CsvBindByName(column= "business_type")
    private String businessType;

    @CsvBindByName(column= "revenue_2024-03_collectionAmount")
    private String revenue;

}
