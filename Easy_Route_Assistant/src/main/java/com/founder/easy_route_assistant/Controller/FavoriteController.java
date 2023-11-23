package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.DTO.FavoriteDTO;
import com.founder.easy_route_assistant.Service.FavoriteService;
import com.founder.easy_route_assistant.token.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final JwtProvider jwtProvider;

    @PutMapping(value = "/add")
    public ResponseEntity<?> saveFavorite(@RequestHeader String jwt, @RequestBody FavoriteDTO favoriteDTO){
        String userId = jwtProvider.getUserID(jwt);
        FavoriteDTO dto = favoriteService.savefavorite(userId,favoriteDTO);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping(value = "/find")
    public  ResponseEntity<?> getFavoriteList(@RequestHeader String jwt){
        String userId = jwtProvider.getUserID(jwt);
        List<FavoriteDTO> dto = favoriteService.getFavoriteList(userId);
        return ResponseEntity.ok().body(dto);
    }



}
