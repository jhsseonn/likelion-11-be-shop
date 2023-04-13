package com.likelion.beshop.service;

import com.likelion.beshop.constant.Role;
import com.likelion.beshop.entity.Member;
import com.likelion.beshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember!=null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);
        if (member==null) {
            throw new UsernameNotFoundException(email);
        }
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString()) // roles은 enum이라 String 타입으로 가져와야 함
                .build();

    }



//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Optional<Member> memberEntityWrapper = Optional.ofNullable(memberRepository.findByEmail(email));
//        Member memberEntity = memberEntityWrapper.orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        List<GrantedAuthority> authorities = new ArrayList<>();
//
//        if (("admin@example.com").equals(email)) {
//            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.name()));
//        } else {
//            authorities.add(new SimpleGrantedAuthority(Role.USER.name()));
//        }
//
//        return User.builder()
//                .username(memberEntity.getEmail())
//                .password(memberEntity.getPassword())
//                .authorities(authorities)
//                .build();
//    }
}
