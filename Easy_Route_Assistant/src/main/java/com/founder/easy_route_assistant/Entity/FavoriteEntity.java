package com.founder.easy_route_assistant.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "favorite")
public class FavoriteEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String userId;

    //일대다 매핑
    @OneToMany(mappedBy = "favoriteEntity",cascade = CascadeType.ALL)
    private List<FavoriteItemEntity> favoriteCollection;

}
