package com.codeclass4u.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class WeatherDetail {

    private Map<String,String> coord;
    private List<Weather> weather;
    private String base;
    private Map<String,String> main;
    private String visibility;
    private Map<String,String> wind;
    private Map<String,String> clouds;
    private String dt;
    private Map<String,String> sys;
    private String timezone;
    private String id;
    private String name;
    private String cod;

}
