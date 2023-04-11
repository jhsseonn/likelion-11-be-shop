package com.likelion.beshop.repository;

import com.likelion.beshop.constant.Role;
import com.likelion.beshop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);
}
