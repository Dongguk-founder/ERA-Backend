package com.founder.easy_route_assistant.Service;

import com.founder.easy_route_assistant.DTO.ConvenientDTO;
import com.founder.easy_route_assistant.DTO.RequestDTO;
import com.founder.easy_route_assistant.Entity.RequestEntity;
import com.founder.easy_route_assistant.Entity.UserEntity;
import com.founder.easy_route_assistant.Repository.RequestRepository;
import com.founder.easy_route_assistant.Repository.UserRepository;
import com.founder.easy_route_assistant.config.token.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

        RequestEntity requestEntity = RequestEntity
                .builder()
                .convenientName(requestDTO.getConvenientName())
                .point(requestDTO.getPoint())
                .content(requestDTO.getContent())
                .userEntity(userEntity)
                .accepted(false)
                .build();

        requestRepository.save(requestEntity);
    }

    public List<RequestDTO> getAllRequests(String jwt) {
        String role = jwtProvider.getRole(jwt);
        String userID = jwtProvider.getUserID(jwt);

        List<RequestEntity> requestEntities = new ArrayList<>();
        List<RequestDTO> requestDTOList = new ArrayList<>();

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
                    .convenientName(requestEntity.getConvenientName())
                    .point(requestEntity.getPoint())
                    .content(requestEntity.getContent())
                    .accepted(requestEntity.getAccepted())
                    .userID(userEntity.getUserID())
                    .build();

            requestDTOList.add(requestDTO);
        }

        return requestDTOList;
    }

    public HttpStatus updateRequest(String jwt, RequestDTO requestDTO) {
        String role = jwtProvider.getRole(jwt);
        if (role.equals("ADMIN")) {
            RequestEntity requestEntity = requestRepository.findById(requestDTO.getId())
                    .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 요청입니다."));

            if (requestDTO.isAccepted()) { // request에서 accepted가 true일 경우
                ConvenientDTO convenientDTO = ConvenientDTO.builder()
                        .convenientName(requestDTO.getConvenientName())
                        .content(requestDTO.getContent())
                        .point(requestDTO.getPoint())
                        .build(); // 요청 받은 변경 사항 그대로 등록

                convenientService.save(convenientDTO);
            }

            requestRepository.delete(requestEntity); // request에서 accepted가 false일 경우
            return HttpStatus.ACCEPTED;
        }
        else {
            return HttpStatus.BAD_REQUEST;
        }
    }

}