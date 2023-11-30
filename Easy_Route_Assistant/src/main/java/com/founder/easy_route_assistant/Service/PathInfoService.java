package com.founder.easy_route_assistant.Service;

import com.founder.easy_route_assistant.DTO.PathInfoDTO;
import com.founder.easy_route_assistant.DTO.PathRequestDTO;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PathInfoService {

    @Value("${PATHINFO_URL}")
    private String PATHINFO_URL;

    @Value("${PATHINFO_APPKEY}")
    private String PATHINFO_APPKEY;


    public JSONObject findPathInfo(PathRequestDTO pathRequestDTO){
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("startX", String.valueOf(pathRequestDTO.getStart().getX()));
        formData.add("startY", String.valueOf(pathRequestDTO.getStart().getY()));
        formData.add("endX", String.valueOf(pathRequestDTO.getEnd().getX()));
        formData.add("endY", String.valueOf(pathRequestDTO.getEnd().getY()));

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(PATHINFO_URL);

        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        WebClient webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(PATHINFO_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String getstring = webClient.post()
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JSONParser jsonParser = new JSONParser();
        JSONObject object = new JSONObject();

        try {
            object = (JSONObject) jsonParser.parse(getstring);
        }catch (Exception e){
            e.printStackTrace();
        }
        return object;
    }
}
