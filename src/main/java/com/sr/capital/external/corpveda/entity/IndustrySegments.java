package com.sr.capital.external.corpveda.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IndustrySegments {

    @JsonProperty("has_industry_segments")
    private boolean hasIndustrySegments;
    @JsonProperty("industry_segment")
    private List<String> industrySegment;
}
