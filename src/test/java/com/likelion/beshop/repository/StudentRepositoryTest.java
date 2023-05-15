package com.likelion.beshop.repository;

import com.likelion.beshop.entity.Student;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
public class StudentRepositoryTest {
    @Autowired
    StudentRepository studentRepository;

    @Test
    @DisplayName("학생등록 테스트")
    public void createStudentTest() {
        Student student = new Student();
        student.setName("최유신");
        student.setAge(22);
        student.setSnum("2021111412");
        student.setDept("Software Convergence");

        Student savedStudent = studentRepository.save(student);
        System.out.println(savedStudent.toString());
    }
}


