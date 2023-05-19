package com.likelion.beshop.service;

import com.likelion.beshop.dto.ItemFormDto;
import com.likelion.beshop.dto.ItemImgDto;
import com.likelion.beshop.dto.ItemSearchDto;
import com.likelion.beshop.entity.Item;
import com.likelion.beshop.entity.ItemImg;
import com.likelion.beshop.repository.ItemImgRepository;
import com.likelion.beshop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        //상품 등록
        Item item = itemFormDto.createItem();// 상품 등록 폼으로부터 입력 받은 데이터를 이용하여 item 객체를 생성
        itemRepository.save(item);//상품 데이터 저장

        //이미지 등록
        for (int i = 0; i < itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            if (i == 0) // 첫 번째 이미지일 경우 대표 상품 이미지 여부 값을 "Y" 로 설정
                itemImg.setRepimgYn("Y");
            else
                itemImg.setRepimgYn("N");  // 나머지 상품 이미지는 "N" 로 설정

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));//상품 이미지 정보 저장
        }

        return item.getId(); // 상품 ID 값 return
    }

    @Transactional(readOnly=true) // 상품 데이터를 읽어오기 위한 함수로 트랜잭션 읽기 전용 어노테이션 필요 <= JPA가 더티체킹을 안해서 성능 향상시킬 수 있음
    public ItemFormDto getItemDtl(Long itemId) { // 상품 데이터를 받아올 함수

        // 상품에 해당하는 이미지 조회
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        // 매개변수로 받은 인스턴스를 이용해서 새 인스턴스 생성
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg); //ItemImgDto 객체로 만들어서
            itemImgDtoList.add(itemImgDto); // list에 추가
        }

        // 상품의 id로 상품 엔티티 조회 + 예외 처리
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        // 받아온 상품은 ItemFromDto 객체로 만들기
        ItemFormDto itemFormDto = ItemFormDto.of(item);

        // ItemFormDto 객체에 이미지 리스트 세팅
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto; // 세팅 후 반환
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
        Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);
        List<Long> itemImgIds = itemFormDto.getItemImgIds();


        for(int i=0;i<itemImgFileList.size();i++){
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

}
