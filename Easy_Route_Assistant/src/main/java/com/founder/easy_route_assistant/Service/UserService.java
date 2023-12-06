package com.founder.easy_route_assistant.Service;

import com.founder.easy_route_assistant.DTO.User.LoginDTO;
import com.founder.easy_route_assistant.DTO.User.UserDTO;
import com.founder.easy_route_assistant.Entity.UserEntity;
import com.founder.easy_route_assistant.Repository.UserRepository;
import com.founder.easy_route_assistant.config.Role;
import com.founder.easy_route_assistant.config.token.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired // 필요한 Bean 알아서 주입
    private final UserRepository userRepository; // jpa, MySQL dependency 추가

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public UserDTO join(UserDTO userDTO) {

        if (userRepository.findById(userDTO.getUserID()).isPresent()) {
            System.out.println("아이디가 이미 존재합니다.");
            return null;
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

        return userDTO;
    }

    @Transactional
    public String login(LoginDTO loginDTO) {
        UserEntity userEntity = userRepository.findById(loginDTO.getUserID())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 사용자입니다."));

        if (!passwordEncoder.matches(loginDTO.getPwd(), userEntity.getPwd())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        // redisTemplate.opsForValue().set("userID:"+userEntity.getUserID(), jwt, jwtProvider.getExpiration(jwt));

        return jwtProvider.createToken(userEntity.getUserID(), userEntity.getRole());
    }

    public Optional<UserEntity> getUserEntity(String userID) {
        Optional<UserEntity> userEntity = userRepository.findById(userID);

        return userEntity;
    }

    @Transactional
    public void logout(String jwt) {
        redisTemplate.opsForValue().set(jwt, "logout", jwtProvider.getExpiration(jwt), TimeUnit.MILLISECONDS);
    }
}
