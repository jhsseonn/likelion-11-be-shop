package com.likelion.beshop.entity;

import com.likelion.beshop.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

// lombok 어노테이션 -> getter, setter, toString 메서드 자동 생성 가능
@Entity
@Table(name="member") // 엔티티와 매핑할 테이블 지정, member는 테이블 이름
@Getter @Setter
@ToString
public class Member {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    @Column(unique = true) // unique 속성 부여(type : boolean)
    private String email;
    private String password;
    private String address;

    public static Member createMember(MemberFormDto memberFormDto) {
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setPassword(memberFormDto.getPassword());
        member.setAddress(memberFormDto.getAddress());

        return member;
    }
}


