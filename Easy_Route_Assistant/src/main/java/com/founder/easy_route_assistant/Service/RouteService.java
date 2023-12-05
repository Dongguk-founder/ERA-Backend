package com.founder.easy_route_assistant.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.founder.easy_route_assistant.DTO.Route.RouteDTO;
import com.founder.easy_route_assistant.DTO.Route.RouteElementDTO;
import com.founder.easy_route_assistant.DTO.Route.RouteDTOList;
import com.founder.easy_route_assistant.DTO.Route.RouteRequestDTO;
import com.founder.easy_route_assistant.Entity.ExcelEntity;
import com.founder.easy_route_assistant.Repository.ExcelRepository;
import com.founder.easy_route_assistant.Repository.RouteRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteService {
    @Value("${TMAP_URL}")
    private String TMAP_URL;
    @Value("${TMAP_APPKEY}")
    private String TMAP_APPKEY;

    @Value("${SUBWAY_URL}")
    private String SUBWAY_URL;
    @Value("${SUBWAY_KEY}")
    private String SUBWAY_KEY;

    private final RouteRepository routeRepository;
    private final ExcelRepository excelRepository;

    public RouteDTOList searchRoute(RouteRequestDTO routeRequestDTO) throws IOException {
        OkHttpClient client = new OkHttpClient();

        String startX = String.valueOf(routeRequestDTO.getStart().getX());
        String startY = String.valueOf(routeRequestDTO.getStart().getY());
        String endX = String.valueOf(routeRequestDTO.getEnd().getX());
        String endY = String.valueOf(routeRequestDTO.getEnd().getY());

        String bodyString = String.format("{\"startX\":\"%s\",\"startY\":\"%s\",\"endX\":\"%s\",\"endY\":\"%s\"}", startX, startY, endX, endY);

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, bodyString);
        Request request = new Request.Builder()
                .url(TMAP_URL)
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("appKey", TMAP_APPKEY)
                .addHeader("content-type", "application/json")
                .build();

        Response response = client.newCall(request).execute();

        String jsonData = response.body().string();

        JSONParser jsonParser = new JSONParser();
        JSONObject row = null;
        try {
            JSONObject object = (JSONObject) jsonParser.parse(jsonData);
            JSONObject fullObject = (JSONObject) object.get("metaData");
            row = (JSONObject) fullObject.get("plan");
            JSONArray fullRoutes = (JSONArray) row.get("itineraries");

            RouteDTOList fullRoute = new RouteDTOList();

            List<RouteDTO> routeDTOS = new ArrayList<>();

            Long id = 0L;
            for (Object full : fullRoutes) {
                JSONObject route = (JSONObject) full; // 모든 경로 검색 결과
                Long totalTime = (Long) route.get("totalTime");

                List<RouteElementDTO> singleRoute = new ArrayList<>(); // 하나의 경로

                JSONArray routes = (JSONArray) route.get("legs"); // 모든 경로 중 하나
                for (Object r : routes) {
                    JSONObject element = (JSONObject) r; // 각 경로 속 세부 요소(ex. 도보, 버스, 지하철)

                    JSONObject start = (JSONObject) element.get("start");
                    JSONObject end = (JSONObject) element.get("end");

                    String startName = (String) start.get("name");
                    String endName = (String) end.get("name");
                    String mode = (String) element.get("mode");
                    String routeColor = (String) element.get("routeColor"); // BUS, SUBWAY
                    String name = (String) element.get("route"); // 버스 번호, 지하철 호선

                    RouteElementDTO elementDTO = RouteElementDTO.builder()
                            .start(startName)
                            .end(endName)
                            .mode(mode)
                            .routeColor(routeColor)
                            .name(name)
                            .build();

                    singleRoute.add(elementDTO);
                }

                RouteDTO routeDTO = RouteDTO.builder()
                        .id(id++)
                        .totalTime(totalTime)
                        .routeElements(singleRoute)
                        .build();
                routeDTOS.add(routeDTO);

                /*Map<Long, Object> data = new HashMap<>();
                data.put(routeDTO.getId(), routeDTO);*/
                String jsonString = new ObjectMapper().writeValueAsString(routeDTO);
                routeRepository.save(routeDTO.getId(), jsonString);
            }
            fullRoute.setRouteDTOS(routeDTOS);
            return fullRoute;

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String mapRoute(Long id) {
        String strRoute = routeRepository.findById(id);

        JSONParser jsonParser = new JSONParser();
        Object obj = null;
        try {
            obj = jsonParser.parse(strRoute);
            JSONObject route = (JSONObject) obj;

            Long totalTime = (Long) route.get("totalTime");
            JSONArray elements = (JSONArray) route.get("routeElements");
            for(int i=1; i<elements.size()-1; i++) {
                JSONObject r = (JSONObject) elements.get(i);

                String start = (String) r.get("start");
                String end = (String) r.get("end");
                if(start.equals(end)) { // 출발지와 도착지 명이 같으면 지하철 환승임
                    JSONObject before = (JSONObject) elements.get(i-1);
                    JSONObject after = (JSONObject) elements.get(i+1);

                    String endBefore = (String) before.get("end");
                    String nameBefore = (String) before.get("name"); // lnCd
                    List<String> codeBefore = getStationCode(endBefore, nameBefore); // railOprIsttCd, stinCd

                    String startAfter = (String) after.get("start");
                    String endAfter = (String) after.get("end");
                    String nameAfter = (String) after.get("name"); // chthTgtLn

                    List<String> codeStartAfter = getStationCode(startAfter, nameAfter);
                    List<String> codeEndAfter = getStationCode(endAfter, nameAfter); // chtnNextStinCd
                    int dif = Integer.parseInt(codeEndAfter.get(1)) - Integer.parseInt(codeStartAfter.get(1));
                    String prevStinCd = String.valueOf(Integer.parseInt(codeStartAfter.get(1))-dif); // prevStinCd

                    List<String> transfer = getSubwayTransferRoute(nameBefore, codeBefore.get(1), codeBefore.get(0), nameAfter, prevStinCd, codeEndAfter.get(1));
                }
            }

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return "";
    }

    private List<String> getStationCode(String stationNm, String lineNm) throws ParseException {
        List<ExcelEntity> excelEntities = excelRepository.findAllByStationName(stationNm);
        List<String> answer = new ArrayList<>();
        String opr_code = null, stationCode = null;
        for (ExcelEntity o : excelEntities) {
            if (lineNm.contains(o.getLineNum())) {
                opr_code = o.getOpr_code();
                stationCode = o.getStationCode();
                answer.add(opr_code);
                answer.add(stationCode);
                break;
            }
        }

        return answer;
    }

    private List<String> getSubwayTransferRoute(String lnCd, String stinCd, String railOprIsttCd, String chthTgtLn, String prevStinCd, String chtnNextStinCd) throws ParseException {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(SUBWAY_URL);

        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
                .build();

        WebClient webClient = WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .uriBuilderFactory(factory)
                .baseUrl(SUBWAY_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .build();

        String getstring = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("serviceKey", SUBWAY_KEY)
                        .queryParam("format", "JSON")
                        .queryParam("lnCd", lnCd.charAt(3))
                        .queryParam("stinCd", stinCd)
                        .queryParam("railOprIsttCd", railOprIsttCd)
                        .queryParam("chthTgtLn", chthTgtLn.charAt(3))
                        .queryParam("prevStinCd", prevStinCd)
                        .queryParam("chtnNextStinCd", chtnNextStinCd)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(getstring);
        JSONArray body = (JSONArray) jsonObject.get("body");

        List<String> descriptions = new ArrayList<>();
        for (Object o : body) {
            JSONObject obj = (JSONObject) o;
            String description = (String) obj.get("mvContDtl");
            descriptions.add(description);
        }

        return descriptions;
    }
}
