package com.founder.easy_route_assistant.DTO.User;

import com.founder.easy_route_assistant.config.Role;
import lombok.*;

@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor @Builder
public class UserDTO {
    private String userID;
    private String pwd;
    private String userName;
    private String userEmail;
    private Role role;

}
