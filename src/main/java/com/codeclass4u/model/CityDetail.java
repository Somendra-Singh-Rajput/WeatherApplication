package com.codeclass4u.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CityDetail {
    private String name;
    private Map<String,String> local_names;
    private String lat;
    private String lon;
    private String country;
    private String state;
}
