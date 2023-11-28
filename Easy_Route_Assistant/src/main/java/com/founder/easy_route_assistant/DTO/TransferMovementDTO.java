package com.founder.easy_route_assistant.DTO;

import lombok.*;

import java.net.URL;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferMovementDTO {
    //환승이동유형순서
    Double chtnMvTpOrdr;
    //도착지
    String edMovePath;
    //승강기상태코드
    Double elvtSttCd;
    //승강기유형코드
    Double elvtTpCd;
    //이미지 경로
    String imgPath;
    //상세 이동 내용
    String mvContDtl;
    //이동경로관리번호
    String mvPathMgNo;
    //시작지
    String stMovePath;

}
