package com.founder.easy_route_assistant.Service;
import com.founder.easy_route_assistant.DTO.ElevatorDTO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class ElevatorService {

    @Value("${ELEVATOR_URL}")
    private String ELEVATOR_URL;


    @Value("${ELEVATOR_APPKEY}")
    private String ELEVATOR_APPKEY;


    public List<ElevatorDTO> requestElevatorAPI(String sw_cd) {

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
        String getstring = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(ELEVATOR_APPKEY)
                        .path("/json/tbTraficElvtr/1/500/")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        List<ElevatorDTO> elevatorDTOList = new ArrayList<>();

        JSONParser jsonParser = new JSONParser();
        JSONArray row = null;
        try {
            JSONObject object = (JSONObject) jsonParser.parse(getstring);
            JSONObject fullObject = (JSONObject) object.get("tbTraficElvtr");
            row = (JSONArray) fullObject.get("row");
            for(int i = 0 ; i<row.size() ; i++){
                JSONObject element = (JSONObject) row.get(i);
                String subwayCode = (String) element.get("SW_CD");
                if (Objects.equals(subwayCode, sw_cd)){
                    String s = (String) element.get("NODE_WKT");

                    //정규 표현식 패턴 ( - :문자 혹은 숫자가 있고, ? : 앞의 표현식이 없거나 최대 한개만, \d : 0-9사이의 숫자 ,+: 앞의 표현식이 1개 이상,...)
                    String pattern =  "-?\\d+\\.?\\d*";
                    //패턴 컴파일
                    Pattern p = Pattern.compile(pattern);
                    //매처 생성
                    Matcher m = p.matcher(s);

                    Double latitude = 0.0;
                    Double longtitude = 0.0;

                    int count = 0;
                    while (m.find()){
                        if (count == 0){
                            longtitude = Double.parseDouble(m.group());
                        }else {
                            latitude = Double.parseDouble(m.group());
                        }
                        count++;
                    }
                    Point point = new Point(latitude,longtitude);

                    ElevatorDTO elevatorDTO = ElevatorDTO.builder().sw_cd(sw_cd).point(point).build();
                    elevatorDTOList.add(elevatorDTO);
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }





       return elevatorDTOList;

    }
}
