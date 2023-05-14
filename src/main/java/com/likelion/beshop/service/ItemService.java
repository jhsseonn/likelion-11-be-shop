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

        return item.getId(); // 상품의 Code(ID) 값 리턴
    }

    @Transactional(readOnly = true) // 트랜잭션 읽기 전용 설정 어노테이션 -> 해당 어노테이션 사용 시, JPA가 더티체킹이라고 하는 변경을 감지하는 행위를 수행하지 않으므로 성능을 향상시킬 수 있어서 이런 함수는 읽기 전용으로 설정
    public ItemFormDto getItemDtl(long itemId){ // 상품 데이터를 받아올 함수
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

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{ // itemFormDto, itemImgFileList 매개변수로 받고 예외처리 (메서드 타입: Long)
        Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new); // 상품 등록 화면으로부터 전달 받은 상품 아이디를 이용해 상품 엔티티 조회
        System.out.println("상품 엔티티 업데이트 전");
        item.updateItem(itemFormDto); // 상품 등록 화면으로부터 전달 받은 itemFormDto를 통해 상품 엔티티 업데이트

        List<Long> itemImgIds = itemFormDto.getImageIds(); // 상품 이미지 아이디 리스트 조회
        System.out.println("상품 엔티티 업데이트 후");

        for(int i=0; i<itemImgFileList.size(); i++){
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i)); // 상품 이미지를 업데이트 하기 위해 updateItemImg() 메소드에 상품 이미지 아이디 상품 이미지 파일 정보를 파라미터로 전달
        }
        System.out.println("아이템 아이디 주기");
        return item.getId(); // 아이템 아이디 최종 리턴
    }

    @Transactional(readOnly = true) // 데이터 수정이 일어나지 않기 때문에 @Transactional(readOnly = true)로 설정
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }
}