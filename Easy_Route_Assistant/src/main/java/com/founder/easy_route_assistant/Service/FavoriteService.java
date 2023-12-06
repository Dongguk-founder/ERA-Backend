package com.founder.easy_route_assistant.Service;

import com.founder.easy_route_assistant.DTO.Favorite.FaviriteListDTO;
import com.founder.easy_route_assistant.DTO.Favorite.FavoriteDTO;
import com.founder.easy_route_assistant.DTO.User.UserDTO;
import com.founder.easy_route_assistant.Entity.FavoriteEntity;
import com.founder.easy_route_assistant.Entity.UserEntity;
import com.founder.easy_route_assistant.Repository.FavoriteRepository;
import com.founder.easy_route_assistant.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

        Optional<UserEntity> userEntity = userRepository.findById(userId);
        UserDTO userDTO = new UserDTO();
        if (userEntity.isPresent()){
            userDTO = UserDTO.builder()
                    .userID(userEntity.get().getUserID())
                    .pwd(userEntity.get().getPwd())
                    .userName(userEntity.get().getUserName())
                    .userEmail(userEntity.get().getUserEmail())
                    .role(userEntity.get().getRole())
                    .build();

            // 즐겨찾기 중복 값 처리
            if (favoriteRepository.findByUserAndRoadNameAddress(userEntity.get(),favoriteDTO.getRoadNameAddress()).isEmpty()) {
                favoriteRepository.save(FavoriteEntity.builder()
                        .placeName(favoriteDTO.getPlaceName())
                        .roadNameAddress(favoriteDTO.getRoadNameAddress())
                        .point(favoriteDTO.getPoint())
                        .user(userEntity.get())
                        .build());

            } else {
                System.out.println("중복된 값을 넣을 수 없음");
            }
        }else {
            System.out.println("해당 아이디를 찾을 수 없음");
        }
        return favoriteDTO;

    }

    public FaviriteListDTO getFavoriteList(String userId) {

        FaviriteListDTO favoriteListDTO = new FaviriteListDTO();

        Optional<UserEntity> userEntity = userRepository.findById(userId);
        List<FavoriteEntity> favoritecollection = favoriteRepository.findAllByUser(userEntity);

        List<FavoriteDTO> favoriteDTOList = new ArrayList<>();

        for (FavoriteEntity f : favoritecollection) {
            FavoriteDTO favoriteDTO = FavoriteDTO.builder()
                    .id(f.getId())
                    .placeName(f.getPlaceName())
                    .roadNameAddress(f.getRoadNameAddress())
                    .point(f.getPoint())
                    .build();

            favoriteDTOList.add(favoriteDTO);
        }
        favoriteListDTO.setFavoriteList(favoriteDTOList);
        return favoriteListDTO;
    }

    public void deleteFavorite(Long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }
}
