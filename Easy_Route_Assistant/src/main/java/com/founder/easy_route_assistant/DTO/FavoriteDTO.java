package com.founder.easy_route_assistant.DTO;

import com.founder.easy_route_assistant.Entity.FavoriteEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@NoArgsConstructor
@ToString
public class FavoriteDTO {
    private String userID;
    private String placeName;
    private String roadNameAddress;
    private Double latitude;
    private Double longitude;


    public static FavoriteDTO toFavoriteDTO(FavoriteEntity favoriteEntity) {
        FavoriteDTO favoriteDTO = new FavoriteDTO();



        return favoriteDTO;
    }

}
