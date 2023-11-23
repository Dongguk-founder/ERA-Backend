package com.founder.easy_route_assistant.Repository;

import com.founder.easy_route_assistant.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository // <객체 type, pk type>
public interface UserRepository extends JpaRepository<UserEntity, String> {
    // Optional<UserEntity> findByUserID(String userID); // 여기 문제 있음?
}
