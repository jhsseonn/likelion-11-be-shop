package com.likelion.beshop.repository;


import com.likelion.beshop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> { //<엔티티 타입 클래스, 기본키 타입>

}
