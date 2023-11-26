package com.founder.easy_route_assistant.DTO;

import com.founder.easy_route_assistant.security.Role;
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
