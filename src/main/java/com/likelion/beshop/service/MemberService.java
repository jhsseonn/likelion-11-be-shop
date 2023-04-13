package com.likelion.beshop.service;

import com.likelion.beshop.constant.Role;
import com.likelion.beshop.entity.Member;
import com.likelion.beshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Collection;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    public Boolean isAuthenticatedAdmin(String email){
        if (isAdmin(email))
            return true;
        else
            return false;
    }

    private boolean isAdmin(String email){
        Member new_member = memberRepository.findByEmail(email);
        if (new_member.getRole().equals(Role.ADMIN)) {
            return true;
        }
        return false;
    }


    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null)
            throw new IllegalStateException("이미 가입된 회원입니다.");
    }

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);
        if (member == null)
            throw new UsernameNotFoundException(email);
        return User.builder()
                .username(member.getName())
                .password(member.getPwd())
                .roles(member.getRole().toString())
                .build();
    }
}
