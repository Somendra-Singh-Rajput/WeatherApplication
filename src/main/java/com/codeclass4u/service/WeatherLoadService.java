package com.codeclass4u.service;

import com.codeclass4u.model.CityDetail;
import com.codeclass4u.model.WeatherDetail;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class WeatherLoadService {

    Logger log = LoggerFactory.getLogger(WeatherLoadService.class);

    RestTemplate restTemplate = new RestTemplate();

    RestTemplate restTemplate1 = new RestTemplate();

    @Value("${city.url}")
    private String cityUrl;

    @Value("${weather.api}")
    private String apiKey;

    @Value("${weather.url}")
    private String weatherUrl;

    public WeatherDetail getCityDetails(String city){

        ResponseEntity<String> response = null;
        List<CityDetail> cityDetailList = new ArrayList<>();

        WeatherDetail weatherDetail = new WeatherDetail();

        try {

            response  = restTemplate.getForEntity(cityUrl + "q=" + city + "&appid=" + apiKey, String.class);
            String str = response.getBody();
            cityDetailList = jsonToObject(str);

            log.info(String.valueOf(response));

            weatherDetail = getWeatherDetails(cityDetailList);

        }catch (HttpServerErrorException httpServerErrorException){

            httpServerErrorException.printStackTrace();
            log.error("",HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (JsonProcessingException e) {

            e.printStackTrace();
            log.info("Not able to parse Json response");

        }
        return weatherDetail;
    }

    public WeatherDetail getWeatherDetails(List<CityDetail> cityDetailList) throws JsonProcessingException {

        AtomicReference<WeatherDetail> weatherDetail = new AtomicReference<>(new WeatherDetail());

        String lat = null, lon = null;

        if(!cityDetailList.isEmpty()){

            cityDetailList.stream().filter(cityDetail -> cityDetail.getLat() != null && cityDetail.getLon() != null).forEach(cityDetail -> {

                ResponseEntity<String> responseEntity = restTemplate1.getForEntity(weatherUrl + "lat=" + cityDetail.getLat() + "&lon=" + cityDetail.getLon() + "&appid=" + apiKey, String.class);

                String str = responseEntity.getBody();

                try {
                        weatherDetail.set(jsonToObj(str));
                        log.info(String.valueOf(weatherDetail.get()));

                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    log.error("Not able to parse json response into WeatherDetail object.");
                }
            });
        }

        return weatherDetail.get();
    }

    public List<CityDetail> jsonToObject(String json) throws JsonProcessingException {

        ObjectMapper m = new ObjectMapper();
        m.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        List<CityDetail> cityDetailList = m.readValue(json, new TypeReference<List<CityDetail>>() {});

        return cityDetailList;
    }

    public WeatherDetail jsonToObj(String json) throws JsonProcessingException {

        ObjectMapper m = new ObjectMapper();
        m.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        WeatherDetail weatherDetail = m.readValue(json, new TypeReference<WeatherDetail>() {});

        return weatherDetail;
    }

}
