package com.founder.easy_route_assistant.Service;

import com.founder.easy_route_assistant.DTO.ConvenientDTO;
import com.founder.easy_route_assistant.DTO.FavoriteDTO;
import com.founder.easy_route_assistant.Entity.FavoriteEntity;
import com.founder.easy_route_assistant.Entity.UserEntity;
import com.founder.easy_route_assistant.Repository.FavoriteRepository;
import com.founder.easy_route_assistant.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    private final UserRepository userRepository;

    public FavoriteDTO savefavorite(String userId, FavoriteDTO favoriteDTO) {
        // Returns whether an entity with the given id exists.
        // 객체 생성 (부모테이블에 먼저 데이터를 삽입해야 자식테이블에 데이터삽입 가능)
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        // 해당 유저가 없는 경우

        // 즐겨찾기 중복 값 처리
        if (favoriteRepository.findByRoadNameAddress(favoriteDTO.getRoadNameAddress()).isEmpty()) {
            favoriteRepository.save(FavoriteEntity.builder()
                    .placeName(favoriteDTO.getPlaceName())
                    .roadNameAddress(favoriteDTO.getRoadNameAddress())
                    .longitude(favoriteDTO.getLongitude())
                    .latitude(favoriteDTO.getLatitude())
                    .user(userEntity.get())
                    .build());
        } else {
            System.out.println("중복된 값을 넣을 수 없음");
        }
        return  favoriteDTO;
    }

    public List<FavoriteDTO> getFavoriteList(String userId) {
        UserEntity userEntity = UserEntity.builder().userID(userId).build();
        List<FavoriteEntity> favoritecollection = favoriteRepository.findAllByUser(userEntity);

        List<FavoriteDTO> favoriteDTOList = new ArrayList<>();

        for (FavoriteEntity f : favoritecollection) {
            FavoriteDTO favoriteDTO = FavoriteDTO.builder()
                    .placeName(f.getPlaceName())
                    .roadNameAddress(f.getRoadNameAddress())
                    .latitude(f.getLatitude())
                    .longitude(f.getLongitude())
                    .build();
            favoriteDTOList.add(favoriteDTO);
        }
        return favoriteDTOList;
    }


}
