package com.founder.easy_route_assistant.Service;

import com.founder.easy_route_assistant.DTO.FavoriteDTO;
import com.founder.easy_route_assistant.DTO.UserDTO;
import com.founder.easy_route_assistant.Entity.FavoriteEntity;
import com.founder.easy_route_assistant.Entity.FavoriteItemEntity;
import com.founder.easy_route_assistant.Entity.UserEntity;
import com.founder.easy_route_assistant.Repository.FavoriteItemRepository;
import com.founder.easy_route_assistant.Repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final FavoriteItemRepository favoriteItemRepository;

    public FavoriteDTO savefavorite(String userId, FavoriteDTO favoriteDTO) {
        // Returns whether an entity with the given id exists.
        // 객체 생성 (부모테이블에 먼저 데이터를 삽입해야 자식테이블에 데이터삽입 가능)
        FavoriteEntity entity ;
        if(favoriteRepository.findByUserID(userId).isEmpty()) {
            entity = FavoriteEntity.builder().userID(userId).build();
            // 만든 객체를 favorite db - Favorite db에 저장해야 함
            favoriteRepository.save(entity);
        }else {
            entity = favoriteRepository.findByUserID(userId).get();
        }
        if (favoriteItemRepository.findByRoadNameAddress(favoriteDTO.getRoadNameAddress()).isEmpty()) {
            favoriteItemRepository.save(FavoriteItemEntity.builder()
                    .placeName(favoriteDTO.getPlaceName())
                    .roadNameAddress(favoriteDTO.getRoadNameAddress())
                    .longitude(favoriteDTO.getLongitude())
                    .latitude(favoriteDTO.getLatitude())
                    .favorite(entity)
                    .build());
        }

        return favoriteDTO;
    }
}
