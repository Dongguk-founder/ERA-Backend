package com.founder.easy_route_assistant.Service;

import com.founder.easy_route_assistant.DTO.TmapDTO;
import okhttp3.*;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
public class TmapService {
    @Value("${TMAP_URL}")
    private String TMAP_URL;

    @Value("${TMAP_APPKEY}")
    private String TMAP_APPKEY;

    public void searchRoute(TmapDTO tmapDTO) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"startX\":\"126.926493082645\",\"startY\":\"37.6134436427887\",\"endX\":\"127.126936754911\",\"endY\":\"37.5004198786564\"}");
        Request request = new Request.Builder()
                .url("https://apis.openapi.sk.com/transit/routes")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("appKey", "m50KE4IWf82jLCezkw0no9HlupPepnr81BPl5LxV")
                .addHeader("content-type", "application/json")
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response);
    }
}
