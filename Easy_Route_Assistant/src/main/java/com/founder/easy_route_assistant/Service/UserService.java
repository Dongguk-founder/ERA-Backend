package com.founder.easy_route_assistant.Service;

import com.founder.easy_route_assistant.DTO.LoginDTO;
import com.founder.easy_route_assistant.DTO.UserDTO;
import com.founder.easy_route_assistant.Entity.UserEntity;
import com.founder.easy_route_assistant.Repository.UserRepository;
import com.founder.easy_route_assistant.security.Role;
import com.founder.easy_route_assistant.token.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.ManyToAny;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired // 필요한 Bean 알아서 주입
    private final UserRepository userRepository; // jpa, MySQL dependency 추가

    @Autowired
    private JwtProvider jwtProvider;

    public String join(UserDTO userDTO) {
        if (userRepository.findById(userDTO.getUserID()).isPresent()) {
            return "아이디가 이미 존재합니다.";
        }

        String rawPwd = userDTO.getPwd();
        String encodedPwd = passwordEncoder.encode(rawPwd);

        // request -> DTO -> Entity -> Repository에서 save
        UserEntity userEntity = UserEntity.builder()
                .userID(userDTO.getUserID())
                .pwd(userDTO.getPwd())
                .userName(userDTO.getUserName())
                .userEmail(userDTO.getUserEmail())
                .role(Role.USER)
                .build();
        userEntity.setPwd(encodedPwd);

        userRepository.save(userEntity);
        //Repository의 save메서드 호출 (조건. entity객체를 넘겨줘야 함)

        return "회원가입 성공";
    }

    @Transactional
    public String login(LoginDTO loginDTO) {
        UserEntity userEntity = userRepository.findById(loginDTO.getUserID())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 사용자입니다."));

        if (!passwordEncoder.matches(loginDTO.getPwd(), userEntity.getPwd())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        return jwtProvider.createToken(userEntity.getUserID(), userEntity.getRole());
    }

    public Optional<UserEntity> getUserEntity(String userID) {
        Optional<UserEntity> userEntity = userRepository.findById(userID);

        return userEntity;
    }
}
