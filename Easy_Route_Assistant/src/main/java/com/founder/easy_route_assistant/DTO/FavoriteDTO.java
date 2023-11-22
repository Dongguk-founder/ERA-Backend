package com.founder.easy_route_assistant.DTO;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor @Builder
@ToString
public class FavoriteDTO {
    private String placeName;
    private String roadNameAddress;
    private Double latitude;
    private Double longitude;


}
