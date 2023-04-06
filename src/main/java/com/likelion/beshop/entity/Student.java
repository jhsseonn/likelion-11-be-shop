package com.likelion.beshop.entity;

import com.likelion.beshop.dto.StudentFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
@Entity
@Table(name="student")
@Getter
@Setter
@ToString
public class Student {
    @Id
    @Column(name="student_id")
    @GeneratedValue(strategy= GenerationType.AUTO)

    private Long id;
    private String name;
    private Integer age;
    @Column(unique=true)
    private String snum;
    private String dept;

    public static Student createMember(StudentFormDto studentFormDto) {
        Student student = new Student();
        student.setName(studentFormDto.getName());
        student.setAge(studentFormDto.getAge());
        student.setSnum(studentFormDto.getSnum());
        student.setDept(studentFormDto.getDept());

        return student;
    }
}
