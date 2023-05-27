package com.likelion.beshop.service;

import com.likelion.beshop.constant.ItemSellStatus;
import com.likelion.beshop.dto_.ItemFormDto;
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
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemServiceTest {
    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemImgRepository itemImgRepository;


    List<MultipartFile> createMultipartFiles() throws Exception { //MockMultipartFile 클래스를 사용하여 가짜 multipartFile 리스트 만들어주는 메소드

        List<MultipartFile> multipartFileList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            String path = "C:/lionimage";
            String imageName = "image" + i + ".jpg";
            MockMultipartFile multipartFile = new MockMultipartFile(path, imageName,
                    "image/jpg", new byte[]{1, 2, 3, 4});
            multipartFileList.add(multipartFile);
        }
        return multipartFileList;
    }

    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void saveItem() throws Exception{
        ItemFormDto itemFormDto = new ItemFormDto();
        itemFormDto.setItemName("상품이름");
        itemFormDto.setItemSellStatus(ItemSellStatus.SELL);
        itemFormDto.setDetail("상품 상세설명");
        itemFormDto.setPrice(1000);
        itemFormDto.setStockNumber(10);

        List<MultipartFile> multipartFileList = createMultipartFiles();
        Long itemId = itemService.saveItem(itemFormDto,multipartFileList); //상품 데이터, 생성한 이미지 정보를 파라미터로 넘겨서 저장, 해당 상품 아이디 값 받아오기
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);//레포지에서 생성한 쿼리 메소드 사용하여 해당 상품 이미지 리스트 받아오기

        Item item = itemRepository.findById(itemId) //아이템레포지토리의 쿼리 메소드를 사용해서 해당 상품 받아오기(예외처리)
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(itemFormDto.getItemName(),item.getItemName());
        assertEquals(itemFormDto.getItemSellStatus(),item.getItemSellStatus());
        assertEquals(itemFormDto.getDetail(),item.getDetail());
        assertEquals(itemFormDto.getPrice(),item.getPrice());
        assertEquals(itemFormDto.getStockNumber(),item.getStockNumber());
        if(itemImgList.size() !=0){ //첫번째 파일의 원본 이미지 파일명은 상품 데이터가 아닌 '이미지'에 저장해둔것에서 비교
            assertEquals(multipartFileList.get(0).getOriginalFilename(),itemImgList.get(0).getOriImgName());
        }
    }

}