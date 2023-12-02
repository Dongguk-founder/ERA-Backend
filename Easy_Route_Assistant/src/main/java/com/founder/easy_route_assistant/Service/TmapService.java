package com.founder.easy_route_assistant.Service;

import com.founder.easy_route_assistant.DTO.Route.RouteDTO;
import com.founder.easy_route_assistant.DTO.Route.RouteElementDTO;
import com.founder.easy_route_assistant.DTO.Route.RouteDTOList;
import com.founder.easy_route_assistant.DTO.TmapDTO;
import okhttp3.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TmapService {
    @Value("${TMAP_URL}")
    private String TMAP_URL;

    @Value("${TMAP_APPKEY}")
    private String TMAP_APPKEY;

    public RouteDTOList searchRoute(TmapDTO tmapDTO) throws IOException {
        OkHttpClient client = new OkHttpClient();

        String startX = String.valueOf(tmapDTO.getStart().getX());
        String startY = String.valueOf(tmapDTO.getStart().getY());
        String endX = String.valueOf(tmapDTO.getEnd().getX());
        String endY = String.valueOf(tmapDTO.getEnd().getY());

        String bodyString = String.format("{\"startX\":\"%s\",\"startY\":\"%s\",\"endX\":\"%s\",\"endY\":\"%s\"}", startX, startY, endX, endY);

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, bodyString);
        Request request = new Request.Builder()
                .url("https://apis.openapi.sk.com/transit/routes")
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
                        .totalTime(totalTime)
                        .routeElements(singleRoute)
                        .build();
                routeDTOS.add(routeDTO);
            }
            fullRoute.setRouteDTOS(routeDTOS);
            return fullRoute;

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
