package com.founder.easy_route_assistant.Service;

import com.founder.easy_route_assistant.DTO.ChargerDTO;
import com.founder.easy_route_assistant.DTO.ConvenientDTO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.*;

@Service
public class ChargerService {
    @Value("${SEOUL_URL}")
    private String SEOUL_URL;

    @Value("${SEOUL_KEY}")
    private String SEOUL_APPKEY;

    public List<ConvenientDTO> requestChargerAPI(String target) { // target = 시군구코드
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(SEOUL_URL);

        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
                .build();

        WebClient webClient = WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .uriBuilderFactory(factory)
                .baseUrl(SEOUL_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String getstring = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(SEOUL_APPKEY)
                        .path("/json/tbElecWheelChrCharge/1/500/")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // List<ChargerDTO> chargerDTOList = new ArrayList<>();
        List<ConvenientDTO> convenientDTOS = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        JSONArray row = null;
        try {
            JSONObject object = (JSONObject) jsonParser.parse(getstring);
            JSONObject fullObject = (JSONObject) object.get("tbElecWheelChrCharge");
            row = (JSONArray) fullObject.get("row");
            for (Object o : row) {
                JSONObject element = (JSONObject) o;
                String signgunm = (String) element.get("SIGNGUNM");
                if (Objects.equals(target, signgunm)) {
                    String latitude = (String) element.get("LATITUDE"); // 위도
                    String longitude = (String) element.get("LONGITUDE"); // 경도
                    String placeDescript = (String) element.get("INSTLLCDESC");
                    String weekStart = (String) element.get("WEEKDAYOPEROPENHHMM");
                    String weekEnd = (String) element.get("WEEKDAYOPERCOLSEHHMM");
                    String satStart = (String) element.get("SATOPEROPEROPENHHMM");
                    String satEnd = (String) element.get("SATOPERCLOSEHHMM");
                    String holiStart = (String) element.get("HOLIDAYOPEROPENHHMM");
                    String holiEnd = (String) element.get("HOLIDAYCLOSEOPENHHMM");

                    String weekday = weekStart + " - " + weekEnd;
                    String saturday = satStart + " - " + satEnd;
                    String holiday = holiStart + " - " + holiEnd;

                    double lat = Double.parseDouble(latitude);
                    double lon = Double.parseDouble(longitude);
                    Point point = new Point(lon, lat);

                    ConvenientDTO convenientDTO = ConvenientDTO.builder()
                            .convenientType("charger")
                            .point(point)
                            .description(placeDescript)
                            .weekday(weekday)
                            .saturday(saturday)
                            .holiday(holiday)
                            .build();

                    convenientDTOS.add(convenientDTO);

                    /*ChargerDTO chargerDTO = ChargerDTO.builder()
                            .point(point)
                            .placeDescript(placeDescript)
                            .weekStart(weekStart)
                            .weekEnd(weekEnd)
                            .satStart(satStart)
                            .satEnd(satEnd)
                            .holiStart(holiStart)
                            .holiEnd(holiEnd)
                            .build();

                    chargerDTOList.add(chargerDTO);*/
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return convenientDTOS;
    }
}
