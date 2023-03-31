package com.likelion.beshop.entity;

import com.likelion.beshop.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
public class Member {
    @Id
    @Column(name = "member_id") //기본키 이름
    @GeneratedValue(strategy = GenerationType.AUTO) //자동 생성
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;
    private String pwd;
    private String address;

    public static Member createMember(MemberFormDto memberFormDto){
        Member member = new Member(); //인스턴스는 하나만 만듦
;       member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setPwd(memberFormDto.getPwd());
        member.setAddress(memberFormDto.getAddress());
        return member;
    }


}
