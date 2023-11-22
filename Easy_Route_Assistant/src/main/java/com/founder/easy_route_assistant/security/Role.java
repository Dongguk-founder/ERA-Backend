package com.founder.easy_route_assistant.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    ADMIN("admin"),
    USER("user");

    private String role;

    /*Role(String role) {
        this.role = role;
    }

    public String value() {
        return role;
    }*/
}
