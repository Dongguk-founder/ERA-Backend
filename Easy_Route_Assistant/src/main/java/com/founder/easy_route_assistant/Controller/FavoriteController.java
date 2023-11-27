package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.DTO.FavoriteDTO;
import com.founder.easy_route_assistant.Service.FavoriteService;
import com.founder.easy_route_assistant.token.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<FavoriteDTO> saveFavorite(@RequestHeader String jwt, @RequestBody FavoriteDTO favoriteDTO) {
        String userId = jwtProvider.getUserID(jwt);
        FavoriteDTO dto = favoriteService.savefavorite(userId, favoriteDTO);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping(value = "/find")
    public ResponseEntity<List<FavoriteDTO>> getFavoriteList(@RequestHeader String jwt) {
        String userId = jwtProvider.getUserID(jwt);
        List<FavoriteDTO> favoriteList = favoriteService.getFavoriteList(userId);

        if (!favoriteList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(favoriteList);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @DeleteMapping(value = "/delete/{favoriteId}")
    public ResponseEntity<List<FavoriteDTO>> deleteFavorite(@RequestHeader String jwt, @PathVariable Long favoriteId) {
        String userId = jwtProvider.getUserID(jwt);
        favoriteService.deleteFavorite(favoriteId);
        List<FavoriteDTO> favoriteList = favoriteService.getFavoriteList(userId);
        return ResponseEntity.ok().body(favoriteList);
    }


}
