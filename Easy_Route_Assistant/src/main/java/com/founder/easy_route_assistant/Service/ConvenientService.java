package com.founder.easy_route_assistant.Service;

import com.founder.easy_route_assistant.DTO.Convenient.BathroomDTO;
import com.founder.easy_route_assistant.DTO.Convenient.ConvenientDTO;
import com.founder.easy_route_assistant.DTO.Convenient.ConvenientListDTO;
import com.founder.easy_route_assistant.Entity.ConvenientEntity;
import com.founder.easy_route_assistant.Repository.ConvenientRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class ConvenientService {

    private final ConvenientRepository convenientRepository;

    @Value("${SEOUL_URL}")
    private String SEOUL_URL;


    @Value("${SEOUL_KEY}")
    private String SEOUL_KEY;


    @Value("${BATHROOM_URL}")
    private String BATHROOM_URL;

    @Value("${BATHROOM_APPKEY}")
    private String BATHROOM_APPKEY;

    public void update(ConvenientDTO convenientDTO) {
        ConvenientEntity convenientEntity = convenientRepository.findByPoint(convenientDTO.getPoint());

        if (convenientEntity != null) { // 기존 항목 수정일 때
            if (convenientDTO.getConvenientType() != null)
                convenientEntity.setConvenientType(convenientDTO.getConvenientType());
            if (convenientDTO.getDescription() != null) convenientEntity.setDescription(convenientDTO.getDescription());
            if (convenientDTO.getWeekday() != null) convenientEntity.setWeekday(convenientDTO.getWeekday());
            if (convenientDTO.getSaturday() != null) convenientEntity.setSaturday(convenientDTO.getSaturday());
            if (convenientDTO.getHoliday() != null) convenientEntity.setHoliday(convenientDTO.getHoliday());

            /*// 주소 정보는 바꿀 필요 없을 것 같은데 어떻게 생각?
            if (convenientDTO.getRoadAddr() != null) convenientEntity.setRoadAddr(convenientDTO.getRoadAddr());
            if (convenientDTO.getPoint() != null) convenientEntity.setPoint(convenientDTO.getPoint());*/

            convenientRepository.save(convenientEntity);
        } else { // 새로 등록일 때
            ConvenientEntity newConvenient = ConvenientEntity.builder()
                    .convenientType(convenientDTO.getConvenientType())
                    .point(convenientDTO.getPoint())
                    .description(convenientDTO.getDescription())
                    .weekday(convenientDTO.getWeekday())
                    .saturday(convenientDTO.getSaturday())
                    .holiday(convenientDTO.getHoliday())
                    .build();

            convenientRepository.save(newConvenient);
        }
    }


    public ConvenientListDTO getConvenientList(String convenientType) {
        List<ConvenientEntity> convenientEntities = convenientRepository.findAllByConvenientType(convenientType);
        List<ConvenientDTO> convenientDTOS = new ArrayList<>();

        ConvenientListDTO convenientListDTO = new ConvenientListDTO();

        for (ConvenientEntity convenientEntity : convenientEntities) {
            ConvenientDTO convenientDTO = ConvenientDTO.builder()
                    .convenientType(convenientEntity.getConvenientType())
                    // .roadAddr(convenientEntity.getRoadAddr())
                    .description(convenientEntity.getDescription())
                    .point(convenientEntity.getPoint())
                    .weekday(convenientEntity.getWeekday())
                    .saturday(convenientEntity.getSaturday())
                    .holiday(convenientEntity.getHoliday())
                    .build();
            convenientDTOS.add(convenientDTO);
        }


        if (convenientType.equals("elevator")) { // elevator api
            List<ConvenientDTO> elevatorDTOS = requestElevatorAPI();
            convenientDTOS.addAll(elevatorDTOS);
        } else if (convenientType.equals("charger")) {
            List<ConvenientDTO> chargerDTOS = requestChargerAPI();
            convenientDTOS.addAll(chargerDTOS);
        } else if ( convenientType.equals("bathroom")) {
            List<ConvenientDTO> bathroomDTOS = requestBathroomAPI();
            convenientDTOS.addAll(bathroomDTOS);
        } else {
            System.out.println("잘못된 편의시설 타입을 입력하였습니다.");
        }

        convenientListDTO.setConvenientDTOList(convenientDTOS);

        return convenientListDTO;
    }

    //elevator
    public List<ConvenientDTO> requestElevatorAPI() {

        //UriBulider 설정을 해주는 DefaultUriBuilderFactory
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(SEOUL_URL);

        //API를 사용하기 위한 인증key를 사용하기 위해 인코딩을 해야함 (그냥 호출하게 되면 API키가 달라지는 경우가 생길 수 있다.)
        //VALUES_ONLY : URI템플릿은 인코딩하지 않고 URI변수를 템플릿에 적용하기 전에 엄격히 인코딩한다.
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        //WebClient는 기본적으로 Immutable
        //WebClient 디폴트 세팅
        WebClient webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(SEOUL_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        //WebClient.get() Returns: a spec for specifying the target URL
        String getstring = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(SEOUL_KEY)
                        .path("/json/tbTraficElvtr/1/500/")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        List<ConvenientDTO> elevatorDTOList = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        JSONArray row = null;
        try {
            JSONObject object = (JSONObject) jsonParser.parse(getstring);
            JSONObject fullObject = (JSONObject) object.get("tbTraficElvtr");
            row = (JSONArray) fullObject.get("row");
            for (Object o : row) {
                JSONObject element = (JSONObject) o;
                String s = (String) element.get("NODE_WKT");
                String station = (String) element.get("SW_NM");

                //정규 표현식 패턴 ( - :문자 혹은 숫자가 있고, ? : 앞의 표현식이 없거나 최대 한개만, \d : 0-9사이의 숫자 ,+: 앞의 표현식이 1개 이상,...)
                String pattern = "-?\\d+\\.?\\d*";
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
                Point point = new Point(longtitude, latitude);

                ConvenientDTO elevatorDTO = ConvenientDTO.builder()
                        .convenientType("elevator")
                        .point(point)
                        .description(station)
                        .build();
                elevatorDTOList.add(elevatorDTO);
                /*String SGG_NM = (String) element.get("SGG_NM");
                if (Objects.equals(SGG_NM, sgg_nm)) {
                    String s = (String) element.get("NODE_WKT");
                    String station = (String) element.get("SW_NM");

                    //정규 표현식 패턴 ( - :문자 혹은 숫자가 있고, ? : 앞의 표현식이 없거나 최대 한개만, \d : 0-9사이의 숫자 ,+: 앞의 표현식이 1개 이상,...)
                    String pattern = "-?\\d+\\.?\\d*";
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
                    Point point = new Point(longtitude, latitude);

                    ConvenientDTO elevatorDTO = ConvenientDTO.builder()
                            .convenientType("elevator")
                            .point(point)
                            .description(station)
                            .build();
                    elevatorDTOList.add(elevatorDTO);
                }*/

            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return elevatorDTOList;

    }
    // bathroom
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
                String description = String.valueOf(lineNm.intValue()) + "호선 " + detail;

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
    //charger
    public List<ConvenientDTO> requestChargerAPI() { // target = 시군구코드
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
                        .path(SEOUL_KEY)
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

                /*String signgunm = (String) element.get("SIGNGUNM");
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

                    *//*ChargerDTO chargerDTO = ChargerDTO.builder()
                            .point(point)
                            .placeDescript(placeDescript)
                            .weekStart(weekStart)
                            .weekEnd(weekEnd)
                            .satStart(satStart)
                            .satEnd(satEnd)
                            .holiStart(holiStart)
                            .holiEnd(holiEnd)
                            .build();

                    chargerDTOList.add(chargerDTO);*//*
                }*/
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return convenientDTOS;
    }
}
