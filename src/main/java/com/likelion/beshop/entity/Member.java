package com.likelion.beshop.entity;

import com.likelion.beshop.dto.MemberFormDto;
import com.likelion.beshop.dto.StudentFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name="member")
@Getter
@Setter
@ToString

public class Member {
    @Id
    @Column(name="student_id")
    @GeneratedValue(strategy= GenerationType.AUTO)

    private Long id;
    private String name;
    @Column(unique=true)
    private String email;
    private String password;
    private String address;

    @Enumerated(EnumType.STRING)
    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setPassword(memberFormDto.getPassword());
        member.setAddress(memberFormDto.getAddress());

        String pwd = passwordEncoder.encode(memberFormDto.getPassword());

        return member;
    }

}
