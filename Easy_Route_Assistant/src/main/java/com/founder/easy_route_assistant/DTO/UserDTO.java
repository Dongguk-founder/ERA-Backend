package com.founder.easy_route_assistant.DTO;

import com.founder.easy_route_assistant.Entity.UserEntity;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@ToString
public class UserDTO {
    private String userID;
    private String pwd;
    private String userName;
    private String userEmail;

    public static UserDTO toUserDTO(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();

        userDTO.setUserID(userEntity.getUserID());
        userDTO.setPwd(userEntity.getPwd());
        userDTO.setUserName(userEntity.getUserName());
        userDTO.setUserEmail(userEntity.getUserEmail());

        return userDTO;
    }
}
