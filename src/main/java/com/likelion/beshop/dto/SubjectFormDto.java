package com.likelion.beshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectFormDto {
    private String cno;     // 과목번호
    private String cname;   // 과목이름
    private int credit;     // 학점
    private String dept;    // 학과
    private String prName;     // 담당교수
}
