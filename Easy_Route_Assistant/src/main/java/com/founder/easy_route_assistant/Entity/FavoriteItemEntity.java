package com.founder.easy_route_assistant.Entity;

import com.founder.easy_route_assistant.DTO.FavoriteDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "favorite_item")
public class FavoriteItemEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String placeName;

    @Column
    private String roadNameAddress;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    //다대일 매핑
    @ManyToOne
    @JoinColumn(name = "favorite_id")
    private FavoriteEntity favoriteEntity;

    public static FavoriteItemEntity toFavoriteItemEntity(FavoriteDTO favoriteDTO) {
        FavoriteItemEntity favoriteItemEntity = new FavoriteItemEntity();

        favoriteItemEntity.placeName = favoriteDTO.getPlaceName();
        favoriteItemEntity.roadNameAddress = favoriteDTO.getRoadNameAddress();
        favoriteItemEntity.latitude = favoriteDTO.getLatitude();
        favoriteItemEntity.longitude = favoriteItemEntity.getLongitude();


        return favoriteItemEntity;
    }
}
