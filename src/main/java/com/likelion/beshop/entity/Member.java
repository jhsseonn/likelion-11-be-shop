package com.likelion.beshop.entity;

import com.likelion.beshop.constant.Role;
import com.likelion.beshop.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name="member")
@Getter @Setter @ToString
public class Member extends BaseEntity {
    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String name;
    @Column(unique = true)
    private String email;
    private String pwd;
    private String address;
    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());

        String pwd = passwordEncoder.encode(memberFormDto.getPwd());
        member.setPwd(pwd);

        member.setRole(Role.USER);

        return member;
    }

//    public interface UserDetailService{
//        UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
//    }
//
//    public interface UserDetails extends Serializable {
//        Collection<? extends GrantedAuthority> getAuthorities();
//        String getPassword();
//        String getUsername();
//        boolean isAccountNonExpired();
//        boolean isAccountNonLocked();
//        boolean isCredentialsNonExpired();
//        boolean isEnabled();
//    }
}
