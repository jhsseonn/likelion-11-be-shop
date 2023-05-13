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

import javax.persistence.EntityExistsException;
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
    ItemImgRepository itemImgRepository;

    @Autowired
    ItemRepository itemRepository;


    List<MultipartFile> createMultipartFiles() throws Exception { // MockMultipartFile 클래스를 사용해 가짜 MultipartFile 리스트를 만들어주는 메소드 작성
        List<MultipartFile> multipartFileList = new ArrayList<>();

        for(int i=0; i<5; i++) {
            String path = "C:/Users/sjtjd/Downloads/shop (1)/likelion-11-be-shop/src/main/resources/static/item";
            String imageName = "image" + i + ".jpg";
            MockMultipartFile multipartFile =
                    new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1,2,3,4});
            multipartFileList.add(multipartFile);
        }

        return multipartFileList;
    }


    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username = "admin", roles="ADMIN") //MockUser 사용
    void saveItem() throws Exception {

        // 상품 데이터 세팅
        ItemFormDto itemFormDto = new ItemFormDto();
        itemFormDto.setItemDetail("킨더초콜릿입니다.");
        itemFormDto.setItemNm("초콜릿");
        itemFormDto.setStockNumber(5);
        itemFormDto.setPrice(3000);
        itemFormDto.setItemSellStatus(ItemSellStatus.SELL);

        // createMultipartFiles로 가짜 MultipartFile 리스트 생성
        List<MultipartFile> multipartFileList = createMultipartFiles();

        // 상품 데이터(itemFormDto), 생성한 이미지 정보(multipartFileList)를 파라미터로 넘겨서 저장하고, 해당 상품 id 값 받아오기
        Long itemId = itemService.saveItem(itemFormDto,multipartFileList);

        // itemImgRepository에서 생성한 쿼리메소드 사용해서 해당 상품의 이미지 리스트 받아오기
        List<ItemImg> itemImgList =itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        // itemRepository의 쿼리 메소드 사용해서 해당 상품 받아오기 (.orElseThrow로 예외 처리)
        Item item = itemRepository.findById(itemId).orElseThrow(EntityExistsException::new);

        // assertEquals로 입력한 상품 데이터와 VS 저장된 상품 데이터가 동일한지 확인
        assertEquals(itemFormDto.getItemNm(),item.getItemNm()); //상품명
        assertEquals(itemFormDto.getItemSellStatus(), item.getItemSellStatus()); //상품 판매 상태
        assertEquals(itemFormDto.getItemDetail(), item.getItemDetail()); // 상품 상세 설명
        assertEquals(itemFormDto.getPrice(),item.getPrice()); // 상품 가격
        assertEquals(itemFormDto.getStockNumber(),item.getStockNumber()); // 상품 재고수
        if(itemImgList.size() != 0) {
            assertEquals(multipartFileList.get(0).getOriginalFilename(), itemImgList.get(0).getOriImgName());// 첫 번째 파일의 원본 이미지 파일명
        }
    }
}
