package com.likelion.beshop.service;

import com.likelion.beshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.likelion.beshop.entity.Member;
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());

        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }
}

