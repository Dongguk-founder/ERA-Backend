package com.founder.easy_route_assistant.Repository;

import com.founder.easy_route_assistant.Entity.ConvenientEntity;
import org.springframework.data.geo.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConvenientRepository extends JpaRepository<ConvenientEntity, Integer> {
    List<ConvenientEntity> findAllByConvenientType(String convenientType);

    ConvenientEntity findByPoint(Point point);
}
