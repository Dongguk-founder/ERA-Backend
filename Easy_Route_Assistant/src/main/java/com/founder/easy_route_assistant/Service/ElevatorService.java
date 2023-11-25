package com.founder.easy_route_assistant.Service;
import com.founder.easy_route_assistant.DTO.ElevatorDTO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;


@Service
public class ElevatorService {

    @Value("${ELEVATOR_URL}")
    private String ELEVATOR_URL;


    @Value("${ELEVATOR_APPKEY}")
    private String ELEVATOR_APPKEY;


    public JSONArray requestElevatorAPI() {

        //UriBulider 설정을 해주는 DefaultUriBuilderFactory
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(ELEVATOR_URL);

        //API를 사용하기 위한 인증key를 사용하기 위해 인코딩을 해야함 (그냥 호출하게 되면 API키가 달라지는 경우가 생길 수 있다.)
        //VALUES_ONLY : URI템플릿은 인코딩하지 않고 URI변수를 템플릿에 적용하기 전에 엄격히 인코딩한다.
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        //WebClient는 기본적으로 Immutable
        //WebClient 디폴트 세팅
        WebClient webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(ELEVATOR_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        //WebClient.get() Returns: a spec for specifying the target URL
        // JSON 파싱
        JSONObject object = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(ELEVATOR_APPKEY)
                        .path("/json/tbTraficElvtr/1/500/")
                        .build())
                .retrieve()
                .bodyToMono(JSONObject.class)
                .block();

        JSONObject subobject = (JSONObject) object.get("tbTraficElvtr");
        JSONArray row = (JSONArray) subobject.get("row");


        ElevatorDTO elevatorDTO = new ElevatorDTO();
        for (int i = 0; i < row.size(); i++){
            object = (JSONObject) row.get(i);
            String subway_name = (String) object.get("SW_NM");
            Point point = (Point) object.get("NODE_WKT");

            elevatorDTO.setSubway_name(subway_name);
            elevatorDTO.setPoint(point);
        }
        return row;

    }
}
