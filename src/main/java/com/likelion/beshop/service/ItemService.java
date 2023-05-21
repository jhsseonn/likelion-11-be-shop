package com.likelion.beshop.service;

import com.likelion.beshop.dto_.ItemFormDto;
import com.likelion.beshop.dto_.ItemImgDto;
import com.likelion.beshop.dto_.ItemSearchDto;
import com.likelion.beshop.entity.Item;
import com.likelion.beshop.entity.ItemImg;
import com.likelion.beshop.repository.ItemImgRepository;
import com.likelion.beshop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
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
        Item item = itemFormDto.createItem(); //itemFormDto로 입력받은 데이터를 item에 받아와서 상품 데이터 저장
        itemRepository.save(item);

        //이미지 등록
        //이미지 메소드 아이템필드에 위에 만든 아이템 넣음
        for (int i = 0; i < itemImgFileList.size(); i++) { //아이템이미지파일만큼 크기로 반복문 돌림
            //첫번째이미지일 경우, 대표 상품 이미지 여부 값을 y로 설정, 이외는 N으로 설정

            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            if (i == 0) {
                itemImg.setRepImgYn("Y");
            } else {
                itemImg.setRepImgYn("N");
            }

            System.out.println("이미지 저장 전");
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i)); //아이템객체, 아이템파일객체 매개변수
            System.out.println("이미지 저장 완료");
        }
        return item.getId(); //상품의 id값 리턴
    }

    @Transactional(readOnly = true) //DB 에서 객체를 조회할 때, 읽기 전용 상태로 설정하는 어노테이션과 속성
    public ItemFormDto getItemDtl(long itemId) {
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);//상품에 해당하는 이미지 조회

        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg); //메서드는 ItemImg 객체를 매개변수로 받아, ItemImgDto 객체로 변환하여 반환합니다
            itemImgDtoList.add(itemImgDto);
        }
        Item item = itemRepository.findById(itemId) //상품의 id로 상품 엔티티 조회, 예외처리 필요
                .orElseThrow(EntityExistsException::new);

        ItemFormDto itemFormDto = ItemFormDto.of(item); //받아온 상품은 itemFormDto 객체로 만듬
        itemFormDto.setItemImgDtoList(itemImgDtoList); //itemFormDto 겍채에 이미지 리스트세팅 후
        return itemFormDto; //반환


    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
        Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);

        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        for(int i=0; i<itemImgFileList.size();i++){
            itemImgService.updateItemImg(itemImgIds.get(i),itemImgFileList.get(i));
        }
        return item.getId();
    }

    @Transactional(readOnly = true) //데이터 수정이 일어나지 않기 때문데
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto,pageable);
    }
}