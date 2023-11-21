package com.founder.easy_route_assistant.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name="request")
public class RequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    // @ColumnDefault("false")
    private Boolean accepted = false;

    // @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ManyToOne
    @JoinColumn(name="userid")
    private UserEntity userEntity;

    /*@Builder
    public static RequestEntity toRequestEntity(RequestDTO requestDTO) {
        RequestEntity requestEntity = new RequestEntity();

        requestEntity.title = requestDTO.getTitle();
        requestEntity.content = requestDTO.getContent();
        // requestEntity.userId = requestDTO.getUserID();

        return requestEntity;
    }*/
}
