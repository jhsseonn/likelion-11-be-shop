package com.likelion.beshop.service;

import com.likelion.beshop.dto.ItemFormDto;

import com.likelion.beshop.dto.ItemImgDto;
import com.likelion.beshop.entity.Item;
import com.likelion.beshop.entity.ItemImg;
import com.likelion.beshop.repository.ItemImgRepository;
import com.likelion.beshop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    private final ItemImgService itemImgService;

    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        // ItemFormDto로 입력받은 데이터를 Item에 받아와서 상품 데이터 저장
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        for(int i=0;i<itemImgFileList.size();i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            // 첫 번째 이미지일 경우, 대표 상품 이미지 여부 값을 Y로 설정, 이외는 N으로 설정
            if(i == 0)
                itemImg.setRepImage("Y");
            else
                itemImg.setRepImage("N");

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i)); // 상품 이미지 정보 저장
        }

        return item.getCode(); // 상품의 Code(ID) 값 리턴
    }

    @Transactional(readOnly = true) // 트랜잭션 읽기 전용 설정 어노테이션 -> 해당 어노테이션 사용 시, JPA가 더티체킹이라고 하는 변경을 감지하는 행위를 수행하지 않으므로 성능을 향상시킬 수 있어서 이런 함수는 읽기 전용으로 설정
    public ItemFormDto getItemDtl(Long itemId){ // 상품 데이터를 받아올 함수
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId); // 상품에 해당하는 이미지 조회
        List<ItemImgDto> itemImgDtoList = new ArrayList<>(); // 매개변수로 받은 인스턴스를 이용해 새로운 인스턴스 생성 (ModelMapper)
        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto); // 받아온 이미지들을 ItemImgDto 객체로 만들어서 List에 추가
        }

        //) 상품의 ID로 상품 엔티티 조회
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item); // 받아온 상품은 ItemFormDto 객체로 만들기
        //  ItemFormDto 객체에 이미지 리스트 세팅 후 반환
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }
}