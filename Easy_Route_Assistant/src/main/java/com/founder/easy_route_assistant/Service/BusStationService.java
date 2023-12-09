package com.founder.easy_route_assistant.Service;

import com.founder.easy_route_assistant.Entity.BusStationEntity;
import com.founder.easy_route_assistant.Repository.BusStationRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BusStationService {

    private final BusStationRepository busStationRepository;

    @Value("${REALTIMEBUS_URL}")
    private String REALTIMEBUS_URL;


    @Value("${REALTIMEBUS_KEY}")
    private String REALTIMEBUS_KEY;


    public void getRealtimeBusData(String stId, String busRouteId, String ord) throws IOException {

        StringBuilder sb = new StringBuilder();
        sb.append(REALTIMEBUS_URL);
        sb.append("?ServiceKey=" + REALTIMEBUS_KEY);
        sb.append("&stId=" + stId);
        sb.append("&busRouteId=" + busRouteId);
        sb.append("&ord=" + ord);

        //Api 호출
        URL url = new URL(sb.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Content-Type", "application/xml");
        conn.setRequestMethod("GET");


        // 응답 코드 확인
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // 응답 데이터 읽기
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer st = new StringBuffer();

            String line;
            while ((line = reader.readLine()) != null) {
                st.append(line);
            }

            // JSON데이터로 변환
            JSONObject jsonObject = XML.toJSONObject(st.toString());
            JSONObject msgBody = jsonObject.getJSONObject("ServiceResult").getJSONObject("msgBody").getJSONObject("itemList");
            String jsonPrettyPrintString = msgBody.toString(4);


            // 이제 xmlResponse를 원하는 방식으로 처리하면 됩니다.
            // 예: XML 파싱, 객체로 매핑 등
            System.out.println("JSON Response:\n" + jsonPrettyPrintString);
        } else {
            // 오류 처리
            System.out.println("HTTP Request Failed with response code: " + responseCode);
        }
    }

    //busRouteId : 노선 ID - 버스 번호 고유 아이디
//    public Long getBusRouteId(String busName) {
//        Optional<BusStationEntity> busEntity = lowBusRepository.findByBusName(busName);
//        if (busEntity.isPresent()) {
//            return busEntity.get().getBusRouteId();
//        } else {
//            return null;
//        }
//    }

//    public Optional<Long> getBusRouteId(String busName) {
//        return lowBusRepository.findByBusName(busName)
//                .map(BusStationEntity::getBusRouteId);
//    }

    public Optional<Long> getBusRouteId(String busName) {
        try {
            busName = busName.substring(3);
            return busStationRepository.findByBusName(busName)
                    .map(BusStationEntity::getBusRouteId);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //    public List<BusStationEntity> getStationId(String stName) {
//        try {
//            return busStationRepository.findAllByStName(stName)
//                    .stream().toList();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }
    public List<BusStationEntity> getStationId(String stName) {
        List<BusStationEntity> data = busStationRepository.findAllByStName(stName);
        return data;
    }

    public Optional<BusStationEntity> getAllParam(String busName, String stName){
        return busStationRepository.findByBusNameAndStName(busName,stName);
    }
}
