package com.likelion.beshop.repository;

import com.likelion.beshop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface MemberRepository extends JpaRepository<Member, Long>{
    Member findByEmail(String email);

}
