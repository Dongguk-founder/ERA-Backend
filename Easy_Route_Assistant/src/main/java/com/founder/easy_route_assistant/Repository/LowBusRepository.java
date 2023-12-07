package com.founder.easy_route_assistant.Repository;

import com.founder.easy_route_assistant.Entity.LowBusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // <객체 type, pk type>
public interface LowBusRepository extends JpaRepository<LowBusEntity,String> {
    Optional<LowBusEntity> findById(String busNum);
}
