package com.founder.easy_route_assistant.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@NoArgsConstructor
@ToString
public class LoginDTO {
    private String userID;
    private String pwd;
}
