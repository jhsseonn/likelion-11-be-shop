package com.likelion.beshop.entity;

import com.likelion.beshop.dto.CharacterFormDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "character")
@Getter @Setter
@ToString
public class Character {
    @Id
    @Column(name = "character_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String character_name;
    private String position_name;
    private String hair_color;
    public static Character createCharacter(CharacterFormDto characterFormDto) {
        Character character = new Character ();
        character.setCharacter_name(characterFormDto.getCharacter_name());
        character.setPosition_name(characterFormDto.getPosition_name());
        character.setHair_color(characterFormDto.getHair_color());


        return character;
    }
}
