package com.founder.easy_route_assistant.Repository;

import com.founder.easy_route_assistant.Entity.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, Integer> {
}
