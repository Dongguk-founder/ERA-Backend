package com.founder.easy_route_assistant.Entity;

import com.founder.easy_route_assistant.DTO.UserDTO;
import com.founder.easy_route_assistant.security.Role;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
@Table(name = "user")
public class UserEntity {
    // @GeneratedValue(strategy = GenerationType.IDENTITY) 지금은 안 쓸 건데 자동으로 값 1씩 증가해주며, pk 속성을 가짐
    @Id // primary key
    private String userID;

    @Column
    private String pwd;

    @Column
    private String userName;

    @Column
    private String userEmail;

    // enum 이름을 DB에 저장
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<FavoriteEntity> favorite;

}
