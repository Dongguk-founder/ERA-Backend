package com.founder.easy_route_assistant.Service;

import com.founder.easy_route_assistant.DTO.RequestDTO;
import com.founder.easy_route_assistant.Entity.RequestEntity;
import com.founder.easy_route_assistant.Entity.UserEntity;
import com.founder.easy_route_assistant.Repository.RequestRepository;
import com.founder.easy_route_assistant.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestService {
    @Autowired
    RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    public void createRequest(RequestDTO requestDTO) {
        RequestEntity requestEntity = converToEntity(requestDTO);

        requestRepository.save(requestEntity);
    }

    private RequestEntity converToEntity(RequestDTO requestDTO) {
        RequestEntity requestEntity = new RequestEntity();

        requestEntity.setTitle(requestDTO.getTitle());
        requestEntity.setContent(requestDTO.getContent());
        UserEntity userEntity = userRepository.findById(requestDTO.getUserID())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 사용자입니다."));
        requestEntity.setUserEntity(userEntity);

        return requestEntity;
    }
}
