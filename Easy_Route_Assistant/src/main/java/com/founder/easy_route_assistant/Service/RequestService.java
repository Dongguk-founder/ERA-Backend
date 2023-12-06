package com.founder.easy_route_assistant.Service;

import com.founder.easy_route_assistant.DTO.Convenient.ConvenientDTO;
import com.founder.easy_route_assistant.DTO.Request.RequestDTO;
import com.founder.easy_route_assistant.DTO.Request.RequestDTOList;
import com.founder.easy_route_assistant.Entity.RequestEntity;
import com.founder.easy_route_assistant.Entity.UserEntity;
import com.founder.easy_route_assistant.Repository.RequestRepository;
import com.founder.easy_route_assistant.Repository.UserRepository;
import com.founder.easy_route_assistant.Service.Convenient.ConvenientService;
import com.founder.easy_route_assistant.config.token.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;

    private final UserRepository userRepository;

    private final ConvenientService convenientService;

    @Autowired
    private JwtProvider jwtProvider;

    public void createRequest(String jwt, RequestDTO requestDTO) {
        String userID = jwtProvider.getUserID(jwt);
        UserEntity userEntity = userRepository.findById(userID).orElseThrow(
                () -> new IllegalArgumentException("가입되지 않은 사용자입니다.")
        );

        RequestEntity requestEntity = RequestEntity.builder()
                .convenientType(requestDTO.getConvenientType())
                .point(requestDTO.getPoint())
                .content(requestDTO.getContent())
                .userEntity(userEntity)
                .accepted(false)
                .build();

        requestRepository.save(requestEntity);
    }

    public RequestDTOList getAllRequests(String jwt) {
        String role = jwtProvider.getRole(jwt);
        String userID = jwtProvider.getUserID(jwt);

        List<RequestEntity> requestEntities = new ArrayList<>();
        List<RequestDTO> requestDTOS = new ArrayList<>();

        RequestDTOList requestDTOList = new RequestDTOList();

        UserEntity userEntity = new UserEntity();

        if (role.equals("USER")) {
            userEntity = userRepository.findById(userID)
                    .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 사용자입니다."));
            requestEntities = requestRepository.findByUserEntity(userEntity);
        } else if(role.equals("ADMIN")) {
            requestEntities = requestRepository.findAll();
        }

        for(RequestEntity requestEntity : requestEntities) {
            RequestDTO requestDTO = RequestDTO
                    .builder()
                    .id(requestEntity.getId())
                    .convenientType(requestEntity.getConvenientType())
                    .point(requestEntity.getPoint())
                    .roadAddr(requestEntity.getRoadAddr())
                    .content(requestEntity.getContent())
                    .userID(userEntity.getUserID())
                    .build();

            requestDTOS.add(requestDTO);
        }

        requestDTOList.setRequestDTOList(requestDTOS);

        return requestDTOList;
    }

    public void updateRequest(String jwt, int id, boolean accepted, ConvenientDTO convenientDTO) {
        String role = jwtProvider.getRole(jwt);
        if (role.equals("ADMIN")) {
            RequestEntity requestEntity = requestRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 요청입니다."));

            if (accepted) { // request에서 accepted가 true일 경우 ConvenientService로 보내버리기
                convenientService.update(convenientDTO);
            }

            requestRepository.delete(requestEntity); // request에서 accepted가 false일 경우
        }
    }

}