package com.likelion.beshop.repository;

import com.likelion.beshop.entity.Subject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class SubjectRepositoryTest {
    @Autowired
    SubjectRepository subjectRepository;

    @Test
    @DisplayName("교과목 테스트")
    public void createSubjectTest() {
        //(1) 객체 생성
        Subject subject = new Subject();

        //(2) 객체의 필드 값 설정
        subject.setCno("20230331");
        subject.setCname("김학생");
        subject.setCredit(3);
        subject.setDept("컴퓨터");
        subject.setPrName("이선생");

        // (3) subjectRepository의 save 메서드를 사용해 해당 객체 저장
        // save 메서드의 파라미터에 위에서 생성한 객체 넣기
        Subject savedSub = subjectRepository.save(subject);

        // (4) toString() 메서드 이용해서 객체 출력
        System.out.println(savedSub.toString());

    }
}
