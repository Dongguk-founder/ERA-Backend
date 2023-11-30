package com.founder.easy_route_assistant.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor //final로 선언해야하기 때문 (불변성)
public enum Role {
    ADMIN("admin"),
    USER("user");

    private final String role;

}
