package com.founder.easy_route_assistant.Service;

import com.founder.easy_route_assistant.DTO.RealTimeParamDTO;
import com.founder.easy_route_assistant.Entity.BusStationEntity;
import com.founder.easy_route_assistant.Repository.BusStationRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
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
import java.util.ArrayList;
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


    public List<String> getRealtimeBusData(Integer stId, Integer busRouteId, Integer ord) throws IOException {

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

        // return값 관리
        List<String> arrmsgList = new ArrayList<>();

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
            try {
                JSONObject itemList = jsonObject.getJSONObject("ServiceResult").getJSONObject("msgBody").getJSONObject("itemList");
                //첫번째 도착예정 버스의 도착정보메시지 2분44초후[0번째 전]
                String arrmsg1 = itemList.getString("arrmsg1");
                //두번째 도착예정 버스의 도착정보메시지 12분35초후[7번째 전]
                String arrmsg2 = itemList.getString("arrmsg2");
                //첫번째 도착예정버스의 차량유형 (0 : 일반버스, 1: 저상버스)
                int busType1 = itemList.getInt("busType1");
                //두번째 도착예정버스의 차량유형 (0 : 일반버스, 1: 저상버스)
                int busType2 = itemList.getInt("busType2");
                // 첫번째 도착예정 버스가 일반버스면 없다고 판단
                if (busType1 == 0) arrmsg1 = null;
                // 첫번째 도착예정 버스가 일반버스면 없다고 판단
                if (busType2 == 0) arrmsg2 = null;

                arrmsgList.add(arrmsg1);
                arrmsgList.add(arrmsg2);

                System.out.println("JSON Response:\n" + busType1);
                System.out.println("JSON Response:\n" + busType2);
            }catch (JSONException e){
                System.out.println("제공하지 않는 버스입니다.");
                e.printStackTrace();
            }
        } else {
            // 오류 처리
            System.out.println("HTTP Request Failed with response code: " + responseCode);
        }
        return arrmsgList;
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

    public Optional<Integer> getBusRouteId(String busName) {
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

    public RealTimeParamDTO getAllParam(String busName, String stName){
        busName = busName.substring(3);
        Optional<BusStationEntity> temp = busStationRepository.findByBusNameAndStName(busName,stName);
        RealTimeParamDTO realTimeParamDTO = null;
        if(temp.isPresent()){
            realTimeParamDTO= RealTimeParamDTO.builder()
                    .busRouteId(temp.get().getBusRouteId())
                    .ord(temp.get().getOrd())
                    .stId(temp.get().getStId())
                    .build();
        }
        return realTimeParamDTO;
    }
}
