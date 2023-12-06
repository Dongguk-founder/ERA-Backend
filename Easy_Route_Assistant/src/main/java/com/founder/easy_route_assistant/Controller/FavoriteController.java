package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.DTO.Favorite.FaviriteListDTO;
import com.founder.easy_route_assistant.DTO.Favorite.FavoriteDTO;
import com.founder.easy_route_assistant.Service.FavoriteService;
import com.founder.easy_route_assistant.config.token.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final JwtProvider jwtProvider;

    @PostMapping(value = "/add")
    public ResponseEntity<FavoriteDTO> saveFavorite(@RequestHeader String jwt, @RequestBody FavoriteDTO favoriteDTO){

        JSONObject res = new JSONObject();
        String userId = jwtProvider.getUserID(jwt);
        FavoriteDTO dto = favoriteService.savefavorite(userId, favoriteDTO);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping(value = "/find")
    public ResponseEntity<FaviriteListDTO> getFavoriteList(@RequestHeader String jwt) {

        String userId = jwtProvider.getUserID(jwt);
        FaviriteListDTO favoriteList = favoriteService.getFavoriteList(userId);
        if (!favoriteList.getFavoriteList().isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(favoriteList);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @DeleteMapping(value = "/delete/{favoriteId}")
    public ResponseEntity<FaviriteListDTO> deleteFavorite(@RequestHeader String jwt, @PathVariable Long favoriteId) {

        String userId = jwtProvider.getUserID(jwt);
        favoriteService.deleteFavorite(favoriteId);
        FaviriteListDTO favoriteList = favoriteService.getFavoriteList(userId);
        if(!favoriteList.getFavoriteList().isEmpty()){
            return ResponseEntity.ok().body(favoriteList);
        }else {
            return ResponseEntity.noContent().build();
        }
    }


}
