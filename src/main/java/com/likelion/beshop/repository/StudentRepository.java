package com.likelion.beshop.repository;

import com.likelion.beshop.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

}

