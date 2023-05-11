package com.likelion.beshop.service;

import com.likelion.beshop.constant.ItemSellStatus;
import com.likelion.beshop.dto.ItemFormDto;
import com.likelion.beshop.dto.ItemImgDto;
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
    ItemImgRepository itemImgRepository;
    @Autowired
    ItemRepository itemRepository;
    List<MultipartFile> createMultipartFiles() throws Exception{
        List<MultipartFile> multipartFileList = new ArrayList<>();
        for(int i=0;i<5;i++){
            String path="/Users/soobin/backend/likelion-11-be-shop/src/main/resources/static/images/";
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
    void saveItem() throws Exception{
        ItemFormDto itemFormDto = new ItemFormDto();
        itemFormDto.setName("상품명");
        itemFormDto.setItemSellStatus(ItemSellStatus.SALE);
        itemFormDto.setDescription("상품 설명");
        itemFormDto.setPrice(10000);
        itemFormDto.setCount(10);

        //가짜 multipartfiles 생성
        List<MultipartFile> multipartFileList = createMultipartFiles();
        //상품 등록
        Long itemId = itemService.saveItem(itemFormDto, multipartFileList);
        //상품이미지 가져오기
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderById(itemId);

        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(itemFormDto.getName(), item.getName());
        assertEquals(itemFormDto.getItemSellStatus(), item.getItemSellStatus());
        assertEquals(itemFormDto.getDescription(), item.getDescription());
        assertEquals(itemFormDto.getPrice(), item.getPrice());
        assertEquals(itemFormDto.getCount(), item.getCount());
        if (itemImgList.size() != 0){
//            assertEquals(itemFormDto.getItemImages().get(0).getOriginalFileName(), itemImgList.get(0).getOriginalFileName());
        }
    }
}