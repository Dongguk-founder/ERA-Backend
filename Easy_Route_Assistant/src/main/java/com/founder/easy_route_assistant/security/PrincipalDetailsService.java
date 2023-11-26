package com.founder.easy_route_assistant.security;

import com.founder.easy_route_assistant.Entity.UserEntity;
import com.founder.easy_route_assistant.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public PrincipalDetails loadUserByUsername(String userID) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findById(userID).orElseThrow(
                () -> new UsernameNotFoundException("not found userID: " + userID)
        );

        return new PrincipalDetails(userEntity);
    }
}
