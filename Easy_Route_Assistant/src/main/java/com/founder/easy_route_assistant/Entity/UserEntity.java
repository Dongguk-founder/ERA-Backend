package com.founder.easy_route_assistant.Entity;

import com.founder.easy_route_assistant.security.Role;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
@Table(name = "user")
public class UserEntity {
    @Id // primary key
    private String userID;

    @Column(length = 100)
    private String pwd;

    @Column(length = 100)
    private String userName;

    @Column(length = 100)
    private String userEmail;

    // enum 이름을 DB에 저장
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<FavoriteEntity> favorite;

}
