package com.founder.easy_route_assistant.Service;

import com.founder.easy_route_assistant.DTO.TransferMovementDTO;
import lombok.RequiredArgsConstructor;
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

    @Value("${TRANSFERMOVEMENT_URL}")
    private String TRANSFERMOVEMENT_URL;

    @Value("${TRANSFERMOVEMENT_APPKEY}")
    private String TRANSFERMOVEMENT_APPKEY;


    public List<TransferMovementDTO> findTransferMovement(RouteDTO routeDTO){
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(TRANSFERMOVEMENT_URL);

        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        WebClient webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(TRANSFERMOVEMENT_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String getstring = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(TRANSFERMOVEMENT_URL)
                        .queryParam("serviceKey",TRANSFERMOVEMENT_APPKEY)
                        .queryParam("format","json")
                        .queryParam("railOprIsttCd", "S1")
                        // 여기에 요청인자 추가해야 함
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

    }
}
