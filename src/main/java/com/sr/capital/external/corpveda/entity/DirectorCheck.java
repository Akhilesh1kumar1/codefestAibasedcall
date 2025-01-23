package com.sr.capital.external.corpveda.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DirectorCheck {

    @JsonProperty("status")
    private boolean hasDirectorCheck;
    @JsonProperty("director_check")
    private List<DirectorCheckDetail> directorCheck;
}
