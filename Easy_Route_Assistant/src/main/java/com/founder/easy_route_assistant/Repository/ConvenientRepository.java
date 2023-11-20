package com.founder.easy_route_assistant.Repository;

import com.founder.easy_route_assistant.Entity.ConvenientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConvenientRepository extends JpaRepository<ConvenientEntity, Integer> { // int 안 됨?
}
