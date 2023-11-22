package com.founder.easy_route_assistant.Repository;

import com.founder.easy_route_assistant.Entity.FavoriteItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteItemRepository extends JpaRepository<FavoriteItemEntity,Long> {
    Optional<FavoriteItemEntity> findByRoadNameAddress(String s);
}
