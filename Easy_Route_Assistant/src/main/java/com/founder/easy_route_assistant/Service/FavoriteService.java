package com.founder.easy_route_assistant.Service;

import com.founder.easy_route_assistant.DTO.UserDTO;
import com.founder.easy_route_assistant.Entity.FavoriteEntity;
import com.founder.easy_route_assistant.Entity.FavoriteItemEntity;
import com.founder.easy_route_assistant.Entity.UserEntity;
import com.founder.easy_route_assistant.Repository.FavoriteItemRepository;
import com.founder.easy_route_assistant.Repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final FavoriteItemRepository favoriteItemRepository;

    public String save(String userId) {
        Optional<FavoriteEntity> temp = favoriteRepository.findById(userId);
        if(temp.isEmpty()){
            return "해당 아이디를 찾을 수 없습니다.";
        }else {
            // Optional 언매핑
            FavoriteEntity favor = temp.get();

            return "즐겨찾기 저장 완료";
        }
    }
}
