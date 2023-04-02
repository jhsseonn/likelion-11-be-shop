package com.likelion.beshop.entity;

import com.likelion.beshop.dto_.MemberFormDto;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name ="member")
@Getter
@Setter
@ToString

public class Member {
    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; //기본키가 되어있는거랑 연결된 것

    private String name;
    @Column(unique = true)
    private String email;
    private String pwd;
    private String address;
    public static Member createMember(MemberFormDto memberFormDto){
        Member member = new Member();
        member.setName(memberFormDto.getName());
       member.setPwd(memberFormDto.getPassword());
       member.setEmail(memberFormDto.getEmail());
       member.setAddress(memberFormDto.getAddress());

        return member;
    }
}

