package com.founder.easy_route_assistant.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.founder.easy_route_assistant.DTO.Route.*;
import com.founder.easy_route_assistant.Entity.ExcelEntity;
import com.founder.easy_route_assistant.Repository.ExcelRepository;
import com.founder.easy_route_assistant.Repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouteService {
    @Value("${TMAP_URL}")
    private String TMAP_URL;
    @Value("${TMAP_APPKEY}")
    private String TMAP_APPKEY;

    @Value("${SUBWAYTRANSFER_URL}")
    private String SUBWAYTRANSFER_URL;
    @Value("${SUBWAYENEX_URL}")
    private String SUBWAYENEX_URL;
    @Value("${SUBWAY_KEY}")
    private String SUBWAY_KEY;

    private final RouteRepository routeRepository;
    private final ExcelRepository excelRepository;

    // 요약 경로
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
            RouteDTOList fullRoute_ = new RouteDTOList();

            List<RouteDTO> routeDTOS = new ArrayList<>();
            List<RouteDTO> routeDTOS_ = new ArrayList<>();

            Long id = 0L;
            try {
                for (Object full : fullRoutes) {
                    JSONObject route = (JSONObject) full; // 모든 경로 검색 결과
                    Long tempTime = (Long) route.get("totalTime");
                    String totalTime = tempTime / 60 + "분" ;

                    List<RouteElementDTO> singleRoute = new ArrayList<>(); // 하나의 경로
                    List<RouteElementDTO> singleRoute_ = new ArrayList<>();

                    JSONArray routes = (JSONArray) route.get("legs"); // 모든 경로 중 하나
                    for (Object r : routes) {
                        JSONObject element = (JSONObject) r; // 각 경로 속 세부 요소(ex. 도보, 버스, 지하철)

                        JSONObject start = (JSONObject) element.get("start");
                        JSONObject end = (JSONObject) element.get("end");

                        String startName = (String) start.get("name");
                        String endName = (String) end.get("name");
                        String routeColor = (String) element.get("routeColor"); // BUS, SUBWAY
                        String mode = (String) element.get("mode");
                        Long distance = (Long) element.get("distance");
                        Long sectionTime = (Long) element.get("sectionTime");
                        String name = (String) element.get("route"); // 버스 번호(유지), 지하철 호선(-> 방향)
                        String line = null;
                        if (mode.equals("SUBWAY")) { // name = 지하철 방향
                            List<String> startStationCodes = getStationCode(startName, name);
                            List<String> endStationCodes = getStationCode(endName, name);

                            int tmp1 = Integer.parseInt(startStationCodes.get(1).replaceAll("[^0-9]", ""));
                            int tmp2 = Integer.parseInt(endStationCodes.get(1).replaceAll("[^0-9]", ""));
                            int dif = ((tmp2-tmp1)>0) ? tmp1+1 : tmp1-1;
                            String after;
                            if (Character.isLetter(startStationCodes.get(1).charAt(0))) {
                                after = startStationCodes.get(1).charAt(0) + String.valueOf(dif);
                            }
                            else {
                                after = String.valueOf(dif);
                            }
                            ExcelEntity excelEntity = excelRepository.findByStationCode(after);
                            if (excelEntity == null) {
                                after = endStationCodes.get(1).charAt(0) + String.valueOf(dif);
                                excelEntity = excelRepository.findByStationCode(after);
                            }
                            name = excelEntity.getStationName();
                            line = startStationCodes.get(2);
                        }

                        RouteElementDTO elementDTO = RouteElementDTO.builder()
                                .start(startName)
                                .end(endName)
                                .mode(mode)
                                .routeColor("#"+routeColor)
                                .name(name)
                                .line(line)
                                .distance(distance)
                                .sectionTime(sectionTime)
                                .build();

                        if (mode.equals("WALK")) {
                            singleRoute_.add(elementDTO);
                            continue;
                        }
                        singleRoute.add(elementDTO);
                        singleRoute_.add(elementDTO);
                    }

                    RouteElementDTO tmp = singleRoute.get(singleRoute.size()-1);
                    RouteElementDTO lastElement = RouteElementDTO.builder()
                            .start(tmp.getEnd())
                            .routeColor(tmp.getRouteColor())
                            .build();
                    singleRoute.add(lastElement);

                    RouteDTO routeDTO = RouteDTO.builder()
                            .id(id)
                            .totalTime(totalTime)
                            .routeElements(singleRoute)
                            .build();
                    routeDTOS.add(routeDTO);
                    RouteDTO routeDTO_ = RouteDTO.builder()
                            .id(id++)
                            .totalTime(totalTime)
                            .routeElements(singleRoute_)
                            .build();

                    String jsonString = new ObjectMapper().writeValueAsString(routeDTO_);
                    routeRepository.save(routeDTO_.getId(), jsonString);
                }
            } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        fullRoute.setRouteDTOS(routeDTOS);
            return fullRoute;

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    // 상세 경로
    public DetailRouteDTO mapRoute(Long id) {
        String strRoute = routeRepository.findById(id);

        JSONParser jsonParser = new JSONParser();
        Object obj = null;
        try {
            obj = jsonParser.parse(strRoute);
            JSONObject route = (JSONObject) obj;

            List<DetailElementDTO> detailElementDTOS = new ArrayList<>();

            Long totalTime = (Long) route.get("totalTime");
            JSONArray elements = (JSONArray) route.get("routeElements");
            for(int i=0; i<elements.size()-1; i++) {
                JSONObject current = (JSONObject) elements.get(i);
                Long tempSectionTime = (Long) current.get("sectionTime");
                String sectionTime = tempSectionTime / 60 + "분" ;

                DetailElementDTO detailElementDTO = DetailElementDTO.builder()
                        .start((String) current.get("start"))
                        .end((String) current.get("end"))
                        .mode((String) current.get("mode"))
                        .routeColor((String) current.get("routeColor"))
                        .name((String) current.get("name"))
                        .line((String) current.get("line"))
                        .distance((Long) current.get("distance"))
                        .sectionTime(sectionTime)
                        .build();
                detailElementDTOS.add(detailElementDTO);

                JSONObject after = (JSONObject) elements.get(i + 1);

                String modeCurrent = (String) current.get("mode");
                String startCurrent = (String) current.get("start");
                String endCurrent = (String) current.get("end");

                String modeAfter = (String) after.get("mode");
                String startAfter = (String) after.get("start");
                String endAfter = (String) after.get("end");
                String nameAfter = (String) after.get("name");
                String lineAfter = (String) after.get("line");

                // 출발지==도착지, mode==WALK -> 지하철 환승
                if (startCurrent.equals(endCurrent) && modeCurrent.equals("WALK")) {
                    JSONObject before = (JSONObject) elements.get(i - 1);
                    String lineBefore = (String) before.get("line");
                    // stinNm == startCurrent, endCurrent
                    List<String> codesCurrent = getStationCode(startCurrent, lineBefore);
                    String lnCd = codesCurrent.get(2);
                    String stinCd = codesCurrent.get(1);
                    String railOprIsttCd = codesCurrent.get(0);

                    List<String> codesStartAfter = getStationCode(startAfter, lineAfter);
                    List<String> codesNameAfter = getStationCode(nameAfter, lineAfter);
                    String chthTgtLn = codesStartAfter.get(2);
                    String chtnNextStinCd = codesNameAfter.get(1);
                    int tmp1 = Integer.parseInt(codesStartAfter.get(1).replaceAll("[^0-9]", ""));
                    int tmp2 = Integer.parseInt(codesNameAfter.get(1).replaceAll("[^0-9]", ""));
                    int prev = ((tmp2-tmp1)>0) ? tmp1-1 : tmp1+1;
                    String prevStinCd;
                    if (Character.isLetter(codesStartAfter.get(1).charAt(0))) {
                        prevStinCd = codesStartAfter.get(1).charAt(0) + String.valueOf(prev);
                    }
                    else {
                        prevStinCd = String.valueOf(prev);
                    }

                    JSONObject transfer = getSubwayTransferRoute(lnCd, stinCd, railOprIsttCd, chthTgtLn, prevStinCd, chtnNextStinCd);
                    DetailElementDTO elevator = DetailElementDTO.builder()
                            .mode("elevator")
                            .description(transfer)
                            .build();
                    detailElementDTOS.add(elevator);
                    System.out.println("\ntransfer route: " + transfer + "\n");
                } else if (!startCurrent.equals(endCurrent) && modeCurrent.equals("WALK") && modeAfter.equals("SUBWAY")) {
                    // 쟈철 들어가는 길
                    List<String> codesStartAfter = getStationCode(startAfter, lineAfter);
                    String lnCd = codesStartAfter.get(2);
                    String stinCd = codesStartAfter.get(1);
                    String railOprIsttCd = codesStartAfter.get(0);

                    List<String> codesNameAfter = getStationCode(nameAfter, lineAfter);
                    String nextStinCd = codesNameAfter.get(1);

                    JSONObject enEx = getSubwayEnEx(lnCd, stinCd, railOprIsttCd, nextStinCd);
                    DetailElementDTO elevator = DetailElementDTO.builder()
                            .mode("elevator")
                            .description(enEx)
                            .build();
                    detailElementDTOS.add(elevator);
                    System.out.println("\nentranceInfo: " + enEx + "\n");
                } else if (!startAfter.equals(endAfter) && modeCurrent.equals("SUBWAY") && modeAfter.equals("WALK")) {
                    // 쟈철 나가는 길
                    String lineCurrent = (String) current.get("line");
                    List<String> codesEndCurrent = getStationCode(endCurrent, lineCurrent);
                    List<String> codesStartCurrent = getStationCode(startCurrent, lineCurrent);

                    String lnCd = codesEndCurrent.get(2);
                    String stinCd = codesEndCurrent.get(1);
                    String railOprIsttCd = codesEndCurrent.get(0);

                    int tmp1 = Integer.parseInt(codesEndCurrent.get(1).replaceAll("[^0-9]", ""));
                    int tmp2 = Integer.parseInt(codesStartCurrent.get(1).replaceAll("[^0-9]", ""));
                    int tmp = ((tmp1-tmp2)>0) ? tmp1+1 : tmp1-1;
                    String nextStinCd;
                    if (Character.isLetter(codesEndCurrent.get(1).charAt(0))) {
                        nextStinCd = codesEndCurrent.get(1).charAt(0) + String.valueOf(tmp);
                    }
                    else {
                        nextStinCd = String.valueOf(tmp);
                    }

                    JSONObject enEx = getSubwayEnEx(lnCd, stinCd, railOprIsttCd, nextStinCd);
                    DetailElementDTO elevator = DetailElementDTO.builder()
                            .mode("elevator")
                            .description(enEx)
                            .build();
                    detailElementDTOS.add(elevator);
                    System.out.println("\nexitInfo: " + enEx + "\n");
                }
            }

            JSONObject lastElement = (JSONObject) elements.get(elements.size()-1);
            DetailElementDTO lastDetail = DetailElementDTO.builder()
                    .start((String) lastElement.get("start"))
                    .end((String) lastElement.get("end"))
                    .mode((String) lastElement.get("mode"))
                    .routeColor((String) lastElement.get("routeColor"))
                    .name((String) lastElement.get("name"))
                    .line((String) lastElement.get("line"))
                    .distance((Long) lastElement.get("distance"))
                    .sectionTime((Long) lastElement.get("sectionTime"))
                    .build();
            detailElementDTOS.add(lastDetail);

            DetailRouteDTO detailRouteDTO = DetailRouteDTO.builder()
                    .totalTime(totalTime)
                    .detailElements(detailElementDTOS)
                    .build();

            return detailRouteDTO;

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getStationCode(String stationNm, String lineNm) throws ParseException {
        // ex) stationNm = "옥수", lineNm = "3" or "수도권3호선"
        List<ExcelEntity> excelEntities = excelRepository.findAllByStationName(stationNm);
        List<String> answer = new ArrayList<>();
        String opr_code, stationCode, lineCode;
        for (ExcelEntity o : excelEntities) {
            String lineNum = o.getLineNum();
            if ((lineNum.length()>lineNm.length()) && o.getLineNum().contains(lineNm)) {
                opr_code = o.getOpr_code();
                stationCode = o.getStationCode();
                lineCode = o.getLineCode();
                answer.add(opr_code);
                answer.add(stationCode);
                answer.add(lineCode);
                return answer;
            } else if ((lineNum.length()<lineNm.length()) && lineNm.contains(lineNum)) {
                opr_code = o.getOpr_code();
                stationCode = o.getStationCode();
                lineCode = o.getLineCode();
                answer.add(opr_code);
                answer.add(stationCode);
                answer.add(lineCode);
                return answer;
            }
        }

        return null;
    }

    // 지하철 입출구
    private JSONObject getSubwayEnEx(String lnCd, String stinCd, String railOprIsttCd, String nextStinCd) throws ParseException {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(SUBWAYENEX_URL);

        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
                .build();

        WebClient webClient = WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .uriBuilderFactory(factory)
                .baseUrl(SUBWAYENEX_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .build();

        String getstring = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("serviceKey", SUBWAY_KEY)
                        .queryParam("format", "JSON")
                        .queryParam("lnCd", lnCd)
                        .queryParam("stinCd", stinCd)
                        .queryParam("railOprIsttCd", railOprIsttCd)
                        .queryParam("nextStinCd", nextStinCd)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(getstring);
        JSONArray body = (JSONArray) jsonObject.get("body");

        JSONObject enExInfo = new JSONObject();
        List<String> d = new ArrayList<>();

        int cnt = 1;
        for(int i=0; i<body.size()-1; i++) {
            JSONObject obj = (JSONObject) body.get(i);

            String description = (String) obj.get("mvContDtl");
            d.add(description);

            JSONObject tmp = (JSONObject) body.get(i+1);
            Long nextOrder = (Long) tmp.get("exitMvTpOrdr");
            if (nextOrder == 1L) {
                String imgPath = (String) obj.get("imgPath");
                JSONObject save = new JSONObject();
                save.put("imgPath", imgPath);
                save.put("descriptions", d);

                enExInfo.put(String.valueOf(cnt++), save);
                d = new ArrayList<>();
            }
        }
        JSONObject lastObj = (JSONObject) body.get(body.size()-1);
        String lastDes = (String) lastObj.get("mvContDtl");
        d.add(lastDes);
        String lastImg = (String) lastObj.get("imgPath");
        JSONObject lastSave = new JSONObject();
        lastSave.put("imgPath", lastImg);
        lastSave.put("descriptions", d);
        enExInfo.put(String.valueOf(cnt), lastSave);

        return enExInfo;
    }

    // 지하철 환승
    private JSONObject getSubwayTransferRoute(String lnCd, String stinCd, String railOprIsttCd, String chthTgtLn, String prevStinCd, String chtnNextStinCd) throws ParseException {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(SUBWAYTRANSFER_URL);

        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
                .build();

        WebClient webClient = WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .uriBuilderFactory(factory)
                .baseUrl(SUBWAYTRANSFER_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .build();

        String getstring = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("serviceKey", SUBWAY_KEY)
                        .queryParam("format", "JSON")
                        .queryParam("lnCd", lnCd)
                        .queryParam("stinCd", stinCd)
                        .queryParam("railOprIsttCd", railOprIsttCd)
                        .queryParam("chthTgtLn", chthTgtLn)
                        .queryParam("prevStinCd", prevStinCd)
                        .queryParam("chtnNextStinCd", chtnNextStinCd)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(getstring);
        JSONArray body = (JSONArray) jsonObject.get("body");

        JSONObject transferInfo = new JSONObject();
        List<String> d = new ArrayList<>();

        int cnt = 1;
        for(int i=0; i<body.size()-1; i++) {
            JSONObject currentObj = (JSONObject) body.get(i);

            String description = (String) currentObj.get("mvContDtl");
            d.add(description);

            JSONObject nextObj = (JSONObject) body.get(i+1);
            Long nextOrder = (Long) nextObj.get("chtnMvTpOrdr");
            if (nextOrder == 1L) {
                String imgPath = (String) currentObj.get("imgPath");
                JSONObject save = new JSONObject();
                save.put("imgPath", imgPath);
                save.put("descriptions", d);

                transferInfo.put(String.valueOf(cnt++), save);
                d = new ArrayList<>();
            }
        }
        JSONObject lastObj = (JSONObject) body.get(body.size()-1);
        String lastDes = (String) lastObj.get("mvContDtl");
        d.add(lastDes);
        String lastImg = (String) lastObj.get("imgPath");
        JSONObject lastSave = new JSONObject();
        lastSave.put("imgPath", lastImg);
        lastSave.put("descriptions", d);
        transferInfo.put(String.valueOf(cnt), lastSave);

        return transferInfo;
    }
}
