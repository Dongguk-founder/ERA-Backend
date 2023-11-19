package com.founder.easy_route_assistant.Entity;

import com.founder.easy_route_assistant.DTO.FavoriteDTO;
import com.founder.easy_route_assistant.DTO.UserDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "user")
public class UserEntity {
    // @GeneratedValue(strategy = GenerationType.IDENTITY) 지금은 안 쓸 건데 자동으로 값 1씩 증가해주며, pk 속성을 가짐
    @Id // primary key
    private String userID;

    @Column(length = 100)
    private String pwd;

    @Column(length = 100)
    private String userName;

    @Column(length = 100)
    private String userEmail;

    public static UserEntity toUserEntity(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();

        userEntity.setUserID(userDTO.getUserID());
        userEntity.setPwd(userDTO.getPwd());
        userEntity.setUserName(userDTO.getUserName());
        userEntity.setUserEmail(userDTO.getUserEmail());

        return userEntity;
    }
}