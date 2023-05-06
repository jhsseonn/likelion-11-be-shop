package com.likelion.beshop.entity;

import com.likelion.beshop.dto.SubjectFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="subject")
@Getter
@Setter
@ToString
public class Subject {
    @Id
    @Column(name = "subject_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String cno;     // 과목번호
    private String cname;   // 과목이름
    private int credit;     // 학점
    private String dept;    // 학과
    private String prName;     // 담당교수


    public static Subject createSubject(SubjectFormDto subjectFormDto) { // Subject 엔터티 생성 메소드
        Subject subject = new Subject();
        subject.setCno(subjectFormDto.getCno());
        subject.setCname(subjectFormDto.getCname());
        subject.setCredit(subjectFormDto.getCredit());
        subject.setDept(subjectFormDto.getDept());
        subject.setPrName(subjectFormDto.getPrName());

        return subject;
    }
}
