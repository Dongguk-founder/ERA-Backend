package com.founder.easy_route_assistant.Service;

import com.founder.easy_route_assistant.Controller.ConvenientController;
import com.founder.easy_route_assistant.DTO.ConvenientDTO;
import com.founder.easy_route_assistant.DTO.RequestDTO;
import com.founder.easy_route_assistant.Entity.ConvenientEntity;
import com.founder.easy_route_assistant.Entity.RequestEntity;
import com.founder.easy_route_assistant.Entity.UserEntity;
import com.founder.easy_route_assistant.Repository.ConvenientRepository;
import com.founder.easy_route_assistant.Repository.RequestRepository;
import com.founder.easy_route_assistant.Repository.UserRepository;
import com.founder.easy_route_assistant.security.Role;
import com.founder.easy_route_assistant.token.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestService {
    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConvenientService convenientService;

    @Autowired
    private JwtProvider jwtProvider;

    public void createRequest(String userID, RequestDTO requestDTO) {
        requestDTO.setUserID(userID);
        RequestEntity requestEntity = converToEntity(requestDTO);

        requestRepository.save(requestEntity);
    }

    private RequestEntity converToEntity(RequestDTO requestDTO) {
        RequestEntity requestEntity = new RequestEntity();

        requestEntity.setConvenientName(requestDTO.getConvenientName());
        requestEntity.setContent(requestDTO.getContent());
        UserEntity userEntity = userRepository.findById(requestDTO.getUserID())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 사용자입니다."));
        requestEntity.setUserEntity(userEntity);

        return requestEntity;
    }

    public List<RequestDTO> getAllRequests(String jwt) {
        String role = jwtProvider.getRole(jwt);
        String userID = jwtProvider.getUserID(jwt);

        List<RequestEntity> requestEntities = new ArrayList<>();
        List<RequestDTO> requestDTOS = new ArrayList<>();

        if (role.equals("USER")) {
            UserEntity userEntity = userRepository.findById(userID)
                    .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 사용자입니다."));
            requestEntities = requestRepository.findByUserEntity(userEntity);
        } else if(role.equals("ADMIN")) {
            requestEntities = requestRepository.findAll();
        }

        for(RequestEntity requestEntity : requestEntities) {
            RequestDTO requestDTO = RequestDTO.toRequestDTO(requestEntity);

            requestDTOS.add(requestDTO);
        }

        return requestDTOS;
    }

    public void updateRequest(String jwt, RequestDTO requestDTO) {
        String role = jwtProvider.getRole(jwt);
        if (role.equals("ADMIN")) {
            RequestEntity requestEntity = requestRepository.findById(requestDTO.getId())
                    .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 요청입니다."));

            if (requestDTO.isAccepted()) { // request에서 accepted가 true일 경우
                ConvenientDTO convenientDTO = ConvenientDTO.toConvenient(requestEntity); // 요청 받은 변경 사항 그대로 등록

                convenientService.save(convenientDTO);
            }

            requestRepository.delete(requestEntity); // request에서 accepted가 false일 경우
        }
    }

}
