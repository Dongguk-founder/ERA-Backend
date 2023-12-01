package com.founder.easy_route_assistant.Service;

import com.founder.easy_route_assistant.DTO.RouteRequestDTO;
import com.founder.easy_route_assistant.DTO.TransferMovementDTO;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferMovementService {

    @Value("${TRANSFETMOVEMENT_URL}")
    private String TRANSFERMOVEMENT_URL;

    @Value("${TRANSFETMOVEMENT_APPKEY}")
    private String TRANSFERMOVEMENT_APPKEY;




    public JSONObject findTransferMovement(RouteRequestDTO routeRequestDTO) {

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(TRANSFERMOVEMENT_URL);

        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        WebClient webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(TRANSFERMOVEMENT_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String getstring = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("serviceKey",TRANSFERMOVEMENT_APPKEY)
                        .queryParam("format","json")
                        .queryParam("railOprIsttCd", "S1")
                        .queryParam("lnCd", "3")
                        .queryParam("stinCd", "321")
                        .queryParam("prevStinCd", "422")
                        .queryParam("chthTgtLn", "4")
                        .queryParam("chtnNextStinCd", "424")
                        // 여기에 요청인자 추가해야 함
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JSONParser jsonParser = new JSONParser();
        JSONObject full = new JSONObject();
        try {
            full = (JSONObject) jsonParser.parse(getstring);
        }catch (Exception p){
            p.printStackTrace();
        }
        return full;
    }
}
