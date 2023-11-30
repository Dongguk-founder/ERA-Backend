package com.founder.easy_route_assistant.DTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaviriteListDTO {
    List<FavoriteDTO> favoriteList;
}
