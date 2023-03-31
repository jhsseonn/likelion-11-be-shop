package com.likelion.beshop.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

}
