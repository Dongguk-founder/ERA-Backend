package com.founder.easy_route_assistant.Controller;

import com.founder.easy_route_assistant.DTO.FavoriteDTO;
import com.founder.easy_route_assistant.Entity.FavoriteEntity;
import com.founder.easy_route_assistant.Entity.FavoriteItemEntity;
import com.founder.easy_route_assistant.Service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PutMapping(value = "/add/{userId}")
    public ResponseEntity<?> saveFavorite(@PathVariable("userId") String userId, @RequestBody FavoriteDTO favoriteDTO){
        FavoriteDTO dto = favoriteService.savefavorite(userId,favoriteDTO);
        return ResponseEntity.ok().body(dto);
    }
}