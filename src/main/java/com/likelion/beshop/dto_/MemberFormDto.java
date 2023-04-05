package com.likelion.beshop.dto_;

import lombok.Getter;
import lombok.Setter;
import org.yaml.snakeyaml.events.Event;

@Getter
@Setter
public class MemberFormDto {
private String name;
private String email;
private String password;
private String address;

}
