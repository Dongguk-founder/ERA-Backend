package com.founder.easy_route_assistant.DTO;

import com.founder.easy_route_assistant.Entity.RequestEntity;
import com.founder.easy_route_assistant.Entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter @Setter
@ToString
public class RequestDTO {
    private String title;
    private String content;
    private boolean accepted;
    private String userID;

    public static RequestDTO toRequestDTO(RequestEntity requestEntity) {
        RequestDTO requestDTO = new RequestDTO();

        requestDTO.setTitle(requestEntity.getTitle());
        requestDTO.setContent(requestEntity.getContent());
        requestDTO.setAccepted(requestEntity.getAccepted());

        UserEntity userEntity = requestEntity.getUserEntity();
        requestDTO.setUserID(userEntity.getUserID());

        return requestDTO;
    }
}
