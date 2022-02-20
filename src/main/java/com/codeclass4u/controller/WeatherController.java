package com.codeclass4u.controller;

import com.codeclass4u.model.WeatherDetail;
import com.codeclass4u.service.WeatherLoadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    Logger log = LoggerFactory.getLogger(WeatherController.class);

    @Autowired
    WeatherLoadService weatherLoadService;

    @GetMapping(path="/show/{city}",produces= MediaType.APPLICATION_JSON_VALUE)
    public WeatherDetail showWeather(@PathVariable("city") String city){

        ResponseEntity response = null;
        WeatherDetail weatherDetail = new WeatherDetail();
        try{
            weatherDetail  = weatherLoadService.getCityDetails(city);
            log.info("Details fetched successfully.");
        }catch (Exception e){
            e.printStackTrace();
            log.error("Not able to get response.");
        }
        return weatherDetail;
    }
}
