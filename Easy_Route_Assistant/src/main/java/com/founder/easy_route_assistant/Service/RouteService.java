package com.founder.easy_route_assistant.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.founder.easy_route_assistant.DTO.Route.RouteDTO;
import com.founder.easy_route_assistant.DTO.Route.RouteElementDTO;
import com.founder.easy_route_assistant.DTO.Route.RouteDTOList;
import com.founder.easy_route_assistant.DTO.Route.RouteRequestDTO;
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
import org.springframework.stereotype.Service;

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

    private final RouteRepository routeRepository;

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
            for(Object o : elements) {
                JSONObject tmp = (JSONObject) o; // RouteElementDTO

                String start = (String) tmp.get("start");
                String end = (String) tmp.get("end");
                String mode = (String) tmp.get("mode");
                String routeColor = (String) tmp.get("routeColor");
                String name = (String) tmp.get("name");

                if (mode.equals("SUBWAY")) {

                }
            }

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return "";
    }
}
