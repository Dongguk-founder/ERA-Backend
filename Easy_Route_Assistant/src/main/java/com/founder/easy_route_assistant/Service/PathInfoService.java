package com.founder.easy_route_assistant.Service;

import com.founder.easy_route_assistant.DTO.RouteRequestDTO;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
public class PathInfoService {
    @Value("${PATHINFO_URL}")
    private String PATHINFO_URL;


    @Value("${PATHINFO_APPKEY}")
    private String PATHINFO_APPKEY;

    public JSONObject requestLocationAPI(RouteRequestDTO routeRequestDTO){

        //UriBulider 설정을 해주는 DefaultUriBuilderFactory
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(PATHINFO_URL);

        //API를 사용하기 위한 인증key를 사용하기 위해 인코딩을 해야함 (그냥 호출하게 되면 API키가 달라지는 경우가 생길 수 있다.)
        //VALUES_ONLY : URI템플릿은 인코딩하지 않고 URI변수를 템플릿에 적용하기 전에 엄격히 인코딩한다.
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        //WebClient는 기본적으로 Immutable
        //WebClient 디폴트 세팅
        WebClient webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(PATHINFO_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        //WebClient.get() Returns: a spec for specifying the target URL
        String getstring = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("ServiceKey", PATHINFO_APPKEY)
                        .queryParam("startX",routeRequestDTO.getStart().getX())
                        .queryParam("startY",routeRequestDTO.getStart().getY())
                        .queryParam("endX",routeRequestDTO.getEnd().getX())
                        .queryParam("endY",routeRequestDTO.getEnd().getY())
                        .build())
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
