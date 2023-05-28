package com.likelion.beshop.service;

import com.likelion.beshop.constant.ItemSellStatus;
import com.likelion.beshop.dto.ItemFormDto;
import com.likelion.beshop.entity.Item;
import com.likelion.beshop.entity.ItemImg;
import com.likelion.beshop.repository.ItemImgRepository;
import com.likelion.beshop.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;

import javax.lang.model.element.Name;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
public class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemImgRepository itemImgRepository;

    // MockMultipartFile 클래스를 사용하여 가짜 MultipartFile 리스트를 만들어주는 메소드
    List<MultipartFile> createMultipartFiles() throws Exception{

        List<MultipartFile> multipartFileList = new ArrayList<>();

        for(int i=0;i<5;i++){
            String path = "C:/shop/item";
            String imageName = "image" + i + ".jpg";
            MockMultipartFile multipartFile =
                    new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1,2,3,4});
            multipartFileList.add(multipartFile);
        }

        return multipartFileList;
    }

    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void saveItem() throws Exception {
        //상품 데이터 세팅
        ItemFormDto itemFormDto = new ItemFormDto();
        itemFormDto.setName("테스트상품");
        itemFormDto.setStatus(ItemSellStatus.SELLING);
        itemFormDto.setContent("테스트 상품 입니다.");
        itemFormDto.setPrice(1000);
        itemFormDto.setNum(100);

        List<MultipartFile> multipartFileList = createMultipartFiles(); // createMultipartFiles로 가짜 MultipartFile 리스트 생성

        Long itemId = itemService.saveItem(itemFormDto, multipartFileList); // 상품 데이터, 생성한 이미지 정보를 파라미터로 넘겨서 저장하고, 해당 상품 아이디 값 받아오기
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId); //  itemImgRepository에서 생성한 쿼리메소드 사용해서 해당 상품의 이미지 리스트 받아오기

        // itemRepository의 쿼리메소드 사용해서 해당 상품 받아오기 (.orElseThrow로 예외 처리)
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        // assertEquals로 입력한 상품 데이터와 저장된 상품 데이터가 동일한 지 확인
        assertEquals(itemFormDto.getName(), item.getName());
        assertEquals(itemFormDto.getStatus(), item.getStatus());
        assertEquals(itemFormDto.getContent(), item.getContent());
        assertEquals(itemFormDto.getPrice(), item.getPrice());
        assertEquals(itemFormDto.getNum(), item.getNum());
        assertEquals(multipartFileList.get(0).getOriginalFilename(), itemImgList.get(0).getOriginalImageName()); // 첫 번째 파일의 원본 이미지 파일명은 상품 데이터가 아닌 '이미지'를 저장해둔 것에서 비교
    }


}
