package com.founder.easy_route_assistant.Repository;

import com.founder.easy_route_assistant.Entity.FavoriteEntity;
import com.founder.easy_route_assistant.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository// <객체 type, pk type>
public interface FavoriteRepository extends JpaRepository<FavoriteEntity,Long> {
    Optional<FavoriteEntity> findByRoadNameAddress(String s);

    List<FavoriteEntity> findAllByUser(UserEntity userEntity);
}
