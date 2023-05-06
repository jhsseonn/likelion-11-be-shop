package com.likelion.beshop.repository;

import com.likelion.beshop.entity.Character;
import com.likelion.beshop.repository.CharacterRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class CharacterRepositoryTest {

    @Autowired
    CharacterRepository characterRepository;
    @Test
    @DisplayName("캐릭터 정보 테스트")
    public void createCharacterTest() {
        Character character = new Character();
        character.setCharacter_name("MINA");
        character.setPosition_name("HEALER");
        character.setHair_color("BLUE");
        Character savedCharacter = characterRepository.save(character);
        System.out.println(savedCharacter.toString());
    }

}
