package com.founder.easy_route_assistant.Service.Convenient;

import com.founder.easy_route_assistant.DTO.Convenient.ConvenientDTO;
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

import java.util.ArrayList;
import java.util.List;

@Service
public class BathroomService {
    @Value("${BATHROOM_URL}")
    private String BATHROOM_URL;

    @Value("${BATHROOM_APPKEY}")
    private String BATHROOM_APPKEY;

    public List<ConvenientDTO> requestBathroomAPI() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(BATHROOM_URL);

        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
                .build();

        WebClient webClient = WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .uriBuilderFactory(factory)
                .baseUrl(BATHROOM_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String headerValue = "Infuser " + BATHROOM_APPKEY;

        String getstring = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("?page=1&perPage=10&returnType=JSON")
                        .build())
                .header("Authorization", headerValue)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        List<ConvenientDTO> bathroomDTOS = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        JSONArray row = null;
        try {
            JSONObject object = (JSONObject) jsonParser.parse(getstring);
            JSONArray fullObject = (JSONArray) object.get("data");

            for (Object o : fullObject) {
                JSONObject element = (JSONObject) o;
                Long lineNm = (Long) element.get("호선");
                Double longitude = Double.parseDouble((String) element.get("경도"));
                Double latitude = Double.parseDouble((String) element.get("위도"));
                String weekday = (String) element.get("개방시간");
                String detail = (String) element.get("화장실 상세위치");

                Point point = new Point(longitude, latitude);
                String description = String.valueOf(lineNm.intValue()) + "호선, " + detail;

                ConvenientDTO bathroomDTO = ConvenientDTO.builder()
                        .convenientType("bathroom")
                        .weekday(weekday)
                        .point(point)
                        .description(description)
                        .build();

                bathroomDTOS.add(bathroomDTO);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return bathroomDTOS;
    }
}
